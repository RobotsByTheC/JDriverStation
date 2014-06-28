/* 
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation;

import org.usfirst.frc2084.jdriverstation.args.Arguments;
import javax.swing.SwingUtilities;
import org.usfirst.frc2084.jdriverstation.library.LibraryLoader;
import org.usfirst.frc2084.jdriverstation.gui.MainWindow;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.usfirst.frc2084.jdriverstation.args.ArgParser;
import org.usfirst.frc2084.jdriverstation.communication.CommunicationManager;
import org.usfirst.frc2084.jdriverstation.input.JoystickManager;
import org.usfirst.frc2084.jdriverstation.logging.ApplicationLogging;
import org.usfirst.frc2084.jdriverstation.preferences.PreferencesManager;
import org.usfirst.frc2084.jdriverstation.utils.Utils;

/**
 *
 * @author Ben Wolsieffer
 */
public class JDriverStation {

    private static String[] args;
    private final Arguments arguments;

    private MainWindow mainWindow;
    private final CommunicationManager communicationManager;
    private final PreferencesManager preferencesManager;
    private final JoystickManager joystickManager;

    @SuppressWarnings("LeakingThisInConstructor")
    private JDriverStation() {
        instance = this;

        ApplicationLogging.initializeDefaults();

        // Load JCommander before the rest of the libraries in order to parse
        // the arguments and set the log level. If JCommander was loaded
        // afterward, there would be no way to debug errors in loading.
        LibraryLoader.loadJCommander();

        // Parse the command line arguments.
        arguments = ArgParser.parse(args);

        ApplicationLogging.initializeArguments(arguments);

        // Load the rest of the libraries.
        LibraryLoader.loadLibraries();

        preferencesManager = new PreferencesManager();
        communicationManager = new CommunicationManager();
        joystickManager = new JoystickManager();

        SwingUtilities.invokeLater(() -> mainWindow = new MainWindow());
    }

    public Arguments getArguments() {
        return arguments;
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public CommunicationManager getCommunicationManager() {
        return communicationManager;
    }

    public PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }

    public JoystickManager getJoystickManager() {
        return joystickManager;
    }

    private static JDriverStation instance;

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static JDriverStation getInstance() {
        if (instance == null) {
            new JDriverStation();
        }
        return instance;
    }

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void main(String[] args) {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        }
        JDriverStation.args = args;
        getInstance();
    }
}
