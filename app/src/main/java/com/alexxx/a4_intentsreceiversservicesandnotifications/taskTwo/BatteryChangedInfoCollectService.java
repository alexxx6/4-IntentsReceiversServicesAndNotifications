package com.alexxx.a4_intentsreceiversservicesandnotifications.taskTwo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class BatteryChangedInfoCollectService extends Service {
    private final static int INTERVAL = 1000 * 60 * 60; //1 hour
    private int mBatteryPercentageDropped;
    private IBinder mBinder = new BatteryChangedInfoCollectServiceBinder();
    private BatteryChangedReceiver mBatteryChangedReceiver;
    private int mLastBatteryLevel;

    public class BatteryChangedInfoCollectServiceBinder extends Binder {
        public BatteryChangedInfoCollectService getService() {
        return BatteryChangedInfoCollectService.this;
    }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.mBatteryPercentageDropped = 0;
        this.mBatteryChangedReceiver = new BatteryChangedReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        super.registerReceiver(this.mBatteryChangedReceiver, intentFilter);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                BatteryChangedInfoCollectService.this.mBatteryPercentageDropped = 0;
                handler.postDelayed(this, BatteryChangedInfoCollectService.INTERVAL);
            }
        }, BatteryChangedInfoCollectService.INTERVAL);
    }

    public int getBatteryPercentageDropped() {
        return mBatteryPercentageDropped;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (this.mBatteryChangedReceiver != null)
        super.unregisterReceiver(this.mBatteryChangedReceiver);
    }

    public class BatteryChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int currBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            if (currBatteryLevel < BatteryChangedInfoCollectService.this.mLastBatteryLevel) {
                BatteryChangedInfoCollectService.this.mBatteryPercentageDropped++;
            }

            BatteryChangedInfoCollectService.this.mLastBatteryLevel = currBatteryLevel;

            BroadCastHandler.getInstance().updateValue(String.valueOf(BatteryChangedInfoCollectService.this.mBatteryPercentageDropped));
        }
    }
}
