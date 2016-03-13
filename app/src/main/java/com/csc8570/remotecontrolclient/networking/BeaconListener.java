package com.csc8570.remotecontrolclient.networking;

import android.os.*;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;

/**
 * Created by Tom on 3/13/2016.
 */
public class BeaconListener
{
    volatile boolean receiveBeacon;

    public  BeaconListener()
    {
        receiveBeacon = false;
    }

    public void startListening()
    {
        receiveBeacon = true;
        new Thread(new BeaconListenThread()).start();
    }

    private class BeaconListenThread implements Runnable
    {
        MulticastSocket client;
        InetAddress beaconAddress;

        @Override
        public void run()
        {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            try
            {
                client = new MulticastSocket(50000);
                beaconAddress = InetAddress.getByName("224.0.0.1");
                client.joinGroup(beaconAddress);
                byte[] buffer = new byte[512];
                DatagramPacket packet = new DatagramPacket(buffer,buffer.length);

                while (receiveBeacon)
                {
                    Log.i("Networking", "Listening for beacon");
                    client.receive(packet);

                    String packetBytes = new String(packet.getData(), StandardCharsets.US_ASCII);
                    Log.i("Networking",packetBytes);
                    Thread.sleep(2500);
                }
            }
            catch(Exception ex)
            {
                Log.e("Networking","Failed to run beacon listener",ex);
            }
            finally
            {
                if(client != null)
                {
                    try
                    {
                        client.leaveGroup(beaconAddress);
                        client.close();
                    }
                    catch (Exception ex)
                    {
                        Log.e("Networking","Failed to clean up beacon listener",ex);
                    }
                }
            }
        }
    }
}
