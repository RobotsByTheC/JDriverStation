/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.communication;

/**
 *
 * @author Ben Wolsieffer
 */
public class Joystick {

    private final float[] axes = new float[4];
    private final boolean[] buttons = new boolean[12];

    public Joystick() {
    }

    public float[] getAxes() {
        return axes;
    }

    public void setAxis(int axis, float value) {
        try {
            if (value < -1 || value > 1) {
                throw new IllegalArgumentException("Axis value must be between -1 and 1.");
            } else {
                axes[axis] = value;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("Axis index must be between 0 and " + axes.length + ".", ex);
        }
    }

    public boolean[] getButtons() {
        return buttons;
    }

    public void setButton(int button, boolean pressed) {
        try {
            buttons[button] = pressed;
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("Button index must be between 0 and " + buttons.length + ".", ex);
        }
    }
}
