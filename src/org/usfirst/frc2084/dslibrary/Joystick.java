/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.dslibrary;

/**
 *
 * @author Ben Wolsieffer
 */
public class Joystick {

    public static final int NUM_AXES = 6;
    public static final int NUM_BUTTONS = 12;

    private final Robot.Data data;

    private final byte num;
    private final float[] axes = new float[NUM_AXES];
    private final boolean[] buttons = new boolean[NUM_BUTTONS];

    Joystick(int num, Robot.Data data) {
        this.num = (byte) num;
        this.data = data;
    }

    public float getAxis(int axis) {
        return axes[axis];
    }

    public void setAxis(int axis, float value) {
        try {
            if (value < -1 || value > 1) {
                throw new IllegalArgumentException("Axis value must be between -1 and 1.");
            } else {
                axes[axis - 1] = value;
                data.setJoystickAxis(num, (byte) axis, (byte) (value * (value < 0 ? 128 : 127)));
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("Axis index must be between 1 and " + axes.length + ".", ex);
        }
    }

    public boolean getButton(int button) {
        return buttons[button];
    }

    public void setButton(int button, boolean pressed) {
        try {
            buttons[button - 1] = pressed;
            data.setJoystickButton(num, (byte) button, pressed);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("Button index must be between 1 and " + buttons.length + ".", ex);
        }
    }

    public int getNum() {
        return num;
    }
}
