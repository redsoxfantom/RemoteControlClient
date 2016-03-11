package com.csc8570.remotecontrolclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.csc8570.remotecontrolclient.networking.BeaconListener;

public class SelectServerActivity extends AppCompatActivity {

    BeaconListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_server);

        listener = new BeaconListener();
        listener.startListening();
    }
}
