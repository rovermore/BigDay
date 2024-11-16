package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter;

import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.FLAG_IMAGE_ASSETS;
import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.FLAG_IMAGE_EXTENSION;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.ImageViewExtKt;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;

/**
 * Created by luismiguel on 7/8/17
 */
public class GenericDropContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<KeyValueData> mData;
    private Context mContext;
    private SelectItemListener mListener;
    private String mSelectedValue;
    private String mTypeCell;


    private static final int VIEW_TYPE_BASIC = 0;
    private static final int VIEW_TYPE_VALUE_IMAGE = 1;


    public GenericDropContentAdapter(Context context, ArrayList<KeyValueData> data, String keySeleted, String typeCell) {
        this.mContext = context;
        this.mData = data;
        this.mTypeCell = typeCell;
        this.mSelectedValue = keySeleted;
    }

    public void updateData(ArrayList<KeyValueData> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public interface SelectItemListener {
        void onSelectItemSelected(KeyValueData data);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case VIEW_TYPE_VALUE_IMAGE: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_country, parent, false);
                viewHolder = new GenericDropContentAdapter.ImageContentViewHolder(v);
                return viewHolder;
            }
            case VIEW_TYPE_BASIC:
            default: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_generic_drop_content, parent, false);
                viewHolder = new GenericDropContentAdapter.GenericDropContentViewHolder(v);
                return viewHolder;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final KeyValueData dataValue = mData.get(position);

        if (holder instanceof GenericDropContentViewHolder) {
            ((GenericDropContentViewHolder) holder).bind(dataValue);
        } else if (holder instanceof ImageContentViewHolder) {

            ((ImageContentViewHolder) holder).countryText.setText(dataValue.getValue());

            if (!TextUtils.isEmpty(dataValue.getKey())) {

                ((ImageContentViewHolder) holder).countryImage.setScaleType(ImageView.ScaleType.FIT_XY);

                ImageViewExtKt.loadCircularImage(
                        ((ImageContentViewHolder) holder).countryImage,
                        holder.itemView.getContext(),
                        R.drawable.placeholder_country_adapter,
                        FLAG_IMAGE_ASSETS + dataValue.getKey() + FLAG_IMAGE_EXTENSION
                );

            } else {
                ((ImageContentViewHolder) holder).countryImage.setScaleType(ImageView.ScaleType.CENTER);

                ImageViewExtKt.loadImage(
                        ((ImageContentViewHolder) holder).countryImage,
                        R.drawable.account_icn_selectcountry
                );
            }

            ((ImageContentViewHolder) holder).rootView.setOnClickListener(view -> {
                if (mListener != null) {
                    mListener.onSelectItemSelected(dataValue);
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        switch (mTypeCell) {
            case "":
                return VIEW_TYPE_BASIC;
            case Constants.FIELD_SUBTYPES.PHONE:
                return VIEW_TYPE_VALUE_IMAGE;
            default:
                return VIEW_TYPE_BASIC;
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    public void setSelectDataItemListener(SelectItemListener listener) {
        this.mListener = listener;
    }

    class GenericDropContentViewHolder extends RecyclerView.ViewHolder {

        StyledTextView text;
        private KeyValueData model;

        GenericDropContentViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onSelectItemSelected(model);
                }
            });
        }

        void bind(KeyValueData model) {
            this.model = model;
            text.setText(model.getValue());

            if (!TextUtils.isEmpty(mSelectedValue) && mSelectedValue.equalsIgnoreCase(model.getKey())) {
                text.setBackgroundColor(mContext.getResources().getColor(R.color.medium_blue_color));
                text.setTextColor(Color.WHITE);
            } else {
                text.setBackgroundColor(Color.TRANSPARENT);
                text.setTextColor(mContext.getResources().getColor(R.color.dark_grey_text));
            }
        }
    }

    class ImageContentViewHolder extends RecyclerView.ViewHolder {

        StyledTextView countryText;
        ImageView countryImage;
        RelativeLayout rootView;

        ImageContentViewHolder(View itemView) {
            super(itemView);

            countryText = itemView.findViewById(R.id.country_text);
            countryImage = itemView.findViewById(R.id.country_image);
            rootView = itemView.findViewById(R.id.root_view);
        }

    }
}
