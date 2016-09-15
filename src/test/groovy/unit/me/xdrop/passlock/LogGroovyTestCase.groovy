package me.xdrop.passlock

import org.apache.log4j.BasicConfigurator
import org.junit.Ignore

@Ignore
class LogGroovyTestCase extends GroovyTestCase {

    static {
        BasicConfigurator.configure();
    }
}
