package com.example.bluetoothtest;

import static com.example.bluetoothtest.StringConstant.DEVICE_NAME;
import static com.example.bluetoothtest.StringConstant.EXTRA_DEVICE_ADDRESS;
import static com.example.bluetoothtest.StringConstant.MESSAGE_DEVICE_NAME;
import static com.example.bluetoothtest.StringConstant.MESSAGE_READ;
import static com.example.bluetoothtest.StringConstant.MESSAGE_STATE_CHANGE;
import static com.example.bluetoothtest.StringConstant.MESSAGE_TOAST;
import static com.example.bluetoothtest.StringConstant.MESSAGE_WRITE;
import static com.example.bluetoothtest.StringConstant.REQUEST_CONNECT_DEVICE_INSECURE;
import static com.example.bluetoothtest.StringConstant.REQUEST_CONNECT_DEVICE_SECURE;
import static com.example.bluetoothtest.StringConstant.REQUEST_ENABLE_BT;
import static com.example.bluetoothtest.StringConstant.TOAST;

import java.security.PublicKey;

import android.R.string;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import bluetooth.BluetoothChatService;
import bluetooth.CHexConver;
import bluetooth.DiscoveryDevicesActivity;

public class MainActivity extends Activity {
    private static final int FRAME_MAX_NUM = 30;
    private static final int DATA_MAX_LEN = 252;
    private static final int FRAME_MAX_LEN = 255;
    private static final int FILE_BUF_LEN = (FRAME_MAX_NUM*DATA_MAX_LEN);
    private byte[][] glbFileDataBuf = new byte[FRAME_MAX_NUM][DATA_MAX_LEN];
    private int[] glbFileDataLen = new int[FRAME_MAX_NUM];
    private boolean D = true;
    private boolean hasGetFileName = false;
    int lowFrame = 0x00 ;
    private String TAG = "this";
    private ListView listView;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothChatService mChatService;
    private String mConnectedDeviceName;
    private ArrayAdapter mConversationArrayAdapter;
    private StringBuffer mOutStringBuffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	
//	byte[] bytes = BuildFrameUtil.buildMyMessage(StringConstant.TYPE_GETFI_FLRD, "e:\\pec\\4", -1, -1);
//	byte[] bytess = BuildFrameUtil.calculateCRC(bytes);
//	byte[] bytesss = BuildFrameUtil.buildMyFrame(bytes, bytess);
//	byte[] bytessss = BuildFrameUtil.FrameAnalyse(bytesss);
//	CHexConver.printHexString("analyseMessage---->", bytessss);
	Button button = (Button) findViewById(R.id.button);
	Button replyFDButton = (Button) findViewById(R.id.replayFD);
	Button replyFLSDButton = (Button) findViewById(R.id.replayFLSD);
	Button replyFLREButton = (Button) findViewById(R.id.replayFLRE);
	replyFLSDButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		byte[] writeBytes = BuildFrameUtil.FrameBuid(StringConstant.TYPE_SndFlNm_YFLSDOK, "", -1, -1);
		sendMyMessage(writeBytes);
	    }
	});
	replyFDButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		if (lowFrame <= 0x11) {
		    byte[] writeBytes = BuildFrameUtil.FrameBuid(StringConstant.TYPE_SndData_YFD, "", lowFrame,0x00);
		    sendMyMessage(writeBytes);
		    lowFrame++;
		}
	    }
	});
	replyFLREButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
                byte[] writeBytes = BuildFrameUtil.FrameBuid(StringConstant.TYPE_SndFlEnd_FLRE, "", -1, -1);
                sendMyMessage(writeBytes);
	    }
	});
	listView = (ListView) findViewById(R.id.listview);
	button.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    Toast.makeText(getApplicationContext(), "蓝牙不可用..", Toast.LENGTH_LONG).show();
		}else {
		if (!mBluetoothAdapter.isEnabled()) {
	            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
	            }else {
	        	if (mChatService == null) setupChat();
		    }
		}
	    }

	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }

    private void setupChat() {
        System.out.println("setupChat---------->");
	mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_message_item);
	listView.setAdapter(mConversationArrayAdapter);
	// Initialize the BluetoothChatService to perform bluetooth connections
	mChatService = new BluetoothChatService(this, mHandler);
	// Initialize the buffer for outgoing messages
	mOutStringBuffer = new StringBuffer("");
	Intent serverIntent = null;
	serverIntent = new Intent(this, DiscoveryDevicesActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
    }

    
    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
//                    mTitle.setText(R.string.title_connected_to);
//                    mTitle.append(mConnectedDeviceName);
                    System.out.println("hanlder---------->STATE_CONNECTED");
                    mConversationArrayAdapter.clear();
                    byte[] bytes = BuildFrameUtil.FrameBuid(StringConstant.TYPE_GETFI_FLRD, "e:\\pecfile.dat"/*"e:\\pecfile.dat"*/, -1, -1);
                    sendMyMessage(bytes);
                    break;
                case BluetoothChatService.STATE_CONNECTING:
//                    mTitle.setText(R.string.title_connecting);

                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                    System.out.println("hanlder---------->STATE_LISTEN or STATE_NONE");
//                    mTitle.setText(R.string.title_not_connected);
                    if(mChatService != null ) mChatService =null;
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                SppMessage sppMessage = BuildFrameUtil.AnalyseMyFrame(readBuf);
                hanlderMySppMessage(sppMessage);
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);

                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                System.out.println("MESSAGE_DEVICE_NAME---------->"+mConnectedDeviceName);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                System.out.println("MESSAGE_DEVICE_NAME---------->"+ msg.getData().getString(TOAST));
                break;
            }
        }
    };
    
    private void sendMyMessage(String message) {
	sendMyMessage(message.getBytes());
	return;
    }
    protected void hanlderMySppMessage(SppMessage sppMessage) {
	byte[] writeBytes;
	switch (sppMessage.getType()) {
	case StringConstant.TYPE_GetFl_YERROR:
	    System.out.println("halderSppMessage error");
	    //error
	    break;
	case StringConstant.TYPE_FLSD:
	    //发送应答读文件返回命令
	    System.out.println("halderSppMessage 应答读文件");
		writeBytes = BuildFrameUtil.FrameBuid(StringConstant.TYPE_SndFlNm_YFLSDOK, "", -1, -1);
		sendMyMessage(writeBytes);
	    break;
	case StringConstant.TYPE_FD:
	    //发送应答帧命令
	    int NumFrameNow = (sppMessage.getHighFrame())<<8;
	    NumFrameNow = NumFrameNow + sppMessage.getLowFrame();;
	    if (NumFrameNow >= FRAME_MAX_NUM) {
		//发出消息停止获取
		break;
	    }
	    if (sppMessage.getDataLen() <= DATA_MAX_LEN) {
		//存数据
		System.arraycopy(sppMessage.getData(), 0, glbFileDataBuf[NumFrameNow], 0, sppMessage.getDataLen());
		glbFileDataLen[NumFrameNow] = sppMessage.getDataLen();
		writeBytes = BuildFrameUtil.FrameBuid(StringConstant.TYPE_SndData_YFD, "", sppMessage.getLowFrame(),sppMessage.getHighFrame());
		sendMyMessage(writeBytes);
	    }else {
		//停止获取
	    }
	    break;
	case StringConstant.TYPE_FLSE:
	    //发送应答文件结束命令
            writeBytes = BuildFrameUtil.FrameBuid(StringConstant.TYPE_SndFlEnd_FLRE, "", -1, -1);
            sendMyMessage(writeBytes);
	    int FileBufferWriteInd = 0;
	    byte[] FileBufferTmp = new byte[FILE_BUF_LEN+1];
	    for (int i = 0; i < FRAME_MAX_NUM; i++) {
		if (FileBufferWriteInd + glbFileDataLen[i] > FILE_BUF_LEN) {
		    //异常：FLSEWaitData收到的数据长度大于数据接收缓冲";
		}
		System.arraycopy(glbFileDataBuf[i], 0 , FileBufferTmp , FileBufferWriteInd, glbFileDataLen[i]);
		FileBufferWriteInd += glbFileDataLen[i];	
	    }
	    if (FileBufferWriteInd == 0) {
		//异常：取回的文件的长度为0
		break;
	    }
	    if (hasGetFileName) {
		String fileName = BuildFrameUtil.getDataFileName(FileBufferTmp,FileBufferWriteInd);
		if (fileName.equals("")) {
		    //异常：FLSEWaitData收到的配置文件中没有可以读取的数据文件名称"
		}else {
		writeBytes = BuildFrameUtil.FrameBuid(StringConstant.TYPE_GETFI_FLRD, fileName/*"e:\\pecfile.dat"*/, -1, -1);
		sendMyMessage(writeBytes);
		System.out.println("halderSppMessage 读文件 "+fileName);
		hasGetFileName = true;
		glbFileDataBuf = new byte[FRAME_MAX_NUM][DATA_MAX_LEN];
		glbFileDataLen = new int[FRAME_MAX_NUM];
		}
	    }else {
		//格式化到的数据
	    }
	    break;
	default:
	    break;
	}
    }

    private void sendMyMessage(byte[] message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, "未连接", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length > 0) {
            mChatService.write(message);
            
            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE_SECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, true);
                System.out.println("REQUEST_CONNECT_DEVICE_SECURE------>OK");
            }else {
                System.out.println("REQUEST_CONNECT_DEVICE_SECURE------>CANCLE");
		mChatService.stop();
		mChatService = null;
	    }
            break;
        case REQUEST_CONNECT_DEVICE_INSECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, false);
        	System.out.println("REQUEST_CONNECT_DEVICE_INSECURE------>OK");
            }else {
        	System.out.println("REQUEST_CONNECT_DEVICE_INSECURE------>CANCLE");
	    }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                setupChat();
            } else {
                // User did not enable Bluetooth or an error occured
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, "无法打开蓝牙", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
            .getString(EXTRA_DEVICE_ADDRESS);
        // Get the BLuetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }
    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	if(mChatService != null ) mChatService.stop();
	super.onDestroy();
    }
}
