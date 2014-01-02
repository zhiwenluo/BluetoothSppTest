package com.example.bluetoothtest;

import java.io.UnsupportedEncodingException;

import android.R.integer;
import android.R.string;
import bluetooth.CHexConver;

public class BuildFrameUtil {
    static int DATA_MAX_LEN = 128;

    static char auchCRCHi[] = { 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
	    0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80,
	    0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
	    0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00,
	    0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
	    0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81,
	    0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1,
	    0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01,
	    0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
	    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80,
	    0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
	    0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01,
	    0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
	    0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80,
	    0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1,
	    0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00,
	    0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
	    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81,
	    0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
	    0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00,
	    0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
	    0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80,
	    0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
	    0x80, 0x41, 0x00, 0xC1, 0x81, 0x40 };

    static char auchCRCLo[] = { 0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2,
	    0xC6, 0x06, 0x07, 0xC7, 0x05, 0xC5, 0xC4, 0x04, 0xCC, 0x0C, 0x0D,
	    0xCD, 0x0F, 0xCF, 0xCE, 0x0E, 0x0A,  0xCA, 0xCB, 0x0B, 0xC9, 0x09,
	    0x08, 0xC8, 0xD8, 0x18, 0x19, 0xD9, 0x1B, 0xDB, 0xDA, 0x1A, 0x1E,
	    0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C, 0xDC, 0x14, 0xD4, 0xD5, 0x15,
	    0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13, 0xD3, 0x11, 0xD1, 0xD0,
	    0x10, 0xF0, 0x30, 0x31, 0xF1, 0x33, 0xF3, 0xF2, 0x32, 0x36, 0xF6,
	    0xF7, 0x37, 0xF5, 0x35, 0x34, 0xF4, 0x3C, 0xFC, 0xFD, 0x3D, 0xFF,
	    0x3F, 0x3E, 0xFE, 0xFA, 0x3A, 0x3B, 0xFB, 0x39, 0xF9, 0xF8, 0x38,
	    0x28, 0xE8, 0xE9, 0x29, 0xEB, 0x2B, 0x2A, 0xEA, 0xEE, 0x2E, 0x2F,
	    0xEF, 0x2D, 0xED, 0xEC, 0x2C, 0xE4, 0x24, 0x25, 0xE5, 0x27, 0xE7,
	    0xE6, 0x26, 0x22, 0xE2, 0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0, 0xA0,
	    0x60, 0x61, 0xA1, 0x63, 0xA3, 0xA2, 0x62, 0x66, 0xA6, 0xA7, 0x67,
	    0xA5, 0x65, 0x64, 0xA4, 0x6C, 0xAC, 0xAD, 0x6D, 0xAF, 0x6F, 0x6E,
	    0xAE, 0xAA, 0x6A, 0x6B, 0xAB, 0x69, 0xA9, 0xA8, 0x68, 0x78, 0xB8,
	    0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA, 0xBE, 0x7E, 0x7F, 0xBF, 0x7D,
	    0xBD, 0xBC, 0x7C, 0xB4, 0x74, 0x75, 0xB5, 0x77, 0xB7, 0xB6, 0x76,
	    0x72, 0xB2, 0xB3, 0x73, 0xB1, 0x71, 0x70, 0xB0, 0x50, 0x90, 0x91,
	    0x51, 0x93, 0x53, 0x52, 0x92, 0x96, 0x56, 0x57, 0x97, 0x55, 0x95,
	    0x94, 0x54, 0x9C, 0x5C, 0x5D, 0x9D, 0x5F, 0x9F, 0x9E, 0x5E, 0x5A,
	    0x9A, 0x9B, 0x5B, 0x99, 0x59, 0x58, 0x98, 0x88, 0x48, 0x49, 0x89,
	    0x4B, 0x8B, 0x8A, 0x4A, 0x4E, 0x8E, 0x8F, 0x4F, 0x8D, 0x4D, 0x4C,
	    0x8C, 0x44, 0x84, 0x85, 0x45, 0x87, 0x47, 0x46, 0x86, 0x82, 0x42,
	    0x43, 0x83, 0x41, 0x81, 0x80, 0x40 };

    
    /**	通过接收到的数据得到下一步要读的文件名
     * @param data	接收到的数据
     * @return	要读的文件名字符串
     */
    public static String getDataFileName(byte[] data) {
	String fileName = "";
	int dataLength = data.length;
	if(dataLength == 0)
	    return fileName;
	int lastSpaceIndex = 0;
	for (lastSpaceIndex = 0; lastSpaceIndex < data.length; lastSpaceIndex++) {
	    if (data[dataLength - 1 - lastSpaceIndex] == ' ' ) 
		break;
	}
	if(lastSpaceIndex == dataLength || lastSpaceIndex <= 2)
	    return fileName;
	int index = 0 ;
	byte[] fileNameTemp = new byte[lastSpaceIndex-2];
	for (index = 0; index < lastSpaceIndex ; index++) {
	    if (data[dataLength - lastSpaceIndex + index] != 13) {
		fileNameTemp[index] = data[dataLength - lastSpaceIndex + index];
	    }
	    else 
		break;
	}
	if (index != lastSpaceIndex -2) 
	    return fileName;
	
	fileName = "e:\\pec\\" + CHexConver.decode(CHexConver.printHexString("", fileNameTemp));
	return fileName;
    }
    /**
     * 根据type类型建立要发送的数据，并转换成byte数组
     * 
     * @param type
     *            要生成的消息类型
     * @param fileName
     *            FLRD 时所需的 文件名
     * @param lowFrame
     *            YFD 时所需的 帧低位
     * @param highFrame
     *            YFD 时所需的 帧高位
     * @return 转换成功后建立的message的byte数组
     */
    public static byte[] buildMyMessage(int type, String fileName,
	    int lowFrame, int highFrame) {
	String string = "";
	switch (type) {
	case StringConstant.TYPE_GETFI_FLRD:
	    string = "FLRD" + fileName;
	    break;
	case StringConstant.TYPE_SndFlNm_YFLSDOK:
	    string = "YFLSDOK";
	    break;
	case StringConstant.TYPE_SndData_YFD:
	    byte[] messageByte = new byte[7];
	    System.arraycopy("YFD".getBytes(), 0, messageByte, 0, 3);
	    messageByte[3] = (byte) lowFrame;
	    messageByte[4] = (byte) highFrame;
	    System.arraycopy("OK".getBytes(), 0, messageByte, 5, 2);
	    return messageByte;
	case StringConstant.TYPE_SndFlEnd_FLRE:
	    string = "FLRE";
	    break;
	default:
	    break;
	}
	return string.getBytes();
    }

    /**
     * 计算CRC校验位
     * 
     * @param message
     *            根据message的内容和长度来计算
     * @return 返回计算号的CRC ， crcBytes[0] = CRCL , crcBytes[1] = CRCH
     */
    public static byte[] calculateCRC(byte[] message) {
	byte[] crcBytes = new byte[2];
	char uchCRCHi = 0xFF;
	char uchCRCLo = 0xFF;
	int uIndex;
	int len = message.length;
	for (int i = 0; i < len; i++) {
	    uIndex = uchCRCHi ^ (char) message[i];
	    uchCRCHi = (char) (uchCRCLo ^ auchCRCHi[uIndex]);
	    uchCRCLo = auchCRCLo[uIndex];
	}
	System.out.println(uchCRCLo + "<----->" + uchCRCHi);
	crcBytes[0] = (byte) uchCRCLo;
	crcBytes[1] = (byte) uchCRCHi;
	return crcBytes;
    }
    	
    /**通过message和CRC来建立帧
     * @param message	建立好的message
     * @param crcbytes	计算好的CRC检验位, crcbytes[0] 为CRCL , crcbytes[1] 为CRCH
     * @return	返回建立好的帧Frame, byte数组形式 
     */
    public static byte[] buildMyFrame(byte[] message , byte[] crcbytes) {
	byte[] myFrame = new byte[DATA_MAX_LEN];
	int index = 3;
	if (message.length > DATA_MAX_LEN) {
	    System.out.println("message.length > 128");
	    return null;
	}
	//插入帧头
	myFrame[0] = (byte)0xAA;
	myFrame[1] = (byte)0xAA;
	//插入数据位
	for (int i = 0 ; i < message.length; i++, index++) {
	    myFrame[index] = message[i];
	    if (message[i] ==  ((byte)0xAA)) {
		 index++;
		myFrame[index] = (byte)0x01;
	    }
	}
	//插入帧低位
	myFrame[index] = crcbytes[0];
	index++;
	if (crcbytes[0] == (byte)0xAA) {
	    myFrame[index] = (byte) 0x01;
	    index++;
	}
	//插入帧高位
	myFrame[index] = crcbytes[1];
	if (crcbytes[1] == (byte)0xAA) {
	    index++;
	    myFrame[index] = (byte) 0x01;
	}
	//插入长度位
	myFrame[2] = (byte) (index - 3 + 1);
	byte[] resultFrame = new byte[index+1];
	System.arraycopy(myFrame, 0, resultFrame, 0, index+1);
	return resultFrame;
    }
    
    /**	建立帧
     * @param type	数据类型
     * @param fileName	文件名
     * @param lowFrame	帧低位
     * @param highFrame	帧高位
     * @return	建立好的帧
     */
    public static byte[] FrameBuid(int type, String fileName,
	    int lowFrame, int highFrame) {
	byte[] messageBytes = buildMyMessage(type, fileName, lowFrame, highFrame);//Messgae
	byte[] CRCbytes = new byte[2];//CRC
	if (type == StringConstant.TYPE_SndData_YFD) {
	    CRCbytes[0] = (byte) 0xFF;
	    CRCbytes[1] = (byte) 0xFF;
	}
	else
	    CRCbytes = calculateCRC(messageBytes);
	byte[] frameBytes = buildMyFrame(messageBytes, CRCbytes);//Frame
	return frameBytes;
    }
    
    /**解析接收到的帧数据，取出message
     * @param receivedFrame	接收到的帧数据
     * @return	返回取出的message部分，byte数组形式 || null
     */
    public static byte[] FrameAnalyse(byte[] receivedFrame) {
	byte[] receivedMessage = null;
	byte[] crcBytes = new byte[2];
	int frameLength = receivedFrame.length;
	int length = receivedFrame[2];
	//起始符检查
	if((receivedFrame[0] != (byte)0xAA) && (receivedFrame[1] != (byte)0xAA))
	    return null;
	//长度合法性检查
	if( (length > DATA_MAX_LEN ) || (length > frameLength)) 
	    return null;
	//CRC与长度分析
	if (receivedFrame[frameLength - 2] == (byte) 0xAA) {
	    crcBytes[1] = (byte) 0xAA;
	    if (receivedFrame[frameLength-4] == (byte) 0xAA) {
		length -= 4;
		crcBytes[0] = (byte) 0xAA;
	    }else {
		crcBytes[0] = receivedFrame[frameLength -3] ;
		length -= 3;
	    }
	}else {
	    crcBytes[1] = receivedFrame[frameLength -1];
	    if (receivedFrame[frameLength - 3] == (byte)0xAA) {
		length -= 3;
		crcBytes[0] = (byte) 0xAA;
	    }else {
		crcBytes[0] = receivedFrame[frameLength -2];
		length -= 2;
	    }
	}
	receivedMessage = new byte[length];
	int j = 0 ;
	for (int i = 0; i < length; i++) {
	    receivedMessage[i] = receivedFrame[i + 3];
	    j++;
	    if(receivedMessage[i] == (byte) 0xAA)
		i++;
	}
	byte[] message = new byte[j];
	//System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length) 截取数组
	System.arraycopy(receivedMessage, 0, message, 0, j );
	return message;
    }
    
    /**根据由帧解析出来的Message进一步解析出里面的数据部分
     * @param message	解析出来的message
     * @return	SppMessage类型 || null，由解析Message得来 
     */
    public static SppMessage MessageAnalyse(byte[] message) {
	SppMessage sppMessage ;
	int sDataLen = message.length;
	//FLSD
	if((message[0]=='F') && (message[1] == 'L') && (message[2] == 'S') && (message[3] == 'D')) {
	    sppMessage = new SppMessage(StringConstant.TYPE_FLSD, 0, 0, sDataLen - 4);
	    if(sDataLen - 4 < 0 || sDataLen > DATA_MAX_LEN) 
		return null;
	    byte[] data = new byte[sppMessage.getDataLen()];
	    System.arraycopy(message, 4, data, 0, sppMessage.getDataLen());
	    sppMessage.setData(data);
	}
	//FD
	else if ((message[0] == 'F') && (message[1] == 'D')) {
	    sppMessage = new SppMessage(StringConstant.TYPE_FD, message[2], message[3], sDataLen - 4);
	    if(sDataLen - 4 < 0 || sDataLen-4 > DATA_MAX_LEN-2) 
		return null;
	    byte[] data = new byte[sppMessage.getDataLen()];
	    System.arraycopy(message, 4,  data, 0, sppMessage.getDataLen());
	    sppMessage.setData(data);
	}
	//FLSE
	else if ((message[0]=='F') && (message[1] == 'L') && (message[2] == 'S') && (message[3] == 'E')) {
	    sppMessage = new SppMessage(StringConstant.TYPE_FLSE,0, 0, 0);
	}
	//YFLRDOK
	else if ((message[0]=='Y') &&(message[1]=='F') && (message[2] == 'L') && (message[3] == 'R') && (message[4] == 'D')
		&& (message[5] == 'O')&& (message[6] == 'K')) 
	{
	    sppMessage = new SppMessage(StringConstant.TYPE_GetFl_YFLRDOK,0, 0, 0);
	}
	//YERROR
	else if ((message[0]=='Y') &&(message[1]=='E') && (message[2] == 'R') && (message[3] == 'R') && (message[4] == 'O')
		&& (message[5] == 'R')) 
	{
	    sppMessage = new SppMessage(StringConstant.TYPE_GetFl_YERROR,0, 0, 0);
	}
	else {
	    System.out.println("MessageAnalyse is null");
	    return new SppMessage(-1, 0, 0, 0);
	}
	return sppMessage;
    }
    
    /** 封装的函数解析帧数据
     * @param frame
     * @return
     */
    public static SppMessage AnalyseMyFrame(byte[] frame) {
	byte[] message = FrameAnalyse(frame);
	CHexConver.printHexString("", message);
	SppMessage sppMessage ;
	if (message != null) {
	    sppMessage = MessageAnalyse(message);
	}else {
	    sppMessage = new SppMessage(-1, 0, 0, 0);;
	    System.out.println("message is null");
	}
	return sppMessage;
    }
    
    /**	格式化解析后的message数据
     * @return	需要的数据,已PecData类型保存
     */
    public PecData FormatPecData() {
	PecData pecData = new PecData();
	return pecData;
    }
}
