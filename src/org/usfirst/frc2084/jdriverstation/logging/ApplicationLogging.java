/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.usfirst.frc2084.jdriverstation.args.Arguments;

/**
 *
 * @author Ben Wolsieffer
 */
public class ApplicationLogging {

    private static ConsoleHandler consoleHandler;
    @SuppressWarnings("NonConstantLogger")
    private static Logger rootLogger;

    public static void initializeDefaults() {
        // Remove the default ConsoleHandler.
        LogManager.getLogManager().reset();

        // Get root logger
        rootLogger = Logger.getLogger("");
        // By default, set it to display warnings or more serious messages. This
        // will be overridden by the command line arguments.
        rootLogger.setLevel(Level.WARNING);
        // Add the ConsoleLogger, which logs to System.out. A ConsoleHandler
        // exists by default, but it needs to be replaced so its log level can
        // be set.
        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new ShortFormatter());
        rootLogger.addHandler(consoleHandler);
        // Add the ErrorHandler, which automatically exits the application when
        // a SEVERE level error is logged. There is some code (in
        // LibraryLoader), that relies on this, which isn't good.
        rootLogger.addHandler(new ErrorHandler());
    }

    public static void initializeArguments(Arguments args) {
        // Set the log level of the root logger and its associated
        // ConsoleHandler to the one passed on the command line.
        Level logLevel = args.getLogLevel();
        rootLogger.setLevel(logLevel);
        consoleHandler.setLevel(logLevel);
    }

    private ApplicationLogging() {
    }
}
