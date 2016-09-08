package com.xdrop.passlock;

import groovy.util.GroovyTestCase;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

public class LogGroovyTestCase extends GroovyTestCase {

    static {
        BasicConfigurator.configure();
    }

}
