/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.logging;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author Ben Wolsieffer
 */
public class ErrorHandler extends Handler {

    public ErrorHandler() {
        setLevel(Level.SEVERE);
    }

    @Override
    public void publish(LogRecord record) {
        if (record.getLevel().equals(Level.SEVERE)) {
            System.exit(1);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

}
