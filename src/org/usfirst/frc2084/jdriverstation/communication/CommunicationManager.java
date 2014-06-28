/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.communication;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.EventListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.usfirst.frc2084.dslibrary.CommunicationAdapter;
import org.usfirst.frc2084.dslibrary.CommunicationListener;
import org.usfirst.frc2084.dslibrary.ConnectionListener;
import org.usfirst.frc2084.dslibrary.DriverStation;
import org.usfirst.frc2084.dslibrary.Joystick;
import org.usfirst.frc2084.dslibrary.Robot;
import org.usfirst.frc2084.dslibrary.Robot.Mode;
import org.usfirst.frc2084.dslibrary.RobotCommunication;
import static org.usfirst.frc2084.jdriverstation.gui.ApplicationElement.*;

/**
 *
 * @author Ben Wolsieffer
 */
public class CommunicationManager {

    public static enum DriverStationMode {

        TELEOPERATED,
        AUTONOMOUS,
        PRACTICE,
        TEST;

        public static DriverStationMode fromRobotMode(Robot.Mode mode) {
            return valueOf(mode.name());
        }
    }

    private static final Logger LOGGER = Logger.getLogger(CommunicationManager.class.getName());

    public static final String TEAM_NUMBER_PROPERTY = "team_number";
    public static final String ENABLED_PROPERTY = "enabled";
    public static final String MODE_PROPERTY = "mode";
    public static final String ALLIANCE_PROPERTY = "alliance";
    public static final String POSITION_PROPERTY = "position";
    public static final String CONNECTED_PROPERTY = "connected";

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private JoystickUpdater joystickUpdater;
    private DriverStationMode mode;

    public interface JoystickUpdater extends EventListener {

        public void updateJoysticks(List<Joystick> joysticks);
    }

    private final RobotCommunication robotCommunication;
    private final Robot robot;
    private final DriverStation driverStation;

    public CommunicationManager() {
        int teamNumber = getPreferencesManager().getTeamNumber();
        robot = new Robot(teamNumber);
        driverStation = new DriverStation();
        mode = DriverStationMode.fromRobotMode(robot.getMode());

        {
            RobotCommunication rc;
            try {
                rc = new RobotCommunication(robot, getIPFromTeamNumber(teamNumber), driverStation);
                rc.start();
            } catch (SocketException ex) {
                rc = null;
                LOGGER.log(Level.SEVERE, "Unable to create robot connection: {0}", ex.getMessage());
            }
            robotCommunication = rc;
        }

        robotCommunication.addCommunicationListener(new CommunicationAdapter() {

            @Override
            public void preSend() {
                updateJoysticks();
            }

        });

        robotCommunication.addConnectionListener(new ConnectionListener() {

            @Override
            public void robotConnected() {
                firePropertyChange(CONNECTED_PROPERTY, false, true);
            }

            @Override
            public void robotDisconnected() {
                firePropertyChange(CONNECTED_PROPERTY, true, false);
            }
        });
    }

    private static InetAddress getIPFromTeamNumber(int teamNumber) {
        String teamNumberString = Integer.toString(teamNumber);
        while (teamNumberString.length() < 4) {
            teamNumberString = "0" + teamNumberString;
        }

        try {
            return InetAddress.getByName("10."
                    + teamNumberString.substring(0, 2)
                    + "."
                    + teamNumberString.substring(2, 4)
                    + ".2");
        } catch (UnknownHostException ex) {
            // Should never happen
            return null;
        }
    }

    public int getTeamNumber() {
        return robot.getTeamNumber();
    }

    public void setTeamNumber(int teamNumber) {
        if (teamNumber != robot.getTeamNumber()) {
            int oldTeamNumber = robot.getTeamNumber();
            robotCommunication.setRobotAddress(getIPFromTeamNumber(teamNumber));
            robot.setTeamNumber(teamNumber);
            getPreferencesManager().setTeamNumber(teamNumber);
            firePropertyChange(TEAM_NUMBER_PROPERTY, oldTeamNumber, teamNumber);
        }
    }

    public boolean isEnabled() {
        return robot.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        boolean wasEnabled = robot.isEnabled();
        robot.setEnabled(enabled);
        firePropertyChange(ENABLED_PROPERTY, wasEnabled, enabled);
    }

    public DriverStationMode getMode() {
        return mode;
    }

    public void setMode(DriverStationMode mode) {
        DriverStationMode oldMode = this.mode;
        if (mode == DriverStationMode.PRACTICE) {
            robot.setMode(Mode.TELEOPERATED);
        }
        this.mode = mode;
        firePropertyChange(MODE_PROPERTY, oldMode, mode);
    }

    public Robot.Alliance getAlliance() {
        return robot.getAlliance();
    }

    public void setAlliance(Robot.Alliance alliance) {
        Robot.Alliance oldAlliance = robot.getAlliance();
        robot.setAlliance(alliance);
        firePropertyChange(ALLIANCE_PROPERTY, oldAlliance, alliance);
    }

    public int getPosition() {
        return robot.getPosition();
    }

    public void setPosition(int position) {
        int oldPosition = robot.getPosition();
        robot.setPosition(position);
        firePropertyChange(POSITION_PROPERTY, oldPosition, position);
    }

    public boolean isConnected() {
        return robotCommunication.isConnected();
    }

    public void addCommunicationListener(CommunicationListener cl) {
        robotCommunication.addCommunicationListener(cl);
    }

    public void removeCommunicationListener(CommunicationListener cl) {
        robotCommunication.removeCommunicationListener(cl);
    }

    public void addPropertyChangeListener(PropertyChangeListener pl) {
        pcs.addPropertyChangeListener(pl);
    }
    
    public void addPropertyChangeListener(String property, PropertyChangeListener pl) {
        pcs.removePropertyChangeListener(property, pl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pl) {
        pcs.removePropertyChangeListener(pl);
    }
    
    public void removePropertyChangeListener(String property, PropertyChangeListener pl) {
        pcs.removePropertyChangeListener(property, pl);
    }

    private void firePropertyChange(String property, Object oldValue, Object newValue) {
        pcs.firePropertyChange(property, oldValue, newValue);
    }

    public void setJoystickUpdater(JoystickUpdater jul) {
        joystickUpdater = jul;
    }

    private void updateJoysticks() {
        if (joystickUpdater != null) {
            joystickUpdater.updateJoysticks(robot.getJoysticks());
        }
    }
}
