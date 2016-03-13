package com.csc8570.remotecontrolclient.networking;

import android.os.*;
import android.util.Log;

import com.csc8570.remotecontrolclient.Interfaces.IBeaconReceiver;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Listens for beacon packets from the server
 *
 * Created by Tom on 3/13/2016.
 */
public class BeaconListener
{
    volatile boolean receiveBeacon;
    Runnable listener;
    IBeaconReceiver receiver;
    MulticastSocket client;
    InetAddress beaconAddress;

    public  BeaconListener(IBeaconReceiver receiver)
    {
        listener = new BeaconListenThread();
        receiveBeacon = false;
        this.receiver = receiver;
    }

    public void startListening()
    {
        receiveBeacon = true;
        new Thread(listener).start();
    }

    public void stopListening()
    {
        receiveBeacon = false;
        try {
            client.leaveGroup(beaconAddress);
            client.close();
        }
        catch(Exception ex)
        {
            Log.e("Networking","Failed to close client",ex);
        }
    }

    /**
     * Stores the current message count (i.e, the count of the last message received
     * and the number of concurrent messages received for an IP address
     */
    private class CountIncrementTuple
    {
        private int lastMsgCount;
        private int numConcurrentMsgs;

        public int getLastMsgCount() {
            return lastMsgCount;
        }

        public void setLastMsgCount(int lastMsgCount) {
            this.lastMsgCount = lastMsgCount;
        }

        public int getNumConcurrentMsgs() {
            return numConcurrentMsgs;
        }

        public void setNumConcurrentMsgs(int numConcurrentMsgs) {
            this.numConcurrentMsgs = numConcurrentMsgs;
        }
    }

    private class BeaconListenThread implements Runnable
    {
        // Maps an IP address to the count of messages received and the current count
        HashMap<String,CountIncrementTuple> msgDictionary;
        // List of servers that have already been validated
        Set<String> validServers;

        public BeaconListenThread()
        {
            msgDictionary = new HashMap<>();
            validServers = new HashSet<>();
        }

        @Override
        public void run()
        {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            try
            {
                client = new MulticastSocket(50000);
                client.setSoTimeout(3000);
                beaconAddress = InetAddress.getByName("224.0.0.1");
                client.joinGroup(beaconAddress);
                byte[] buffer = new byte[512];
                DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
                ObjectMapper mapper = new ObjectMapper();

                Log.i("Networking", "Listening for beacon");
                while (receiveBeacon)
                {
                    try
                    {
                        client.receive(packet);
                        String packetBytes = new String(packet.getData()).trim();
                        Data.BeaconPacket beaconData = mapper.readValue(packetBytes, Data.BeaconPacket.class);

                        Log.d("Networking", "Got message: "+beaconData.toString());
                        // Only do beacon validation if the server has not already been validated
                        if(!validServers.contains(beaconData.getIpAddress()))
                        {
                            if (!msgDictionary.containsKey(beaconData.getIpAddress())) {
                                CountIncrementTuple tuple = new CountIncrementTuple();
                                tuple.setLastMsgCount(beaconData.getCount());
                                tuple.setNumConcurrentMsgs(1);
                                msgDictionary.put(beaconData.getIpAddress(), tuple);
                            } else {
                                // We got this IP address before. Make sure the count incremented by one.
                                // If we got three successful increments, than this is a valid server
                                CountIncrementTuple tuple = msgDictionary.get(beaconData.getIpAddress());
                                if (beaconData.getCount() == tuple.getLastMsgCount() + 1) {
                                    if (tuple.getNumConcurrentMsgs() == 3) {
                                        Log.i("Networking", "Got three concurrent messages for IP address " + beaconData.getIpAddress());
                                        receiver.addServer(beaconData.getIpAddress(), beaconData.getFriendlyName());
                                        validServers.add(beaconData.getIpAddress());
                                    } else {
                                        Log.d("Networking", "Got concurrent message for IP address " + beaconData.getIpAddress());
                                        tuple.setNumConcurrentMsgs(tuple.getNumConcurrentMsgs() + 1);
                                        tuple.setLastMsgCount(beaconData.getCount());
                                    }
                                } else {
                                    Log.w("Networking", "Message did not have an increment of one! Resetting counter");
                                    tuple.setNumConcurrentMsgs(1);
                                    tuple.setLastMsgCount(beaconData.getCount());
                                }
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        Log.e("Networking","Error while listening for beacon packet",ex);
                    }
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
