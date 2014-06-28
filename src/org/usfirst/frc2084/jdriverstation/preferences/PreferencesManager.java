/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.preferences;

import java.awt.Dimension;
import java.awt.Point;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author Ben Wolsieffer
 */
public class PreferencesManager {

    private static final Logger logger = Logger.getLogger(PreferencesManager.class.getName());

    private final Preferences preferences;
    private final Preferences joystickPreferences;

    private static final String TEAM_NUMBER_KEY = "team_number";
    private static final String WINDOW_DOCKED_KEY = "window_docked";
    private static final String WINDOW_X_KEY = "window_x";
    private static final String WINDOW_Y_KEY = "window_y";
    private static final String WINDOW_WIDTH_KEY = "window_width";
    private static final String WINDOW_HEIGHT_KEY = "window_height";
    private static final String JOYSTICK_NODE_KEY = "joysticks";

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public PreferencesManager() {
        preferences = Preferences.userRoot().node("JDriverStation");
        joystickPreferences = preferences.node(JOYSTICK_NODE_KEY);
    }

    public int getTeamNumber() {
        return preferences.getInt(TEAM_NUMBER_KEY, 0);
    }

    public void setTeamNumber(int teamNumber) {
        preferences.putInt(TEAM_NUMBER_KEY, teamNumber);
    }

    public boolean isWindowDocked() {
        return preferences.getBoolean(WINDOW_DOCKED_KEY, false);
    }

    public void setWindowDocked(boolean docked) {
        preferences.putBoolean(WINDOW_DOCKED_KEY, docked);
    }

    public Point getWindowLocation() {
        Point p = new Point(
                preferences.getInt(WINDOW_X_KEY, -1),
                preferences.getInt(WINDOW_Y_KEY, -1)
        );
        if (p.x == -1 && p.y == -1) {
            return null;
        } else {
            return p;
        }
    }

    public void setWindowLocation(Point location) {
        preferences.putInt(WINDOW_X_KEY, location.x);
        preferences.putInt(WINDOW_Y_KEY, location.y);
    }

    public Dimension getWindowSize() {
        Dimension d = new Dimension(
                preferences.getInt(WINDOW_WIDTH_KEY, -1),
                preferences.getInt(WINDOW_HEIGHT_KEY, -1)
        );
        if (d.width == -1 && d.height == -1) {
            return null;
        } else {
            return d;
        }
    }

    public void setWindowSize(Dimension size) {
        preferences.putInt(WINDOW_WIDTH_KEY, size.width);
        preferences.putInt(WINDOW_HEIGHT_KEY, size.height);
    }

    public void setJoystickPositions(List<Integer> order) {
        try {
            joystickPreferences.clear();
            for (int i = 0; i < order.size(); i++) {
                int id = order.get(i);
                joystickPreferences.putInt(Integer.toString(id), i);
            }
        } catch (BackingStoreException ex) {
            logger.log(Level.WARNING, "Unable to save joystick order:", ex);
        }
    }
    
    public int getJoystickPosition(int id) {
        return joystickPreferences.getInt(Integer.toString(id), -1);
    }
}
