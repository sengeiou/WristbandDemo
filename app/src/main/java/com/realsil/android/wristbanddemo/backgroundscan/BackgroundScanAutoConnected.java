package com.realsil.android.wristbanddemo.backgroundscan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.realsil.android.wristbanddemo.R;
import com.realsil.android.wristbanddemo.constant.ConstantParam;
import com.realsil.android.wristbanddemo.utility.GlobalGatt;
import com.realsil.android.wristbanddemo.utility.JudgeActivityFront;
import com.realsil.android.wristbanddemo.utility.SPWristbandConfigInfo;
import com.realsil.android.wristbanddemo.utility.SpecScanRecord;
import com.realsil.android.wristbanddemo.utility.WristbandManager;
import com.realsil.android.wristbanddemo.utility.WristbandManagerCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Administrator on 2016/5/23.
 */
public class BackgroundScanAutoConnected {
    // Log
    private final static String TAG = "BackgroundScanAutoConnected";
    private final static boolean D = true;

    // User Id
    private final String USER_ID = "1495015811";//1495015811,1234567890

    // State
    public static final int STATE_IDLE          = 0;
    public static final int STATE_CONNECTING    = 1;
    public static final int STATE_LOGGING       = 2;
    public static final int STATE_SYNCING       = 3;

    private int mState = STATE_IDLE;
    private String mStateString = "";

    // Message
    public static final int MSG_STATE_CONNECTED = 0;
    public static final int MSG_STATE_DISCONNECTED = 1;
    public static final int MSG_WRIST_STATE_CHANGED = 2;
    public static final int MSG_RECEIVE_SPORT_INFO = 3;//characteristic read
    public static final int MSG_RECEIVE_SLEEP_INFO = 4;
    public static final int MSG_BOND_STATE_ERROR = 5;
    public static final int MSG_BOND_STATE_SUCCESS = 6;

    public static final int MSG_FIND_BONDED_DEVICE = 7;

    public static final int MSG_ERROR = 10;

    private static Context mContext;

    private GlobalGatt mGlobalGatt;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;

    private WristbandManager mWristbandManager;

    //private BackgroundScanCallback mCallback;

    ArrayList<BackgroundScanCallback> mCallbacks;

    //private BondStateReceiver mBondStateReceiver;

    private ProgressDialog mProgressDialog = null;

    private boolean isFirstInSyncState;

    private Toast mToast;

    private boolean isInLogin = false;

    private boolean isForceDisableAutoConnect = false;

    // Read info Lock
    private Object mLock = new Object();
    private final int LOCK_WAIT_TIME = 15000;

    // instance
    private static BackgroundScanAutoConnected mInstance;

    private BluetoothOnOffStateReceiver mBluetoothOnOffStateReceiver;

    private boolean mScanning;

    // Use this count to count current receive adv count, may be something error can not scan.
    private int mReceiveAdvCount = 0;

    public static void initial(Context context) {
        mInstance = new BackgroundScanAutoConnected();
        mContext = context;

        mInstance.mWristbandManager = WristbandManager.getInstance();

        if (mInstance.mBluetoothManager == null) {
            mInstance.mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mInstance.mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
            }
        }

        if(mInstance.mBluetoothAdapter == null) {
            mInstance.mBluetoothAdapter = mInstance.mBluetoothManager.getAdapter();
            if (mInstance.mBluetoothAdapter == null) {
                Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            }
        }

        // Initial Callback list
        mInstance.mCallbacks = new ArrayList<>();

    }

    public static BackgroundScanAutoConnected getInstance() {
        return mInstance;
    }


    public void registerCallback(BackgroundScanCallback callback) {
        if(!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    public void unregisterCallback(BackgroundScanCallback callback) {
        if(mCallbacks.contains(callback)) {
            mCallbacks.remove(callback);
        }
    }

    public void closeConnect() {
        isInLogin = false;
        mWristbandManager.close();
    }

    public void startAutoConnect() {
        if(D) Log.d(TAG, "startAutoConnect()");

        checkAndResumeProgressBar();

        if(!mWristbandManager.isConnect()
                && SPWristbandConfigInfo.getBondedDevice(mContext) != null) {
            /*
            if(mGlobalGatt.isHostConnected(SPWristbandConfigInfo.getBondedDevice(WristbandHomeActivity.this))) {
                if(D) Log.e(TAG, "Is Connected. Reconnect it.");
                //mWristbandManager;
                return;
            }
            */
            /*
            if(mBondStateReceiver == null) {
                // Broadcast to receive Hid connect message
                mBondStateReceiver = new BondStateReceiver();
                IntentFilter filter = new IntentFilter();
                filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
                filter.addAction(BluetoothDevice.ACTION_UUID);
                this.registerReceiver(mBondStateReceiver, filter);
            }
            */
            if(mBluetoothAdapter.isEnabled()) {
                scanLeDevice(true);
            }
        } else {
            stopAutoConnect();
        }
    }

    public void forceLeScan() {
        checkAndResumeProgressBar();
        if(mBluetoothAdapter.isEnabled()) {
            scanLeDevice(true);
        }
    }

    /**
     * 连接手环
     * @param device
     */
    public void connectWristbandDevice(BluetoothDevice device) {
        if(D) Log.d(TAG, "connectWristbandDevice, device: " + device.getAddress());
        if(D) Log.d("connectWristbandDevice", "connectWristbandDevice, device: " + device.getAddress());
		if(isInLogin) {
            if(D) Log.w(TAG, "connectWristbandDevice, is in login, do nothing.");
			return;
		}
        stopAutoConnect();

        SendMessage(MSG_FIND_BONDED_DEVICE, device, -1, -1);
    }

    public void stopAutoConnect() {
        if(D) Log.d(TAG, "stopAutoConnect()");
        if(mBluetoothAdapter.isEnabled()) {
            scanLeDevice(false);
        }
    }

    public void checkAndResumeProgressBar() {
        if(D) Log.d(TAG, "checkAndResumeProgressBar, mState: " + mState + ", mStateString: " + mStateString);
        if(mState != STATE_IDLE) {
            if(mProgressDialog != null) {
                if(!mProgressDialog.isShowing()) {
                    showProgressBar(mStateString);
                }
            }
        }
    }

    public void registerBluetoothOnOffAutoStartBroadcast() {
        if(D) Log.d(TAG, "registerBluetoothOnOffAutoStartBroadcast");
        mBluetoothOnOffStateReceiver = new BluetoothOnOffStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mContext.registerReceiver(mBluetoothOnOffStateReceiver, filter);
    }

    public void unregisterBluetoothOnOffAutoStartBroadcast() {
        if(D) Log.d(TAG, "unregisterBluetoothOnOffAutoStartBroadcast");
        if(mBluetoothOnOffStateReceiver != null) {
            mContext.unregisterReceiver(mBluetoothOnOffStateReceiver);
        }
    }

    // Stops scanning after 120 seconds. This is too long
    // Stops scanning after 30 seconds. 30 seconds is enough
    private static final long SCAN_PERIOD = 30000;
    //Rtkband
    private final static UUID WRISTBAND_SERVICE_UUID = UUID.fromString("000001ff-3c17-d293-8e48-14fe2e4da212");
    private final static UUID JXWRISTBAND_SERVICE_UUID = UUID.fromString("000010ff-0000-1000-8000-00805f9b34fb");
    //jixianBand
    private Handler mScanHandler = new Handler();
    // Control le scan
    public void scanLeDevice(boolean enable) {
        if(isForceDisableAutoConnect) {
            if(D) Log.w(TAG, "scanLeDevice, isForceDisableAutoConnect: " + isForceDisableAutoConnect);
            return;
        }
        if(!mBluetoothAdapter.isEnabled()) {
            if(D) Log.d(TAG, "scanLeDevice, enable: " + enable + ", wrong with bt not enable.");
            return;
        }
        // control the process bar and le scan
        if(true == enable) {
            // avoid repetition operator
            if(mScanning == enable) {
                if(D) Log.e(TAG, "the le scan is already on");
                if(mReceiveAdvCount == 0) {
                    if(D) Log.w(TAG, "May be something wrong, le scan may be not real start, try restart it.");
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);

                    mBluetoothAdapter.startLeScan(mLeScanCallback);
                }
                // restart timer
                mScanHandler.removeCallbacks(mStopLeScan);
                mScanHandler.postDelayed(mStopLeScan, SCAN_PERIOD);
                for(BackgroundScanCallback callback: mCallbacks) {
                    callback.onLeScanEnable(true);
                }
                return;
            }
            // Stops scanning after a pre-defined scan period.
            mScanHandler.postDelayed(mStopLeScan, SCAN_PERIOD);
            if(D) Log.d(TAG, "start the le scan, on time is " + SCAN_PERIOD + "ms");
            mReceiveAdvCount = 0;
            //if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // Android4.4 don't support 128bit UUID filter.
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            //} else {
            //    UUID[] serviceUUIDs = {WRISTBAND_SERVICE_UUID};
            //    mBluetoothAdapter.startLeScan(serviceUUIDs, mLeScanCallback);
            //}

        } else {
            // avoid repetition operator
            if(mScanning == enable) {
                if(D) Log.e(TAG, "the le scan is already off");
                for(BackgroundScanCallback callback: mCallbacks) {
                    callback.onLeScanEnable(false);
                }
                return;
            }
            //remove the stop le scan runnable
            mScanHandler.removeCallbacks(mStopLeScan);

            if(D) Log.d(TAG, "stop the le scan");
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        // update le scan status
        mScanning = enable;
        for(BackgroundScanCallback callback: mCallbacks) {
            callback.onLeScanEnable(mScanning);
        }
    }


    // Stops scanning after a pre-defined scan period.
    Runnable mStopLeScan = new Runnable() {
        @Override
        public void run() {
            if(D) Log.d(TAG, "le delay time reached");
            // Stop le scan, delay SCAN_PERIOD ms
            scanLeDevice(false);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            if(mScanning != true) {
                if(D) Log.e(TAG, "is stop le scan, return");
                return;
            }
            mReceiveAdvCount ++;
//            if(D) Log.d("saomiao","scanRecord"+Arrays.toString(scanRecord));
//            if(D) Log.d("saomiao","16进制"+bytes2HexString(scanRecord));

            SpecScanRecord record = SpecScanRecord.parseFromBytes(scanRecord);
//            if(D) Log.d("saomiao", record.toString());
//            for(int i=0;i<record.getServiceUuids().size();i++){
//                Log.d("saomiao",i+"getServiceUuids "+record.getServiceUuids().get(i));
//            }
            //两个手环都能搜索到
            if(record.getServiceUuids()==null){
                return;
            }
            if(record.getServiceUuids().contains(new ParcelUuid(WRISTBAND_SERVICE_UUID))||
                    record.getServiceUuids().contains(new ParcelUuid(JXWRISTBAND_SERVICE_UUID))){
            }else {
                return;
            }
//            if((record.getServiceUuids() == null)
//                    || (!record.getServiceUuids().contains(new ParcelUuid(WRISTBAND_SERVICE_UUID)))
//                    ) {
//                return;
//            }
            //从本地得到绑定的设备
            final String addr = SPWristbandConfigInfo.getBondedDevice(mContext);

            for(BackgroundScanCallback callback: mCallbacks) {
                callback.onWristbandDeviceFind(device, rssi, scanRecord);
            }

            if(addr == null) {
                return;
            }
            if(!addr.equals(device.getAddress())) {
                return;
            }

            scanLeDevice(false);
            if(isInLogin) {
                if (D) Log.w(TAG, "onLeScan() in Login, return.");
                return;
            }
            if (D) Log.d(TAG, "onLeScan() - Device name is: " + device.getName() +
                    " - address is: " + device.getAddress());
            if(D) Log.d("connectWristbandDevice", "mLeScanCallback, device: " + device.getAddress());
            SendMessage(MSG_FIND_BONDED_DEVICE, device, -1, -1);
        }
    };

    /**
     * 将byte数组装换为16进制
     * @param
     * @param b
     */
    public  String bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex;
        }
        return ret;
    }

    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }

    // Application Layer callback
    WristbandManagerCallback mWristbandManagerCallback = new WristbandManagerCallback() {
        @Override
        public void onConnectionStateChange(final boolean status) {
            if(D) Log.d("ssonConnectionStateChange", "onConnectionStateChange, status: " + status);
            // if already connect to the remote device, we can do more things here. 开始进入绑定设备与登陆设备
            if(status) {
                SendMessage(MSG_STATE_CONNECTED, null, -1, -1);
            } else {
                //if(D) Log.d("bondremove","MSG_STATE_DISCONNECTED");
                SendMessage(MSG_STATE_DISCONNECTED, null, -1, -1);
            }
        }

        @Override
        public void onLoginStateChange(final int state) {
            if(!isInLogin) {
                return;
            }
            if(D) Log.d(TAG, "onLoginStateChange, state: " + state);
            SendMessage(MSG_WRIST_STATE_CHANGED, null, state, -1);
        }

        @Override
        public void onError(final int error) {
            if(D) Log.d(TAG, "onError, error: " + error);
            SendMessage(MSG_ERROR, null, error, -1);
        }
    };

    private void showToast(final int message) {
        if(!JudgeActivityFront.isAppOnForeground(mContext)) {
            if(D) Log.e(TAG, "showToast, Is not in top.");
            return;
        }
        if(mToast == null) {
            mToast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }

    private void showProgressBar(final int message) {
        showProgressBar(mContext.getResources().getString(message));
    }

    private void showProgressBar(final String message) {
        if(!JudgeActivityFront.isAppOnForeground(mContext)) {
            if(D) Log.e(TAG, "showProgressBar, Is not in top.");
            return;
        }
        mProgressDialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        //mProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);// Some device can not show dialog
        mProgressDialog.setMessage(message);
        mProgressDialog.setTitle(null);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mProgressBarSuperHandler.postDelayed(mProgressBarSuperTask, 30 * 1000);
    }

    private void cancelProgressBar() {
        if(mProgressDialog != null) {
            if(mProgressDialog.isShowing()) {
                mProgressDialog.cancel();
            }
        }
        // update state info
        mState = STATE_IDLE;
        mStateString = "";
        mProgressBarSuperHandler.removeCallbacks(mProgressBarSuperTask);
    }

    // Alarm timer
    Handler mProgressBarSuperHandler = new Handler();
    Runnable mProgressBarSuperTask = new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if(D) Log.w(TAG, "Wait Progress Timeout");
            showToast(R.string.progress_bar_timeout);
            mWristbandManager.close();
            isInLogin = false;
            // stop timer
            cancelProgressBar();

            startAutoConnect();
        }
    };

    /**
     * send message
     * @param msgType Type message type
     * @param obj object sent with the message set to null if not used
     * @param arg1 parameter sent with the message, set to -1 if not used
     * @param arg2 parameter sent with the message, set to -1 if not used
     **/
    private void SendMessage(int msgType, Object obj, int arg1, int arg2) {
        if(mHandler != null) {
            //	Message msg = new Message();
            Message msg = Message.obtain();
            msg.what = msgType;
            if(arg1 != -1) {
                msg.arg1 = arg1;
            }
            if(arg2 != -1) {
                msg.arg2 = arg2;
            }
            if(null != obj) {
                msg.obj = obj;
            }
            mHandler.sendMessage(msg);
        }
        else {
            if(D) Log.e(TAG,"handler is null, can't send message");
        }
    }

    // Broadcast to receive BT on/off broadcast
    public class BluetoothOnOffStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                if(D) Log.d(TAG, "BluetoothOnOffStateReceiver: state: " + state);
                if(state == BluetoothAdapter.STATE_ON) {
                    // Need wait a while
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isInLogin = false;
                    startAutoConnect();
                } else if(state == BluetoothAdapter.STATE_TURNING_OFF){
                    if(mScanning) {
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        mScanHandler.removeCallbacks(mStopLeScan);
                        for(BackgroundScanCallback callback: mCallbacks) {
                            callback.onLeScanEnable(false);
                        }
                    }
                } else if(state == BluetoothAdapter.STATE_OFF){
                    if(mWristbandManager.isConnect()) {
                        if(D) Log.w(TAG, "May be close bluetooth, but not disconnect, something may be error!");
                        for(BackgroundScanCallback callback: mCallbacks) {
                            callback.onWristbandLoginStateChange(false);
                        }
                    }
                    isInLogin = false;
                    mWristbandManager.close();
                }
            }
        }
    }

    // Broadcast to receive RCU reconnect broadcast
    public class BondStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device != null
                        && device.getAddress().equals(mWristbandManager.getBluetoothAddress())) {
                    if(D) Log.d(TAG, "Receive, " + BluetoothDevice.ACTION_BOND_STATE_CHANGED +" broadcast, address: " + device.getAddress());
                    int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                    switch (bondState) {
                        case BluetoothDevice.BOND_NONE:
                            if (D) Log.i(TAG, " Braodcast: RCU unpaired!");
                            SendMessage(MSG_BOND_STATE_ERROR, null, -1, -1);
                            break;

                        case BluetoothDevice.BOND_BONDING:
                            if (D) Log.i(TAG, " Braodcast: RCU BONDING!");
                            break;

                        case BluetoothDevice.BOND_BONDED:
                            if (D) Log.i(TAG, " Braodcast: RCU BONDED!");
                            SendMessage(MSG_BOND_STATE_SUCCESS, null, -1, -1);
                            break;
                    }//switch(bondState)
                } else {
                    if(D) Log.d(TAG, "Receive other address broadcast");
                }

            } else if(BluetoothDevice.ACTION_UUID.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device != null
                        && device.getAddress().equals(mWristbandManager.getBluetoothAddress())) {
                    if(D) Log.d(TAG, "Receive, " + BluetoothDevice.ACTION_UUID +" broadcast, address: " + device.getAddress());
                    synchronized (mLock) {
                        mLock.notifyAll();
                    }
                } else {
                    if(D) Log.d(TAG, "Receive other address broadcast");
                }
            }
        }
    }


    public boolean isInLogin() {
        return isInLogin;
    }

    public String getUserIdByImei() {
        String userId = "";
        String imei = getIMEI();
        if(imei != null
                && !imei.equals("")
                && imei.length() >= 10) {
            userId = imei.substring(imei.length() - 10);
        } else {
            userId = USER_ID;
        }

        if(D) Log.d("userID", "getUserIdByImei, imei: " + imei + ", userId: " + userId);
        return userId;
    }

    public String getIMEI() {
        return ((TelephonyManager) mContext.getSystemService(
                Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    // The Handler that gets information back from test thread
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_STATE_CONNECTED:
                    if(D) Log.d(TAG, "MSG_STATE_CONNECTED, connect");
                    /*
                    if(!mWristbandManager.readLinkLossLevel()) {
                        showToast(R.string.connect_failed);
                        mWristbandManager.close();
                        isInLogin = false;
                        cancelProgressBar();
                        return;
                    }*/
                    showToast(R.string.connect_success);
                    cancelProgressBar();
                    // update state info
                    mState = STATE_LOGGING;
                    mStateString = mContext.getString(R.string.connect_band);
                    showProgressBar(R.string.connect_band);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int cl = mBluetoothAdapter.getRemoteDevice(mWristbandManager.getBluetoothAddress()).getBluetoothClass().getDeviceClass();
                            if (D) Log.i(TAG, "Class is: " + cl);
                            /*
                            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                                // Attempts to discover services after successful connection.
                                synchronized (mLock) {
                                    try {
                                        if (D) Log.i(TAG, "Start wait bond to refresh service list.");
                                        mLock.wait(LOCK_WAIT_TIME);
                                        if (D) Log.i(TAG, "Time reach or refresh service complete.");
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                if (D) Log.i(TAG, "Current version is up to LOLLIPOP.");
                            }
                            */
                            // try to login the bond
                            String userId = SPWristbandConfigInfo.getUserId(mContext);
                            if(userId != null
                                    && ConstantParam.APP_WORK_TYPE == ConstantParam.WORK_TYPE_INTERNET) {
                                if(D) Log.d(TAG, "Login with user id:" + userId);
                                mWristbandManager.StartLoginProcess(userId);
                            } else {
                                mWristbandManager.StartLoginProcess(getUserIdByImei());
                            }
                        }
                    }).start();

                    break;
                case MSG_STATE_DISCONNECTED:
                    // do something
                    if(D) Log.d(TAG, "MSG_STATE_DISCONNECTED, something is error");
                    showToast(R.string.connect_disconnect);
                    cancelProgressBar();
                    isInLogin = false;
                    // Need start scan
                    startAutoConnect();
                    for(BackgroundScanCallback callback: mCallbacks) {
                        callback.onWristbandLoginStateChange(false);
                    }
                    //mWristbandManager.close();----for test
                    break;
                case MSG_BOND_STATE_SUCCESS:
                    if(D) Log.d(TAG, "MSG_BOND_STATE_SUCCESS, bond success, start connect.");
                    showToast(R.string.bond_success);
                    mWristbandManager.Connect(SPWristbandConfigInfo.getBondedDevice(mContext), mWristbandManagerCallback);
                    break;
                case MSG_BOND_STATE_ERROR:
                    if(D) Log.d(TAG, "MSG_BOND_STATE_ERROR, bond failure, try again.");
                    showToast(R.string.bond_failure);
                    cancelProgressBar();

                    mWristbandManager.close();
                    break;
                case MSG_ERROR:
                    showToast(R.string.something_error);
                    cancelProgressBar();
                    isInLogin = false;
                    mWristbandManager.close();
                    // Need start scan
                    startAutoConnect();
                    for(BackgroundScanCallback callback: mCallbacks) {
                        callback.onWristbandLoginStateChange(false);
                    }
                    break;
                case MSG_WRIST_STATE_CHANGED:
                   // if(D) Log.d("BatteryService", "MSG_WRIST_STATE_CHANGED, current state: " + msg.arg1);
                    if(D) Log.d(TAG, "MSG_WRIST_STATE_CHANGED, current state: " + msg.arg1);
                    // show state
                    if(msg.arg1 == WristbandManager.STATE_WRIST_LOGIN) {
                        if(D) Log.d(TAG, "start data sync");
                        showToast(R.string.connect_band_success);
                        cancelProgressBar();
                        SPWristbandConfigInfo.setBondedDevice(mContext, mWristbandManager.getBluetoothAddress());
                        isFirstInSyncState = true;
                        // update state info
                        mState = STATE_SYNCING;
                        //同步历史数据
                        mStateString = mContext.getString(R.string.syncing_data);
                        showProgressBar(R.string.syncing_data);
                        // Should start data sync.  同步历史数据
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(!mWristbandManager.SetDataSync(true)) {
                                    SendMessage(MSG_ERROR, null, -1, -1);
                                }
                            }
                        }).start();
                    } else if(msg.arg1 == WristbandManager.STATE_WRIST_SYNC_DATA){
                        if(isFirstInSyncState) {
                            isFirstInSyncState = false;
                            if(D) Log.d("BatteryService", "true");
                            return;
                        }

                        isInLogin = false;
                        
                        showToast(R.string.sync_data_success);
                        cancelProgressBar();
                        for(BackgroundScanCallback callback: mCallbacks) {
                            callback.onWristbandLoginStateChange(true);
                        }
                    }

                    break;
                case MSG_FIND_BONDED_DEVICE:
                    BluetoothDevice device = (BluetoothDevice)msg.obj;
                    String useName;
                    String name = SPWristbandConfigInfo.getInfoKeyValue(mContext, device.getAddress());
                    if(name != null) {
                        useName = name;
                    } else {
                        useName = device.getName();
                    }
                    if(D) Log.d(TAG, "MSG_FIND_BONDED_DEVICE. device.getName(): " + device.getName() + ", device.getAddress(): " + device.getAddress());
                    isInLogin = true;
                    // update state info
                    mState = STATE_CONNECTING;
                    mStateString = String.format(mContext.getResources().getString(R.string.connect_with_device_name), useName);
                    // add device to adapter  //正在与手环建立连接
                    showProgressBar(String.format(mContext.getResources().getString(R.string.connect_with_device_name), useName));
                    /* for test
                    // create bond
                    if (!(mBluetoothAdapter.getRemoteDevice(addr).getBondState() == BluetoothDevice.BOND_BONDED)) {
                        if (D) Log.i(TAG, "Not bond, start bond.");
                        mWristbandManager.setBluetoothAddress(device.getAddress());
                        if (!device.createBond()) {
                            if (D) Log.d(TAG, "Pair failed");
                            showToast(R.string.bond_failure);
                            cancelProgressBar();
                        }
                        return;
                    }*/

                    mWristbandManager.Connect(device.getAddress(), mWristbandManagerCallback);
                break;
                default:
                    break;
            }
        }
    };

    public static class BackgroundScanCallback {
        public void onWristbandDeviceFind(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {

        }

        public void onLeScanEnable(boolean enable) {

        }

        public void onWristbandLoginStateChange(boolean connected) {

        }
    }

    public boolean isForceDisableAutoConnect() {
        return isForceDisableAutoConnect;
    }

    public void setIsForceDisableAutoConnect(boolean isForceDisableAutoConnect) {
        this.isForceDisableAutoConnect = isForceDisableAutoConnect;
    }
}
