package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.GenericFormField;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by luismiguel on 27/7/17.
 */

public class Field extends GenericFormField implements Parcelable {

    private String title;

    private String name;

    private String type;

    private String subtype;

    private Attributes attributes;

    private boolean required;

    private boolean searchable;

    private String value;

    private String keyValue;

    private String placeholder;

    private String usertype;

    private ArrayList<Field> childs;

    private ArrayList<TreeMap<String, String>> data;

    private ArrayList<Field> comboList;

    private String errorMessage;

    @SerializedName("refAPI")
    private RefApi refApi;

    private String triggers;

    private boolean hidden;

    private Payout payout;

    private ArrayList<KeyValueData> dataReplicated;

    public Field(String type, String subtype, String value) {
        this.type = type;
        this.subtype = subtype;
        this.value = value;
    }

    public ArrayList<KeyValueData> getDataReplicated() {
        return dataReplicated;
    }

    public void setDataReplicated(ArrayList<KeyValueData> dataReplicated) {
        this.dataReplicated = dataReplicated;
    }

    protected Field(Parcel in) {
        super(in);
        title = in.readString();
        name = in.readString();
        type = in.readString();
        subtype = in.readString();
        attributes = in.readParcelable(Attributes.class.getClassLoader());
        required = in.readByte() != 0;
        searchable = in.readByte() != 0;
        value = in.readString();
        keyValue = in.readString();
        placeholder = in.readString();
        usertype = in.readString();
        childs = in.createTypedArrayList(Field.CREATOR);
        comboList = in.createTypedArrayList(Field.CREATOR);
        errorMessage = in.readString();
        refApi = in.readParcelable(RefApi.class.getClassLoader());
        triggers = in.readString();
        hidden = in.readByte() != 0;
        payout = in.readParcelable(Payout.class.getClassLoader());
        dataReplicated = in.createTypedArrayList(KeyValueData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(title);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(subtype);
        dest.writeParcelable(attributes, flags);
        dest.writeByte((byte) (required ? 1 : 0));
        dest.writeByte((byte) (searchable ? 1 : 0));
        dest.writeString(value);
        dest.writeString(keyValue);
        dest.writeString(placeholder);
        dest.writeString(usertype);
        dest.writeTypedList(childs);
        dest.writeTypedList(comboList);
        dest.writeString(errorMessage);
        dest.writeParcelable(refApi, flags);
        dest.writeString(triggers);
        dest.writeByte((byte) (hidden ? 1 : 0));
        dest.writeParcelable(payout, flags);
        dest.writeTypedList(dataReplicated);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Field> CREATOR = new Creator<Field>() {
        @Override
        public Field createFromParcel(Parcel in) {
            return new Field(in);
        }

        @Override
        public Field[] newArray(int size) {
            return new Field[size];
        }
    };

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public ArrayList<Field> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<Field> childs) {
        this.childs = childs;
    }

    public ArrayList<TreeMap<String, String>> getData() {
        return data;
    }

    public void setData(ArrayList<TreeMap<String, String>> data) {
        this.data = data;
    }

    public ArrayList<Field> getComboList() {
        return comboList;
    }

    public void setComboList(ArrayList<Field> comboList) {
        this.comboList = comboList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public RefApi getRefApi() {
        return refApi;
    }

    public void setRefApi(RefApi refApi) {
        this.refApi = refApi;
    }

    public String getTriggers() {
        return triggers;
    }

    public void setTriggers(String triggers) {
        this.triggers = triggers;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public Payout getPayout() {
        return payout;
    }

    public void setPayout(Payout payout) {
        this.payout = payout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Field)) return false;

        Field field = (Field) o;

        if (required != field.required) return false;
        if (searchable != field.searchable) return false;
        if (title != null ? !title.equals(field.title) : field.title != null) return false;
        if (name != null ? !name.equals(field.name) : field.name != null) return false;
        if (type != null ? !type.equals(field.type) : field.type != null) return false;
        if (subtype != null ? !subtype.equals(field.subtype) : field.subtype != null) return false;
        if (attributes != null ? !attributes.equals(field.attributes) : field.attributes != null)
            return false;
        if (value != null ? !value.equals(field.value) : field.value != null) return false;
        if (keyValue != null ? !keyValue.equals(field.keyValue) : field.keyValue != null)
            return false;
        if (placeholder != null ? !placeholder.equals(field.placeholder) : field.placeholder != null)
            return false;
        if (usertype != null ? !usertype.equals(field.usertype) : field.usertype != null)
            return false;
        if (childs != null ? !childs.equals(field.childs) : field.childs != null) return false;
        if (data != null ? !data.equals(field.data) : field.data != null) return false;
        if (comboList != null ? !comboList.equals(field.comboList) : field.comboList != null)
            return false;
        if (errorMessage != null ? !errorMessage.equals(field.errorMessage) : field.errorMessage != null)
            return false;
        if (refApi != null ? !refApi.equals(field.refApi) : field.refApi != null) return false;
        if (triggers != null ? !triggers.equals(field.triggers) : field.triggers != null)
            return false;
        if (payout != null ? !payout.equals(field.payout) : field.payout != null) return false;
        return dataReplicated != null ? dataReplicated.equals(field.dataReplicated) : field.dataReplicated == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (subtype != null ? subtype.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + (required ? 1 : 0);
        result = 31 * result + (searchable ? 1 : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (keyValue != null ? keyValue.hashCode() : 0);
        result = 31 * result + (placeholder != null ? placeholder.hashCode() : 0);
        result = 31 * result + (usertype != null ? usertype.hashCode() : 0);
        result = 31 * result + (childs != null ? childs.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (comboList != null ? comboList.hashCode() : 0);
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        result = 31 * result + (refApi != null ? refApi.hashCode() : 0);
        result = 31 * result + (triggers != null ? triggers.hashCode() : 0);
        result = 31 * result + (payout != null ? payout.hashCode() : 0);
        result = 31 * result + (dataReplicated != null ? dataReplicated.hashCode() : 0);
        return result;
    }
}
