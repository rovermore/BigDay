package com.smallworldfs.moneytransferapp.modules.register.domain.model;

import com.google.gson.annotations.SerializedName;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;

import java.util.ArrayList;

/**
 * Created by luismiguel on 12/9/17.
 */

public class Form {

    private ArrayList<Group> groups;

    @SerializedName("inputs")
    private ArrayList<Field> fields;


    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }
}
