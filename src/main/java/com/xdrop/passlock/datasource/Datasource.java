package com.xdrop.passlock.datasource;

import com.xdrop.passlock.model.Password;
import com.xdrop.passlock.search.FuzzySearcher;

public interface Datasource {

    Password getPass(String ref);

    Password getPass(String fuzzyRef, FuzzySearcher fuzzySearcher);

    void delPass(String ref);

    void updatePass(String ref, Password newPass);

    void addPass(String ref, String encryptedPass);

    void addPass(String ref, String encryptedPass, String desc);

}
