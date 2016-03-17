package com.csc8570.remotecontrolclient.networking;

import android.util.Log;

import com.csc8570.remotecontrolclient.Interfaces.IBeaconReceiver;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Listens for beacon packets from the server
 *
 * Created by Tom on 3/13/2016.
 */
public class BeaconListener
{
    volatile boolean receiveBeacon;
    Runnable listener;
    Runnable msgHandler;
    IBeaconReceiver receiver;
    MulticastSocket client;
    InetAddress beaconAddress;
    BlockingQueue<String> msgQueue;

    public  BeaconListener(IBeaconReceiver receiver)
    {
        msgQueue = new LinkedBlockingQueue<>();
        listener = new BeaconListenThread(msgQueue);
        msgHandler = new BeaconMessageHandler(msgQueue);
        receiveBeacon = false;
        this.receiver = receiver;
    }

    public void startListening()
    {
        receiveBeacon = true;
        new Thread(listener).start();
        new Thread(msgHandler).start();
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

    // Consumer of beacon messages
    private class BeaconMessageHandler implements Runnable
    {
        HashMap<String,CountIncrementTuple> msgDictionary;
        Set<String> validServers;
        BlockingQueue<String> msgQueue;
        ObjectMapper mapper;

        public BeaconMessageHandler(BlockingQueue<String> msgQueue)
        {
            mapper = new ObjectMapper();
            msgDictionary = new HashMap<>();
            validServers = new HashSet<>();
            this.msgQueue = msgQueue;
        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            while(receiveBeacon)
            {
                String msg = null;
                try
                {
                    msg = msgQueue.take();
                }
                catch (Exception ex)
                {
                    Log.e("networking","Failed to check for messages",ex);
                }
                if (msg.equals("")) {
                    break;
                }

                try {
                    Data.BeaconPacket beaconData = mapper.readValue(msg, Data.BeaconPacket.class);

                    Log.d("Networking", "Got message: " + beaconData.toString());
                    // Only do beacon validation if the server has not already been validated
                    if (!validServers.contains(beaconData.getIpAddress())) {
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
                    Log.e("networking","Failed to parse beacon message",ex);
                }
            }
        }
    }

    // Listens for beacon messages and sends them to the listener thread
    private class BeaconListenThread implements Runnable
    {
        private BlockingQueue<String> msgQueue;

        public BeaconListenThread(BlockingQueue<String> msgQueue)
        {
            this.msgQueue = msgQueue;
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
                        msgQueue.add(packetBytes);
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
                msgQueue.add("");
            }
        }
    }
}
