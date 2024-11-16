package com.smallworldfs.moneytransferapp.modules.country.presentation.presenter.implementation;

import android.content.Context;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.country.domain.interactors.CountryInteractor;
import com.smallworldfs.moneytransferapp.modules.country.domain.interactors.implementation.CountryInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.country.presentation.presenter.SelectCountryDialogPresenter;
import com.smallworldfs.moneytransferapp.modules.country.presentation.ui.fragment.SelectCodeCountryDialogFragment;
import com.smallworldfs.moneytransferapp.utils.Utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import rx.Scheduler;

/**
 * Created by luis on 14/6/17
 */
public class SelectCountryCodeDialogPresenterImpl extends GenericPresenterImpl implements SelectCountryDialogPresenter, CountryInteractor.CountryCodesCallback {

    private SelectCountryDialogPresenter.View mView;
    private ArrayList<Pair<String, String>> mListCountries;
    private ArrayList<Pair<String, String>> mListCountriesPermanentData;
    private SelectCodeCountryDialogFragment.CountrySelectedInterface mCountrySelectedInterface;


    public SelectCountryCodeDialogPresenterImpl(Scheduler observeOn, Context context, View view, SelectCodeCountryDialogFragment.CountrySelectedInterface countrySelectedInterface) {
        super(observeOn, context);
        mView = view;
        mListCountries = new ArrayList<>();
        mListCountriesPermanentData = new ArrayList<>();
        mCountrySelectedInterface = countrySelectedInterface;
        CountryInteractorImpl.getInstance().addCodesCallback(this);
    }

    @Override
    public void create() { mView.configureView(); }
    @Override
    public void resume() { }
    @Override
    public void pause() { }
    @Override
    public void stop() { }
    @Override
    public void destroy() { CountryInteractorImpl.getInstance().removeCodesCallback(this); }
    @Override
    public void onError(String message) { }
    @Override
    public void refresh() { }

    public void getData() { CountryInteractorImpl.getInstance().getCountryPrefix(); }

    @Override
    public void onCountryCodesReady(TreeMap<String, String> countries) {
        if (countries != null && countries.size() > 0) {
            // Convert Map to ArrayList Pair <String, String>
            for(Map.Entry<String, String> country : countries.entrySet()) {
                mListCountries.add(new Pair<>(country.getKey(), country.getValue()));
            }
            mListCountriesPermanentData.addAll(mListCountries);
            mView.fillCountriesInDialog(mListCountries);
            mView.hideLoadingView();
            mView.hideSearchEmptyView();
        }
    }

    public void performSearch(String text) {
        filterBy(text);
    }

    public void onClearSearchClick() { mView.clearEditText(); }

    private void filterBy(String filterBy) {
        String normalizedText = Utils.removeAccents(filterBy, true);
        mListCountries.clear();
        if (filterBy.equals("")) {
            mListCountries.addAll(mListCountriesPermanentData);
        }
        else {
            for (Pair<String, String> s : mListCountriesPermanentData) {
                String normalizedCountry = Utils.removeAccents(s.second, true);
                if ((normalizedCountry.toLowerCase().contains(normalizedText.toLowerCase()))) {
                    mListCountries.add(s);
                }
            }
        }

        mView.onFilterApplied(mListCountries);

        if (mListCountries.size() == 0)
            mView.showSearchEmptyView();
        else
            mView.hideSearchEmptyView();
    }

    public void onCountrySelected(Pair<String, String> country) {
        mCountrySelectedInterface.onCountryCodeSelected(country);
        mView.closeWindow();
    }
}
