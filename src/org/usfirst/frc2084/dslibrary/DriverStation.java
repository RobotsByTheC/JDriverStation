/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.dslibrary;

import static org.usfirst.frc2084.dslibrary.Robot.Data.PACKET_NUMBER_INDEX;

/**
 *
 * @author Ben Wolsieffer
 */
public class DriverStation {

    private final Data data = new Data();

    public class Data {

        public static final int PACKET_LENGTH = 1152;
        public static final int PACKET_INDEX_INDEX = 13;

        private final byte[] data = new byte[PACKET_LENGTH];

        public int getPacketNumber() {
            return (data[PACKET_NUMBER_INDEX] << 8) | data[PACKET_NUMBER_INDEX + 1];
        }

        private void setData(byte[] data) {
            if (data.length == this.data.length) {
                System.arraycopy(data, 0, this.data, 0, data.length);
            } else {
                throw new IllegalArgumentException("Data array needs to have " + PACKET_LENGTH + " elements.");
            }
        }
    }

    public void setData(byte[] data) {
        this.data.setData(data);
    }
}
