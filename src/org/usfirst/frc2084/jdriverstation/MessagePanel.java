/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation;

import com.jidesoft.swing.StyledLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

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
        messageLabel = new StyledLabel("Teleoperated Disabled");
        messageLabel.setLineWrap(true);
        messageLabel.setHorizontalAlignment(StyledLabel.CENTER);
        messageLabel.setPreferredWidth(120);
        messageLabel.setFont(messageFont);
        add(messageLabel, BorderLayout.CENTER);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

}
