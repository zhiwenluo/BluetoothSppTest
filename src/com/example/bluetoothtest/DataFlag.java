package com.example.bluetoothtest;

public class DataFlag {
	private double Data;
	private boolean Flag;
	public void setData(double data) {
	    this.Data = data;
	}
	
	public void setFlag(boolean flag) {
	    this.Flag = flag;
	}
	
	public double getData() {
	    return this.Data;
	}
	
	public boolean getFlag() {
	    return this.Flag;
	}
	
	public void setDataFlag(DataFlag dataFlag) {
	    this.Data = dataFlag.getData();
	    this.Flag = dataFlag.getFlag();
	}
}
