package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.QuickReminderMessage;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;

/**
 * Created by luismiguel on 20/11/17.
 */

public class QuickReminderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<QuickReminderMessage> mData;

    public QuickReminderAdapter(ArrayList<QuickReminderMessage> data) {
        this.mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_quick_reminder, parent, false);
        viewHolder = new MessageViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        QuickReminderMessage message = mData.get(position);

        if (TextUtils.isEmpty(message.getDescription())) {
            ((MessageViewHolder) holder).title.setVisibility(View.GONE);
        } else {
            ((MessageViewHolder) holder).title.setVisibility(View.VISIBLE);
            ((MessageViewHolder) holder).title.setText(message.getTitle());
        }

        if (TextUtils.isEmpty(message.getDescription())) {
            ((MessageViewHolder) holder).messageTextView.setVisibility(View.GONE);
        } else {
            ((MessageViewHolder) holder).messageTextView.setVisibility(View.VISIBLE);
            ((MessageViewHolder) holder).messageTextView.setText(message.getDescription());
        }

        ((MessageViewHolder) holder).separatorView.setVisibility(position == mData.size() - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        StyledTextView messageTextView;
        StyledTextView title;
        View separatorView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            title = (StyledTextView) itemView.findViewById(R.id.title);

            messageTextView = (StyledTextView) itemView.findViewById(R.id.message);
            separatorView = itemView.findViewById(R.id.view_separator);
        }
    }
}
