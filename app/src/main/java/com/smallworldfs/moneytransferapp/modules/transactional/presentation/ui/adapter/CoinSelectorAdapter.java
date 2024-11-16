package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter;

import android.graphics.drawable.Drawable;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;

/**
 * Created by luismiguel on 20/6/17
 */
public class CoinSelectorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Pair<String, String>> mData;

    private SelectCoinListener mListener;
    private Drawable mBackgroundDrawable;


    public interface SelectCoinListener {
        void onSelectCoinSelected(Pair<String, String> selectedCoin);
    }

    public CoinSelectorAdapter() {
        this.mData = new ArrayList<>();
    }

    public CoinSelectorAdapter(Drawable drawable) {
        this.mData = new ArrayList<>();
        this.mBackgroundDrawable = drawable;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        if(mBackgroundDrawable != null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_coin_selector_expandable, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_coin_selector, parent, false);
        }
        viewHolder = new CoinSelectorViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Pair<String, String> coin = mData.get(position);


        String coinKey = coin.first;
        String coinCountry = coin.second;

        ((CoinSelectorViewHolder) holder).mCoinText.setText(coinKey);
        ((CoinSelectorViewHolder) holder).mCountryText.setText(coinCountry);

        ((CoinSelectorViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onSelectCoinSelected(coin);
                }
            }
        });

        if(position == mData.size() - 1) {
            ((CoinSelectorViewHolder) holder).mBottomLine.setVisibility(View.GONE);
        } else {
            ((CoinSelectorViewHolder) holder).mBottomLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateData(ArrayList<Pair<String, String>> listBeneficiary) {
        mData.clear();
        mData.addAll(listBeneficiary);
        notifyDataSetChanged();
    }

    public void clearData(){
        mData.clear();
        notifyDataSetChanged();
    }

    public void setOnSelectCoinListener(SelectCoinListener listener){
        this.mListener = listener;
    }


    class CoinSelectorViewHolder extends RecyclerView.ViewHolder {

        StyledTextView mCoinText, mCountryText;
        View mBottomLine;
        RelativeLayout mMainItem;

        public CoinSelectorViewHolder(View itemView) {
            super(itemView);

            mCoinText = (StyledTextView) itemView.findViewById(R.id.coin_text);
            mCountryText = (StyledTextView) itemView.findViewById(R.id.country_text);
            mBottomLine = itemView.findViewById(R.id.bottom_line);
            mMainItem = (RelativeLayout) itemView.findViewById(R.id.main_item);

        }
    }
}
