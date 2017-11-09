package com.mvp.example.messages;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mvp.example.R;
import com.mvp.example.messages.domain.model.FriendlyMessage;

import java.util.List;

/**
 * Created by vitalii_serbyn on 10/26/17.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private final Context mContext;
    private List<FriendlyMessage> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView mTextOwner;
        public ImageView mImageView;
        public ViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.messageTextView);
            mImageView = v.findViewById(R.id.messengerImageView);
            mTextOwner = v.findViewById(R.id.messengerTextView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MessagesAdapter(Context context, List<FriendlyMessage> myDataset) {
        mDataset = myDataset;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        FriendlyMessage friendlyMessage = mDataset.get(position);
        holder.mTextView.setText(friendlyMessage.getText());
        holder.mTextOwner.setText(friendlyMessage.getName());
        if (friendlyMessage.getImageUrl() == null) {
            holder.mImageView
                    .setImageDrawable(ContextCompat
                            .getDrawable(mContext,
                                    R.drawable.common_google_signin_btn_icon_light_normal));
        } else {
            Glide.with(mContext)
                    .load(friendlyMessage.getImageUrl())
                    .into(holder.mImageView);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

