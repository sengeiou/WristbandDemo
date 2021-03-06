package com.realsil.android.wristbanddemo.applicationlayer;


import android.content.Context;
import android.util.Log;

import com.realsil.android.wristbanddemo.utility.StringByteTrans;
import com.realsil.android.wristbanddemo.transportlayer.TransportLayer;
import com.realsil.android.wristbanddemo.transportlayer.TransportLayerCallback;

import java.util.Arrays;
import java.util.List;


public class ApplicationLayer {
	// Log
	private final static String TAG = "ApplicationLayer";
	private final static boolean D = true;
		
	// Support Command Id
    public final static byte CMD_IMAGE_UPDATE 	= 0x01;
    public final static byte CMD_SETTING 		= 0x02;
    public final static byte CMD_BOND_REG 		= 0x03;
    public final static byte CMD_NOTIFY 		= 0x04;
    public final static byte CMD_SPORTS_DATA 	= 0x05;
    public final static byte CMD_FACTORY_TEST	= 0x06;
    public final static byte CMD_CTRL			= 0x07;
    public final static byte CMD_DUMP_STACK 	= 0x08;
    public final static byte CMD_TEST_FLASH 	= 0x09;
    public final static byte CMD_LOG 			= 0x0a;
    
    /*CMD_UPDATE : key */
    public final static byte KEY_UPDATE_REQUEST = 0x01;
    public final static byte KEY_UPDATE_RESPONSE = 0x02;
    public final static byte KEY_UPDATE_RESPONSE_OK = 0x00;
    public final static byte KEY_UPDATE_RESPONSE_ERR = 0x01;
    public final static byte UPDATE_RESPONSE_ERRCODE = 0x01;       //low power

    /*CMD_SETTING : key */
    public final static byte KEY_SETTING_TIMER = 0x01;
    public final static byte KEY_SETTING_ALARM = 0x02;
    public final static byte KEY_SETTING_GET_ALARM_LIST_REQ = 0x03;
    public final static byte KEY_SETTING_GET_ALARM_LIST_RSP = 0x04;
    public final static byte KEY_SETTING_STEP_TARGET = 0x05;
    public final static byte KEY_SETTING_USER_PROFILE = 0x10;
    public final static byte KEY_SETTING_LOST_MODE = 0x20;
    public final static byte KEY_SETTING_SIT_TOOLONG_NOTIFY = 0x21;
    public final static byte KEY_SETTING_LEFT_RIGHT_HAND_NOTIFY = 0x22;
    public final static byte KEY_SETTING_PHONE_OS = 0x23;
    public final static byte KEY_SETTING_INCOMING_CALL_LIST = 0x24;
    public final static byte KEY_SETTING_INCOMING_ON_OFF = 0x25;
    public final static byte KEY_SETTING_SIT_TOOLONG_SWITCH_REQ = 0x26;
    public final static byte KEY_SETTING_SIT_TOOLONG_SWITCH_RSP = 0x27;
    public final static byte KEY_SETTING_NOTIFY_SWITCH_REQ = 0x28;
    public final static byte KEY_SETTING_NOTIFY_SWITCH_RSP = 0x29;
    //public final static byte KEY_SETTING_INCOMING_NOTIFY = 0x26;

    /*CMD_SETTING : key  绑定命令*/
    public final static byte KEY_BOND_REQ = 0x01;
    public final static byte KEY_BOND_RSP = 0x02;
    public final static byte KEY_LOGIN_REQ = 0x03;
    public final static byte KEY_LOGIN_RSP = 0x04;
    public final static byte KEY_UNBOND = 0x05;
    public final static byte KEY_SUP_BOND_KEY = 0x06;
    public final static byte KEY_SUP_BOND_KEY_RSP = 0x07;

    /* CMD_NOTIFY: key:*/
    public final static byte KEY_NOTIFY_IMCOMING_CALL = 0x01;
    public final static byte KEY_NOTIFY_IMCOMING_CALL_ACC = 0x02;
    public final static byte KEY_NOTIFY_IMCOMING_CALL_REJ = 0x03;
    public final static byte KEY_NOTIFY_INCOMING_OTHER_NOTIFY = 0x04;

    /* CMD_SPORTS: key:*/
    public final static byte KEY_SPORTS_REQ = 0x01;
    public final static byte KEY_SPORTS_RUNNIG_RSP = 0x02;
    public final static byte KEY_SPORTS_SLEEP_RSP = 0x03;
    public final static byte KEY_SPORTS_RUNNIG_RSP_MORE = 0x04;
    public final static byte KEY_SPORTS_SLEEP_SET_RSP = 0x05;
    public final static byte KEY_SPORTS_DATA_SYNC = 0x06;
    public final static byte KEY_SPORTS_HIS_SYNC_BEG = 0x07;
    public final static byte KEY_SPORTS_HIS_SYNC_END = 0x08;
    public final static byte KEY_SPORTS_DATA_TODAY_SYNC = 0x09;
    public final static byte KEY_SPORTS_DATA_LAST_SYNC = 0x0a;


    /* CMD_FAC_TEST: key:*/
    public final static byte KEY_FAC_TEST_ECHO_REQ = 0x01;
    public final static byte KEY_FAC_TEST_ECHO_RSP = 0x02;
    public final static byte KEY_FAC_TEST_CHAR_REQ = 0x03;
    public final static byte KEY_FAC_TEST_CHAR_RSP = 0x04;
    public final static byte KEY_FAC_TEST_LED = 0x05;
    public final static byte KEY_FAC_TEST_MOTO = 0x06;
    public final static byte KEY_FAC_TEST_WRITE_SN = 0x07;
    public final static byte KEY_FAC_TEST_READ_SN = 0x08;
    public final static byte KEY_FAC_TEST_SN_RSP = 0x09;
    public final static byte KEY_FAC_TEST_WRITE_TEST_FLAG = 0x0a;
    public final static byte KEY_FAC_TEST_READ_TEST_FLAG = 0x0b;
    public final static byte KEY_FAC_TEST_FLAG_RSP = 0x0c;
    public final static byte KEY_FAC_TEST_SENSOR_DATA_REQ = 0x0d;
    public final static byte KEY_FAC_TEST_SENSOR_DATA_RSP = 0x0e;
    public final static byte KEY_FAC_TEST_ENTER_SPUER_KEY = 0x10;
    public final static byte KEY_FAC_TEST_LEAVE_SPUER_KEY = 0x11;
    public final static byte KEY_FAC_TEST_BUTTON_TEST = 0x21;
    public final static byte KEY_FAC_TEST_MOTO_OLD = 0x31;
    public final static byte KEY_FAC_TEST_LED_OLD = 0x32;

    /* CMD_CONTROL: key:*/
    public final static byte KEY_CTRL_PHOTO_RSP = 0x01;
    public final static byte KEY_CTRL_CLICK_RSP = 0x02;
    public final static byte KEY_CTRL_DOUBLE_CLICK_RSP = 0x03;
    public final static byte KEY_CTRL_APP_REQ = 0x11;

    /* CMD_DUMP: key:*/
    public final static byte KEY_DUMP_ASSERT_LOCATE_REQ = 0x01;
    public final static byte KEY_DUMP_ASSERT_LOCATE_RSP = 0x02;
    public final static byte KEY_DUMP_ASSERT_STACK_REQ = 0x03;
    public final static byte KEY_DUMP_ASSERT_STACK_RSP = 0x14;

    /* LOG: key:*/
    public final static byte KEY_LOG_FUNC_OPEN = 0x01;
    public final static byte KEY_LOG_FUNC_CLOSE = 0x02;
    public final static byte KEY_LOG_RSP = 0x03;

    /* Login response*/
    public final static byte LOGIN_RSP_SUCCESS = 0x00;
    public final static byte LOGIN_RSP_ERROR = 0x01;

    /* Bond response*/
    public final static byte BOND_RSP_SUCCESS = 0x00;
    public final static byte BOND_RSP_ERROR = 0x01;

    /* Sport Sync Mode */
    public final static byte SPORT_DATA_SYNC_MODE_DISABLE = 0x00;
    public final static byte SPORT_DATA_SYNC_MODE_ENABLE = 0x01;

    /* err code*/
    public final static byte SUCCESS = 0x00;
    public final static byte BOND_FAIL_TIMEOUT = 0x01;
    public final static byte SUPER_KEY_FAIL = 0x02;
    public final static byte LOW_POWER = 0x03;
    
    // Day Flags
 	public final static byte REPETITION_NULL 	= 0x00;
    public final static byte REPETITION_ALL 	= 0x7f;
 	public final static byte REPETITION_MON 	= 0x01;
 	public final static byte REPETITION_TUES 	= 0x02;
 	public final static byte REPETITION_WED 	= 0x04;
 	public final static byte REPETITION_THU 	= 0x08;
 	public final static byte REPETITION_FRI 	= 0x10;
 	public final static byte REPETITION_SAT 	= 0x20;
 	public final static byte REPETITION_SUN 	= 0x40;
 	
 	// Sex Flags
 	public final static boolean SEX_MAN 	= true;
 	public final static boolean SEX_WOMAN 	= false;

    // Call notify flags
    public final static byte PHONE_OS_IOS    	            = 0x01;
    public final static byte PHONE_OS_ANDROID 	            = 0x02;

    // Call notify flags
    public final static byte CALL_NOTIFY_MODE_ON 	            = 0x01;
    public final static byte CALL_NOTIFY_MODE_OFF 	            = 0x02;
    public final static byte CALL_NOTIFY_MODE_ENABLE_QQ 	    = 0x03;
    public final static byte CALL_NOTIFY_MODE_DISABLE_QQ    	= 0x04;
    public final static byte CALL_NOTIFY_MODE_ENABLE_WECHAT 	= 0x05;
    public final static byte CALL_NOTIFY_MODE_DISABLE_WECHAT 	= 0x06;
    public final static byte CALL_NOTIFY_MODE_ENABLE_MESSAGE 	= 0x07;
    public final static byte CALL_NOTIFY_MODE_DISABLE_MESSAGE 	= 0x08;

    // notify flags
    public final static byte OTHER_NOTIFY_INFO_QQ 	            = 0x01;
    public final static byte OTHER_NOTIFY_INFO_WECHAT     	    = 0x02;
    public final static byte OTHER_NOTIFY_INFO_MESSAGE     	    = 0x04;

    public final static byte NOTIFY_SWITCH_SETTING_CALL 	        = 0x01;
    public final static byte NOTIFY_SWITCH_SETTING_QQ 	            = 0x02;
    public final static byte NOTIFY_SWITCH_SETTING_WECHAT     	    = 0x04;
    public final static byte NOTIFY_SWITCH_SETTING_MESSAGE     	    = 0x08;

    // Long sit Flags
    public final static byte LONG_SIT_CONTROL_ENABLE 	= 0x01;
    public final static byte LONG_SIT_CONTROL_DISABLE 	= 0x00;

    // FAC Led Flags
    public final static byte FAC_LED_CONTROL_ENABLE_ALL 	= (byte)0xFF;
    public final static byte FAC_LED_CONTROL_ENABLE_0    	= 0x00;
    public final static byte FAC_LED_CONTROL_ENABLE_1    	= 0x01;
    public final static byte FAC_LED_CONTROL_ENABLE_2    	= 0x02;

    // Sleep Mode
    public static final int SLEEP_MODE_START_SLEEP = 1;
    public static final int SLEEP_MODE_START_DEEP_SLEEP = 2;
    public static final int SLEEP_MODE_START_WAKE = 3;
 	
    // Transport Layer Object
    TransportLayer mTransportLayer;
     	
 	// Application Layer Call
 	private ApplicationLayerCallback mCallback;
    
    public ApplicationLayer(Context context, ApplicationLayerCallback callback) {
    	if(D) Log.d(TAG, "initial");
    	// register callback
    	mCallback = callback;
    	
    	// initial the transport layer
    	mTransportLayer = new TransportLayer(context, mTransportCallback);
    	
    }

    /**
     * Connect to the remote device.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onConnectionStateChange} callback is invoked, reporting the result of the operation.
     *
     * @return the operation result
     *
     * */
    public boolean connect(String addr) {
        if(D) Log.d(TAG, "connect with: " + addr);
        return mTransportLayer.connect(addr);
    }

    /**
     * Close, it will disconnect to the remote, and close gatt.
     *
     *
     * */
    public void close() {
        if(D) Log.d(TAG, "close()");
        mTransportLayer.close();
    }

    /**
     * Disconnect, it will disconnect to the remote.
     *
     *
     * */
    public void disconnect() {
        mTransportLayer.disconnect();
    }

    /**
     * Set the name
     *
     * @param name 		the name
     */
    public void setDeviceName(String name) {
        if(D) Log.d(TAG, "set name, name: " + name);
        mTransportLayer.setDeviceName(name);
    }

    /**
     * Get the name
     *
     */
    public void getDeviceName() {
        if(D) Log.d(TAG, "getDeviceName");
        mTransportLayer.getDeviceName();
    }

    /**
	 * Request Remote enter OTA mode. Command: 0x01, Key: 0x01.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
	 * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
	 * then the remote will response some information, the {@link ApplicationLayerCallback#onUpdateCmdRequestEnterOtaMode}
     * callback will invoked, response some information to host
	 * 
	 * @return the operation result
	 * 
	 * */
    public boolean UpdateCmdRequestEnterOtaMode() {
    	if(D) Log.d(TAG, "UpdateCmdRequestEnterOtaMode");
    	// generate key data
    	byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_UPDATE_REQUEST, null);
    	// generate application layer packet
    	byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_IMAGE_UPDATE, keyData);
    	Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);
    	
    	// sent the data
    	return mTransportLayer.sendData(appPacketData);
    }
    
    /**
	 * Set the remote time. Command: 0x02, Key: 0x01.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
	 * 
	 * @param year	the current time of year.
	 * @param mon	the current time of mon.
	 * @param day	the current time of day.
	 * @param hour	the current time of hour.
	 * @param min	the current time of min.
	 * @param sec	the current time of sec.
	 * @return the operation result
	 * 
	 * */
    public boolean SettingCmdTimeSetting(int year, int mon, int day, int hour, int min, int sec) {
    	if(D) Log.d(TAG, "SettingCmdTimeSetting, year: " + year
                + ", mon: " + mon
                + ", day: " + day
                + ", hour: " + hour
                + ", min: " + min
                + ", sec: " + sec );
    	// generate key value data
    	ApplicationLayerTimerPacket timePackt = new ApplicationLayerTimerPacket(year, mon, day, hour, min, sec);
    	// generate key data
    	byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_TIMER, timePackt.getPacket());
    	// generate application layer packet
    	byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
    	Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);
    	
    	// sent the data
    	return mTransportLayer.sendData(appPacketData);
    }
    
    /**
	 * Set the remote time. Command: 0x02, Key: 0x02.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
	 * 
	 * @param Alarms	the alarm list.
	 * @return the operation result
	 * 
	 * */
    public boolean SettingCmdAlarmsSetting(ApplicationLayerAlarmsPacket Alarms) {
    	if(D) Log.d(TAG, "SettingCmdAlarmSetting");
        // generate key value data
        byte[] keyValue = null;
        if(Alarms == null || (Alarms.size() == 0)) {
            //do nothing
        } else {
            if(Alarms.size() > 8) {
                return false;
            }
            keyValue = Alarms.getPacket();
        }
    	// generate key data
    	byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_ALARM, keyValue);
    	// generate application layer packet
    	byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
    	Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);
    	
    	// sent the data
    	return mTransportLayer.sendData(appPacketData);
    }
    
    /**
	 * Request Remote alarms list. Command: 0x01, Key: 0x03.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
	 * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
	 * then the remote will response some information, the {@link ApplicationLayerCallback#onSettingCmdRequestAlarmList}
     * callback will invoked, response some information to host
	 * 
	 * @return the operation result
	 * 
	 * */
    public boolean SettingCmdRequestAlarmList() {
    	if(D) Log.d(TAG, "SettingCmdRequestAlarmList");
    	// generate key data
    	byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_GET_ALARM_LIST_REQ, null);
    	// generate application layer packet
    	byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
    	Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);
    	
    	// sent the data
    	return mTransportLayer.sendData(appPacketData);
    }
    
    /**
	 * Set the remote user profile. Command: 0x02, Key: 0x10.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
	 * 
	 * @param user	the user profile packet.
	 * @return the operation result
	 * 
	 * */
    public boolean SettingCmdUserSetting(ApplicationLayerUserPacket user) {
    	if(D) Log.d(TAG, "SettingCmdUserSetting");
    	// generate key value data
    	byte[] keyValue = user.getPacket();
    	// generate key data
    	byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_USER_PROFILE, keyValue);
    	// generate application layer packet
    	byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
    	Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);
    	
    	// sent the data
    	return mTransportLayer.sendData(appPacketData);
    }
    
    /**
	 * Set the remote lost mode. Command: 0x02, Key: 0x20.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
	 * 
	 * @param mode	the lost mode.
	 * @return the operation result
	 * 
	 * */
    public boolean SettingCmdLostModeSetting(byte mode) {
    	if(D) Log.d(TAG, "SettingCmdLostModeSetting, mode: " + mode);
    	// generate key value data
    	byte[] keyValue = {mode};
    	// generate key data
    	byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_LOST_MODE, keyValue);
    	// generate application layer packet
    	byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
    	Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);
    	
    	// sent the data
    	return mTransportLayer.sendData(appPacketData);
    }
    
    /**
	 * Set the remote step target. Command: 0x02, Key: 0x05.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
	 * 
	 * @param step	the step target.
	 * @return the operation result
	 * 
	 * */
    public boolean SettingCmdStepTargetSetting(long step) {
    	if(D) Log.d(TAG, "SettingCmdStepTargetSetting, step: " + step);
    	// generate key value data
    	ApplicationLayerStepPacket p = new ApplicationLayerStepPacket(step);
    	byte[] keyValue = p.getPacket();
    	// generate key data
    	byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_STEP_TARGET, keyValue);
    	// generate application layer packet
    	byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
    	Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);
    	
    	// sent the data
    	return mTransportLayer.sendData(appPacketData);
    }
    
    /**
	 * Set the remote long sit notification. Command: 0x02, Key: 0x21.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
	 * 
	 * @param packet	the sit packet.
	 * @return the operation result
	 * 
	 * */
    public boolean SettingCmdLongSitSetting(ApplicationLayerSitPacket packet) {
    	if(D) Log.d(TAG, "SettingCmdLongSitSetting");
    	// generate key value data
    	byte[] keyValue = packet.getPacket();
    	// generate key data
    	byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_SIT_TOOLONG_NOTIFY, keyValue);
    	// generate application layer packet
    	byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
    	Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);
    	
    	// sent the data
    	return mTransportLayer.sendData(appPacketData);
    }
    
    /**
	 * Set the remote left right hand. Command: 0x02, Key: 0x22.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
	 * 
	 * @param mode	the left right hand mode.
	 * @return the operation result
	 * 
	 * */
    public boolean SettingCmdLeftRightSetting(byte mode) {
    	if(D) Log.d(TAG, "SettingCmdLeftRightSetting, mode: " + mode);
    	// generate key value data
    	byte[] keyValue = {mode};
    	// generate key data
    	byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_LEFT_RIGHT_HAND_NOTIFY, keyValue);
    	// generate application layer packet
    	byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
    	Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);
    	
    	// sent the data
    	return mTransportLayer.sendData(appPacketData);
    }
    /**
     * Set the phone OS. Command: 0x02, Key: 0x23.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param mode	the phone os.
     * @return the operation result
     *
     * */
    public boolean SettingCmdPhoneOSSetting(byte mode) {
        if(D) Log.d(TAG, "SettingCmdPhoneOSSetting, mode: " + mode);
        // generate key value data
        byte[] keyValue = {mode};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_PHONE_OS, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }
    /**
     * Set the remote call notify mode. Command: 0x02, Key: 0x25.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param mode	call notify mode.
     * @return the operation result
     *
     * */
    public boolean SettingCmdCallNotifySetting(byte mode) {
        if(D) Log.d(TAG, "SettingCmdCallNotifySetting, mode: " + mode);
        // generate key value data
        byte[] keyValue = {mode};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_INCOMING_ON_OFF, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request the remote current long sit setting. Command: 0x02, Key: 0x26.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onSettingCmdRequestLongSit}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     *
     * */
    public boolean SettingCmdRequestLongSitSetting() {
        if(D) Log.d(TAG, "SettingCmdRequestLongSitSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_SIT_TOOLONG_SWITCH_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }
    /**
     * Request the remote current notify setting. Command: 0x02, Key: 0x28.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onSettingCmdRequestNotifySwitch}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     *
     * */
    public boolean SettingCmdRequestNotifySwitchSetting() {
        if(D) Log.d(TAG, "SettingCmdRequestNotifySwitchSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_NOTIFY_SWITCH_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
	 * Request bond to remote. Command: 0x03, Key: 0x01.  绑定用户请求
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
	 * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
	 * then the remote will response some information, the {@link ApplicationLayerCallback#onBondCmdRequestBond}
     * callback will invoked, response some information to host
	 * 
	 * @param userId	the user id string.
	 * @return the operation result
	 * 
	 * */
    public boolean BondCmdRequestBond(String userId) {
    	if(D) Log.d(TAG, "BondCmdRequestBond, user id: " + userId);
        if(D) Log.d("requestBondLogin","RequestBond id"+userId);

    	// generate key value data
    	// be careful java use the UTF-16 code, so we need change the ascii byte array
    	byte[] asciiArray = StringByteTrans.Str2Bytes(userId);
    	byte[] keyValue = new byte[32];
    	if(asciiArray.length > 32) {
    		System.arraycopy(asciiArray, 0, keyValue, 0, 32);
    	} else {
    		System.arraycopy(asciiArray, 0, keyValue, 0, asciiArray.length);
    	}
    	Log.e(TAG, "keyValue: " + StringByteTrans.byte2HexStr(keyValue));
    	// generate key data
    	byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_BOND_REQ, keyValue);
    	// generate application layer packet
    	byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_BOND_REG, keyData);
    	Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

    	// sent the data
        if(D) Log.d("requestBondLogin","BandBond appPacketData"+Arrays.toString(appPacketData));
    	return mTransportLayer.sendData(appPacketData);
    }
    
    /**
	 * Request login to remote. Command: 0x03, Key: 0x03.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
	 * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
	 * then the remote will response some information, the {@link ApplicationLayerCallback#onBondCmdRequestLogin}
     * callback will invoked, response some information to host
	 * 
	 * @param userId	the user id string.
	 * @return the operation result
	 * 
	 * */
    public boolean BondCmdRequestLogin(String userId) {
    	if(D) Log.d(TAG, "BondCmdRequestLogin, user id: " + userId);
        if(D) Log.d("requestLoginBond", "RequestLogin, id: " + userId);
    	// generate key value data
    	// be careful java use the UTF-16 code, so we need change the ascii byte array
    	byte[] asciiArray = StringByteTrans.Str2Bytes(userId);
    	byte[] keyValue = new byte[32];
    	if(asciiArray.length > 32) {
    		System.arraycopy(asciiArray, 0, keyValue, 0, 32);
    	} else {
    		System.arraycopy(asciiArray, 0, keyValue, 0, asciiArray.length);
    	}
    	Log.e(TAG, "keyValue: " + StringByteTrans.byte2HexStr(keyValue));
    	// generate key data
    	byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_LOGIN_REQ, keyValue);
    	// generate application layer packet
    	byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_BOND_REG, keyData);
    	Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);
    	
    	// sent the data
        Log.d("BandLogin","BandLogin appPacketData"+Arrays.toString(appPacketData));
        if(D) Log.d("requestLoginBond", "BandLogin appPacketData" + Arrays.toString(appPacketData));
    	return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request clear bond to remote. Command: 0x03, Key: 0x05.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     *
     * @return the operation result
     *
     * */
    public boolean BondCmdRequestRemoveBond() {
        if(D) Log.d(TAG, "BondCmdRequestRemoveBond()");
        if(D) Log.d("SendRemoveBondCommand","SendRemoveBondCommand");
        // generate key value data, reserve one byte
        byte[] keyValue = new byte[1];
        Log.e(TAG, "keyValue: " + StringByteTrans.byte2HexStr(keyValue));
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_UNBOND, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_BOND_REG, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }
    /**
     * Send notify info to the remote. Command: 0x04, Key: 0x01.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @return the operation result
     *
     * */
    public boolean NotifyCmdCallNotifyInfoSetting() {
        if(D) Log.d(TAG, "NotifyCmdCallNotifyInfoSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_IMCOMING_CALL, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }
    /**
     * Send call accept notify info to the remote. Command: 0x04, Key: 0x02.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @return the operation result
     *
     * */
    public boolean NotifyCmdCallAcceptNotifyInfoSetting() {
        if(D) Log.d(TAG, "NotifyCmdCallAcceptNotifyInfoSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_IMCOMING_CALL_ACC, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }
    /**
     * Send call reject notify info to the remote. Command: 0x04, Key: 0x03.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @return the operation result
     *
     * */
    public boolean NotifyCmdCallRejectNotifyInfoSetting() {
        if(D) Log.d(TAG, "NotifyCmdCallRejectNotifyInfoSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_IMCOMING_CALL_REJ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Send notify info to the remote. Command: 0x04, Key: 0x04.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param info	call notify info.
     * @return the operation result
     *
     * */
    public boolean NotifyCmdOtherNotifyInfoSetting(byte info) {
        if(D) Log.d(TAG, "NotifyCmdOtherNotifyInfoSetting, info: " + info);
        // generate key value data
        byte[] keyValue = {info};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_INCOMING_OTHER_NOTIFY, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }
    
    /**
	 * Request remote send sport data. Command: 0x05, Key: 0x01.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
	 * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
	 * then the remote will response some information, the {@link ApplicationLayerCallback#onSportDataCmdSportData}
	 * and {@link ApplicationLayerCallback#onSportDataCmdSleepData}
     * callback will invoked, response some information to host
	 *
	 * @return the operation result
	 * 请求运动数据
	 * */
    public boolean SportDataCmdRequestData() {
    	if(D) Log.d(TAG, "SportDataCmdRequestData");
    	// generate key value data
        if(D) Log.d("SportDataCmdRequestData", "SportDataCmdRequestData");
    	// generate key data    请求数据(包括运动数据和睡眠数据)
    	byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_REQ, null);
    	// generate application layer packet
    	byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
    	Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);
       if(D) Log.d("SportDataCmdRequestData","SportDataCmdRequestData appPacketData"+ Arrays.toString(appPacketData));
    	// sent the data
    	return mTransportLayer.sendData(appPacketData);
    }
    
    /**
	 * Set the remote sync mode. Command: 0x05, Key: 0x06.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
	 * 
	 * @param mode	the left right hand mode.
	 * @return the operation result
	 * 数据实时同步
	 * */
    public boolean SportDataCmdSyncSetting(byte mode) {
    	if(D) Log.d(TAG, "SportDataCmdSyncSetting, mode: " + mode);
        if(D) Log.d("SetDataSync()","SportDataCmdSyncSetting, mode: " + mode);
    	// generate key value data
    	byte[] keyValue = {mode};
    	// generate key data
    	byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_DATA_SYNC, keyValue);
    	// generate application layer packet
    	byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
    	Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);
    	
    	// sent the data
        if(D) Log.d("SetDataSync()","SportDataCmdSyncSetting, appPacketData" + Arrays.toString(appPacketData));
    	return mTransportLayer.sendData(appPacketData);
    }
    
    /**
	 * Set the remote sync today. Command: 0x05, Key: 0x09.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
	 * 
	 * @param packet	the today sport packet.
	 * @return the operation result
	 * 
	 * */
    public boolean SportDataCmdSyncToday(ApplicationLayerTodaySportPacket packet) {
    	if(D) Log.d(TAG, "SportDataCmdSyncToday");
    	// generate key value data
    	byte[] keyValue = packet.getPacket();
    	// generate key data  当天运动数据同步
    	byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_DATA_TODAY_SYNC, keyValue);
    	// generate application layer packet
    	byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
    	Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);
    	
    	// sent the data
    	return mTransportLayer.sendData(appPacketData);
    }
    
    /**
	 * Set the remote sync recently. Command: 0x05, Key: 0x0a.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
	 * 
	 * @param packet	the recently sport packet.
	 * @return the operation result
	 * 
	 * */
    public boolean SportDataCmdSyncRecently(ApplicationLayerRecentlySportPacket packet) {
    	if(D) Log.d(TAG, "SportDataCmdSyncRecently");
    	// generate key value data
    	byte[] keyValue = packet.getPacket();
    	// generate key data              最近一次运动状态同步
    	byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_DATA_LAST_SYNC, keyValue);
    	// generate application layer packet
    	byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
    	Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);
    	
    	// sent the data
    	return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Fac Command use to enable led. Command: 0x06, Key: 0x05.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param led the led want to enable
     * @return the operation result
     *
     * */
    public boolean FACCmdEnableLed(byte led) {
        if(D) Log.d(TAG, "FACCmdEnableLed");
        byte[] keyData;
        if(led != FAC_LED_CONTROL_ENABLE_ALL) {
            // generate key value data
            byte[] keyValue = {led};
            // generate key data
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_LED, keyValue);
        } else {
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_LED, null);
        }
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_FACTORY_TEST, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Fac Command use to enable vibrate. Command: 0x06, Key: 0x06.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @return the operation result
     *
     * */
    public boolean FACCmdEnableVibrate() {
        if(D) Log.d(TAG, "FACCmdEnableVibrate");
        // generate key value data
        //byte[] keyValue = packet.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_MOTO, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_FACTORY_TEST, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Fac Command use to request sensor data. Command: 0x06, Key: 0x0d.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onFACCmdSensorData}
     * and {@link ApplicationLayerCallback#onSportDataCmdSleepData}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     *
     * */
    public boolean FACCmdRequestSensorData() {
        if(D) Log.d(TAG, "FACCmdRequestSensorData");
        // generate key value data
        //byte[] keyValue = packet.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_SENSOR_DATA_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_FACTORY_TEST, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Fac Command use to enter test mode. Command: 0x06, Key: 0x0e.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param key the 32 byte token key
     * @return the operation result
     *
     * */
    public boolean FACCmdEnterTestMode(byte[] key) {
        if(D) Log.d(TAG, "FACCmdEnterTestMode");
        byte[] keyData;
        if(key != null) {
            if (key.length != 32) {
                if (D) Log.d(TAG, "The length is not right.");
                return false;
            }
            byte[] keyValue = new byte[32];
            // generate key value data
            System.arraycopy(key, 0, keyValue, 0, 32);
            // generate key data
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_ENTER_SPUER_KEY, keyValue);
        } else {
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_ENTER_SPUER_KEY, null);
        }
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_FACTORY_TEST, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Fac Command use to exit test mode. Command: 0x06, Key: 0x0e.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param key the 32 byte token key
     * @return the operation result
     *
     * */
    public boolean FACCmdExitTestMode(byte[] key) {
        if(D) Log.d(TAG, "FACCmdExitTestMode");
        byte[] keyData;
        if(key != null) {
            if (key.length != 32) {
                if (D) Log.d(TAG, "The length is not right.");
                return false;
            }
            byte[] keyValue = new byte[32];
            // generate key value data
            System.arraycopy(key, 0, keyValue, 0, 32);
            // generate key data
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_LEAVE_SPUER_KEY, keyValue);
        } else {
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_LEAVE_SPUER_KEY, null);
        }
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_FACTORY_TEST, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }
    
    
    
    TransportLayerCallback mTransportCallback = new TransportLayerCallback() {
    	@Override
    	public void onConnectionStateChange(final boolean status, final boolean newState) {
    		if(D) Log.d(TAG, "onConnectionStateChange, status: " + status + "newState: " + newState);
            mCallback.onConnectionStateChange(status, newState);
        }
    	
    	@Override
    	public void onDataSend(final boolean status, byte[] data) {
    		if(D) Log.d(TAG, "onDataSend, status: " + status);
    		// decode the packet
    		ApplicationLayerPacket appPacket = new ApplicationLayerPacket();
    		appPacket.parseData(data);
    		// dispatch the key value
    		List<ApplicationLayerKeyPacket> keyPackets = appPacket.getKeyPacketArrays();
    		for (ApplicationLayerKeyPacket keyPacket : keyPackets) {
    			byte commandId = appPacket.getCommandId();
    			byte keyId = keyPacket.getKey();
    			if(D) Log.d(TAG, "onDataSend, commandId: " + commandId + ", keyId: " + keyId);
    			mCallback.onCommandSend(status, commandId, keyId);
    		}
    		
        }
    	@Override
    	public void onDataReceive(byte[] data) {
    		if(D) Log.d(TAG, "onDataReceive, data length: " + data.length);
    		// decode the packet
    		ApplicationLayerPacket appPacket = new ApplicationLayerPacket();
    		appPacket.parseData(data);
    		
    		// dispatch the key value
    		List<ApplicationLayerKeyPacket> keyPackets = appPacket.getKeyPacketArrays();
    		for (ApplicationLayerKeyPacket keyPacket : keyPackets) {
    			byte commandId = appPacket.getCommandId();
    			byte keyId = keyPacket.getKey();
    			byte[] keyData = keyPacket.getKeyData();
    			if(D) Log.d(TAG, "onDataReceive, commandId: " + commandId + ", keyId: " + keyId);
    			// check the command id
    			switch(commandId) {
    			case CMD_IMAGE_UPDATE:
    				// check the key id
	    			switch(keyId) {
	    			case KEY_UPDATE_RESPONSE:
	    				mCallback.onUpdateCmdRequestEnterOtaMode(keyData[0], keyData[1]);
	    				break;
	    				
    				default:
    					if(D) Log.e(TAG, "onDataReceive, unknown key id: " + keyId);
    					break;
	    			}
	    			break;
	    		case CMD_SETTING:
	    			// check the key id
	    			switch(keyId) {
	    			case KEY_SETTING_GET_ALARM_LIST_RSP:
	    				// parse the alarm list
	    				ApplicationLayerAlarmsPacket alarms = new ApplicationLayerAlarmsPacket();
	    				alarms.parseData(keyData);
	    				mCallback.onSettingCmdRequestAlarmList(alarms);
	    				break;
                    case KEY_SETTING_NOTIFY_SWITCH_RSP:
                        mCallback.onSettingCmdRequestNotifySwitch(keyData[0]);
                        break;
                    case KEY_SETTING_SIT_TOOLONG_SWITCH_RSP:
                        mCallback.onSettingCmdRequestLongSit(keyData[0]);
                        break;

    				default:
    					if(D) Log.e(TAG, "onDataReceive, unknown key id: " + keyId);
    					break;
	    			}
	    			break;
	    		case CMD_BOND_REG:
	    			// check the key id 得到绑定设备和登陆设备的命令回复
	    			switch(keyId) {
	    			case KEY_BOND_RSP:
                        if(D) Log.d("requestLoginBond KEY_BOND_RSP", "KEY_BOND_RSP onDataReceive, data" +Arrays.toString(data));
	    				mCallback.onBondCmdRequestBond(keyData[0]);
	    				break;
	    			case KEY_LOGIN_RSP:
                        if(D) Log.d("requestLoginBond KEY_LOGIN_RSP", "KEY_LOGIN_RSP onDataReceive, data" +Arrays.toString(data));
	    				mCallback.onBondCmdRequestLogin(keyData[0]);
	    				break;
    				default:
    					if(D) Log.e(TAG, "onDataReceive, unknown key id: " + keyId);
    					break;
	    			}
	    			break;
	    		case CMD_SPORTS_DATA:
	    			// check the key id
	    			switch(keyId) {
	    			case KEY_SPORTS_RUNNIG_RSP:
	    				// parse the alarm list  运动数据返回
                        if(D) Log.d("requestSports", "onSportsDataReceive, data" +Arrays.toString(data));
	    				ApplicationLayerSportPacket sport = new ApplicationLayerSportPacket();
	    				sport.parseData(keyData);
	    				mCallback.onSportDataCmdSportData(sport);
	    				break;
	    			case KEY_SPORTS_SLEEP_RSP:
	    				// parse the alarm list  睡眠数据返回
	    				ApplicationLayerSleepPacket sleep = new ApplicationLayerSleepPacket();
	    				sleep.parseData(keyData);
	    				mCallback.onSportDataCmdSleepData(sleep);
	    				break;
	    			case KEY_SPORTS_RUNNIG_RSP_MORE:
                        //More flag,更多运动数据flag
	    				mCallback.onSportDataCmdMoreData();
	    				break;
	    			case KEY_SPORTS_SLEEP_SET_RSP:
	    				// parse the alarm list  睡眠设定数据返回
	    				ApplicationLayerSleepPacket sleepSet = new ApplicationLayerSleepPacket();
	    				sleepSet.parseData(keyData);
	    				mCallback.onSportDataCmdSleepSetData(sleepSet);
	    				break;
                    case KEY_SPORTS_HIS_SYNC_BEG:
                        //历史数据同步开始
                        mCallback.onSportDataCmdHistorySyncBegin();
                        break;
                    case KEY_SPORTS_HIS_SYNC_END:
                        // Here need check have total data or not  历史数据同步结束
                        ApplicationLayerTodaySumSportPacket todaySumSportPacket = new ApplicationLayerTodaySumSportPacket();

                        if(keyData.length != 0) {
                            if(!todaySumSportPacket.parseData(keyData)) {
                                todaySumSportPacket = null;
                            }
                        } else {
                            todaySumSportPacket = null;
                        }
                        mCallback.onSportDataCmdHistorySyncEnd(todaySumSportPacket);
                        break;
    				default:
    					if(D) Log.e(TAG, "onDataReceive, unknown key id: " + keyId);
    					break;
	    			}
	    			break;
                case CMD_FACTORY_TEST:
                    // check the key id
                    switch(keyId) {
                        case KEY_FAC_TEST_SENSOR_DATA_RSP:
                            // parse the alarm list
                            ApplicationLayerFacSensorPacket sensor = new ApplicationLayerFacSensorPacket();
                            sensor.parseData(keyData);
                            mCallback.onFACCmdSensorData(sensor);
                            break;
                        default:
                            if(D) Log.e(TAG, "onDataReceive, unknown key id: " + keyId);
                            break;
                    }
                    break;
    			default:
    				if(D) Log.e(TAG, "onDataReceive, unknown command id: " + commandId);
    				break;
    			}
    		}
    	}
        @Override
        public void onNameReceive(final String data) {
            mCallback.onNameReceive(data);
        }
    };
}
