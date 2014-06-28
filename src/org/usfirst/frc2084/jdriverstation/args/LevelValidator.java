/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.args;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import java.util.logging.Level;

/**
 *
 * @author Ben Wolsieffer
 */
public class LevelValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        try {
            Level.parse(name);
        } catch(IllegalArgumentException ex) {
            throw new ParameterException(ex);
        }
    }

}
