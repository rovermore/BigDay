package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.viewholder;

import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

/**
 * Created by luismiguel on 25/8/17.
 */

public class CashPickupLocationViewType extends RecyclerView.ViewHolder {

    public StyledTextView numLocations, feeIncluded, rate, timeDelivery, locationTitle, iofAmount;
    public StyledTextView bankName;
    public LinearLayout cellLocationContainer;
    public RelativeLayout borderContainerLayout;
    public View iofSeparator;

    public CashPickupLocationViewType(View itemView) {
        super(itemView);

        bankName = itemView.findViewById(R.id.bank_name);
        feeIncluded = itemView.findViewById(R.id.fee_included);
        rate = itemView.findViewById(R.id.rate_value);
        numLocations = itemView.findViewById(R.id.num_locations);
        cellLocationContainer = itemView.findViewById(R.id.cell_location_container);
        borderContainerLayout = itemView.findViewById(R.id.border_container_layout);
        timeDelivery = itemView.findViewById(R.id.time_delivery);
        locationTitle = itemView.findViewById(R.id.locations_title);
        iofAmount = itemView.findViewById(R.id.tax_iof_amount);
        iofSeparator = itemView.findViewById(R.id.separator_line);
    }

    public void styleLocationCell(int textColor, Drawable cellLocationContainerBackground, Drawable borderContainerLayoutBackground, int separatorColor,
                                  int iofColor) {

        this.bankName.setTextColor(textColor);
        this.cellLocationContainer.setBackground(cellLocationContainerBackground);
        this.borderContainerLayout.setBackground(borderContainerLayoutBackground);
        this.feeIncluded.setTextColor(textColor);
        this.rate.setTextColor(textColor);
        if (timeDelivery != null) {
            timeDelivery.setTextColor(textColor);
        }
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
