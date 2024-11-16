package com.smallworldfs.moneytransferapp.utils;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.AppendableRecyclerViewAdapter;

import java.util.ArrayList;

public class RecyclerViewPaginationManager<Data> {
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<Data> mItems;
    private AppendableRecyclerViewAdapter<?, Data> mAdapter;
    private int mPageSize = 100;
    private int mThreshold = 20;

    public RecyclerViewPaginationManager(@NonNull final ArrayList<Data> items, final int pageSize,
                                         final int threshold) {
        mPageSize = pageSize;
        mThreshold = threshold;
        mItems = items;
    }

    public void attach(@NonNull final RecyclerView recyclerView,
                       @NonNull final AppendableRecyclerViewAdapter<?, Data> adapter) {

        mAdapter = adapter;
        mLinearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new OnScrollListener());
        loadMoreItems();
    }

    private void loadMoreItems() {
        final ArrayList<Data> chunk;
        if (mItems.size() >= mPageSize) {
            chunk = new ArrayList<>(mItems.subList(0, mPageSize));
        } else {
            chunk = new ArrayList<>(mItems);
        }
        mItems.removeAll(chunk);
        mAdapter.addItems(chunk);
    }

    private void onScroll() {
        if (!mItems.isEmpty()) {
            final int itemsCount = mLinearLayoutManager.getItemCount();
            final int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (itemsCount <= lastVisibleItem + mThreshold) {
                loadMoreItems();
            }
        }
    }

    private final class OnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            onScroll();
        }
    }
}
