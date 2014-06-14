/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation;

import javax.swing.JPanel;

/**
 *
 * @author Ben Wolsieffer
 */
public class DriverStationTab extends JPanel {

    private final MainWindow mainWindow;

    public DriverStationTab(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

}
