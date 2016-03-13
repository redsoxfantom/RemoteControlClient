package com.csc8570.remotecontrolclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.csc8570.remotecontrolclient.Interfaces.IBeaconReceiver;
import com.csc8570.remotecontrolclient.networking.BeaconListener;

public class SelectServerActivity extends AppCompatActivity implements IBeaconReceiver {

    BeaconListener listener;
    Button serverSearchButton;
    TextView foundServersBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_server);

        listener = new BeaconListener(this);
        serverSearchButton = (Button)findViewById(R.id.ServerSearchButton);
        foundServersBox = (TextView)findViewById(R.id.FoundServersBox);
    }

    // Called when the user requests a search for active servers
    public void ServerSearchClick(View v)
    {
        listener.startListening();
    }

    @Override
    public void addServer(final String ipAddress, final String friendlyName)
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                foundServersBox.append("FriendlyName "+friendlyName+", IPAddress "+ipAddress);
            }
        });
    }

    @Override
    public void removeServer(String ipAddress, String friendlyName)
    {

    }
}
