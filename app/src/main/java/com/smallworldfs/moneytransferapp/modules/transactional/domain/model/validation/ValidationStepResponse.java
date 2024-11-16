package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation;

import com.google.gson.annotations.SerializedName;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;

/**
 * Created by luismiguel on 28/7/17.
 */

public class ValidationStepResponse {

    @SerializedName("step_id")
    private String stepId;

    @SerializedName("nextstep")
    private Step nextStep;

    private Transaction transaction;

    private ValidationsMessages validationErrors;

    public ValidationStepResponse(ValidationsMessages validationErrors){
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
