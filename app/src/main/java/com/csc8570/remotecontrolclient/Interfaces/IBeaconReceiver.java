package com.csc8570.remotecontrolclient.Interfaces;

/**
 * Defines a class that will receive information about active servers
 * Created by Tom on 3/13/2016.
 */
public interface IBeaconReceiver {
    void addServer(String ipAddress, String friendlyName);
    void removeServer(String ipAddress, String friendlyName);
}
