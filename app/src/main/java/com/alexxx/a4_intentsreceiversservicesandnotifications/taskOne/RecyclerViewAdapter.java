package com.alexxx.a4_intentsreceiversservicesandnotifications.taskOne;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexxx.a4_intentsreceiversservicesandnotifications.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements View.OnClickListener {


    private ArrayList<RecyclerViewItem> mAdapterData;
    private Context mContext;
    private static RecyclerViewSelectedElementListener mRecyclerViewSelectedElementListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleTextView;
        TextView mDescriptionTextView;
        int mPosition;

        public ViewHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerViewAdapter.mRecyclerViewSelectedElementListener.onItemSelected(mPosition);
            }
        });
        }
    }

    public RecyclerViewAdapter(ArrayList<RecyclerViewItem> data, Context context, RecyclerViewSelectedElementListener recyclerViewSelectedElementListener) {
        this.mAdapterData = data;
        this.mContext = context;
        RecyclerViewAdapter.mRecyclerViewSelectedElementListener = recyclerViewSelectedElementListener;
    }

    @Override
    public int getItemCount() {
        return mAdapterData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item, parent, false);
        View popupButton = view.findViewById(R.id.button_popup);

        popupButton.setOnClickListener(this);

        ViewHolder vh = new ViewHolder(view);


        return vh;
    }
    @Override
    public void onClick(final View view) {
        // We need to post a Runnable to show the popup to make sure that the PopupMenu is
        // correctly positioned. The reason being that the view may change position before the
        // PopupMenu is shown.
        view.post(new Runnable() {
            @Override
            public void run() {
                showPopupMenu(view);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this.mContext, view);

        // Inflate our menu resource into the PopupMenu's Menu
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

        // Finally show the PopupMenu
        popup.show();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder != null) {
            RecyclerViewItem item = mAdapterData.get(position);
            holder.mTitleTextView.setText(item.mTitle);
            holder.mDescriptionTextView.setText(String.format("Artist: %s; Duration: %s", item.mArtist, item.mDuration));
            holder.mPosition = position;
        }
    }
}
