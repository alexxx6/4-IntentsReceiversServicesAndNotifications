package com.alexxx.a4_intentsreceiversservicesandnotifications.taskTwo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alexxx.a4_intentsreceiversservicesandnotifications.R;
import com.alexxx.a4_intentsreceiversservicesandnotifications.taskOne.ServiceUtil;

import java.util.Observable;
import java.util.Observer;

public class TaskTwoActivity extends AppCompatActivity implements Observer {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_two);

        this.mTextView = (TextView) super.findViewById(R.id.textView2);

        Intent intent = new Intent(this, BatteryChangedInfoCollectService.class);
        if (!ServiceUtil.isServiceRunning(this, BatteryChangedInfoCollectService.class)){
            super.startService(intent);
        }

        super.bindService(intent, this.conn, Context.BIND_AUTO_CREATE);

        BroadCastHandler.getInstance().addObserver(this);
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BatteryChangedInfoCollectService.BatteryChangedInfoCollectServiceBinder serviceToOperate =
                    (BatteryChangedInfoCollectService.BatteryChangedInfoCollectServiceBinder) service;
            BatteryChangedInfoCollectService batteryChangedInfoCollectService = serviceToOperate.getService();

            TaskTwoActivity.this.mTextView.setText(String.format("%d%%", batteryChangedInfoCollectService.getBatteryPercentageDropped()));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (ServiceUtil.isServiceRunning(this, BatteryChangedInfoCollectService.class)){
            super.unbindService(this.conn);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        this.mTextView.setText(String.format("%s%%", (String) data));
    }
}
