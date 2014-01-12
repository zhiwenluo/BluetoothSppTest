package bluetooth;

import static com.example.bluetoothtest.StringConstant.DEVICE_NAME;
import static com.example.bluetoothtest.StringConstant.EXTRA_DEVICE_ADDRESS;
import static com.example.bluetoothtest.StringConstant.MESSAGE_DEVICE_NAME;
import static com.example.bluetoothtest.StringConstant.MESSAGE_READ;
import static com.example.bluetoothtest.StringConstant.MESSAGE_STATE_CHANGE;
import static com.example.bluetoothtest.StringConstant.MESSAGE_TOAST;
import static com.example.bluetoothtest.StringConstant.MESSAGE_WRITE;
import static com.example.bluetoothtest.StringConstant.REQUEST_CONNECT_DEVICE_SECURE;
import static com.example.bluetoothtest.StringConstant.TOAST;
import android.R.string;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.bluetoothtest.BuildFrameUtil;
import com.example.bluetoothtest.PecData;
import com.example.bluetoothtest.R;
import com.example.bluetoothtest.SppMessage;
import com.example.bluetoothtest.StringConstant;

public class BluetoothSppClient {
    private static final int FRAME_MAX_NUM = 30;
    private static final int DATA_MAX_LEN = 252;
    private static final int FRAME_MAX_LEN = 255;
    private static final int FILE_BUF_LEN = (FRAME_MAX_NUM * DATA_MAX_LEN);
    private byte[][] glbFileDataBuf = new byte[FRAME_MAX_NUM][DATA_MAX_LEN];
    private int[] glbFileDataLen = new int[FRAME_MAX_NUM];
    private boolean isfinish = false;
    private PecData pecData;
    private String pin = "00000000";
    
    private String TAG = "BluetoothSppClient";
    private BluetoothChatService mChatService;
//    private BluetoothAdapter mBluetoothAdapter;
    private String mConnectedDeviceName;
    private Context mContext;
    private boolean hasGetFileName = false;
    
    public BluetoothSppClient(Context context, String mac) {
	this.mContext = context;
	this.mChatService = new BluetoothChatService(this.mContext, mHandler);
	this.connectDevice(mac);
    }
    
    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MESSAGE_STATE_CHANGE:
		if (true)
		    Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
		switch (msg.arg1) {
		case BluetoothChatService.STATE_CONNECTED:
		    System.out.println("hanlder---------->STATE_CONNECTED");
		    byte[] bytes = BuildFrameUtil.FrameBuid(
			    StringConstant.TYPE_GETFI_FLRD,
			    "e:\\pecfile.dat"/* "e:\\pecfile.dat" */, -1, -1);
		    sendMyMessage(bytes);
		    break;
		case BluetoothChatService.STATE_CONNECTING:
		    // mTitle.setText(R.string.title_connecting);
		    break;
		case BluetoothChatService.STATE_LISTEN:
		case BluetoothChatService.STATE_NONE:
		    System.out
			    .println("hanlder---------->STATE_LISTEN or STATE_NONE");
		    // mTitle.setText(R.string.title_not_connected);
		    if (mChatService != null)
			mChatService = null;
		    break;
		}
		break;
	    case MESSAGE_WRITE:
		byte[] writeBuf = (byte[]) msg.obj;
		// construct a string from the buffer
		break;
	    case MESSAGE_READ:
		byte[] readBuf = (byte[]) msg.obj;
		SppMessage sppMessage = BuildFrameUtil.AnalyseMyFrame(readBuf);
		hanlderMySppMessage(sppMessage);
		// construct a string from the valid bytes in the buffer

		break;
	    case MESSAGE_DEVICE_NAME:
		// save the connected device's name
		mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
		Toast.makeText(mContext,
			"Connected to " + mConnectedDeviceName,
			Toast.LENGTH_SHORT).show();
		break;
	    case MESSAGE_TOAST:
		Toast.makeText(mContext,
			msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
			.show();
		System.out.println("MESSAGE_DEVICE_NAME---------->"
			+ msg.getData().getString(TOAST));
		break;
	    }
	}
    };

    private void sendMyMessage(String message) {
	sendMyMessage(message.getBytes());
	return;
    }
    private void sendMyMessage(byte[] message) {
	// Check that we're actually connected before trying anything
	if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
	    Toast.makeText(mContext, "未连接", Toast.LENGTH_SHORT).show();
	    return;
	}
	// Check that there's actually something to send
	if (message.length > 0) {
	    mChatService.write(message);
	}
    }
    
    private void connectDevice(String MAC) {
	// 监控蓝牙配对请求,自动设置PIN值连接
	mContext.registerReceiver(_mPairingRequest, new IntentFilter(
		BluetoothCtrl.PAIRING_REQUEST));
	// 监控蓝牙配对是否成功
	mContext.registerReceiver(_mPairingRequest, new IntentFilter(
		BluetoothDevice.ACTION_BOND_STATE_CHANGED));
	// Get the BLuetoothDevice object
	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(MAC);
	if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
	    try {
		BluetoothCtrl.createBond(device); // 开始配对
		// result=true;
	    } catch (Exception e) {
		Log.d("mylog", "setPiN failed!");
		e.printStackTrace();
	    }
	}
	// Attempt to connect to the device
	this.mChatService.connect(device, true);
    }
    protected void hanlderMySppMessage(SppMessage sppMessage) {
	byte[] writeBytes;
	switch (sppMessage.getType()) {
	case StringConstant.TYPE_GetFl_YERROR:
	    System.out.println("halderSppMessage error");
	    // error
	    break;
	case StringConstant.TYPE_FLSD:
	    // 发送应答读文件返回命令
	    System.out.println("halderSppMessage 应答读文件");
	    writeBytes = BuildFrameUtil.FrameBuid(
		    StringConstant.TYPE_SndFlNm_YFLSDOK, "", -1, -1);
	    sendMyMessage(writeBytes);
	    break;
	case StringConstant.TYPE_FD:
	    // 发送应答帧命令
	    int NumFrameNow = (sppMessage.getHighFrame()) << 8;
	    NumFrameNow = NumFrameNow + sppMessage.getLowFrame();
	    ;
	    if (NumFrameNow >= FRAME_MAX_NUM) {
		// 发出消息停止获取
		break;
	    }
	    if (sppMessage.getDataLen() <= DATA_MAX_LEN) {
		// 存数据
		System.arraycopy(sppMessage.getData(), 0,
			this.glbFileDataBuf[NumFrameNow], 0, sppMessage.getDataLen());
		this.glbFileDataLen[NumFrameNow] = sppMessage.getDataLen();
		writeBytes = BuildFrameUtil.FrameBuid(
			StringConstant.TYPE_SndData_YFD, "",
			sppMessage.getLowFrame(), sppMessage.getHighFrame());
		sendMyMessage(writeBytes);
	    } else {
		// 停止获取
	    }
	    break;
	case StringConstant.TYPE_FLSE:
	    // 发送应答文件结束命令
	    writeBytes = BuildFrameUtil.FrameBuid(
		    StringConstant.TYPE_SndFlEnd_FLRE, "", -1, -1);
	    sendMyMessage(writeBytes);
	    int FileBufferWriteInd = 0;
	    byte[] FileBufferTmp = new byte[FILE_BUF_LEN + 1];
	    for (int i = 0; i < FRAME_MAX_NUM; i++) {
		if (FileBufferWriteInd + this.glbFileDataLen[i] > FILE_BUF_LEN) {
		    // 异常：FLSEWaitData收到的数据长度大于数据接收缓冲";
		}
		System.arraycopy(this.glbFileDataBuf[i], 0, FileBufferTmp,
			FileBufferWriteInd, this.glbFileDataLen[i]);
		FileBufferWriteInd += this.glbFileDataLen[i];
	    }
	    if (FileBufferWriteInd == 0) {
		// 异常：取回的文件的长度为0
		break;
	    }
	    if (!hasGetFileName) {
		String fileName = BuildFrameUtil.getDataFileName(FileBufferTmp,
			FileBufferWriteInd);
		if (fileName.equals("")) {
		    // 异常：FLSEWaitData收到的配置文件中没有可以读取的数据文件名称"
		} else {
		    writeBytes = BuildFrameUtil.FrameBuid(
			    StringConstant.TYPE_GETFI_FLRD,
			    fileName/* "e:\\pecfile.dat" */, -1, -1);
		    sendMyMessage(writeBytes);
		    System.out.println("halderSppMessage 读文件 " + fileName);
		    hasGetFileName = true;
		    this.glbFileDataBuf = new byte[FRAME_MAX_NUM][DATA_MAX_LEN];
		    this.glbFileDataLen = new int[FRAME_MAX_NUM];
		}
	    } else {
		// 格式化到的数据
		pecData = BuildFrameUtil.FormatPecData(FileBufferTmp,
			FileBufferWriteInd);
		if (pecData == null) {
		    // error
		} else {
		    pecData.printer();
		}
		this.isfinish = true;
	    }
	    break;
	default:
	    break;
	}
    }
    
    public PecData getPecData() {
	return this.pecData;
    }
    
    public boolean isFinished() {
	return this.isfinish;
    }
    public void close() {
	if (mChatService != null)
	    mChatService.stop();
	//关蓝牙
    }
    
    // 广播监听:蓝牙自动配对处理
    private BroadcastReceiver _mPairingRequest = new BroadcastReceiver() {
	@Override
	public void onReceive(Context context, Intent intent) {
	    BluetoothDevice device = null;
	    if (intent.getAction().equals(BluetoothCtrl.PAIRING_REQUEST)) { // 配对开始时的广播处理
		device = intent
			.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		try {
		    BluetoothCtrl.setPin(device, pin); // 置入配对密码
		} catch (Exception e) {
		    Log.d(TAG,
			    ">>_mPairingRequest err!");
		}
	    }
	}
    };
}
