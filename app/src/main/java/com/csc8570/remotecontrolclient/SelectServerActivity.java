package com.csc8570.remotecontrolclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csc8570.remotecontrolclient.Interfaces.IBeaconReceiver;
import com.csc8570.remotecontrolclient.networking.BeaconListener;
import com.csc8570.remotecontrolclient.views.IpEditText;

public class SelectServerActivity extends AppCompatActivity implements IBeaconReceiver {

    BeaconListener listener;
    Button serverSearchButton;
    ProgressBar serverSearchBar;
    LinearLayout serverButtonsLayout;
    boolean searchForServers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        searchForServers = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_server);

        listener = new BeaconListener(this);
        serverSearchButton = (Button)findViewById(R.id.ServerSearchButton);
        serverSearchBar = (ProgressBar)findViewById(R.id.ServerSearchProgressBar);
        serverButtonsLayout = (LinearLayout)findViewById(R.id.FoundServersLayout);
    }

    // Called when the user selects one of the found servers
    public void ServerSelectClick(View v)
    {
        String ipAddress = ((TextView)v.findViewById(R.id.ServerIPAddress)).getText().toString();
        String friendlyName = ((TextView)v.findViewById(R.id.ServerFriendlyName)).getText().toString();
        Log.i("SelectServer", "Attempting to connect to server " + ipAddress);

        startServerConnection(ipAddress, friendlyName);
    }

    private void startServerConnection(String ipAddress, String friendlyName) {
        stopListening();
        Intent serverConnectionIntent = new Intent(this,ConnectToServerActivity.class);
        serverConnectionIntent.putExtra(ActivityConstants.IP_ADDRESS_EXTRA,ipAddress);
        serverConnectionIntent.putExtra(ActivityConstants.FRIENDLY_NAME_EXTRA,friendlyName);

        startActivity(serverConnectionIntent);
    }

    // Called when the user requests a search for active servers
    public void ServerSearchClick(View v)
    {
        if(searchForServers)
        {
            startListening();
        }
        else
        {
            stopListening();
        }
    }

    // Called when the user opts to manually input a server IP address
    public void manualServerInputClicked(View v)
    {
        LayoutInflater inflater = getLayoutInflater();
        final View ipAddressView = inflater.inflate(R.layout.dialog_input_server_ip,null);
        AlertDialog.Builder bldr = new AlertDialog.Builder(this);
        bldr.setTitle("Input Server IP")
            .setView(ipAddressView)
            .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    IpEditText ip_0_8 = (IpEditText)ipAddressView.findViewById(R.id.ip_addr_0_8);
                    IpEditText ip_8_16 = (IpEditText)ipAddressView.findViewById(R.id.ip_addr_8_16);
                    IpEditText ip_16_24 = (IpEditText)ipAddressView.findViewById(R.id.ip_addr_16_24);
                    IpEditText ip_24_32 = (IpEditText)ipAddressView.findViewById(R.id.ip_addr_24_32);

                    String ipAddr = String.format("%s.%s.%s.%s",ip_0_8.getText().toString(),
                                                                ip_8_16.getText().toString(),
                                                                ip_16_24.getText().toString(),
                                                                ip_24_32.getText().toString());
                    startServerConnection(ipAddr,ipAddr);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

        AlertDialog dialog = bldr.create();
        dialog.show();
    }

    private void stopListening()
    {
        listener.stopListening();
        serverSearchButton.setText("Search For Servers");
        serverSearchBar.setVisibility(View.INVISIBLE);
        searchForServers = true;
    }

    private void startListening()
    {
        listener.startListening();
        serverSearchButton.setText("Stop Searching For Servers");
        serverSearchBar.setVisibility(View.VISIBLE);
        searchForServers = false;
    }

    @Override
    public void addServer(final String ipAddress, final String friendlyName)
    {
        //this.runOnUiThread(new FoundServerHandler(this.getApplicationContext(),friendlyName,ipAddress));
        this.runOnUiThread(new HandleAddServer(friendlyName,ipAddress,getBaseContext()));
    }

    @Override
    public void removeServer(String ipAddress, String friendlyName)
    {

    }

    // Handles a new server being added to the layout
    private class HandleAddServer implements Runnable
    {
        private String friendlyName;
        private String ipAddress;
        private Context ctx;
        private LayoutInflater inflater;

        public HandleAddServer(String friendlyName, String ipAddress, Context ctx)
        {
            this.friendlyName = friendlyName;
            this.ipAddress = ipAddress;
            this.ctx = ctx;
            inflater = LayoutInflater.from(ctx);
        }

        @Override
        public void run()
        {
            LinearLayout foundServerBtn = (LinearLayout)inflater.inflate(R.layout.server_search_result,null);
            TextView friendlyNameText = (TextView) foundServerBtn.findViewById(R.id.ServerFriendlyName);
            TextView ipAddressText = (TextView) foundServerBtn.findViewById(R.id.ServerIPAddress);
            friendlyNameText.setText(friendlyName);
            ipAddressText.setText(ipAddress);
            serverButtonsLayout.addView(foundServerBtn);
        }
    }
}
