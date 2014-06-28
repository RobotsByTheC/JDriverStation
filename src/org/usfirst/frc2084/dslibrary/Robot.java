/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.dslibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.CRC32;

/**
 *
 * @author Ben Wolsieffer
 */
public class Robot {
    
    public static final int NUM_JOYSTICKS = 4;

    public static enum Mode {

        TELEOPERATED,
        AUTONOMOUS,
        TEST
    }

    public static enum Alliance {

        RED,
        BLUE
    }

    private int packetNumber = 1;
    private Alliance alliance = Alliance.RED;
    private int position = 1;
    private Mode mode = Mode.TELEOPERATED;
    private int teamNumber;
    private boolean enabled;
    private boolean estopped;
    private final ArrayList<Joystick> joysticks = new ArrayList<>(NUM_JOYSTICKS);
    private final List<Joystick> unmodifiableJoysticks = Collections.unmodifiableList(joysticks);

    private final Data data = new Data();

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Robot(int teamNumber) {
        setTeamNumber(teamNumber);
        setAlliance(Alliance.RED);
        setPosition(1);
        for (int i = 1; i <= 4; i++) {
            joysticks.add(new Joystick(i, data));
        }
    }
    
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        if (position >= 1 && position <= 3) {
            this.position = position;
            data.setAlliancePosition((byte) position);
        }
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
        switch (alliance) {
            case RED:
                data.setAllianceColor('R');
                break;
            case BLUE:
                data.setAllianceColor('B');
        }
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        data.setAuto(mode == Mode.AUTONOMOUS);
        data.setTest(mode == Mode.TEST);
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        if (teamNumber >= 0 && teamNumber < 10000) {
            this.teamNumber = teamNumber;
            data.setTeamNumber((short) teamNumber);
        } else {
            throw new IllegalArgumentException("Team number must be positive and have a maximum of four digits.");
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        data.setEnabled(enabled);
    }

    public boolean isEStopped() {
        return estopped;
    }

    public void eStop() {
        estopped = true;
        data.setEStop(estopped);
    }

    public List<Joystick> getJoysticks() {
        return unmodifiableJoysticks;
    }

    public class Data {

        public static final int PACKET_LENGTH = 1024;
        public static final int PACKET_NUMBER_INDEX = 0;
        public static final int FLAG_INDEX = 2;
        public static final byte FLAG_RESET_MASK = (byte) 0b10000000;
        public static final byte FLAG_NOT_ESTOP_MASK = 0b01000000;
        public static final byte FLAG_ENABLE_MASK = 0b00100000;
        public static final byte FLAG_AUTO_MASK = 0b00010000;
        public static final byte FLAG_FMS_ATTACHED_MASK = 0b00001000;
        public static final byte FLAG_RESYNC_MASK = 0b00000100;
        public static final byte FLAG_TEST_MASK = 0b00000010;
        public static final byte FLAG_CHECK_VERSIONs_MASK = 0b00000001;
        public static final int DIGITAL_INPUT_INDEX = 3;
        public static final int TEAM_NUMBER_INDEX = 4;
        public static final int ALLIANCE_COLOR_INDEX = 6;
        public static final int ALLIANCE_POSITION_INDEX = 7;
        public static final int JOYSTICK_MULT = 8;
        public static final int BUTTON_OFFSET = 6;
        public static final int ANALOG_INPUT_INDEX = 40;
        public static final int VERSION_INDEX = 72;
        public static final int CRC_INDEX = 1020;

        private final CRC32 crc = new CRC32();
        private final byte[] data = new byte[PACKET_LENGTH];	//The byte array to hold everything.


        /*
         * data[0] and data[1]: packet index
         * data[2]: control byte: reset not_e_stop enabled auto fms_attached resync test fpga
         * data[3]: digital input bits.
         * data[4] and data[5]: team number
         * data[6]: alliance, either red 'R' or blue 'B'
         * data[7]: position
         * data[8]: joystick 1 x value
         * data[9]: joystick 1 y value
         * data[14-15]: joystick 1 buttons bits backward format ...8 7 6 5 4 3 2 1
         * data[1020] to data[1023]: CRC checksum.
         */
        public Data() {
            data[FLAG_INDEX] |= FLAG_NOT_ESTOP_MASK; //Set the not_e_stop control bit.

            // Driverstation version, not sure if it needs this or not,
            // I just used the numbers on the packets captured from the original
            // driver station.
            data[72] = (byte) 0x30;
            data[73] = (byte) 0x31;
            data[74] = (byte) 0x30;
            data[75] = (byte) 0x34;
            data[76] = (byte) 0x31;
            data[77] = (byte) 0x34;
            data[78] = (byte) 0x30;
            data[79] = (byte) 0x30;
        }

        public void setPacketNumber(int num) {
            data[PACKET_NUMBER_INDEX] = (byte) ((num & 0xFF00) >> 8);
            data[PACKET_NUMBER_INDEX + 1] = (byte) (num & 0xFF);
        }
        
        private void setFlag(int index, byte mask, boolean value) {
            if (value) {
                data[index] |= mask;
            } else {
                data[index] &= ~mask;
            }
        }

        public void setReset(boolean reset) {
            setFlag(FLAG_INDEX, FLAG_RESET_MASK, reset);
        }

        public void setEStop(boolean estop) {
            setFlag(FLAG_INDEX, FLAG_NOT_ESTOP_MASK, !estop);
        }

        public void setEnabled(boolean enabled) {
            setFlag(FLAG_INDEX, FLAG_ENABLE_MASK, enabled);
        }

        public void setAuto(boolean auto) {
            setFlag(FLAG_INDEX, FLAG_AUTO_MASK, auto);
        }

        public void setFMSAttached(boolean fmsAttached) {
            setFlag(FLAG_INDEX, FLAG_FMS_ATTACHED_MASK, fmsAttached);
        }

        public void setResync(boolean resync) {
            setFlag(FLAG_INDEX, FLAG_RESYNC_MASK, resync);
        }

        public void setTest(boolean test) {
            setFlag(FLAG_INDEX, FLAG_TEST_MASK, test);
        }

        public void setCheckVersions(boolean checkVersions) {
            setFlag(FLAG_INDEX, FLAG_CHECK_VERSIONs_MASK, checkVersions);
        }

        public void setDigitalInput(int port, boolean set) {												//digital io port number with base 0.
            byte mask = (byte) (0x1 << port);
            setFlag(DIGITAL_INPUT_INDEX, mask, set);
        }

        public void setTeamNumber(short teamNum) {
            data[TEAM_NUMBER_INDEX] = (byte) ((teamNum & 0xff00) >>> 8);
            data[TEAM_NUMBER_INDEX + 1] = (byte) (teamNum & 0xff);
        }

        public void setAllianceColor(char redOrBlue) {
            data[ALLIANCE_COLOR_INDEX] = (byte) redOrBlue;
        }

        public void setAlliancePosition(byte pos) {
            data[ALLIANCE_POSITION_INDEX] = (byte) (pos + 48);
        }

        public void setJoystickAxis(int joystick, byte axis, byte value) {
            data[(joystick * JOYSTICK_MULT) + (axis - 1)] = value;
        }

        public void setJoystickButton(int joystick, byte button, boolean set) {
            int index = joystick * JOYSTICK_MULT + BUTTON_OFFSET;
            short mask = (short) (0x1 << (button - 1));
            if (set) {
                data[index] |= (mask & 0xFF00) >>> 8;
                data[index + 1] |= mask & 0xFF;
            } else {
                data[index] &= ~((mask & 0xFF00) >>> 8);
                data[index + 1] &= ~(mask & 0xFF);
            }
        }

        public void setAnalogInput(int input, short value) {
            int index = ANALOG_INPUT_INDEX + input * 2;
            data[index] = (byte) ((value & 0xFF00) >>> 8);
            data[index + 1] = (byte) (value & 0xFF);
        }

        public void updateCRC() {
            crc.update(data, 0, CRC_INDEX);
            long checksum = crc.getValue();
            data[CRC_INDEX] = (byte) ((checksum & 0xff000000) >> 24);
            data[CRC_INDEX + 1] = (byte) ((checksum & 0xff0000) >> 16);
            data[CRC_INDEX + 2] = (byte) ((checksum & 0xff00) >> 8);
            data[CRC_INDEX + 3] = (byte) (checksum & 0xff);
        }

        private void getData(byte[] data) {
            if (data.length == this.data.length) {
                System.arraycopy(this.data, 0, data, 0, this.data.length);
            } else {
                throw new IllegalArgumentException("Data array needs to have "+ PACKET_LENGTH + " elements.");
            }
        }
    }
    
    void prepareData() {
        data.setPacketNumber(packetNumber++);
        data.updateCRC();
    }
    
    public void getData(byte[] data) {
        this.data.getData(data);
    }
}
