package com.csc8570.remotecontrolclient.Interfaces;

import com.csc8570.remotecontrolclient.networking.Data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

/**
 * Called from the connection negotiator
 * Created by Tom on 3/16/2016.
 */
public interface IConnectionReceiver
{
    // Called when we successfully negotiate a connection
    void gotConnectionResponse(Data.ConnectionResponse response, Socket client);
    // Called when the connection negotiation fails
    void connectionFailed(Exception ex);
}
