package com.smallworldfs.moneytransferapp.modules.documents.model;

import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;

import java.util.ArrayList;

/**
 * Created by luismiguel on 22/9/17.
 */

public class StoragedDocument {

    private KeyValueData documentType;

    private ArrayList<Field> documentFields;

    private String ownerUrlFields;

    private ArrayList<KeyValueData> listKeyValueFilled;

    private String subType;

    public StoragedDocument(KeyValueData documentType, ArrayList<Field> documentFields, String ownerUrl, ArrayList<KeyValueData> listKeyValueFilled, String subType) {
        this.documentType = documentType;
        this.documentFields = documentFields;
        this.ownerUrlFields = ownerUrl;
        this.listKeyValueFilled = listKeyValueFilled;
        this.subType = subType;
    }

    public KeyValueData getDocumentType() {
        return documentType;
    }

    public void setDocumentType(KeyValueData documentType) {
        this.documentType = documentType;
    }

    public ArrayList<Field> getDocumentFields() {
        return documentFields;
    }

    public void setDocumentFields(ArrayList<Field> documentFields) {
        this.documentFields = documentFields;
    }

    public String getOwnerUrlFields() {
        return ownerUrlFields;
    }

    public void setOwnerUrlFields(String ownerUrlFields) {
        this.ownerUrlFields = ownerUrlFields;
    }

    public ArrayList<KeyValueData> getListKeyValueFilled() {
        return listKeyValueFilled;
    }

    public void setListKeyValueFilled(ArrayList<KeyValueData> listKeyValueFilled) {
        this.listKeyValueFilled = listKeyValueFilled;
    }

    public String getSubType() { return subType; }

    public void setSubType(String subType) {
        this.subType = subType;
    }
}
