/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.communication;

import java.net.InetAddress;

/**
 *
 * @author Ben Wolsieffer
 */
public class Robot {

    public static enum Alliance {

        RED,
        BLUE
    }

    private InetAddress address;
    private Alliance alliance = Alliance.RED;
    private int position = 1;
    private int numJoysticks = 0;
    private final Joystick[] joysticks = new Joystick[4];

    private Robot(InetAddress address) {
        this.address = address;
    }

    public void setPosition(int position) {
        if (position >= 1 && position <= 3) {
            this.position = position;
        }
    }

    public int getPosition() {
        return position;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }

    public Joystick[] getJoysticks() {
        return joysticks;
    }

    public void addJoystick(Joystick joy) {
        if (numJoysticks < joysticks.length) {
            joysticks[numJoysticks] = joy;
            numJoysticks++;
        }
    }
    
    private void attemptConnect() {
        
    }
    
    private void sendPacket() {
        
    }
}
