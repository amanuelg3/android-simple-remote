package com.mplourde.remote.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mplourde.remote.R;
import com.mplourde.remote.client.communication.BluetoothSocketContainer;
import com.mplourde.remote.client.communication.SocketManager;

/**
 * 
 * 
 * Icons source:
 * http://www.softicons.com/free-icons/system-icons/hydropro-icons-
 * by-mediadesign/
 * 
 * @author Mathieu
 * 
 */
public class MainMenuActivity extends Activity {

    private static int BLUETOOTH_ACTIVATION = 1;

    private BluetoothAdapter mBluetoothAdapter = null;
    private List<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.mainmenu);

	RelativeLayout rl = (RelativeLayout) findViewById(R.id.bluetoothLayout);

	rl.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		if (!selectBluetoothDevice()) {
		    Toast toast = Toast.makeText(getApplicationContext(),
			    "Your phone does not support bluetooth.",
			    Toast.LENGTH_SHORT);
		    toast.show();
		}
	    }
	});
    }

    private boolean selectBluetoothDevice() {
	boolean success = true;

	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	if (mBluetoothAdapter != null) {
	    if (!mBluetoothAdapter.isEnabled()) {
		Intent enableBtIntent = new Intent(
			BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(enableBtIntent, BLUETOOTH_ACTIVATION);
	    } else {
		showBluetoothDevices();
	    }

	    success = true;
	} else {
	    success = false;
	}

	return success;
    }

    private void showBluetoothDevices() {
	Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
		.getBondedDevices();

	final CharSequence[] items = new CharSequence[pairedDevices.size()];

	// If there are paired devices
	if (pairedDevices.size() > 0) {
	    int i = 0;
	    // Loop through paired devices
	    for (BluetoothDevice device : pairedDevices) {
		// Add the name and address to an array adapter to
		mDevices.add(device);
		items[i] = device.getName() + "\n" + device.getAddress();
		i++;
	    }
	}

	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("Pick a device");
	builder.setItems(items, new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int item) {
		BluetoothDevice bd = mDevices.get(item);
		try {
		    BluetoothSocket bts = bd.createRfcommSocketToServiceRecord(UUID
			    .fromString("60b13880-4dd7-11e1-b86c-0800200c9a66"));
		    bts.connect();

		    SocketManager.getInstance().setSocketContainer(
			    new BluetoothSocketContainer(bts));

		    Intent intent = new Intent(MainMenuActivity.this,
			    TrackpadActivity.class);
		    startActivity(intent);

		} catch (IOException e) {
		    Toast.makeText(getApplicationContext(),
			    "Failed to connect to the server",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});
	AlertDialog alert = builder.create();
	alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (requestCode == BLUETOOTH_ACTIVATION) {
	    if (resultCode != 0) {
		showBluetoothDevices();
	    }
	}
    }

}
