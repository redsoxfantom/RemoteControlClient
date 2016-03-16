package com.csc8570.remotecontrolclient.networking;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Responsible for negotiating the initial connection with the client
 *
 * Created by Tom on 3/14/2016.
 */
public class ConnectionNegotiator
{
    private String ipAddress;
    private Socket client;
    private ObjectMapper mapper;

    public ConnectionNegotiator(String ipAddress)
    {
        mapper = new ObjectMapper();
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
                    client = new Socket(addr,50001);
                    BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    // Read the connection response from the server
                    String resp = input.readLine();
                    Data.ConnectionResponse serverResponse = mapper.readValue(resp, Data.ConnectionResponse.class);
                }
                catch(Exception ex)
                {
                    Log.e("networking","Failed to open connection negotiator",ex);
                }
            }
        }).start();
    }
}
