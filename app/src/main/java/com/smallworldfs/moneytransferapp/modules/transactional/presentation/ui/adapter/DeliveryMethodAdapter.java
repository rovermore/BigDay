package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.ImageViewExtKt;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.GenericFormField;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;


/**
 * Created by luismiguel on 17/7/17.
 */

public class DeliveryMethodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<? extends GenericFormField> mData;
    private Context mContext;
    private Method mSelectedMethod;
    private SelectDeliveryMethodListener mListener;
    private boolean mShouldAskChangeDeliveryMethod = false;

    public DeliveryMethodAdapter(Context context, ArrayList<? extends GenericFormField> data, boolean shouldAskChangeDeliveryMethod) {
        this.mContext = context;
        this.mData = data;
        this.mShouldAskChangeDeliveryMethod = shouldAskChangeDeliveryMethod;
    }

    public interface SelectDeliveryMethodListener {
        void onDeliveryMethodSelected(Method method);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_horizontal_delivery_method, parent, false);
        viewHolder = new DeliveryMethodAdapter.DeliveryMethodViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Method method = (Method) mData.get(position);

        ((DeliveryMethodViewHolder) holder).deliveryMethodName.setText(method.getMethod().firstEntry().getValue());

        if (method.getMethod().firstEntry() != null && method.getMethod().firstEntry().getKey() != null) {
            holder.itemView.setContentDescription(method.getMethod().firstEntry().getKey());
        }

        // Delivery Method icon
        int icon;
        switch (method.getMethod().firstEntry().getKey()) {
            case Constants.DELIVERY_METHODS.BANK_DEPOSIT:
                icon = R.drawable.transactional_icn_bankdeposit;
                break;
            case Constants.DELIVERY_METHODS.CASH_PICKUP:
                icon = R.drawable.transactional_icn_cashpickhup;
                break;
            case Constants.DELIVERY_METHODS.TOP_UP:
                icon = R.drawable.transactional_icn_topup;
                break;
            case Constants.DELIVERY_METHODS.MOBILE_WALLET:
                icon = R.drawable.transactional_icn_mobilewallet;
                break;
            case Constants.DELIVERY_METHODS.PHYSICAL_DELIVERY:
                icon = R.drawable.transactional_icn_homedelivery;
                break;
            case Constants.DELIVERY_METHODS.CASH_CARD_RELOAD:
                icon = R.drawable.transactional_icn_cashcardreload;
                break;
            case Constants.DELIVERY_METHODS.ON_CALL:
                icon = R.drawable.transactional_icn_pickupanywhere;
                break;
            default:
                icon = -1;
                break;
        }

        if (icon != -1) {
            ((DeliveryMethodViewHolder) holder).transactionTypeImage.setVisibility(View.VISIBLE);
            ImageViewExtKt.loadImage(
                ((DeliveryMethodViewHolder) holder).transactionTypeImage,
                icon
            );
        } else {
            ((DeliveryMethodViewHolder) holder).transactionTypeImage.setVisibility(View.INVISIBLE);
        }

        // Selected Method
        if (mSelectedMethod != null && mSelectedMethod.getMethod().firstEntry().getKey().equalsIgnoreCase(method.getMethod().firstEntry().getKey())) {
            ((DeliveryMethodViewHolder) holder).backgroundSelectedBeneficiary.setVisibility(View.VISIBLE);
        } else {
            ((DeliveryMethodViewHolder) holder).backgroundSelectedBeneficiary.setVisibility(View.GONE);
        }

        ((DeliveryMethodViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectedMethod != null
                        && mSelectedMethod.getMethod().firstEntry().getKey().equals(method.getMethod().firstEntry().getKey())) {
                    return;
                } else {
                    if (mSelectedMethod != null && mShouldAskChangeDeliveryMethod) {
                        new DialogExt().showDoubleActionGeneralDialog((AppCompatActivity) mContext, mContext.getString(R.string.edit_beneficiary_change_delivery_method_dialog_title),
                                mContext.getString(R.string.edit_beneficiary_change_delivery_method_dialog_content),
                                mContext.getString(R.string.accept_text),
                                new DialogExt.OnPositiveClick() {
                                    @Override
                                    public void onClick() {
                                        mShouldAskChangeDeliveryMethod = false;
                                        selectDeliveryMethod(method);
                                    }
                                },
                                mContext.getString(R.string.cancel),
                                new DialogExt.OnNegativeClick(){
                                    @Override
                                    public void onClick() {}
                                });

                    } else {
                        selectDeliveryMethod(method);
                    }
                }

            }
        });
    }

    private void selectDeliveryMethod(Method method) {
        mSelectedMethod = method;
        notifyDataSetChanged();

        if (mListener != null) {
            mListener.onDeliveryMethodSelected(mSelectedMethod);
        }
    }

    public void setMethodSelected(Method method) {
        this.mSelectedMethod = method;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setDeliveryMethodListener(SelectDeliveryMethodListener listener) {
        this.mListener = listener;
    }

    class DeliveryMethodViewHolder extends RecyclerView.ViewHolder {

        StyledTextView deliveryMethodName;
        ImageView transactionTypeImage, backgroundSelectedBeneficiary;

        public DeliveryMethodViewHolder(View itemView) {
            super(itemView);

            deliveryMethodName = itemView.findViewById(R.id.delivery_method_name_text);
            transactionTypeImage = itemView.findViewById(R.id.image_type_delivery_method);
            backgroundSelectedBeneficiary = itemView.findViewById(R.id.background_selected_beneficiary);
        }
    }

    public void setSelectedDeliveryMethod(Method deliveryMethod) {
        mSelectedMethod = deliveryMethod;
        notifyDataSetChanged();
    }

    public String getSelectedDeliveryMethod() {
        return mSelectedMethod.getMethod().firstKey();
    }
}
