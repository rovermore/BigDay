package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public abstract class AppendableRecyclerViewAdapter<VH extends RecyclerView.ViewHolder, Data> extends RecyclerView.Adapter<VH> {
    protected final ArrayList<Data> mData = new ArrayList<>();

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItems(@NonNull final List<Data> items) {
        final int startPosition = mData.size() + 1;
        mData.addAll(items);
        notifyItemRangeInserted(startPosition, items.size());
    }
}
