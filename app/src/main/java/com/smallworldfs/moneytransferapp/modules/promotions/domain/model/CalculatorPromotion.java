package com.smallworldfs.moneytransferapp.modules.promotions.domain.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.utils.Constants;

/**
 * Created by luismiguel on 30/6/17.
 */

public class CalculatorPromotion {

    private String promotionName;

    private Double discount;

    private String promotionNumber;

    @SerializedName("automatic")
    private boolean autoAsigned;

    private String promotionType;

    private String promotionCode;

    public CalculatorPromotion(String promotionName, Double promotionDiscount, String promotionNumber, boolean autoAsigned, String promotionType, String promotionCode){
        this.promotionName = promotionName;
        this.discount = promotionDiscount;
        this.promotionNumber = promotionNumber;
        this.autoAsigned = autoAsigned;
        this.promotionType = promotionType;
        this.promotionCode = promotionNumber;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public Double getPromotionDiscount() {
        return discount;
    }

    public void setPromotionDiscount(Double promotionAmount) {
        this.discount = promotionAmount;
    }

    public String getPromotionNumber() {
        return promotionNumber;
    }

    public void setPromotionNumber(String promotionNumber) {
        this.promotionNumber = promotionNumber;
    }

    public boolean isAutoAsigned() {
        return autoAsigned;
    }

    public void setAutoAsigned(boolean autoAsigned) {
        this.autoAsigned = autoAsigned;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getStringPromotinoDescription(Context context, String currency) {
        switch (this.promotionType) {
            case Constants.PROMOTIONS.FEE_AMOUNT:
                return String.format(context.getString(R.string.fee_amount_promotion_text), String.valueOf(discount), currency);
            case Constants.PROMOTIONS.FEE_PERCENT:
                return context.getString(R.string.no_fees_promotion_text);
            case Constants.PROMOTIONS.TOTAL_AMOUNT:
                return String.format(context.getString(R.string.total_amount_promotion_text), String.valueOf(discount), currency);
            case Constants.PROMOTIONS.TOTAL_PERCENT:
                return String.format(context.getString(R.string.total_percent_promotion_text), String.valueOf(discount));
        }
        return "";
    }
}
