package com.csc8570.remotecontrolclient.networking;

import android.util.Log;

import com.csc8570.remotecontrolclient.Interfaces.IConnectionReceiver;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    private BufferedReader input;
    private BufferedWriter output;

    public ConnectionNegotiator(String ipAddress)
    {
        mapper = new ObjectMapper();
        this.ipAddress = ipAddress;
        input = null;
    }

    public void Negotiate(final IConnectionReceiver receiver)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    InetAddress addr = InetAddress.getByName(ipAddress);
                    client = new Socket(addr,50001);
                    input = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    // Read the connection response from the server
                    String resp = input.readLine();
                    Log.i("networking","Got client connection response"+resp);
                    Data.ConnectionResponse serverResponse = mapper.readValue(resp, Data.ConnectionResponse.class);
                    receiver.gotConnectionResponse(serverResponse,client);
                }
                catch(Exception ex)
                {
                    Log.e("networking","Failed to open connection negotiator",ex);
                    if(client != null)
                    {
                        try {
                            client.close();
                        }
                        catch (Exception e)
                        {
                            Log.e("networking","Failed to close socket",e);
                        }
                    }
                    receiver.connectionFailed(ex);
                }
            }
        }).start();
    }
}
