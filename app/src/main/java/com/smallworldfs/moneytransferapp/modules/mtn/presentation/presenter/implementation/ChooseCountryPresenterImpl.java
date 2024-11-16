package com.smallworldfs.moneytransferapp.modules.mtn.presentation.presenter.implementation;

import android.app.Activity;
import android.content.Intent;
import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.country.domain.interactors.CountryInteractor;
import com.smallworldfs.moneytransferapp.modules.country.domain.interactors.implementation.CountryInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.Country;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.CountryResponse;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.mtn.presentation.presenter.ChooseCountryPresenter;
import com.smallworldfs.moneytransferapp.utils.Utils;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import rx.Scheduler;

/**
 * Created by pedro del castillo on 7/9/17
 */
public class ChooseCountryPresenterImpl extends GenericPresenterImpl implements ChooseCountryPresenter, CountryInteractor.Callback {

    public static final String COUNTRY_KEY = "COUNTRY_KEY";
    public static final String COUNTRY_VALUE = "COUNTRY_VALUE";

    private Activity mActivity;
    private View mView;
    private ArrayList<Pair<String, String>> mListCountries;
    private ArrayList<Pair<String, String>> mListCountriesPermanentData;

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        UserDataRepository provideUserDataRepository();
    }

    public UserDataRepository userDataRepository;

    @Inject
    public ChooseCountryPresenterImpl(Scheduler observeOn, Activity activity, View view) {
        super(observeOn, SmallWorldApplication.getApp());
        this.mActivity = activity;
        this.mView = view;

        this.mListCountries = new ArrayList<>();
        this.mListCountriesPermanentData = new ArrayList<>();

        ChooseCountryPresenterImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        ChooseCountryPresenterImpl.DaggerHiltEntryPoint.class);
        userDataRepository = hiltEntryPoint.provideUserDataRepository();
    }

    @Override
    public void create() {
        super.create();
        addCallbacks();
        mView.configureView();
        getPayoutCountries();
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

    private void getPayoutCountries() {

        User user = userDataRepository.retrieveUser();
        if (user != null) {
            Pair<String, String> fromCountry = new Pair<>(user.getCountry().firstEntry().getKey(), user.getCountry().firstEntry().getValue());
            CountryInteractorImpl.getInstance().getPayoutCountries(fromCountry);
        }
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

    }

    @Override
    public void onMostPopularCountriesReceived(CountryResponse countries) {

    }

    @Override
    public void onMostPopularErrorCountries() {

    }

    @Override
    public void onPayoutCountriesReceived(Country countries) {
        if (countries != null && countries.getCountries() != null && countries.getCountries().size() > 0) {
            // Convert Map to ArrayList Pair <String, String>
            for (Map.Entry<String, String> country : countries.getSortedCountries().entrySet()) {
                mListCountries.add(new Pair<>(country.getKey(), country.getValue()));
            }
            mListCountriesPermanentData.addAll(mListCountries);
            mView.fillCountriesInDialog(mListCountries);
            mView.hideLoadingView();
        }
    }

    @Override
    public void onPayoutCountriesError() {

    }
    @Override
    public void onPayoutCountriesReceivedNotSorted(CountryResponse countries) {}

    public void onCountryCodeSelected(Pair<String, String> country) {
        backToPreviousScreenWithResultOK(country);
    }

    public void backToPreviousScreenWithResultOK(Pair<String, String> country) {
        Intent returnIntent = new Intent();

        returnIntent.putExtra(COUNTRY_KEY, country.first);
        returnIntent.putExtra(COUNTRY_VALUE, country.second);

        mActivity.setResult(Activity.RESULT_OK, returnIntent);
        mActivity.finish();
    }
}
