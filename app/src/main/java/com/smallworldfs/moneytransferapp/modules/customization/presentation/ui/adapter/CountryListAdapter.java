package com.smallworldfs.moneytransferapp.modules.customization.presentation.ui.adapter;

import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.FLAG_IMAGE_ASSETS;
import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.FLAG_IMAGE_EXTENSION;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.ImageViewExtKt;
import com.smallworldfs.moneytransferapp.utils.CountryUtils;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;

public class CountryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<Pair<String, String>> mCountryList;
    private OnCountrySelectedListener mListener;
    private boolean blueSeparatorVisible = false;


    public interface OnCountrySelectedListener {
        void onCountrySelected(Pair<String, String> country);
    }

    public CountryListAdapter() {
        this.mCountryList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_list_country, viewGroup, false);
        viewHolder = new CountryViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (position < mCountryList.size()) {
            final Pair<String, String> country = mCountryList.get(position);

            ((CountryViewHolder) holder).countryText.setText(country.second);

            ImageViewExtKt.loadCircularImage(
                ((CountryViewHolder) holder).countryImage,
                    holder.itemView.getContext(),
                    R.drawable.placeholder_country_adapter,
                    FLAG_IMAGE_ASSETS + country.first + FLAG_IMAGE_EXTENSION
            );

            ((CountryViewHolder) holder).rootView.setOnClickListener(view -> {
                if (mListener != null) {
                    mListener.onCountrySelected(country);
                }
            });

            ((CountryViewHolder) holder).adapterListCountryBlueSeparator.setVisibility(position == 4 && blueSeparatorVisible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mCountryList.size();
    }


    static class CountryViewHolder extends RecyclerView.ViewHolder {

        StyledTextView countryText;
        ImageView countryImage;
        RelativeLayout rootView;
        View adapterListCountryBlueSeparator;

        CountryViewHolder(View itemView) {
            super(itemView);
            countryText = itemView.findViewById(R.id.country_text);
            countryImage = itemView.findViewById(R.id.country_image);
            rootView = itemView.findViewById(R.id.root_view);
            adapterListCountryBlueSeparator = itemView.findViewById(R.id.adapterListCountryBlueSeparator);
        }
    }

    public void updateData(ArrayList<Pair<String, String>> data, boolean orderList) {
        this.mCountryList.clear();
        if(orderList) CountryUtils.sortCountryList(data);
        this.mCountryList.addAll(data);
        notifyDataSetChanged();
    }

    public void setCountryClickListener(OnCountrySelectedListener listener) {
        this.mListener = listener;
    }

    public void setBlueSeparatorVisible(boolean blueSeparatorVisible) {
        this.blueSeparatorVisible = blueSeparatorVisible;
        notifyDataSetChanged();
    }

}
