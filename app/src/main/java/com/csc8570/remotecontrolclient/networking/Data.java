package com.csc8570.remotecontrolclient.networking;

/**
 * Created by Tom on 3/9/2016.
 */
public class Data
{
    public static class ConnectionResponse
    {
        private int screenWidth;
        private int screenHeight;
        private String keyboardLocale;

        public int getScreenWidth() {
            return screenWidth;
        }

        public void setScreenWidth(int screenWidth) {
            this.screenWidth = screenWidth;
        }

        public int getScreenHeight() {
            return screenHeight;
        }

        public void setScreenHeight(int screenHeight) {
            this.screenHeight = screenHeight;
        }

        public String getKeyboardLocale() {
            return keyboardLocale;
        }

        public void setKeyboardLocale(String keyboardLocale) {
            this.keyboardLocale = keyboardLocale;
        }
    }

    public static class ConnectionRequest
    {
        private String ipAddress;

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }
    }

    public static class BeaconPacket
    {
        private String ipAddress;
        private String friendlyName;
        private int count;

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getFriendlyName() {
            return friendlyName;
        }

        public void setFriendlyName(String friendlyName) {
            this.friendlyName = friendlyName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String toString()
        {
            return "IP: "+ipAddress+", FriendlyName: "+friendlyName+", Count: "+count;
        }
    }
}
