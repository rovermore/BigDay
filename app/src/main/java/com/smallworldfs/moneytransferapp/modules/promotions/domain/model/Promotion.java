package com.smallworldfs.moneytransferapp.modules.promotions.domain.model;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.utils.Constants;

/**
 * Created by luis on 29/6/17.
 */

public class Promotion {

    private String promotionName;

    private String promotionCode;

    private String type;

    private String countryOrigin;

    private String countryDestination;

    private String discount;

    private String expireDate;

    @SerializedName("discount_type")
    private String discountType;


    public Promotion(String promotionCode, String discountType, String expireDate, String discount) {
        this.promotionCode = promotionCode;
        this.discountType = discountType;
        this.expireDate = expireDate;
        this.discount = discount;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountryOrigin() {
        return countryOrigin;
    }

    public void setCountryOrigin(String countryOrigin) {
        this.countryOrigin = countryOrigin;
    }

    public String getCountryDestination() {
        return countryDestination;
    }

    public void setCountryDestination(String countryDestination) {
        this.countryDestination = countryDestination;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getStringPromotinoDescription(Context context, String currency) {
        if(!TextUtils.isEmpty(this.discountType)){
            switch (this.discountType) {
                case Constants.PROMOTIONS.FEE_AMOUNT:
                    return String.format(context.getString(R.string.fee_amount_promotion_text), discount, currency);
                case Constants.PROMOTIONS.FEE_PERCENT:
                    return context.getString(R.string.no_fees_promotion_text);
                case Constants.PROMOTIONS.TOTAL_AMOUNT:
                    return String.format(context.getString(R.string.total_amount_promotion_text), discount, currency);
                case Constants.PROMOTIONS.TOTAL_PERCENT:
                    return String.format(context.getString(R.string.total_percent_promotion_text), discount);
            }
            return "";
        } else {
            return "";
        }
    }
}
