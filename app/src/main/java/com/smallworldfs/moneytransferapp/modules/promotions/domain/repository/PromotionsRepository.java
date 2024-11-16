package com.smallworldfs.moneytransferapp.modules.promotions.domain.repository;

import android.text.TextUtils;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.CalculatorPromotion;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.Promotion;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.server.PromotionsResponse;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.server.ServerPromotionsRequest;
import com.smallworldfs.moneytransferapp.data.promotions.network.PromotionsNetworkDatasource;
import com.smallworldfs.moneytransferapp.utils.ConstantsKt;

import java.util.ArrayList;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by luis on 29/6/17
 */
public class PromotionsRepository {

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        PromotionsNetworkDatasource providesPromotionsNetworkDatasource();
    }

    public PromotionsNetworkDatasource promotionsNetworkDatasource;

    public static PromotionsRepository sInstance = null;

    /**
     * Current promotion received by server in calculate operation. This promotion is mandatory and user can not disable and select
     * other promotions in the available list
     */
    private CalculatorPromotion mCalculatorPromotion = null;

    /**
     * Promotion selected by user
     */
    private Promotion mSelectedPromotionByUser = null;

    /**
     * Current list promotions availables for current context Origin-PayOut Country, cleared if change destination
     */
    private final ArrayList<Promotion> mListPromotionsAvailables = new ArrayList<>();


    public static PromotionsRepository getInstance() {
        if (sInstance == null) {
            sInstance = new PromotionsRepository();
        }

        return sInstance;
    }

    public PromotionsRepository(){
        PromotionsRepository.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        PromotionsRepository.DaggerHiltEntryPoint.class);
        promotionsNetworkDatasource = hiltEntryPoint.providesPromotionsNetworkDatasource();
    }

    public Observable<Response<PromotionsResponse>> requestPromotions(final ServerPromotionsRequest request) {
        return promotionsNetworkDatasource.requestPromotions(request);
    }

    public void clearCalculatorPromotion() {
        this.mCalculatorPromotion = null;
    }

    public void setCalculatorPromotion(String promotionName, Double promotionDiscount, String promotionNumber, boolean autoAsigned, String promotionType, String promotionCode) {
        this.mCalculatorPromotion = new CalculatorPromotion(promotionName, promotionDiscount, promotionNumber, autoAsigned, promotionType, promotionCode);
    }

    public CalculatorPromotion getCalculatorPromotion() {
        return this.mCalculatorPromotion;
    }

    public void setListPromotionsAvailables(ArrayList<Promotion> listPromotions) {
        this.mListPromotionsAvailables.addAll(listPromotions);
    }

    public ArrayList<Promotion> getListPromotionsAvailables() {
        return this.mListPromotionsAvailables;
    }

    public void clearListPromotionsAvailables() {
        this.mListPromotionsAvailables.clear();
    }

    public void setPromotionSelectedByUser(Promotion promotion) {
        this.mSelectedPromotionByUser = promotion;
    }

    public Promotion getPromotionSelected() {
        return this.mSelectedPromotionByUser;
    }
    public CalculatorPromotion getmCalculatorPromotion() {
        return this.mCalculatorPromotion;
    }

    /**
     * Get promotion code (number). Calculator promotion or user promotions
     *
     * @return
     */
    public String getPromotionCodePresent() {
        if (mCalculatorPromotion != null) {
            return TextUtils.isEmpty(mCalculatorPromotion.getPromotionCode()) ? ConstantsKt.STRING_EMPTY : mCalculatorPromotion.getPromotionCode();
        } else if (mSelectedPromotionByUser != null && !TextUtils.isEmpty(mSelectedPromotionByUser.getPromotionCode())) {
            return TextUtils.isEmpty(mSelectedPromotionByUser.getPromotionCode()) ? ConstantsKt.STRING_EMPTY : mSelectedPromotionByUser.getPromotionCode();
        } else return ConstantsKt.STRING_EMPTY;
    }

    /**
     * Return Pair DiscountType - Discount. Doesn't matter source
     *
     * @return
     */
    public Pair<String, Double> getDiscountAndTypePromotion() {
        if (mCalculatorPromotion != null) {
            return new Pair<>(mCalculatorPromotion.getPromotionType(), mCalculatorPromotion.getPromotionDiscount());
        } else if (mSelectedPromotionByUser != null) {
            return new Pair<>(mSelectedPromotionByUser.getDiscountType(), Double.valueOf(mSelectedPromotionByUser.getDiscount()));
        } else return null;
    }

    public void destroy() {
        sInstance = null;
    }
}
