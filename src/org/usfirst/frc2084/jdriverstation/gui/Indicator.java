/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.gui;

import javax.swing.JLabel;
import org.usfirst.frc2084.jdriverstation.resources.ResourceManager;

/**
 *
 * @author Ben Wolsieffer
 */
public class Indicator extends JLabel{

    public static enum Color {
        RED,
        GREEN,
        GRAY
    }
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Indicator(String title, Color color) {
        super(title);
        setColor(color);
    }
    
    public Indicator(String title) {
        this(title, Color.GRAY);
    }
    
    public void setColor(Color color) {
        switch (color) {
            case RED:
                setIcon(ResourceManager.getRedIndicator());
                break;
            case GREEN:
                setIcon(ResourceManager.getGreenIndicator());
                break;
            case GRAY:
                setIcon(ResourceManager.getGrayIndicator());
        }
    }
}
