package com.alexxx.a4_intentsreceiversservicesandnotifications.taskOne;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexxx.a4_intentsreceiversservicesandnotifications.R;

public class PlayerViewFragment extends Fragment implements View.OnClickListener {
    private ImageView mImageView;
    private TextView mAudioNameTextView;
    private ImageButton mPlayPauseBtn;
    private ImageButton mStopBtn;
    private AudioPlayerService mAudioPlayerService;
    private ImageButton mResetBtn;
    private ImageButton mFastForwardBtn;
    private int mStepForward;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fagment_player_view, null);

        this.mStepForward = 2;

        // TODO: Make interface
        SpotifyActivity spotifyActivity = (SpotifyActivity) super.getActivity();

        this.mAudioPlayerService = spotifyActivity.getAudioPlayerService();
        if (this.mAudioPlayerService != null) {
            RecyclerViewItem item = this.mAudioPlayerService.getAudioModel();
            PlayerStates playerStates = this.mAudioPlayerService.getPlayerStates();

            this.mImageView = (ImageView) view.findViewById(R.id.imageView);
            this.mAudioNameTextView = (TextView) view.findViewById(R.id.textView);
            this.mPlayPauseBtn = (ImageButton) view.findViewById(R.id.imageButton);
            this.mStopBtn = (ImageButton) view.findViewById(R.id.imageButton2);
            this.mResetBtn = (ImageButton) view.findViewById(R.id.imageButton3);
            this.mFastForwardBtn = (ImageButton) view.findViewById(R.id.imageButton4);

            this.mAudioNameTextView.setText(item.mTitle);
            int imageViewSrc = playerStates == PlayerStates.PLAYED ? R.drawable.ic_play_circle_filled_black_24dp :
                    playerStates == PlayerStates.PAUSED ? R.drawable.ic_pause_circle_filled_black_24dp : R.drawable.ic_fast_forward_black_24dp;
            this.mImageView.setImageDrawable(ResourcesCompat.getDrawable(super.getResources(), imageViewSrc, null));

            this.mPlayPauseBtn.setOnClickListener(this);
            this.mStopBtn.setOnClickListener(this);
            this.mResetBtn.setOnClickListener(this);
            this.mFastForwardBtn.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageButton:
                if (this.mAudioPlayerService.getPlayerStates() == PlayerStates.PLAYED){
                    this.mAudioPlayerService.pauseAudio();
                    this.mImageView.setImageDrawable(ResourcesCompat.getDrawable(super.getResources(), R.drawable.ic_pause_circle_filled_black_24dp, null));
                    this.mPlayPauseBtn.setImageDrawable(ResourcesCompat.getDrawable(super.getResources(), R.drawable.ic_play_arrow_black_24dp, null));
                } else {
                    this.mAudioPlayerService.playAudio();
                    this.mImageView.setImageDrawable(ResourcesCompat.getDrawable(super.getResources(), R.drawable.ic_play_circle_filled_black_24dp, null));
                    this.mPlayPauseBtn.setImageDrawable(ResourcesCompat.getDrawable(super.getResources(), R.drawable.ic_pause_black_24dp, null));
                }
                break;
            case R.id.imageButton2:
                this.mAudioPlayerService.stopAudio();
                super.getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
            case R.id.imageButton3:
                this.mAudioPlayerService.reverseAudio();
                this.mImageView.setImageDrawable(ResourcesCompat.getDrawable(super.getResources(), R.drawable.ic_play_circle_filled_black_24dp, null));
                this.mPlayPauseBtn.setImageDrawable(ResourcesCompat.getDrawable(super.getResources(), R.drawable.ic_pause_black_24dp, null));
            case R.id.imageButton4:
                if (this.mAudioPlayerService.getPlayerStates() != PlayerStates.FAST_FORWARD){
                    this.mAudioPlayerService.fastForwardAudio(this.mStepForward);
                    this.mImageView.setImageDrawable(ResourcesCompat.getDrawable(super.getResources(), R.drawable.ic_fast_forward_black_24dp, null));
                    this.mFastForwardBtn.setImageDrawable(ResourcesCompat.getDrawable(super.getResources(), R.drawable.ic_play_arrow_black_24dp, null));
                } else {
                    this.mAudioPlayerService.playAudio();
                    this.mImageView.setImageDrawable(ResourcesCompat.getDrawable(super.getResources(), R.drawable.ic_play_circle_filled_black_24dp, null));
                    this.mFastForwardBtn.setImageDrawable(ResourcesCompat.getDrawable(super.getResources(), R.drawable.ic_fast_forward_black_24dp, null));
                }

        }
    }
}
