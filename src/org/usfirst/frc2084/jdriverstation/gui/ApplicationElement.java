/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.gui;

import org.usfirst.frc2084.jdriverstation.JDriverStation;
import org.usfirst.frc2084.jdriverstation.communication.CommunicationManager;
import org.usfirst.frc2084.jdriverstation.input.JoystickManager;
import org.usfirst.frc2084.jdriverstation.preferences.PreferencesManager;

/**
 *
 * @author Ben Wolsieffer
 */
public class ApplicationElement {

    public static JDriverStation getDriverStation() {
        return JDriverStation.getInstance();
    }

    public static MainWindow getMainWindow() {
        return getDriverStation().getMainWindow();
    }

    public static CommunicationManager getCommunicationManager() {
        return getDriverStation().getCommunicationManager();
    }

    public static PreferencesManager getPreferencesManager() {
        return getDriverStation().getPreferencesManager();
    }

    public static JoystickManager getJoystickManager() {
        return getDriverStation().getJoystickManager();
    }
}
