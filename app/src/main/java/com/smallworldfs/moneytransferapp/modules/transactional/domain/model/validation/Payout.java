package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.content.ContextCompat;

import com.google.gson.annotations.SerializedName;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Taxes;
import com.smallworldfs.moneytransferapp.utils.Constants;

import java.util.ArrayList;

public class Payout implements Parcelable {

    @SerializedName("representativeCode")
    public int representativeCode;
    @SerializedName("representativeName")
    public String representativeName;
    @SerializedName("representativeLogo")
    public String representativeLogo;
    @SerializedName("countLocations")
    public int countLocations;
    @SerializedName("locationCode")
    public String locationCode;
    @SerializedName("totalSale")
    public double totalSale;
    @SerializedName("principal")
    public double principal;
    @SerializedName("payoutPrincipal")
    public double payoutPrincipal;
    @SerializedName("rate")
    public double rate;
    @SerializedName("fee")
    public double fee;
    @SerializedName("promotionAmount")
    public double promotionAmount;
    @SerializedName("promotionName")
    public String promotionName;
    @SerializedName("promotionNumber")
    public String promotionNumber;
    @SerializedName("type")
    public String type;
    @SerializedName("diccDeliveryTime")
    public String diccDeliveryTime;
    @SerializedName("deliveryTime")
    public String deliveryTime;
    @SerializedName("diccType")
    public String diccType;
    @SerializedName("locations")
    public ArrayList<Locations> locations;
    @SerializedName("locationCity")
    public String locationCity;
    @SerializedName("taxes")
    private Taxes taxes;
    @SerializedName("locationName")
    private String locationName;
    @SerializedName("locationAddress")
    private String locationAddress;

    protected Payout(Parcel in) {
        representativeCode = in.readInt();
        representativeName = in.readString();
        representativeLogo = in.readString();
        countLocations = in.readInt();
        locationCode = in.readString();
        totalSale = in.readDouble();
        principal = in.readDouble();
        payoutPrincipal = in.readDouble();
        rate = in.readDouble();
        fee = in.readDouble();
        promotionAmount = in.readDouble();
        promotionName = in.readString();
        promotionNumber = in.readString();
        type = in.readString();
        diccDeliveryTime = in.readString();
        deliveryTime = in.readString();
        diccType = in.readString();
        locations = in.createTypedArrayList(Locations.CREATOR);
        locationCity = in.readString();
        taxes = in.readParcelable(Taxes.class.getClassLoader());
        locationName = in.readString();
        locationAddress = in.readString();
    }

    public static final Creator<Payout> CREATOR = new Creator<Payout>() {
        @Override
        public Payout createFromParcel(Parcel in) {
            return new Payout(in);
        }

        @Override
        public Payout[] newArray(int size) {
            return new Payout[size];
        }
    };

    public int getRepresentativeCode() {
        return representativeCode;
    }

    public void setRepresentativeCode(int representativeCode) {
        this.representativeCode = representativeCode;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public String getRepresentativeLogo() {
        return representativeLogo;
    }

    public void setRepresentativeLogo(String representativeLogo) {
        this.representativeLogo = representativeLogo;
    }

    public int getCountLocations() {
        return countLocations;
    }

    public void setCountLocations(int countLocations) {
        this.countLocations = countLocations;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public double getPayoutPrincipal() {
        return payoutPrincipal;
    }

    public void setPayoutPrincipal(int payoutPrincipal) {
        this.payoutPrincipal = payoutPrincipal;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getFee() {
        return fee;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public double getPromotionAmount() {
        return promotionAmount;
    }

    public void setPromotionAmount(int promotionAmount) {
        this.promotionAmount = promotionAmount;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionNumber() {
        return promotionNumber;
    }

    public void setPromotionNumber(String promotionNumber) {
        this.promotionNumber = promotionNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDiccDeliveryTime() {
        return diccDeliveryTime;
    }

    public void setDiccDeliveryTime(String diccDeliveryTime) {
        this.diccDeliveryTime = diccDeliveryTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public void setPayoutPrincipal(double payoutPrincipal) {
        this.payoutPrincipal = payoutPrincipal;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public void setPromotionAmount(double promotionAmount) {
        this.promotionAmount = promotionAmount;
    }

    public String getDiccType() {
        return diccType;
    }

    public void setDiccType(String diccType) {
        this.diccType = diccType;
    }

    public ArrayList<Locations> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Locations> locations) {
        this.locations = locations;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public Taxes getTaxes() {
        return taxes;
    }

    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(representativeCode);
        dest.writeString(representativeName);
        dest.writeString(representativeLogo);
        dest.writeInt(countLocations);
        dest.writeString(locationCode);
        dest.writeDouble(totalSale);
        dest.writeDouble(principal);
        dest.writeDouble(payoutPrincipal);
        dest.writeDouble(rate);
        dest.writeDouble(fee);
        dest.writeDouble(promotionAmount);
        dest.writeString(promotionName);
        dest.writeString(promotionNumber);
        dest.writeString(type);
        dest.writeString(diccDeliveryTime);
        dest.writeString(deliveryTime);
        dest.writeString(diccType);
        dest.writeTypedList(locations);
        dest.writeString(locationCity);
        dest.writeParcelable(taxes, flags);
        dest.writeString(locationName);
        dest.writeString(locationAddress);
    }

    public Drawable getIconType(Context context, boolean selected) {
        switch (this.type) {
            case Constants.BANK_DEPOSIT_TYPOLOGY.BEST_RATE:
                return selected ? ContextCompat.getDrawable(context, R.drawable.transactional_icn_starselected) : ContextCompat.getDrawable(context, R.drawable.transactional_icn_star);
            case Constants.BANK_DEPOSIT_TYPOLOGY.INSTANT_DELIVERY:
                return selected ? ContextCompat.getDrawable(context, R.drawable.transactional_icn_rocketselected) : ContextCompat.getDrawable(context, R.drawable.transactional_icn_rocket);
            case Constants.BANK_DEPOSIT_TYPOLOGY.SAMEDAY_DELIVERY:
                return selected ? ContextCompat.getDrawable(context, R.drawable.transactional_icn_clockselected) : ContextCompat.getDrawable(context, R.drawable.transactional_icn_clock);
            default:
                return null;
        }
    }
}
