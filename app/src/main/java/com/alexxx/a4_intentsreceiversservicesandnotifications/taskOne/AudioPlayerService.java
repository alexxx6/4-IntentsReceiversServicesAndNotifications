package com.alexxx.a4_intentsreceiversservicesandnotifications.taskOne;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import java.io.IOException;

public class AudioPlayerService extends Service {
    public static final String ACTION_PLAY = "com.alexxx.PLAY";
    private MediaPlayer mPlayer;
    private RecyclerViewItem mAudioModel;
    private IBinder mBinder = new AudioPlayerServiceBinder();
    private boolean mPlayerIsPaused;
    private boolean mPlayerIsFastForward;

    public class AudioPlayerServiceBinder extends Binder {
        public AudioPlayerService getService() {
            return AudioPlayerService.this;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    @Override
    public void onCreate() {
        this.mPlayer = new MediaPlayer();
        this.mPlayerIsPaused = false;
        this.mPlayerIsFastForward = false;
    }

    public RecyclerViewItem getAudioModel() {
        return this.mAudioModel;
    }

    public void setAudioModel(RecyclerViewItem model) {
        this.mAudioModel = model;
        this.mPlayerIsPaused = false;
        this.mPlayerIsFastForward = false;

    }

    public PlayerStates getPlayerStates(){
        if (this.mPlayerIsPaused){
            return PlayerStates.PAUSED;
        } else if(this.mPlayerIsFastForward) {
            return PlayerStates.FAST_FORWARD;
        } else  {
            return PlayerStates.PLAYED;
        }
    }

    public void fastForwardAudio(final int stepForward) {
        this.mPlayerIsFastForward = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (AudioPlayerService.this.mPlayerIsFastForward) {
                    AudioPlayerService.this.mPlayer.seekTo(stepForward);
                    AudioPlayerService.this.mPlayer.start();

                    SystemClock.sleep(1000);
                }
            }
        }).start();
    }

    public void reverseAudio() {
        this.mPlayerIsPaused = false;
        this.mPlayerIsFastForward = false;
        this.start();
    }

    public void pauseAudio(){
        this.mPlayer.pause();
        this.mPlayerIsPaused = true;
    }

    public void playAudio() {
        this.start();
    }

    public void start() {
        if (this.mAudioModel == null) {
            return;
        }

        if (this.mPlayerIsPaused || this.mPlayerIsFastForward) {
            this.mPlayer.start();
            this.mPlayerIsPaused = false;
            this.mPlayerIsFastForward = false;
            return;
        }

        this.stopAudio();

        this.mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        this.mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            this.mPlayer.setDataSource(this.mAudioModel.mUrl);
            this.mPlayer.setLooping(true);
            this.mPlayer.prepare();
            this.mPlayer.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stopAudio() {
        this.mPlayer.reset();
        this.mPlayer.stop();
        this.mPlayerIsPaused = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mPlayer != null) {
            this.mPlayer.stop();
            this.mPlayer.release();
        }
    }
}
