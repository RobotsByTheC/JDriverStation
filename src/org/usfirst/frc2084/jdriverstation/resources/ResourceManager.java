/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.resources;

import java.io.FileNotFoundException;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Ben Wolsieffer
 */
public class ResourceManager {

    private static ImageIcon greenIndicator;
    private static ImageIcon redIndicator;
    private static ImageIcon grayIndicator;

    private static ImageIcon floatingIcon;
    private static ImageIcon dockedIcon;

    static {
        try {
            greenIndicator = createIcon("green-indicator.png");
            redIndicator = createIcon("red-indicator.png");
            grayIndicator = createIcon("gray-indicator.png");
            floatingIcon = createIcon("floating-icon.png");
            dockedIcon = createIcon("docked-icon.png");
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }

    public static ImageIcon getGreenIndicator() {
        return greenIndicator;
    }

    public static ImageIcon getRedIndicator() {
        return redIndicator;
    }

    public static ImageIcon getGrayIndicator() {
        return grayIndicator;
    }

    public static ImageIcon getFloatingIcon() {
        return floatingIcon;
    }

    public static ImageIcon getDockedIcon() {
        return dockedIcon;
    }

    private static ImageIcon createIcon(String name) throws FileNotFoundException {
        URL imgURL = ResourceManager.class.getResource(name);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            throw new FileNotFoundException("Could not load icon: " + name);
        }
    }
}
