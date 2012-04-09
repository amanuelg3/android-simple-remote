package com.mplourde.remote.client.communication;

import java.io.IOException;
import java.io.PrintWriter;

import android.bluetooth.BluetoothSocket;

public class BluetoothSocketContainer implements ISocketContainer {

    private BluetoothSocket bSocket = null;
    private PrintWriter out = null;
    
    public BluetoothSocketContainer(BluetoothSocket bSocket) throws IOException
    {
	this.bSocket = bSocket;
	out = new PrintWriter(bSocket.getOutputStream(), true);
    }
    
    @Override
    public void close() throws IOException {
	this.bSocket.close();
    }

    @Override
    public PrintWriter getPrintWriter() {
	return out;
    }

}
