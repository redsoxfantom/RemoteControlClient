package com.csc8570.remotecontrolclient;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.csc8570.remotecontrolclient.Interfaces.IBeaconReceiver;
import com.csc8570.remotecontrolclient.networking.BeaconListener;

import java.util.ArrayList;

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

    // Called when the user requests a search for active servers
    public void ServerSearchClick(View v)
    {
        if(searchForServers)
        {
            listener.startListening();
            serverSearchButton.setText("Stop Searching For Servers");
            serverSearchBar.setVisibility(View.VISIBLE);
            searchForServers = false;
        }
        else
        {
            listener.stopListening();
            serverSearchButton.setText("Search For Servers");
            serverSearchBar.setVisibility(View.INVISIBLE);
            searchForServers = true;
        }
    }

    @Override
    public void addServer(final String ipAddress, final String friendlyName)
    {
        //this.runOnUiThread(new FoundServerHandler(this.getApplicationContext(),friendlyName,ipAddress));
        this.runOnUiThread(new HandleAddServer(friendlyName,ipAddress,getApplicationContext()));
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
