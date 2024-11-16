package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.FormData;

/**
 * Created by luismiguel on 18/7/17.
 */

public class Step implements Parcelable {

    private String name;

    @SerializedName("step_id")
    private String stepId;

    @SerializedName("step_type")
    private String stepType;

    @SerializedName("step_subtype")
    private String stepSubType;

    @SerializedName("is_final")
    private int isFinal;

    @SerializedName("inputs")
    private FormData formData;

    private String stepSelectedItem;

    private KeyValueData singleStepValue;

    // Control variables
    private boolean isNewStep = false;

    private StepStatus status = StepStatus.EMPTY;


    public Step(String name, String stepId, String stepType) {
        this.name = name;
        this.stepId = stepId;
        this.stepType = stepType;
    }

    protected Step(Parcel in) {
        name = in.readString();
        stepId = in.readString();
        stepType = in.readString();
        stepSubType = in.readString();
        isFinal = in.readInt();
        formData = in.readParcelable(FormData.class.getClassLoader());
        stepSelectedItem = in.readString();
        singleStepValue = in.readParcelable(KeyValueData.class.getClassLoader());
        isNewStep = in.readByte() != 0;
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateStep(Step step) {
        this.stepType = step.getStepType();
        this.isFinal = step.getIsFinal();
        this.name = step.getName();
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public int getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(int isFinal) {
        this.isFinal = isFinal;
    }

    public boolean isNewStep() {
        return isNewStep;
    }

    public void setNewStep(boolean newStep) {
        isNewStep = newStep;
    }

    public FormData getFormData() {
        return formData;
    }

    public void setFormData(FormData formData) {
        this.formData = formData;
    }

    public StepStatus getStatus() {
        return status;
    }

    public void setStatus(StepStatus status) {
        this.status = status;
    }

    public KeyValueData getSingleStepValue() {
        return singleStepValue;
    }

    public void setSingleStepValue(KeyValueData singleStepValue) {
        this.singleStepValue = singleStepValue;
    }

    public String getStepSelectedItem() {
        return stepSelectedItem;
    }

    public void setStepSelectedItem(String stepSelectedItem) {
        this.stepSelectedItem = stepSelectedItem;
    }

    public String getStepSubType() {
        return stepSubType;
    }

    public void setStepSubType(String stepSubType) {
        this.stepSubType = stepSubType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Step)) return false;

        Step step = (Step) o;

        if (getName() != null ? !getName().equals(step.getName()) : step.getName() != null)
            return false;
        if (!getStepId().equals(step.getStepId())) return false;
        return getStepType() != null ? getStepType().equals(step.getStepType()) : step.getStepType() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + getStepId().hashCode();
        result = 31 * result + (getStepType() != null ? getStepType().hashCode() : 0);
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(stepId);
        dest.writeString(stepType);
        dest.writeString(stepSubType);
        dest.writeInt(isFinal);
        dest.writeParcelable(formData, flags);
        dest.writeString(stepSelectedItem);
        dest.writeParcelable(singleStepValue, flags);
        dest.writeByte((byte) (isNewStep ? 1 : 0));
    }
}
