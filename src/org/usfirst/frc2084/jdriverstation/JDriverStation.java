/* 
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Ben Wolsieffer
 */
public class JDriverStation {

    private final MainWindow mainWindow;

    private JDriverStation() {
        mainWindow = new MainWindow(this);
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    private static JDriverStation instance;

    public static JDriverStation getInstance() {
        if (instance == null) {
            instance = new JDriverStation();
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
        getInstance();
    }
}
