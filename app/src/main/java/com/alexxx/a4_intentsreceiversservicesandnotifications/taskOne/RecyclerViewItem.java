package com.alexxx.a4_intentsreceiversservicesandnotifications.taskOne;

import android.os.Parcel;
import android.os.Parcelable;

public class RecyclerViewItem implements Parcelable {
    String mTitle;
    String mUrl;
    String mArtist;
    String mDuration;

    public RecyclerViewItem(String title, String url, String artist, String duration) {
        this.mTitle = title;
        this.mUrl = url;
        this.mArtist = artist;
        this.mDuration = duration;
    }

    protected RecyclerViewItem(Parcel in) {
        mTitle = in.readString();
        mUrl = in.readString();
        mArtist = in.readString();
        mDuration = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mUrl);
        dest.writeString(mArtist);
        dest.writeString(mDuration);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RecyclerViewItem> CREATOR = new Parcelable.Creator<RecyclerViewItem>() {
        @Override
        public RecyclerViewItem createFromParcel(Parcel in) {
            return new RecyclerViewItem(in);
        }

        @Override
        public RecyclerViewItem[] newArray(int size) {
            return new RecyclerViewItem[size];
        }
    };
}