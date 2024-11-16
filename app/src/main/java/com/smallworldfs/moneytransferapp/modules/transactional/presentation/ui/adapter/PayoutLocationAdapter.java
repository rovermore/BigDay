package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.viewholder.BankDepositLocationViewType;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.viewholder.CashPickupLocationViewType;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.viewholder.HomeDeliveryLocationViewType;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.viewholder.MobileWalletLocationViewType;
import com.smallworldfs.moneytransferapp.utils.AmountFormatter;
import com.smallworldfs.moneytransferapp.utils.Constants;

/**
 * Created by luismiguel on 25/8/17
 */
public class PayoutLocationAdapter extends AppendableRecyclerViewAdapter<RecyclerView.ViewHolder, Field> {

    private static final int VIEW_TYPE_CASH_PICKUP = 0;
    private static final int VIEW_TYPE_BANK_DEPOSIT = 1;
    private static final int VIEW_TYPE_HOME_DELIVERY = 2;
    private static final int VIEW_TYPE_MOBILE_WALLET = 3;
    private Context mContext;
    private String mItemIdSelected;
    private String mNameSelected;
    private double mTaxSelected;
    private PayoutListener mListener;
    private String mCurrency;


    public PayoutLocationAdapter(Context context, String itemIdSelected, String currency,
                                 String nameSelected, double taxSelected) {
        mContext = context;
        mItemIdSelected = itemIdSelected;
        mNameSelected = nameSelected;
        mTaxSelected = taxSelected;
        mCurrency = currency;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case VIEW_TYPE_CASH_PICKUP: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payout_cashpickup, parent, false);
                viewHolder = new CashPickupLocationViewType(v);
                return viewHolder;
            }
            case VIEW_TYPE_BANK_DEPOSIT: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payout_bankdeposit, parent, false);
                viewHolder = new BankDepositLocationViewType(v);
                return viewHolder;
            }
            case VIEW_TYPE_HOME_DELIVERY: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payout_home_delivery, parent, false);
                viewHolder = new HomeDeliveryLocationViewType(v);
                return viewHolder;
            }
            case VIEW_TYPE_MOBILE_WALLET: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payout_mobile_wallet, parent, false);
                viewHolder = new MobileWalletLocationViewType(v);
                return viewHolder;
            }

            default:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payout_cashpickup, parent, false);
                viewHolder = new CashPickupLocationViewType(v);
                return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Field field = mData.get(position);
        switch (field.getSubtype()) {
            case Constants.FIELD_SUBTYPES.LOCATION_PICK_UP: {
                processLocationCashPickupCell((CashPickupLocationViewType) holder, field, position);
                break;
            }
            case Constants.FIELD_SUBTYPES.LOCATION_BANK_DEPOSIT: {
                processLocationBankDepositCell((BankDepositLocationViewType) holder, field, position);
                break;
            }
            case Constants.FIELD_SUBTYPES.LOCATION_HOME_DELIVERY: {
                processLocationHomeDeliveryCell((HomeDeliveryLocationViewType) holder, field, position);
                break;
            }
            case Constants.FIELD_SUBTYPES.LOCATION_MOBILE_WALLET: {
                processLocationMobileWalletCell((MobileWalletLocationViewType) holder, field, position);
                break;
            }
        }
    }

    private void processLocationHomeDeliveryCell(HomeDeliveryLocationViewType viewHolder, final Field field, int position) {
        boolean selected = mItemIdSelected.equalsIgnoreCase(field.getPayout().getLocationCode()) &&
                mNameSelected.equalsIgnoreCase(field.getPayout().getRepresentativeName()) &&
                        mTaxSelected == field.getPayout().getRate();
        /*mItemIdSelected.equals(field.getPayout().getLocationCode()) ||
                TextUtils.isEmpty(mItemIdSelected) && position == 0;*/

        if (field.getPayout().getTaxes() != null && field.getPayout().getTaxes().getTaxAmount() != null &&
                field.getPayout().getTaxes().getTaxCode() != null && !field.getPayout().getTaxes().getTaxAmount().isEmpty() &&
                !field.getPayout().getTaxes().getTaxCode().isEmpty()) {
            viewHolder.setIof(field.getPayout().getTaxes().getFormatedTaxAmount() + " " + mCurrency);
        } else {
            viewHolder.hideIof();
        }

        viewHolder.styleLocationCell(
                selected
                        ? ContextCompat.getDrawable(mContext, R.drawable.background_blue_border_location_white)
                        : ContextCompat.getDrawable(mContext, R.drawable.background_grey_border_location_white),
                selected
                        ? ContextCompat.getColor(mContext, R.color.medium_blue_color)
                        : ContextCompat.getColor(mContext, R.color.default_text_color),
                selected ? ContextCompat.getColor(mContext, R.color.medium_blue_color) : ContextCompat.getColor(mContext, R.color.light_grey_text_color),
                selected ? ContextCompat.getColor(mContext, R.color.medium_blue_color) : ContextCompat.getColor(mContext, R.color.secondary_dark_grey_text));


        viewHolder.feeIncludedValue.setText(AmountFormatter.formatDoubleAmountNumber(field.getPayout().getFee()) + " " + mCurrency);
        viewHolder.rateValue.setText(AmountFormatter.formatDoubleRateNumber(field.getPayout().getRate()));
        viewHolder.bankName.setText(TextUtils.isEmpty(field.getPayout().getRepresentativeName()) ? "" : field.getPayout().getRepresentativeName());

        String timeDelivery = TextUtils.isEmpty(field.getPayout().deliveryTime) ? "" : field.getPayout().deliveryTime;

        viewHolder.deliveryTime.setText(timeDelivery);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onSelectPayoutListener(field);
                }
            }
        });
    }

    private void processLocationCashPickupCell(CashPickupLocationViewType viewHolder, final Field field, int position) {
        String bankName = TextUtils.isEmpty(field.getPayout().getRepresentativeName()) ? "" : field.getPayout().getRepresentativeName();
        String feeIncluded = AmountFormatter.formatDoubleAmountNumber(field.getPayout().getFee());
        String rate = AmountFormatter.formatDoubleRateNumber(field.getPayout().getRate());
        String timeDelivery = TextUtils.isEmpty(field.getPayout().deliveryTime) ? "" : field.getPayout().deliveryTime;
        int numLocations = field.getPayout().getCountLocations();

        if (field.getPayout().getTaxes() != null && field.getPayout().getTaxes().getTaxAmount() != null &&
                field.getPayout().getTaxes().getTaxCode() != null && !field.getPayout().getTaxes().getTaxAmount().isEmpty() &&
                !field.getPayout().getTaxes().getTaxCode().isEmpty()) {
            viewHolder.setIof(field.getPayout().getTaxes().getFormatedTaxAmount() + " " + mCurrency);
        } else {
            viewHolder.hideIof();
        }

        if (field.getPayout().getCountLocations() > 1) {
            viewHolder.locationTitle.setText(viewHolder.itemView.getContext().getString(R.string.pick_up_location_plural));
        } else {
            viewHolder.locationTitle.setText(viewHolder.itemView.getContext().getString(R.string.pick_up_location_single));
        }
        viewHolder.bankName.setText(bankName);
        viewHolder.feeIncluded.setText(feeIncluded + " " + mCurrency);
        viewHolder.rate.setText(rate);
        viewHolder.numLocations.setText(String.valueOf(numLocations));
        viewHolder.timeDelivery.setText(timeDelivery);

        //boolean selected = mItemIdSelected.equals(field.getPayout().getLocationCode()) || TextUtils.isEmpty(mItemIdSelected) && position == 0;
        boolean selected = mItemIdSelected.equalsIgnoreCase(field.getPayout().getLocationCode()) &&
                mNameSelected.equalsIgnoreCase(field.getPayout().getRepresentativeName()) &&
                mTaxSelected == field.getPayout().getRate(); // || TextUtils.isEmpty(mItemIdSelected) && position == 0;

        viewHolder.styleLocationCell(
                selected ? ContextCompat.getColor(mContext, R.color.medium_blue_color) : ContextCompat.getColor(mContext, R.color.secondary_dark_grey_text),
                selected ? ContextCompat.getDrawable(mContext, R.drawable.background_blue_location) : ContextCompat.getDrawable(mContext, R.drawable.background_grey_location),
                selected ? ContextCompat.getDrawable(mContext, R.drawable.background_blue_border_location_white) : ContextCompat.getDrawable(mContext, R.drawable.background_grey_border_location_white),
                selected ? ContextCompat.getColor(mContext, R.color.medium_blue_color) : ContextCompat.getColor(mContext, R.color.light_grey_text_color),
                selected ? ContextCompat.getColor(mContext, R.color.medium_blue_color) : ContextCompat.getColor(mContext, R.color.secondary_dark_grey_text)
        );

        if (viewHolder.itemView != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onSelectPayoutListener(field);
                    }
                }
            });
        }

        if (viewHolder.itemView != null){
            viewHolder.itemView.findViewById(R.id.locations_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        mListener.onPayoutMapListener(field);
                    }
                }
            });
        }

    }

    private void processLocationBankDepositCell(BankDepositLocationViewType viewHolder, final Field field, int position) {
        String bankName = TextUtils.isEmpty(field.getPayout().getRepresentativeName()) ? "" : field.getPayout().getRepresentativeName();
        String feeIncluded = AmountFormatter.formatDoubleAmountNumber(field.getPayout().getFee());
        String rate = AmountFormatter.formatDoubleRateNumber(field.getPayout().getRate());
        String deliveryTime = field.getPayout().getDeliveryTime();
        String cellTitle = field.getPayout().getDiccType();

        if (field.getPayout().getTaxes() != null && field.getPayout().getTaxes().getTaxAmount() != null &&
                field.getPayout().getTaxes().getTaxCode() != null && !field.getPayout().getTaxes().getTaxAmount().isEmpty() &&
                !field.getPayout().getTaxes().getTaxCode().isEmpty()) {
            viewHolder.setIof(field.getPayout().getTaxes().getFormatedTaxAmount() + " " + mCurrency);
        } else {
            viewHolder.hideIof();
        }

        viewHolder.bankName.setText(bankName);
        viewHolder.cellTitle.setText(cellTitle);
        viewHolder.feeIncluded.setText(feeIncluded + " " + mCurrency);
        viewHolder.rate.setText(rate);
        viewHolder.timeDelivery.setText(deliveryTime);

        boolean selected = mItemIdSelected.equalsIgnoreCase(field.getPayout().getLocationCode()) &&
                mNameSelected.equalsIgnoreCase(field.getPayout().getRepresentativeName()) &&
                mTaxSelected == field.getPayout().getRate();
        /*mItemIdSelected.equals(field.getPayout().getLocationCode()) ||
                TextUtils.isEmpty(mItemIdSelected) && position == 0;*/

        Drawable iconCell = field.getPayout().getIconType(mContext, selected);

        viewHolder.styleLocationCell(
                selected ? ContextCompat.getColor(mContext, R.color.white) : ContextCompat.getColor(mContext, R.color.secondary_dark_grey_text),
                selected ? ContextCompat.getDrawable(mContext, R.drawable.background_blue_location) : ContextCompat.getDrawable(mContext, R.drawable.background_grey_location),
                selected ? ContextCompat.getDrawable(mContext, R.drawable.background_blue_border_location_white) : ContextCompat.getDrawable(mContext, R.drawable.background_grey_border_location_white),
                selected ? ContextCompat.getColor(mContext, R.color.medium_blue_color) : ContextCompat.getColor(mContext, R.color.secondary_dark_grey_text),
                selected ? ContextCompat.getColor(mContext, R.color.medium_blue_color) : ContextCompat.getColor(mContext, R.color.light_grey_text_color),
                selected ? ContextCompat.getColor(mContext, R.color.medium_blue_color) : ContextCompat.getColor(mContext, R.color.secondary_dark_grey_text)
        );

        viewHolder.cellTitle.setCompoundDrawablesWithIntrinsicBounds(iconCell, null, null, null);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onSelectPayoutListener(field);
                }
            }
        });
    }

    private void processLocationMobileWalletCell(MobileWalletLocationViewType viewHolder, final Field field, int position) {
        String bankName = TextUtils.isEmpty(field.getPayout().getRepresentativeName()) ? "" : field.getPayout().getRepresentativeName();
        String feeIncluded = AmountFormatter.formatDoubleAmountNumber(field.getPayout().getFee());
        String rate = AmountFormatter.formatDoubleRateNumber(field.getPayout().getRate());
        String timeDelivery = TextUtils.isEmpty(field.getPayout().getDeliveryTime()) ? "" : field.getPayout().getDeliveryTime();

        if (field.getPayout().getTaxes() != null && field.getPayout().getTaxes().getTaxAmount() != null &&
                field.getPayout().getTaxes().getTaxCode() != null && !field.getPayout().getTaxes().getTaxAmount().isEmpty() &&
                !field.getPayout().getTaxes().getTaxCode().isEmpty()) {
            viewHolder.setIof(field.getPayout().getTaxes().getFormatedTaxAmount() + " " + mCurrency);
        } else {
            viewHolder.hideIof();
        }

        viewHolder.bankName.setText(bankName);
        viewHolder.feeIncludedValue.setText(feeIncluded + " " + mCurrency);
        viewHolder.rateValue.setText(rate);
        viewHolder.timeDelivery.setText(timeDelivery);

        boolean selected = mItemIdSelected.equalsIgnoreCase(field.getPayout().getLocationCode()) &&
                mNameSelected.equalsIgnoreCase(field.getPayout().getRepresentativeName()) &&
                mTaxSelected == field.getPayout().getRate();
                /*mItemIdSelected.contains(field.getPayout().getLocationCode()) || TextUtils.isEmpty(mItemIdSelected) && position == 0;*/

        viewHolder.styleLocationCell(
                selected
                        ? ContextCompat.getDrawable(mContext, R.drawable.background_blue_border_location_white)
                        : ContextCompat.getDrawable(mContext, R.drawable.background_grey_border_location_white),
                selected
                        ? ContextCompat.getColor(mContext, R.color.medium_blue_color)
                        : ContextCompat.getColor(mContext, R.color.default_text_color),
                selected ? ContextCompat.getColor(mContext, R.color.medium_blue_color) : ContextCompat.getColor(mContext, R.color.light_grey_text_color),
                selected ? ContextCompat.getColor(mContext, R.color.medium_blue_color) : ContextCompat.getColor(mContext, R.color.secondary_dark_grey_text));


        if (viewHolder.itemView != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onSelectPayoutListener(field);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        Field field = mData.get(position);

        switch (field.getSubtype()) {
            case Constants.FIELD_SUBTYPES.LOCATION_PICK_UP:
                return VIEW_TYPE_CASH_PICKUP;
            case Constants.FIELD_SUBTYPES.LOCATION_BANK_DEPOSIT:
                return VIEW_TYPE_BANK_DEPOSIT;
            case Constants.FIELD_SUBTYPES.LOCATION_HOME_DELIVERY:
                return VIEW_TYPE_HOME_DELIVERY;
            case Constants.FIELD_SUBTYPES.LOCATION_MOBILE_WALLET:
                return VIEW_TYPE_MOBILE_WALLET;
            default:
                return VIEW_TYPE_CASH_PICKUP;
        }
    }

    public void setPayoutListener(PayoutListener listener) {
        this.mListener = listener;
    }


    public interface PayoutListener {
        void onSelectPayoutListener(Field field);
        void onPayoutMapListener(Field field);
    }
}


