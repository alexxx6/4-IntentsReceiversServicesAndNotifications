package com.alexxx.a4_intentsreceiversservicesandnotifications.taskOne;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.alexxx.a4_intentsreceiversservicesandnotifications.R;

import java.util.ArrayList;

public class SpotifyActivity extends AppCompatActivity implements RecyclerViewSelectedElementListener {
    private ArrayList<RecyclerViewItem> mData;
    private Intent mAudioPlayerServiceIntent;
    private PlayerViewFragment mPlayerViewFragment;
    private AudioPlayerService mAudioPlayerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);

        this.mData = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            return;
        }

        this.populateDate();
        this.createView();
        if (ServiceUtil.isServiceRunning(this, AudioPlayerService.class)) {
            super.getSupportFragmentManager().beginTransaction().replace(R.id.playerViewContainer, new PlayerViewFragment()).commit();
        }

        if (!ServiceUtil.isServiceRunning(this, AudioPlayerService.class)){
            this.mAudioPlayerServiceIntent = new Intent(this, AudioPlayerService.class);
            super.bindService(this.mAudioPlayerServiceIntent, this.conn, Context.BIND_AUTO_CREATE);
            super.startService(this.mAudioPlayerServiceIntent);
        }
    }

    public AudioPlayerService getAudioPlayerService() {
        return mAudioPlayerService;
    }

    private void populateDate() {
        Cursor mediaCursor = this.getMediaCursor();
        if (mediaCursor != null) {
            while (mediaCursor.moveToNext()) {
                this.mData.add(new RecyclerViewItem(mediaCursor.getString(0),
                        mediaCursor.getString(1),
                        mediaCursor.getString(2),
                        mediaCursor.getString(3)));
            }

            mediaCursor.close();
        }
    }

    private void createView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mData, this, this);

        recyclerView.setAdapter(adapter);

        // recyclerView.addItemDecoration(new RecyclerViewCustomDecoration());
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
           AudioPlayerService.AudioPlayerServiceBinder serviceToOperate = (AudioPlayerService.AudioPlayerServiceBinder) service;
            SpotifyActivity.this.mAudioPlayerService = serviceToOperate.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            SpotifyActivity.this.mAudioPlayerService = null;
        }
    };

    @Override
    public void onItemSelected(int position) {
        if (this.mAudioPlayerService != null){
            this.mAudioPlayerService.setAudioModel(this.mData.get(position));
            this.mAudioPlayerService.playAudio();
        }

        if (this.mPlayerViewFragment != null) {
            super.getSupportFragmentManager().beginTransaction().remove(this.mPlayerViewFragment).commit();
        }

        this.mPlayerViewFragment = new PlayerViewFragment();
        super.getSupportFragmentManager().beginTransaction().replace(R.id.playerViewContainer, this.mPlayerViewFragment).commit();
    }

    private Cursor getMediaCursor() {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 OR "
                + MediaStore.Audio.Media.IS_RINGTONE + " != 0 OR "
                + MediaStore.Audio.Media.IS_ALARM + " != 0 OR "
                + MediaStore.Audio.Media.IS_NOTIFICATION + " != 0";

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION
        };

        Uri uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        Cursor internalStorageCursor = this.getContentResolver().query(uri, projection, selection, null, null);

        Uri uri1 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor externalStorageCursor = this.getContentResolver().query(uri1, projection, selection, null, null);

        Cursor mergedCursor = new MergeCursor(new Cursor[]{externalStorageCursor, internalStorageCursor});

        return mergedCursor;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.populateDate();
                    this.createView();
                } else {
                    this.createView();
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (ServiceUtil.isServiceRunning(this, AudioPlayerService.class)) {
            super.unbindService(this.conn);
            super.stopService(this.mAudioPlayerServiceIntent);
        }
    }
}
