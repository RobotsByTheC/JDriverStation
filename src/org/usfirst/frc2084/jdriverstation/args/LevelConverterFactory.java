/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.args;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.IStringConverterFactory;
import java.util.logging.Level;

/**
 *
 * @author Ben Wolsieffer
 */
public class LevelConverterFactory implements IStringConverterFactory {

    @Override
    public Class<? extends IStringConverter<?>> getConverter(Class forType) {
        if (forType.equals(Level.class)) {
            return LevelConverter.class;
        } else {
            return null;
        }
    }

}
