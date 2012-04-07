package com.mplourde.remote.client;

import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;

import com.mplourde.remote.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class RemoteInputActivity extends Activity {
    /** Called when the activity is first created. */

    private float initialX;
    private float initialY;

    private float tapX;
    private float tapY;
    private long time;

    private PrintWriter out = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	TextView tv = (TextView) findViewById(R.id.touchView);

	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
		.getDefaultAdapter();
	if (mBluetoothAdapter != null) {
	    if (!mBluetoothAdapter.isEnabled()) {
		Intent enableBtIntent = new Intent(
			BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(enableBtIntent, 1);
	    }

	    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
		    .getBondedDevices();
	    // If there are paired devices
	    if (pairedDevices.size() > 0) {
		// Loop through paired devices
		for (BluetoothDevice device : pairedDevices) {
		    // Add the name and address to an array adapter to show in a
		    // ListView
		    tv.setText(tv.getText() + device.getName() + "\n"
			    + device.getAddress() + "\n");

		    try {
			BluetoothSocket bts = device
				.createRfcommSocketToServiceRecord(UUID
					.fromString("60b13880-4dd7-11e1-b86c-0800200c9a66"));
			bts.connect();
			out = new PrintWriter(bts.getOutputStream(), true);
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
	    }

	} else {
	    // Device does not support Bluetooth
	}

	// try {
	// Socket s = new Socket("10.192.168.134", 8154);
	// out = new PrintWriter(s.getOutputStream(), true);
	// } catch (IOException ioe) {
	// tv.setText("socket fail");
	// ioe.printStackTrace();
	// }
	//

	tv.setOnTouchListener(new OnTouchListener() {

	    @Override
	    public boolean onTouch(View v, MotionEvent me) {
		TextView tv = ((TextView) findViewById(R.id.label));

		if (me.getAction() == MotionEvent.ACTION_DOWN) {
		    initialX = me.getX();
		    initialY = me.getY();
		    tapX = initialX;
		    tapY = initialY;
		    tv.setText("started");
		    time = System.currentTimeMillis();

		} else if (me.getAction() == MotionEvent.ACTION_MOVE) {
		    // out.println("m " + (me.getX() - initialX) + " " +
		    // (me.getY() - initialY) + " " +
		    // (System.currentTimeMillis() - time));
		    out.println("m " + (me.getX() - initialX) + " "
			    + (me.getY() - initialY));
		    initialX = me.getX();
		    initialY = me.getY();
		} else if (me.getAction() == MotionEvent.ACTION_UP) {
		    if ((System.currentTimeMillis() - time) < 200
			    && Math.abs(me.getX() - tapX) < 10
			    && Math.abs(me.getY() - tapY) < 10) {
			out.println("t");
		    }
		    tv.setText("finito " + Math.abs(me.getX() - tapX) + " "
			    + Math.abs(me.getY() - tapY));
		}

		return true;
	    }

	});
    }

    int cpt = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.menu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle item selection
	switch (item.getItemId()) {
	case R.id.keyboard:
	    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    inputMethodManager.toggleSoftInput(
		    InputMethodManager.SHOW_IMPLICIT, 0);

	    break;
	case R.id.parameters:
	    break;
	}

	return true;
    }

    private boolean prev = false;

    @Override
    public boolean onKeyDown(int code, KeyEvent msg) {
	boolean toReturn = true;
	if (out != null) {
	    if (!prev) {
		switch (code) {
		case KeyEvent.KEYCODE_SHIFT_LEFT:
		    prev = true;
		    break;
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_1:
		case KeyEvent.KEYCODE_2:
		case KeyEvent.KEYCODE_3:
		case KeyEvent.KEYCODE_4:
		case KeyEvent.KEYCODE_5:
		case KeyEvent.KEYCODE_6:
		case KeyEvent.KEYCODE_7:
		case KeyEvent.KEYCODE_8:
		case KeyEvent.KEYCODE_9:
		    out.println("k " + (code + 41));
		    break;
		case KeyEvent.KEYCODE_A:
		case KeyEvent.KEYCODE_B:
		case KeyEvent.KEYCODE_C:
		case KeyEvent.KEYCODE_D:
		case KeyEvent.KEYCODE_E:
		case KeyEvent.KEYCODE_F:
		case KeyEvent.KEYCODE_G:
		case KeyEvent.KEYCODE_H:
		case KeyEvent.KEYCODE_I:
		case KeyEvent.KEYCODE_J:
		case KeyEvent.KEYCODE_K:
		case KeyEvent.KEYCODE_L:
		case KeyEvent.KEYCODE_M:
		case KeyEvent.KEYCODE_N:
		case KeyEvent.KEYCODE_O:
		case KeyEvent.KEYCODE_P:
		case KeyEvent.KEYCODE_Q:
		case KeyEvent.KEYCODE_R:
		case KeyEvent.KEYCODE_S:
		case KeyEvent.KEYCODE_T:
		case KeyEvent.KEYCODE_U:
		case KeyEvent.KEYCODE_V:
		case KeyEvent.KEYCODE_W:
		case KeyEvent.KEYCODE_X:
		case KeyEvent.KEYCODE_Y:
		case KeyEvent.KEYCODE_Z:
		    if (msg.isShiftPressed()) {
			out.println("K " + (code + 36));
		    } else {
			out.println("k " + (code + 36));
		    }
		    break;
		case KeyEvent.KEYCODE_SPACE:
		    out.println("k 32");
		    break;
		case KeyEvent.KEYCODE_ENTER:
		    out.println("k 10");
		    break;
		case KeyEvent.KEYCODE_DEL:
		    out.println("k 8");
		    break;
		case KeyEvent.KEYCODE_PERIOD:
		    out.println("k 46");
		    break;
		case KeyEvent.KEYCODE_COMMA:
		    out.println("k 44");
		    break;
		case KeyEvent.KEYCODE_AT:
		    out.println("k 512");
		    break;
		case KeyEvent.KEYCODE_POUND:
		    out.println("k 520");
		    break;
		default:
		    toReturn = super.onKeyDown(code, msg);
		}
	    } else {
		prev = false;
		switch (code) {
		case 11:
		    out.println("k 515");
		    break;
		case 12:
		    out.println("k 150");
		    break;
		case 14:
		    
		    //break;
		default:
		    // TODO:REMOVE
		    //out.println("k " + code);
		    toReturn = super.onKeyDown(code, msg);
		}
	    }
	} else {
	    toReturn = super.onKeyDown(code, msg);
	}
	return toReturn;
    }

    public boolean onTouch(View v, MotionEvent event) {
	TextView tv = ((TextView) findViewById(R.id.label));
	tv.setText("touch");

	return true;
    }
}