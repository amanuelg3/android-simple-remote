package com.mplourde.remote.client.communication;

import java.io.IOException;

/**
 * Singleton to access the current connection in all activities
 * 
 * @author Mathieu
 * 
 */
public class SocketManager {

    public static SocketManager cm = null;

    private ISocketContainer socketContainer = null;

    private SocketManager() {

    }

    public static SocketManager getInstance() {
	if (cm == null) {
	    cm = new SocketManager();
	}
	return cm;
    }

    public void setSocketContainer(ISocketContainer newSocketContainer) {
	if (socketContainer != null) {
	    try {
		socketContainer.close();
	    } catch (IOException ioe) {
		ioe.printStackTrace();
	    }
	}
	socketContainer = newSocketContainer;
    }

    public ISocketContainer getSocketContainer() {
	return socketContainer;
    }
}
