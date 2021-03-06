/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.dslibrary;

import java.util.EventListener;

/**
 *
 * @author Ben Wolsieffer
 */
public interface CommunicationListener extends EventListener {

    public void preSend();

    public void postSend();

    public void preReceive();

    public void postReceive();
}
