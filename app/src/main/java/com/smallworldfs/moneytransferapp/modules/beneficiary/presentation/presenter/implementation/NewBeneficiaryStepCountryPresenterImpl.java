package com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.presenter.implementation;

import android.app.Activity;
import android.content.Context;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository;
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.fragments.common.MyBeneficiariesNavigator;
import com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.presenter.NewBeneficiaryStepCountryPresenter;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.country.domain.interactors.CountryInteractor;
import com.smallworldfs.moneytransferapp.modules.country.domain.interactors.implementation.CountryInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.Country;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.CountryModel;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.CountryResponse;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.utils.Log;
import com.smallworldfs.moneytransferapp.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Singleton;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import rx.Scheduler;

@Singleton
public class NewBeneficiaryStepCountryPresenterImpl extends GenericPresenterImpl implements NewBeneficiaryStepCountryPresenter, CountryInteractor.Callback {

    private static final String TAG = NewBeneficiaryStepCountryPresenterImpl.class.getSimpleName();

    private Activity mActivity;
    private View mView;
    private ArrayList<Pair<String, String>> mListCountries;
    private ArrayList<Pair<String, String>> mListCountriesPermanentData;
    private boolean fromTransactional = false;

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        UserDataRepository provideUserDataRepository();
    }

    public MyBeneficiariesNavigator navigator;
    public UserDataRepository userDataRepository;

    public NewBeneficiaryStepCountryPresenterImpl(Scheduler observeOn, Context context, Activity activity, View view, boolean fromTransactional) {
        super(observeOn, context);
        this.mActivity = activity;
        navigator = new MyBeneficiariesNavigator(activity);
        this.mView = view;
        this.fromTransactional = fromTransactional;
        this.mListCountries = new ArrayList<>();
        this.mListCountriesPermanentData = new ArrayList<>();

        NewBeneficiaryStepCountryPresenterImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        NewBeneficiaryStepCountryPresenterImpl.DaggerHiltEntryPoint.class);
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
        try {
            User user = userDataRepository.retrieveUser();
            if (user != null) {
                Pair<String, String> fromCountry = new Pair<>(Objects.requireNonNull(user.getCountry().firstEntry()).getKey(), Objects.requireNonNull(user.getCountry().firstEntry()).getValue());
                CountryInteractorImpl.getInstance().getPayoutCountriesNotSorted(fromCountry);
            }
        } catch (Exception e) {
            Log.e(TAG, "getPayoutCountries: Blame ?", e);
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
    }

    @Override
    public void onPayoutCountriesReceivedNotSorted(CountryResponse countries) {
        if (countries != null && countries.getCountries() != null && countries.getCountries().size() > 0) {
            for (CountryModel country : countries.getCountries()) {
                mListCountries.add(new Pair<>(country.getCountryKey(), country.getCountryValue()));
            }
            mListCountriesPermanentData.addAll(mListCountries);
            mView.fillCountriesInDialog(mListCountries);
            Log.d("Hide Loading", "loading hidden");
            mView.hideLoadingView();
        }
    }

    @Override
    public void onPayoutCountriesError() {

    }

    public void onCountryCodeSelected(Pair<String, String> country) {
        navigator.navigateToC2BActivity(fromTransactional, country);
    }

    public void backToPreviousScreenWithResultOK() {
        mActivity.setResult(Activity.RESULT_OK);
        mActivity.finish();
    }
}
