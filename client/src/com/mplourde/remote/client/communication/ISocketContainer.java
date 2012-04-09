package com.mplourde.remote.client.communication;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Interface of a socket container. This is necessary because some of the socket
 * classes don't share a common parent (example: Android Bluetooth socket and
 * Java TCP/UDP socket).
 * 
 * @author Mathieu
 * 
 */
public interface ISocketContainer {

    /**
     * Closes the socket and its resources
     */
    public void close() throws IOException;

    /**
     * Gets the print writer associated with the socket
     * 
     * @return PrintWriter instance
     */
    public PrintWriter getPrintWriter();

    //Unimplemented because this feature is not available in the bluetooth
    //socket class of API level 10.
    //public boolean isConnected();
}
