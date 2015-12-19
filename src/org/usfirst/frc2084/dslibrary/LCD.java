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
public class LCD {

    public static final int LINE_LENGTH = 21;
    public static final int NUM_LINES = 6;

    public enum Command {

        FULL_DISPLAY_TEXT(0x9FFF);

        private static final Command[] commands = values();

        private final int value;

        private Command(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Command fromValue(final int value) {
            for (Command c : commands) {
                if (value == c.getValue()) {
                    return c;
                }
            }
            return null;
        }
    }

    private final DriverStation.Data data;

    LCD(final DriverStation.Data data) {
        this.data = data;
    }

    public Command getCommand() {
        return Command.fromValue(data.getLCDCommand());
    }

    public String getLine(final int line) {
        if (line > 0 && line <= NUM_LINES) {
            return data.getLCDLine((byte) line);
        } else {
            throw new IllegalArgumentException("line must be between 1 and " + NUM_LINES);
        }
    }

    public String[] getLines() {
        final String[] l = new String[NUM_LINES];
        getLines(l);
        return l;
    }

    public void getLines(final String[] lines) {
        if (lines.length >= NUM_LINES) {
            for (byte i = 1; i <= NUM_LINES; i++) {
                lines[i - 1] = data.getLCDLine(i);
            }
        }
    }
}
