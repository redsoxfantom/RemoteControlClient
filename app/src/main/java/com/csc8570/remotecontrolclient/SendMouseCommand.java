package com.csc8570.remotecontrolclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SendMouseCommand extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mouse_command);

        Bundle bundle = getIntent().getExtras();
        String friendlyName = bundle.getString(ActivityConstants.FRIENDLY_NAME_EXTRA);

        TextView connectionInfo = (TextView) findViewById(R.id.lbl_connection_info_string);
        connectionInfo.setText("Connected to "+friendlyName);
    }
}
