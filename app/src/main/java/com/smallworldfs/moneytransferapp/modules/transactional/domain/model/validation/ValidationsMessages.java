package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by luismiguel on 28/7/17.
 */

public class ValidationsMessages {

    private HashMap<String, ArrayList<String>> msg;

    public HashMap<String, ArrayList<String>> getValidationStepErrros() {
        return msg;
    }

    public void setValidationStepErrros(HashMap<String, ArrayList<String>> validationStepErrros) {
        this.msg = validationStepErrros;
    }
}
