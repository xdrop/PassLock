package com.xdrop.passlock

import com.xdrop.passlock.commands.ResetCommand

class PassLockTestIT extends GroovyTestCase {

    void setUp() {

        new ResetCommand().execute();

    }
}
