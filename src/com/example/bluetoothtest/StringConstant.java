package com.example.bluetoothtest;

import android.os.Environment;

public class StringConstant {

    //send message 
    /**
     * 要取的文件打不开		PECMSG_GetFl_YERROR
     */
    public static final int TYPE_GetFl_YERROR = -1;
    /**
     *读文件  0xAA+0xAA+长度+”FLRD”+”e:\\pec\\4”+ CRCL+CRCH
     */
    public static final int TYPE_GETFI_FLRD = 0;
    /**
     * 要取的文件正常OK		PECMSG_GetFl_YELRDOK
     */
    public static final int TYPE_GetFl_YFLRDOK = 1;
    /**
     * 应答读文件 0xAA+0xAA+长度+”FLSD”+”e:\\pec\\4”（与读文件时的文件名相同+ CRCL+CRCH；
     */
    public static final int TYPE_FLSD = 2;
    /**
     * 应答读文件名返回 0xAA+0xAA+长度+”YFLSDOK”+ CRCL+CRCH；
     */
    public static final int TYPE_SndFlNm_YFLSDOK = 3;
    /**
     * 发送一帧数据：0xAA+0xAA+长度+”FD”+帧序号低位+帧序号帧高位+数据+CRCL+CRCH；
     */
    public static final int TYPE_FD = 4;
    /**
     * 应答帧数据 0xAA+0xAA+长度+”YFD”+帧序号低位+帧序号高位+ “OK” +0xFF+0xFF;
     */
    public static final int TYPE_SndData_YFD = 5;
    /**
     * 文件结束命令：0xAA+0xAA+长度+”FLSE”+CRCL+CRCH；
     */
    public static final int TYPE_FLSE = 6;
    /**
     * 应答文件结束命令0xAA+0xAA+长度 + “FLRE” + CRCL+CRCH；
     */
    public static final int TYPE_SndFlEnd_FLRE = 7;
    
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    public static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    public static final int REQUEST_ENABLE_BT = 3;
    public static String EXTRA_DEVICE_ADDRESS = "MAC";
}
