package com.xdrop.passlock.datasource.sqlite;

import com.xdrop.passlock.datasource.Datasource;
import com.xdrop.passlock.model.Password;
import com.xdrop.passlock.search.FuzzySearcher;

public class SQLiteDatasource implements Datasource {

    public Password getPass(String ref) {
        return null;
    }

    public Password getPass(String fuzzyRef, FuzzySearcher fuzzySearcher) {
        return null;
    }

    public void delPass(String ref) {

    }

    public void updatePass(String ref, Password newPass) {

    }

    public void addPass(String ref, String encryptedPass) {

    }

    public void addPass(String ref, String encryptedPass, String desc) {

    }
}
