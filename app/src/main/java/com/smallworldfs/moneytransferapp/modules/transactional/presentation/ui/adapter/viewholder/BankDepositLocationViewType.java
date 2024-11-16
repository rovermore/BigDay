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

public class BankDepositLocationViewType extends RecyclerView.ViewHolder {

    public StyledTextView feeIncluded, rate, timeDelivery, iofAmount;
    public StyledTextView bankName, cellTitle;
    public LinearLayout cellLocationContainer;
    public RelativeLayout borderContainerLayout;
    public View iofSeparator;


    public BankDepositLocationViewType(View itemView) {
        super(itemView);
        cellTitle = itemView.findViewById(R.id.cell_title);
        bankName = itemView.findViewById(R.id.bank_name);
        feeIncluded = itemView.findViewById(R.id.fee_included);
        rate = itemView.findViewById(R.id.rate_value);
        cellLocationContainer = itemView.findViewById(R.id.cell_location_container);
        borderContainerLayout = itemView.findViewById(R.id.border_container_layout);
        timeDelivery = itemView.findViewById(R.id.time_delivery);
        iofAmount = itemView.findViewById(R.id.tax_iof_amount);
        iofSeparator = itemView.findViewById(R.id.separator_line);
    }

    public void styleLocationCell(int cellTitleColor, Drawable mainBackgroundCell, Drawable contentBackgroundBorder, int textColor, int separatorColor,
                                  int iofColor) {
        this.cellTitle.setTextColor(cellTitleColor);
        this.cellLocationContainer.setBackgroundDrawable(mainBackgroundCell);
        this.bankName.setTextColor(textColor);
        this.borderContainerLayout.setBackgroundDrawable(contentBackgroundBorder);
        this.feeIncluded.setTextColor(textColor);
        this.rate.setTextColor(textColor);
        this.timeDelivery.setTextColor(textColor);
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
