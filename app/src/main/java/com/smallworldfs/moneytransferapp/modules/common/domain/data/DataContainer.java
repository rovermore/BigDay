package com.smallworldfs.moneytransferapp.modules.common.domain.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by luismiguel on 8/9/17.
 */

public class DataContainer {

    private static DataContainer instance = null;

    public HashMap<String, ArrayList<? extends Object>> tempData;

    public static DataContainer getInstance() {
        /**
         * Synchronizing this method is not needed if you only use it in
         * Activity life-cycle methods, like onCreate(). But if you use
         * the singleton outside the UI-thread you must synchronize this
         * method (preferably using the Double-checked locking pattern).
         */
        return instance != null ? instance : (instance = new DataContainer());
    }

    public void clearDataByKey(String key) {
        if (this.tempData != null && this.tempData.containsKey(key)) {
            this.tempData.remove(key);
        }
    }

    public void putData(String key, ArrayList<? extends Object> data) {
        if (this.tempData == null) {
            this.tempData = new HashMap<>();
        }
        if (!this.tempData.containsKey(key)) {
            this.tempData.put(key, data);
        }
    }

    public ArrayList<? extends Object> getDataByKey(String key) {
        if (this.tempData != null && this.tempData.containsKey(key)) {
            return this.tempData.get(key);
        } else return null;
    }


}
