package com.mplourde.remote.client;

import java.io.PrintWriter;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

import com.mplourde.remote.R;
import com.mplourde.remote.client.communication.SocketManager;

public class TrackpadActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.trackpad);

	LinearLayout layout = (LinearLayout) findViewById(R.id.trackpadLayout);

	layout.setOnTouchListener(new OnTouchListener() {

	    private float initialX;
	    private float initialY;

	    private float tapX;
	    private float tapY;
	    private long time;
	    
	    private PrintWriter out = SocketManager.getInstance().getSocketContainer().getPrintWriter();

	    @Override
	    public boolean onTouch(View v, MotionEvent me) {

		if (me.getAction() == MotionEvent.ACTION_DOWN) {
		    initialX = me.getX();
		    initialY = me.getY();
		    tapX = initialX;
		    tapY = initialY;
		    time = System.currentTimeMillis();
		} else if (me.getAction() == MotionEvent.ACTION_MOVE) {
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
		}

		return true;
	    }

	});
    }

}
