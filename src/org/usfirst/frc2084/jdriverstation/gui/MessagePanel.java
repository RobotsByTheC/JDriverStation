/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.gui;

import com.jidesoft.swing.StyledLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.usfirst.frc2084.jdriverstation.communication.CommunicationManager;
import static org.usfirst.frc2084.jdriverstation.gui.ApplicationElement.*;

/**
 *
 * @author Ben Wolsieffer
 */
public class MessagePanel extends JPanel {

    private final StyledLabel messageLabel;

    public MessagePanel() {
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        setLayout(new BorderLayout());

        Font messageFont = new Font("Sans", Font.BOLD, 16);
        messageLabel = new StyledLabel();
        messageLabel.setLineWrap(true);
        messageLabel.setHorizontalAlignment(StyledLabel.CENTER);
        messageLabel.setPreferredWidth(120);
        messageLabel.setFont(messageFont);
        updateMessage();
        add(messageLabel, BorderLayout.CENTER);

        getCommunicationManager().addPropertyChangeListener(evt -> {
            updateMessage();
        });
    }

    private void updateMessage() {
        CommunicationManager cm = getCommunicationManager();

        String message = "";
        if (cm.isConnected()) {
            switch (cm.getMode()) {
                case AUTONOMOUS:
                    message = "Autonomous";
                    break;
                case TELEOPERATED:
                    message = "Teleoperated";
                    break;
                case PRACTICE:
                    message = "Practice";
                    break;
                case TEST:
                    message = "Test";
            }
            message += " ";
            if (cm.isEnabled()) {
                message += "Enabled";
            } else {
                message += "Disabled";
            }
        } else {
            message = "No Robot Communication";
        }
        messageLabel.setText(message);
    }
}
