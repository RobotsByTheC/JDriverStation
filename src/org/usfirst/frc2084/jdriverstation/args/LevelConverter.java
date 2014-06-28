/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.args;

import com.beust.jcommander.IStringConverter;
import java.util.logging.Level;

/**
 *
 * @author Ben Wolsieffer
 */
public class LevelConverter implements IStringConverter<Level> {

    @Override
    public Level convert(String value) {
        return Level.parse(value.toUpperCase());
    }

}
