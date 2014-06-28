/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import javax.swing.JTextArea;

/**
 *
 * @author Ben Wolsieffer
 */
public class TextAreaHandler extends Handler {

    private final JTextArea logArea;

    public TextAreaHandler(JTextArea logArea) {
        this.logArea = logArea;
        setFormatter(new ShortFormatter());
    }

    @Override
    public void publish(LogRecord record) {
        String message = getFormatter().format(record);
        logArea.append(message + "\n");
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

}
