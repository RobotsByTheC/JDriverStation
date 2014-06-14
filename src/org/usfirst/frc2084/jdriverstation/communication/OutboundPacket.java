/* 
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.communication;

import java.util.zip.CRC32;

public class OutboundPacket {

    public byte[] data = new byte[1024];	//The byte array to hold everything.

    //Constants:
    static int JOY1 = 8, JOY2 = 16, JOY3 = 24, JOY4 = 32;

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
    public OutboundPacket() {
        data[2] |= 64; //Set the not_e_stop control bit.

        //Diverstation version, not sure if it needs this or not,
        //I just used the numbers on the packets captured from the original
        //driver station.
        data[72] = (byte) 0x30;
        data[73] = (byte) 0x31;
        data[74] = (byte) 0x30;
        data[75] = (byte) 0x34;
        data[76] = (byte) 0x31;
        data[77] = (byte) 0x34;
        data[78] = (byte) 0x30;
        data[79] = (byte) 0x30;
    }

    /**
     * Gets the low byte of a short.
     *
     * @param i the short
     * @return the low byte of a short
     */
    private byte int4(int i) {
        return (byte) (i & 0xFF);
    }

    /**
     * Gets the high byte of a short.
     *
     * @param i the short
     * @return the high byte of a short
     */
    private byte int3(int i) {
        return (byte) ((i & 0xFF00) >> 8);
    }

    private byte int2(int i) {
        return (byte) ((i & 0xFF0000) >> 16);
    }

    private int int1(int i) {
        return (byte) ((i & 0xFF000000) >> 24);
    }

    public void setIndex(int index) //Set the packet index to the provided value.
    {
        data[0] = int3(index);
        data[1] = int4(index);
    }

    public void setAuto(boolean auto) //Set robot to autonomous mode
    {
        if (auto) {
            data[2] |= 16;
        } else {
            data[2] &= ~16;
        }
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            data[2] |= 32;
        } else {
            data[2] &= ~32;
        }
    }

    public void setDigitalIn(int port, boolean set) //Set digital inputs, port is the
    {												//digital io port number with base 0.
        if (set) {
            data[3] |= (byte) (128 / Math.pow(2, port));
        } else {
            data[3] &= ~((byte) (128 / Math.pow(2, port)));
        }
    }

    public void setTeam(int num) //Set the team number
    {
        
        byte byt1 = (byte) ((num & 0xff00) >> 8); //Get first byte.
        byte byt2 = (byte) (num & 0xff);	//Second byte.

        data[4] = byt1;
        data[5] = byt2;
    }

    public void setAlliance(char redOrBlue) //Set alliance, either red 'R' or blue 'B'
    {
        data[6] = (byte) redOrBlue;
    }

    public void setPosition(int pos) //Set team position.
    {
        data[7] = (byte) pos;
    }

    public void setJoystick(byte x, byte y, int joystick) //Set the joystick values.
    {
        data[joystick] = x;
        data[joystick + 1] = (byte) -y;	//For some reason the robot uses reverse y value,
        //negative goes forward.
    }

    public void setButton(int button, boolean set) {
        if (set) {
            data[15] |= (byte) (Math.pow(2, button));
        } else {
            data[15] &= ~((byte) Math.pow(2, button));
        }
    }

    public void makeCRC() {

        CRC32 crc = new CRC32();
        crc.update(data);
        long checksum = crc.getValue();
        data[1020] = (byte) ((checksum & 0xff000000) >> 24);
        data[1021] = (byte) ((checksum & 0xff0000) >> 16);
        data[1022] = (byte) ((checksum & 0xff00) >> 8);
        data[1023] = (byte) (checksum & 0xff);
    }

    public void clearCRC() {
        data[1020] = 0;
        data[1021] = 0;
        data[1022] = 0;
        data[1023] = 0;
    }
}
