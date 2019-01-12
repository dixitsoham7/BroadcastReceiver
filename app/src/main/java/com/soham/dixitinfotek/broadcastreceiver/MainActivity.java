package com.soham.dixitinfotek.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    TextView textView, textViewBatteryStatus;
    ProgressBar progressBar;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tvBatteryLevel);
        progressBar = (ProgressBar) findViewById(R.id.pbBatteryProgressBar);
        textViewBatteryStatus = findViewById(R.id.tvBatteryStatus);
        broadcastReceiver = new MyBatteryBroadcast();
    }

    // registering the receiver with the intent filter of action of battery changed.
    @Override
    protected void onStart() {
        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onStart();
    }

    // on stop unregistering the receiver.
    @Override
    protected void onStop() {
        unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    // battery class which extends the BroadcastReceiver.
    private class MyBatteryBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // integer value to get the intent data with battery manager class with default value as 0
            int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            textView.setText(batteryLevel + getString(R.string.battery_percentage_symbol));
            progressBar.setProgress(batteryLevel);
            int status = registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED)).getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            // if the battery status is charging the status of the textview and color is set as green.
            if (BatteryManager.BATTERY_STATUS_CHARGING == status) {
                textViewBatteryStatus.setText("Status: Charging");
                textViewBatteryStatus.setBackgroundColor(Color.GREEN);
            } else {
                // if the status is not charging then status is set to not charging
                textViewBatteryStatus.setText("Status: Not Charging");
                if (batteryLevel > 30) {
                    textViewBatteryStatus.setBackgroundColor(Color.YELLOW);
                } else {
                    textViewBatteryStatus.setText("Status: Low Battery");
                    textViewBatteryStatus.setBackgroundColor(Color.RED);
                }
            }
        }
    }
}
