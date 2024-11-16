package com.smallworldfs.moneytransferapp.utils;

import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CountryUtils {

    public static ArrayList<Pair<String, String>> sortCountryList(ArrayList<Pair<String, String>> countryList) {
        Collections.sort(countryList, new Comparator<Pair<String, String>>() {
            @Override
            public int compare(Pair<String, String> o1, Pair<String, String> o2) {
                return o1.second.compareTo(o2.second);
            }
        });
        return countryList;

    }
}
