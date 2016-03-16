package com.csc8570.remotecontrolclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csc8570.remotecontrolclient.Interfaces.IConnectionReceiver;
import com.csc8570.remotecontrolclient.networking.ConnectionNegotiator;
import com.csc8570.remotecontrolclient.networking.Data;

public class ConnectToServerActivity extends AppCompatActivity implements IConnectionReceiver {

    ProgressBar serverConnectionProgress;
    TextView serverConnectionText;
    String ipAddress;
    String friendlyName;
    ConnectionNegotiator connectionNegotiator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_server);

        serverConnectionProgress = (ProgressBar)findViewById(R.id.ServerConnectionStatusBar);
        serverConnectionText = (TextView)findViewById(R.id.ServerConnectionStatusText);

        Bundle bundle = getIntent().getExtras();
        ipAddress = bundle.getString(ActivityConstants.IP_ADDRESS_EXTRA);
        friendlyName = bundle.getString(ActivityConstants.FRIENDLY_NAME_EXTRA);

        serverConnectionText.setText(String.format("Connecting to %s (IP: %s)",friendlyName,ipAddress));
        serverConnectionProgress.setIndeterminate(false);
        serverConnectionProgress.incrementProgressBy(50);

        connectionNegotiator = new ConnectionNegotiator(ipAddress);
        connectionNegotiator.Negotiate(this);
    }

    @Override
    public void gotConnectionResponse(Data.ConnectionResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                serverConnectionProgress.setProgress(100);
                serverConnectionText.setText("Successfully connected!");
            }
        });
    }

    @Override
    public void connectionFailed(final Exception ex) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                serverConnectionProgress.setIndeterminate(true);
                serverConnectionText.setText(String.format("ERROR: Failed to connect to %s! (%s)",friendlyName,ex.getMessage()));
            }
        });
    }
}
