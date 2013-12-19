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

    private boolean D = true;
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
//	CHexConver.printHexString("CRC---->", bytessss);
	
	Button button = (Button) findViewById(R.id.button);
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
                    byte[] bytes = new byte[] {(byte) 0xaa,
                	    					(byte) 0xaa,
                	    					(byte) 0x14,
                	    					(byte) 0x46,
                	    					(byte) 0x4c,
                	    					(byte) 0x52,
                	    					(byte) 0x44,
                	    					(byte) 0x65,
                	    					(byte) 0x3a,
                	    					(byte) 0x5c,
                	    					(byte) 0x70,
                	    					(byte) 0x65,
                	    					(byte) 0x63,
                	    					(byte) 0x66,
                	    					(byte) 0x69,
                	    					(byte) 0x6c,
                	    					(byte) 0x65,
                	    					(byte) 0x2e,
                	    					(byte) 0x64,
                	    					(byte) 0x61,
                	    					(byte) 0x74,
                	    					(byte) 0xf2,
                	    					(byte) 0x3b,
                    						};
                    sendMyMessage(bytes);
//                    byte[] bytess = CHexConver.getHexBytes("FLRD");
//                    System.out.println(bytess[0]);
//                    sendMyMessage(new byte[]{(byte) 0xAA,(byte)0xAA,(byte)0x03});
//                    sendMyMessage(new String("FLRD").getBytes());(byte) 
//                    sendMyMessage(new byte[]{(byte)0x00,(byte)0x00});
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
                System.out.println("MESSAGE_WRITE---------->");
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
                System.out.println("MESSAGE_READ---------->");
                byte[] readBuf = (byte[]) msg.obj;
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
