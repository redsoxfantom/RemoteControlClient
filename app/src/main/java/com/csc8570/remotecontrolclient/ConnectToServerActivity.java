package com.csc8570.remotecontrolclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ConnectToServerActivity extends AppCompatActivity {

    ProgressBar serverConnectionProgress;
    TextView serverConnectionText;
    String ipAddress;
    String friendlyName;

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
    }
}
