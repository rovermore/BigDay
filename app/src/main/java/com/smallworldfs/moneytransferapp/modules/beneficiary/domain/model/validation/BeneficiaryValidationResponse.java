package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.validation;

import com.google.gson.annotations.SerializedName;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Beneficiary;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Transaction;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.ValidationsMessages;

/**
 * Created by pedro del castillo on 10/10/17.
 */

public class BeneficiaryValidationResponse {

    @SerializedName("step_id")
    private String stepId;

    @SerializedName("nextstep")
    private Step nextStep;

    private Transaction transaction;

    private ValidationsMessages validationErrors;

    @SerializedName("data")
    private Beneficiary beneficiary;

    public BeneficiaryValidationResponse(ValidationsMessages validationErrors){
        this.validationErrors = validationErrors;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public ValidationsMessages getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(ValidationsMessages validationErrors) {
        this.validationErrors = validationErrors;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Step getNextStep() {
        return nextStep;
    }

    public void setNextStep(Step nextStep) {
        this.nextStep = nextStep;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
