package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.viewholder;

import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

/**
 * Created by luismiguel on 25/8/17.
 */

public class HomeDeliveryLocationViewType extends RecyclerView.ViewHolder {

    public RelativeLayout cellBackground;
    public StyledTextView feeIncludedValue, rateValue, iofAmount, deliveryTime;
    public StyledTextView bankName;
    public View iofSeparator;

    public HomeDeliveryLocationViewType(View itemView) {
        super(itemView);

        feeIncludedValue = itemView.findViewById(R.id.fee_included);
        rateValue = itemView.findViewById(R.id.rate_value);
        cellBackground = itemView.findViewById(R.id.border_container_layout);
        bankName = itemView.findViewById(R.id.bank_name);
        iofAmount = itemView.findViewById(R.id.tax_iof_amount);
        iofSeparator = itemView.findViewById(R.id.separator_line);
        deliveryTime = itemView.findViewById(R.id.time_delivery);
    }

    public void styleLocationCell(Drawable cellLocationContainerBackground, int textColorValue,
                                  int separatorColor, int iofColor) {

        this.cellBackground.setBackground(cellLocationContainerBackground);
        this.rateValue.setTextColor(textColorValue);
        this.feeIncludedValue.setTextColor(textColorValue);
        this.bankName.setTextColor(textColorValue);
        this.deliveryTime.setTextColor(textColorValue);
        this.iofSeparator.setBackgroundColor(separatorColor);
        this.iofAmount.setTextColor(iofColor);
    }

    public void setIof(String iofAmountText) {
        itemView.findViewById(R.id.iof_box).setVisibility(View.VISIBLE);
        iofAmount.setText(iofAmountText);
    }

    public void hideIof() {
        itemView.findViewById(R.id.iof_box).setVisibility(View.GONE);
    }
}
