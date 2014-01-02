package com.example.bluetoothtest;

import bluetooth.CHexConver;

public class SppMessage {
    private int type;
    private int lowFrame;
    private int highFrame;
    private int dataLen;
    private byte[] data;
    
    public SppMessage(int type , int lowFrame , int highFrame , int dataLen) {
	this.type  = type;
	this.lowFrame = lowFrame;
	this.highFrame = highFrame;
	this.dataLen = dataLen;
	this.data = new byte[dataLen];
    }
    
    public SppMessage(int type , int lowFrame , int highFrame ) {
	this.type  = type;
	this.lowFrame = lowFrame;
	this.highFrame = highFrame;
    }
    
    public int getType() {
	return this.type;
    }
    
    public int getLowFrame() {
	return this.lowFrame;
    }
    
    public int getHighFrame() {
	return this.highFrame;
    }
    
    public int getDataLen() {
	return this.dataLen;
    }
    
    public byte[] getData() {
	return this.data;
    }
    
    public void setData(byte[] data) {
	this.data = new byte[data.length];
	this.dataLen = data.length;
	System.arraycopy(data, 0, this.data, 0, this.dataLen);
    }
    
    public void print() {
	System.out.print("SppMessage:");
	System.out.println("type = " + this.type + ", lowFrame = " + this.lowFrame + ",highFram = " + this.highFrame + ", dataLen = " + this.dataLen);
	String string = CHexConver.printHexString("\t data = ", this.data);
        //以16进制打印出来接收到的message
      System.out.println(CHexConver.decode(string));
    }
}
