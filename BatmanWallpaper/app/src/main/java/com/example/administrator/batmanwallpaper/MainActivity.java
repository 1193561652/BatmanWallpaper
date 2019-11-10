package com.example.administrator.batmanwallpaper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.administrator.service.WorkingServiceConnection;

import tools.PermissionHelper;

public class MainActivity extends Activity {

    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }
    WorkingServiceConnection wsConnection = new WorkingServiceConnection(null);
    Button reqBtn = null;
    TextView textView = null;
    Switch switch1 = null;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionHelper.requestReadExternalPermission(this);
        PermissionHelper.requestWriteExternalPermission(this);

        Intent startIntent = new Intent();
        //ComponentName componentName = new ComponentName("com.example.administrator.batmanwallpaper", "com.example.batservice.WorkingService");
        //ComponentName componentName = new ComponentName("com.example.batservice", "com.example.batservice.WorkingService");
        ComponentName componentName = new ComponentName("com.example.batservice", "com.example.batservice.WorkingService");
        startIntent.setComponent(componentName);
        //getApplicationContext().startService(startIntent);
        getApplicationContext().startForegroundService(startIntent);
        boolean bSec = getApplicationContext().bindService(startIntent, wsConnection, BIND_AUTO_CREATE);

        reqBtn = (Button)findViewById(R.id.reqBtn);
        textView = (TextView)findViewById(R.id.textView);
        switch1 = (Switch)findViewById(R.id.switch1);

        reqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wsConnection != null) {
                    wsConnection.tiggerReq();
                    textView.setText(wsConnection.mixInfo());
                } else {
                    textView.setText("error");
                }
            }
        });

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    wsConnection.setWorking("true");
                } else {
                    wsConnection.setWorking("false");
                }
            }
        });
    }


}
