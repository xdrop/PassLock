package com.xdrop.passlock.logic;

import com.xdrop.passlock.datasource.Datasource;
import com.xdrop.passlock.search.FuzzySearcher;

public class PasswordManager {

    private Datasource datasource;
    private FuzzySearcher fuzzySearcher;

    public String searchPassword(String searchString){
        datasource.getPass(searchString, fuzzySearcher);
        return "PasswordEntry: ";
    }

}
