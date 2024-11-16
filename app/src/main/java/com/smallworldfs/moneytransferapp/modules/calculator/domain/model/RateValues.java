package com.smallworldfs.moneytransferapp.modules.calculator.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by luismiguel on 28/6/17.
 */

public class RateValues {

    private double payoutPrincipal;

    private long payoutRepresentativeCode;

    private double principal;

    private Double promotionAmount;

    private String promotionName;

    private String promotionNumber;

    private double rate;

    private double totalFee;

    @SerializedName("taxes")
    @Expose
    private Taxes taxes;

    private double totalSale;

    private String promotionType;

    private double discount;

    private String promotionCode;

    @SerializedName("automatic")
    private boolean autoAsigned;

    public double getPayoutPrincipal() {
        return payoutPrincipal;
    }

    public void setPayoutPrincipal(double payoutPrincipal) {
        this.payoutPrincipal = payoutPrincipal;
    }

    public long getPayoutRepresentativeCode() {
        return payoutRepresentativeCode;
    }

    public void setPayoutRepresentativeCode(int payoutRepresentativeCode) {
        this.payoutRepresentativeCode = payoutRepresentativeCode;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(int principal) {
        this.principal = principal;
    }

    public Double getPromotionAmount() {
        return promotionAmount;
    }

    public void setPromotionAmount(Double promotionAmount) {
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

    public Double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }

    public void setPayoutRepresentativeCode(long payoutRepresentativeCode) {
        this.payoutRepresentativeCode = payoutRepresentativeCode;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public RateValues(double payoutPrincipal, long payoutRepresentativeCode,
                      int principal, double promotionAmount,
                      String promotionName, String promotionNumber,
                      double rate, double totalFee,
                      Taxes taxes,
                      double totalSale) {
        this.payoutPrincipal = payoutPrincipal;
        this.payoutRepresentativeCode = payoutRepresentativeCode;
        this.principal = principal;
        this.promotionAmount = promotionAmount;
        this.promotionName = promotionName;
        this.promotionNumber = promotionNumber;
        this.rate = rate;
        this.totalFee = totalFee;
        this.taxes = taxes;
        this.totalSale = totalSale;
    }

    public boolean isAuto() {
        return autoAsigned;
    }

    public void setAuto(boolean auto) {
        this.autoAsigned = auto;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public boolean isAutoAsigned() {
        return autoAsigned;
    }

    public void setAutoAsigned(boolean autoAsigned) {
        this.autoAsigned = autoAsigned;
    }

    public Taxes getTaxes() {
        return taxes;
    }

    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }
}
