package com.example.bluetoothtest;

public class PecData {

    private DataFlag[] dataFlag;
    private byte[] stTimer;
    
    public PecData() {
	this.dataFlag = new DataFlag[DataInd.DATA_IND_MAX];
	this.stTimer = new byte[50];
    }
    
    public void setDataFlags(DataFlag[] newDataFlag) {
	if (newDataFlag.length > DataInd.DATA_IND_MAX)
	    return;
	for (int i = 0; i < newDataFlag.length; i++) {
	    this.dataFlag[i].setDataFlag(newDataFlag[i]);
	}
    }
    
    public void setTimer(byte[] newTimer) {
	if (newTimer.length > 50) {
	    return;
	}
	System.arraycopy(newTimer, 0, this.stTimer, 0, newTimer.length);
    }
    
    public DataFlag[] getDataFlags() {
	return this.dataFlag;
    }
    
    public byte[] getTimer() {
	return this.stTimer;
    }
    /*
    float Ua;
    float Ia;
    float Ub;
    float Ib;
    float Uc;
    float Ic;

    float Pa;//A向有功功率
    float Pb;
    float Pc;
    float Qa;//A向无功功率
    float Qb;
    float Qc;

    float Aa; //A向相角
    float Ab;
    float Ac;

    float Pabc;//abc有功总功率
    float Qabc;//abc向无功功率总和

    float Uab;//AB向电压角度
    float Uac;

    float Iac;//AC向电流角度
    float Cos;//功率因素；
    float Fre;//频率;
    float Ucb;*/

}
