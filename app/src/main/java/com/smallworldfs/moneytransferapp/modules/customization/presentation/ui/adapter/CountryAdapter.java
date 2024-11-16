package com.smallworldfs.moneytransferapp.modules.customization.presentation.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.ImageViewExtKt;
import com.smallworldfs.moneytransferapp.utils.CountryUtils;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;

import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.FLAG_IMAGE_ASSETS;
import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.FLAG_IMAGE_EXTENSION;

/**
 * Created by luismiguel on 17/5/17
 */
public class CountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Pair<String, String>> mCountryList;
    private Pair<String, String> mCountrySelected;
    private OnCountrySelectedListener mListener;
    private AdapterType mType;

    public enum AdapterType {
        GRID, HORIZONTAL
    }


    public interface OnCountrySelectedListener {
        void onCountrySelected(Pair<String, String> country);
    }

    public CountryAdapter(Context context, ArrayList<Pair<String, String>> data, Pair<String, String> countrySelected, AdapterType type) {
        this.mContext = context;
        this.mCountrySelected = countrySelected;
        CountryUtils.sortCountryList(data);
        this.mCountryList = data;
        this.mType = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (this.mType == AdapterType.GRID) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_grid_country, viewGroup, false);
            viewHolder = new CountryViewHolder(v);
            return viewHolder;
        }
        else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_horizontal_country, viewGroup, false);
            viewHolder = new CountryViewHolder(v);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (position < mCountryList.size()) {
            final Pair<String, String> country = mCountryList.get(position);


            ((CountryViewHolder) holder).countryText.setText(country.second);

            ImageViewExtKt.loadCircularImage(
                ((CountryViewHolder) holder).countryImage,
                mContext,
                R.drawable.placeholder_country_adapter,
           FLAG_IMAGE_ASSETS + country.first + FLAG_IMAGE_EXTENSION
            );

            if (mCountrySelected != null && mCountrySelected.first.equals(country.first) && mCountrySelected.second.equals(country.second)) {
                ((CountryViewHolder) holder).backgroundSelected.setVisibility(View.VISIBLE);
            } else {
                ((CountryViewHolder) holder).backgroundSelected.setVisibility(View.INVISIBLE);
            }

            ((CountryViewHolder) holder).rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mCountrySelected != null) {
                        int prevIndex = mCountryList.indexOf(mCountrySelected);
                        notifyItemChanged(prevIndex, 1);
                        mCountrySelected = country;
                        notifyItemRangeChanged(position, 1);
                    }

                    if (mListener != null) {
                        mListener.onCountrySelected(country);
                    }

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCountryList.size();
    }


    class CountryViewHolder extends RecyclerView.ViewHolder {

        StyledTextView countryText;
        ImageView countryImage;
        View backgroundSelected;
        LinearLayout rootView;

        public CountryViewHolder(View itemView) {
            super(itemView);
            countryText = itemView.findViewById(R.id.country_text);
            countryImage = itemView.findViewById(R.id.country_image);
            backgroundSelected = itemView.findViewById(R.id.background_selected);
            rootView = itemView.findViewById(R.id.root_view);
        }

    }

    public void setCountryClickListener(OnCountrySelectedListener listener) {
        this.mListener = listener;
    }

    public int getSize(){
        return mCountryList.size();
    }
}
