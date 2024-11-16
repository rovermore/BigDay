package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure;

import java.util.ArrayList;

/**
 * Created by luismiguel on 18/7/17.
 */

public class TransactionalStepResponse {

    private String msg;

    private ArrayList<Step> structure;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<Step> getStructure() {
        return structure;
    }

    public void setStructure(ArrayList<Step> structure) {
        this.structure = structure;
    }
}
