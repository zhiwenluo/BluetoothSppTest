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
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
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
import bluetooth.BluetoothCtrl;
import bluetooth.BluetoothSppClient;
import bluetooth.CHexConver;
import bluetooth.DiscoveryDevicesActivity;

public class MainActivity extends Activity {
    private boolean D = true;
    private String TAG = "this";
    private ListView listView;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter mConversationArrayAdapter;
    private StringBuffer mOutStringBuffer;
    private BluetoothSppClient bluetoothSppClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	
	Button button = (Button) findViewById(R.id.button);

	listView = (ListView) findViewById(R.id.listview);
	button.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    Toast.makeText(getApplicationContext(), "蓝牙不可用..",
			    Toast.LENGTH_LONG).show();
		} else {
		    if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
				BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		    } else {
			    setupChat();
		    }
		}
	    }

	});
    }


    private void setupChat() {
	System.out.println("setupChat---------->");
	mConversationArrayAdapter = new ArrayAdapter<String>(this,
		R.layout.bluetooth_message_item);
	listView.setAdapter(mConversationArrayAdapter);
	// Initialize the buffer for outgoing messages
	mOutStringBuffer = new StringBuffer("");
	Intent serverIntent = null;
	serverIntent = new Intent(this, DiscoveryDevicesActivity.class);
	startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (D)
	    Log.d(TAG, "onActivityResult " + resultCode);
	switch (requestCode) {
	case REQUEST_CONNECT_DEVICE_SECURE:
	    // When DeviceListActivity returns with a device to connect
	    if (resultCode == Activity.RESULT_OK) {
		String address = data.getExtras().getString(EXTRA_DEVICE_ADDRESS);
		bluetoothSppClient = new BluetoothSppClient(MainActivity.this, address);
		System.out.println("REQUEST_CONNECT_DEVICE_SECURE------>OK");
	    } else {
		System.out
			.println("REQUEST_CONNECT_DEVICE_SECURE------>CANCLE");
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

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
    }

}
