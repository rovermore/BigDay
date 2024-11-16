package com.smallworldfs.moneytransferapp.modules.customization.presentation.presenter.implementation;

import android.content.Context;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.country.domain.interactors.CountryInteractor;
import com.smallworldfs.moneytransferapp.modules.country.domain.interactors.implementation.CountryInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.Country;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.CountryResponse;
import com.smallworldfs.moneytransferapp.modules.customization.presentation.presenter.ChooseCountryFromPresenter;
import com.smallworldfs.moneytransferapp.modules.customization.presentation.ui.fragment.ChooseCountryFromFragment;
import com.smallworldfs.moneytransferapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Map;

import rx.Scheduler;

/**
 * Created by ddi-pc-52 on 12.03.18
 */
public class ChooseCountryFromImpl extends GenericPresenterImpl implements ChooseCountryFromPresenter, CountryInteractor.Callback {
    private ChooseCountryFromPresenter.View mView;
    private ArrayList<Pair<String, String>> mListCountries;
    private ArrayList<Pair<String, String>> mListCountriesPermanentData;
    //private ChooseCountryFromFragment.CountrySelectedInterface mCountrySelectedInterface;

    public ChooseCountryFromImpl(Scheduler observeOn, Context context, ChooseCountryFromPresenter.View view) {//}, ChooseCountryFromFragment.CountrySelectedInterface listener) {
        super(observeOn, context);
        mContext = context;
        mView = view;
        //mCountrySelectedInterface = listener;
        mListCountries = new ArrayList<>();
        mListCountriesPermanentData = new ArrayList<>();
    }

    @Override
    public void create() {
        super.create();
        addCallbacks();
        mView.configureView();
        getOriginCountries();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void destroy() {
        removeCallbacks();
        super.destroy();
    }

    private void addCallbacks() {
        CountryInteractorImpl.getInstance().addCallback(this);
    }

    private void removeCallbacks() {
        CountryInteractorImpl.getInstance().removeCallback(this);
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }

    private void getOriginCountries() {
        CountryInteractorImpl.getInstance().getOriginCountries();
    }

    public void performSearch(String text) {
        filterBy(text);
    }

    public void onClearSearchClick() {
        mView.clearEditText();
    }

    private void filterBy(String filterBy) {
        String normalizedText = Utils.removeAccents(filterBy, true);
        mListCountries.clear();
        if (filterBy.equals("")) {
            mListCountries.addAll(mListCountriesPermanentData);
        } else {
            for (Pair<String, String> s : mListCountriesPermanentData) {
                String normalizedCountry = Utils.removeAccents(s.second, true);
                if ((normalizedCountry.toLowerCase().contains(normalizedText.toLowerCase()))) {
                    mListCountries.add(s);
                }
            }
        }

        mView.onFilterApplied(mListCountries);

        if (mListCountries.size() == 0) {
            mView.showSearchEmptyView();
        } else {
            mView.hideSearchEmptyView();
        }
    }

    @Override
    public void onOriginCountriesReceived(Country countries) {
        if (countries != null && countries.getCountries() != null && countries.getCountries().size() > 0) {
            for (Map.Entry<String, String> country : countries.getSortedCountries().entrySet()) {
                mListCountries.add(new Pair<>(country.getKey(), country.getValue()));
            }
            mListCountriesPermanentData.addAll(mListCountries);
            mView.fillCountriesInDialog(mListCountries);
            mView.hideLoadingView();
        }
    }

    @Override
    public void onMostPopularCountriesReceived(CountryResponse countries) { }
    @Override
    public void onMostPopularErrorCountries() { }
    @Override
    public void onPayoutCountriesReceived(Country countries) { }
    @Override
    public void onPayoutCountriesReceivedNotSorted(CountryResponse countries) {}
    @Override
    public void onPayoutCountriesError() { }

    public void onCountrySelected(Pair<String, String> country) {
        //if(mCountrySelectedInterface != null) mCountrySelectedInterface.onCountrySelected(countries);
        EventBus.getDefault().post(new ChooseCountryFromFragment.EventRefreshCountry(country));
        mView.close();//mFragment.getFragmentManager().popBackStack();
    }

}
