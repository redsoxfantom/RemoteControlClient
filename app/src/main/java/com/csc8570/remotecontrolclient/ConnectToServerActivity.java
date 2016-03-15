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

        ipAddress = savedInstanceState.getString(ActivityConstants.IP_ADDRESS_EXTRA);
        friendlyName = savedInstanceState.getString(ActivityConstants.FRIENDLY_NAME_EXTRA);
    }
}
