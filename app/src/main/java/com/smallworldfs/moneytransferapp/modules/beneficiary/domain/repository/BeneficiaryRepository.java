package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.repository;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.BeneficiariesServer;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Beneficiary;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.BeneficiaryRequest;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.CreateBeneficiaryRequest;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.EditBeneficiaryFormRequest;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.NewBeneficiaryFormRequest;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.UpdateBeneficiaryRequest;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.structure.EditBeneficiaryFormResponse;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.structure.NewBeneficiaryFormResponse;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.validation.BeneficiaryValidationResponse;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.service.BeneficiaryNetworkDatasource;

import java.util.ArrayList;
import java.util.Objects;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

public class BeneficiaryRepository {

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        BeneficiaryNetworkDatasource providesBeneficiaryNetworkDatasource();
    }

    public BeneficiaryNetworkDatasource beneficiaryNetworkDatasource;

    public static BeneficiaryRepository sInstance = null;
    private Beneficiary mLastCreatedBeneficiary = null;
    private ArrayList<Beneficiary> mCachedBeneficiaries = null;

    public static BeneficiaryRepository getInstance() {
        if (sInstance == null) {
            sInstance = new BeneficiaryRepository();
        }
        return sInstance;
    }

    public BeneficiaryRepository(){
        BeneficiaryRepository.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        BeneficiaryRepository.DaggerHiltEntryPoint.class);
        beneficiaryNetworkDatasource = hiltEntryPoint.providesBeneficiaryNetworkDatasource();
    }

    /**
     * Request Beneficiaries by user
     */
    public Observable<Response<BeneficiariesServer>> requestBeneficiaries(final BeneficiaryRequest request) {
        Observable<Response<BeneficiariesServer>> response = beneficiaryNetworkDatasource.requestBeneficiaries(request);

        return Observable.create(subscriber ->
                response.subscribe(new Subscriber<Response<BeneficiariesServer>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onNext(null);
                    }

                    @Override
                    public void onNext(Response<BeneficiariesServer> response) {
                        if (response != null) {
                            subscriber.onNext(response);
                            mCachedBeneficiaries = Objects.requireNonNull(response.body()).getBeneficiaries();
                        } else {
                            subscriber.onError(null);
                        }
                        subscriber.onCompleted();
                    }
                }
            )
        );
    }

    /**
     * Request new beneficiary form
     */
    public Observable<Response<NewBeneficiaryFormResponse>> requestNewBeneficiaryForm(final NewBeneficiaryFormRequest request) {
        return beneficiaryNetworkDatasource.requestNewBeneficiaryForm(request);
    }

    public Observable<Response<BeneficiaryValidationResponse>> requestCreateNewBeneficiary(final CreateBeneficiaryRequest request) {
        return beneficiaryNetworkDatasource.requestCreateNewBeneficiary(request);
    }

    public Observable<Response<BeneficiaryValidationResponse>> requestUpdateBeneficiary(final UpdateBeneficiaryRequest request) {
        return beneficiaryNetworkDatasource.requestUpdateBeneficiary(request);
    }

    /**
     * Request edit beneficiary form
     */
    public Observable<Response<EditBeneficiaryFormResponse>> requestEditBeneficiaryForm(final EditBeneficiaryFormRequest request) {
        return beneficiaryNetworkDatasource.requestEditBeneficiaryForm(request);
    }

    public Beneficiary getLastCreatedBeneficiary() {
        return mLastCreatedBeneficiary;
    }

    public void setLastCreatedBeneficiary(Beneficiary beneficiary) {
        this.mLastCreatedBeneficiary = beneficiary;
    }

    public ArrayList<Beneficiary> getCachedBeneficiaries() {
        return mCachedBeneficiaries;
    }

}
