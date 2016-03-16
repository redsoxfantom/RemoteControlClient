package com.csc8570.remotecontrolclient.Interfaces;

import com.csc8570.remotecontrolclient.networking.Data;

/**
 * Called from the connection negotiator
 * Created by Tom on 3/16/2016.
 */
public interface IConnectionReceiver
{
    // Called when we successfully negotiate a connection
    void gotConnectionResponse(Data.ConnectionResponse response);
    // Called when the connection negotiation fails
    void connectionFailed(Exception ex);
}
