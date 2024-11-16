package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by luismiguel on 20/6/17.
 */

public class BeneficiariesServer {

    private String msg;

    private ArrayList<Beneficiary> beneficiaries;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<Beneficiary> getBeneficiaries() {
        Collections.sort(beneficiaries, new SortByIdentifier());
        return beneficiaries;
    }

    //Defining our own Comparator
    class SortByIdentifier implements Comparator<Beneficiary>{
        @Override
        public int compare(Beneficiary s1, Beneficiary s2){
            return Integer.parseInt(s2.getId()) - Integer.parseInt(s1.getId());
        }
    }

    public void setBeneficiaries(ArrayList<Beneficiary> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }
}
