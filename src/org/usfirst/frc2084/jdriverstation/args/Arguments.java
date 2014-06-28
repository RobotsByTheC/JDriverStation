/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.args;

import com.beust.jcommander.Parameter;
import java.util.logging.Level;

/**
 *
 * @author Ben Wolsieffer
 */
@SuppressWarnings("FieldMayBeFinal")
public class Arguments {

    @Parameter(names = "--debug", arity = 1, description = "The minimum level "
            + "of messages to print. This should be a value that can be parsed "
            + "by Level.parse(), (ie. INFO or 800).")
    private Level logLevel = Level.WARNING;

    @Parameter(names = "--help", help = true, description = "Show this help.")
    private boolean help;

    public Level getLogLevel() {
        return logLevel;
    }

    public boolean isHelp() {
        return help;
    }

}
