package com.csc8570.remotecontrolclient.networking;

import android.util.Log;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Responsible for negotiating the initial connection with the server
 *
 * Created by Tom on 3/14/2016.
 */
public class ConnectionNegotiator
{
    private String ipAddress;
    private Socket server;

    public ConnectionNegotiator(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public void Negotiate()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    InetAddress addr = InetAddress.getByName(ipAddress);
                    server = new Socket(addr,50001);

                    server.getOutputStream().write("TEST".getBytes());
                }
                catch(Exception ex)
                {
                    Log.e("networking","Failed to open connection negotiator",ex);
                }
            }
        });
    }
}
