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
public class DriverStation {

    private final LCD lcd;

    private final Data data = new Data();

    public DriverStation() {
        lcd = new LCD(data);
    }

    public int getTeamNumber() {
        return data.getTeamNumber();
    }

    public byte[] getMACAddress() {
        return data.getMACAddress();
    }

    public void getMACAddress(byte[] mac) {
        data.getMACAddress(mac);
    }

    public String getVersion() {
        return data.getVersion();
    }
    
    public byte[] getCRC() {
        return data.getCRC();
    }
    
    public void getCRC(byte[] crc) {
        data.getCRC(crc);
    }
    
    public LCD getLCD() {
        return lcd;
    }

    class Data {

        public static final int PACKET_LENGTH = 1152;
        public static final int FLAG_INDEX = 0;
        public static final byte FLAG_RESET_MASK = (byte) 0b10000000;
        public static final byte FLAG_NOT_ESTOP_MASK = 0b01000000;
        public static final byte FLAG_ENABLE_MASK = 0b00100000;
        public static final byte FLAG_AUTO_MASK = 0b00010000;
        public static final byte FLAG_FMS_ATTACHED_MASK = 0b00001000;
        public static final byte FLAG_RESYNC_MASK = 0b00000100;
        public static final byte FLAG_TEST_MASK = 0b00000010;
        public static final byte FLAG_CHECK_VERSIONS_MASK = 0b00000001;
        public static final int BATTERY_VOLTAGE_INDEX = 1;
        public static final int DIGITAL_OUPUT_INDEX = 3;
        public static final int TEAM_NUM_INDEX = 8;
        public static final int MAC_ADDRESS_INDEX = 10;
        public static final int VERSION_INDEX = 16;
        public static final int PACKET_NUMBER_INDEX = 30;
        public static final int CRC_INDEX = 1020;
        public static final int LCD_COMMAND_INDEX = 1024;
        public static final int LCD_DATA_INDEX = 1026;

        private final byte[] data = new byte[PACKET_LENGTH];

        private boolean getFlag(int index, byte mask) {
            return (data[index] & mask) != 0;
        }

        public boolean isReset() {
            return getFlag(FLAG_INDEX, FLAG_RESET_MASK);
        }

        public boolean isEStopped() {
            return !getFlag(FLAG_INDEX, FLAG_NOT_ESTOP_MASK);
        }

        public boolean isEnabled() {
            return getFlag(FLAG_INDEX, FLAG_ENABLE_MASK);
        }

        public boolean isAuto() {
            return getFlag(FLAG_INDEX, FLAG_AUTO_MASK);
        }

        public boolean isFMSAttached() {
            return getFlag(FLAG_INDEX, FLAG_FMS_ATTACHED_MASK);
        }

        public boolean isResync() {
            return getFlag(FLAG_INDEX, FLAG_RESYNC_MASK);
        }

        public boolean isTest() {
            return getFlag(FLAG_INDEX, FLAG_TEST_MASK);
        }

        public boolean isCheckVersions() {
            return getFlag(FLAG_INDEX, FLAG_CHECK_VERSIONS_MASK);
        }

        public float getBatteryVoltage() {
            byte volts = data[BATTERY_VOLTAGE_INDEX];
            byte millivolts = data[BATTERY_VOLTAGE_INDEX + 1];

            // Really inefficient, but I don't know another way to do this
            // because the voltage is encoded as a hexadecimal number that is
            // read like a decimal
            String voltageString = String.format("%x", volts) + "." + String.format("%x", millivolts);
            return Float.parseFloat(voltageString);
        }

        public boolean getDigitalOutput(byte port) {
            return getFlag(DIGITAL_OUPUT_INDEX, (byte) (0x1 << port));
        }

        public short getTeamNumber() {
            return (short) (data[TEAM_NUM_INDEX] << 8 | data[TEAM_NUM_INDEX + 1]);
        }

        public byte[] getMACAddress() {
            byte[] mac = new byte[4];
            getMACAddress(mac);
            return mac;
        }

        public void getMACAddress(byte[] mac) {
            System.arraycopy(data, MAC_ADDRESS_INDEX, mac, 0, 4);
        }

        public String getVersion() {
            return new String(data, VERSION_INDEX, 8);
        }

        public short getPacketNumber() {
            return (short) (data[PACKET_NUMBER_INDEX] << 8 | data[PACKET_NUMBER_INDEX + 1]);
        }

        public byte[] getCRC() {
            byte[] crc = new byte[4];
            getCRC(crc);
            return crc;
        }

        public void getCRC(byte[] crc) {
            System.arraycopy(data, CRC_INDEX, crc, 0, 4);
        }

        public short getLCDCommand() {
            return (short) (data[LCD_COMMAND_INDEX] << 8 | data[LCD_COMMAND_INDEX + 1]);
        }

        public String getLCDLine(byte line) {
            return new String(data, LCD_DATA_INDEX + (line - 1) * LCD.LINE_LENGTH, LCD.LINE_LENGTH);
        }

        private void set(byte[] data) {
            if (data.length >= this.data.length) {
                System.arraycopy(data, 0, this.data, 0, this.data.length);
            } else {
                throw new IllegalArgumentException("Data array needs to have " + PACKET_LENGTH + " elements.");
            }
        }
    }

    void setData(byte[] data) {
        this.data.set(data);
    }
}
