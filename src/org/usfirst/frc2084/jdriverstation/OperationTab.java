/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import org.usfirst.frc2084.jdriverstation.communication.Robot;
import org.usfirst.frc2084.jdriverstation.resources.ResourceManager;

/**
 *
 * @author Ben Wolsieffer
 */
public class OperationTab extends DriverStationTab {

    private enum AlliancePosition {

        BLUE1(Robot.Alliance.BLUE, 1),
        BLUE2(Robot.Alliance.BLUE, 2),
        BLUE3(Robot.Alliance.BLUE, 3),
        RED1(Robot.Alliance.RED, 1),
        RED2(Robot.Alliance.RED, 2),
        RED3(Robot.Alliance.RED, 3);

        private final Robot.Alliance alliance;
        private final int position;

        AlliancePosition(Robot.Alliance alliance, int position) {
            this.alliance = alliance;
            this.position = position;
        }

        public Robot.Alliance getAlliance() {
            return alliance;
        }

        public int getPosition() {
            return position;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            switch (alliance) {
                case RED:
                    sb.append("Red");
                    break;
                case BLUE:
                    sb.append("Blue");
                    break;
            }
            sb.append(" ");
            sb.append(position);

            return sb.toString();
        }

    }

    private class ControlPanel extends JPanel {

        private final ButtonGroup modeButtonGroup;
        private final JRadioButton teleoperatedRadioButton;
        private final JRadioButton autonomousRadioButton;
        private final JRadioButton practiceRadioButton;
        private final JRadioButton testRadioButton;

        private final ButtonGroup enableDisableButtonGroup;
        private final JToggleButton enableButton;
        private final JToggleButton disableButton;

        public ControlPanel() {
            setLayout(new GridBagLayout());

            {
                modeButtonGroup = new ButtonGroup();

                GridBagConstraints modeButtonConstraints = new GridBagConstraints();
                modeButtonConstraints.fill = GridBagConstraints.VERTICAL;
                modeButtonConstraints.anchor = GridBagConstraints.LINE_START;
                modeButtonConstraints.gridwidth = 2;

                teleoperatedRadioButton = new JRadioButton("Teleoperated");
                teleoperatedRadioButton.setSelected(true);
                modeButtonGroup.add(teleoperatedRadioButton);
                modeButtonConstraints.gridy = 0;
                add(teleoperatedRadioButton, modeButtonConstraints);

                autonomousRadioButton = new JRadioButton("Autonomous");
                modeButtonGroup.add(autonomousRadioButton);
                modeButtonConstraints.gridy = 1;
                add(autonomousRadioButton, modeButtonConstraints);

                practiceRadioButton = new JRadioButton("Practice");
                modeButtonGroup.add(practiceRadioButton);
                modeButtonConstraints.gridy = 2;
                add(practiceRadioButton, modeButtonConstraints);

                testRadioButton = new JRadioButton("Test");
                modeButtonGroup.add(testRadioButton);
                modeButtonConstraints.gridy = 3;
                add(testRadioButton, modeButtonConstraints);
            }

            {
                enableDisableButtonGroup = new ButtonGroup();

                GridBagConstraints buttonConstraints = new GridBagConstraints();
                buttonConstraints.fill = GridBagConstraints.BOTH;
                buttonConstraints.anchor = GridBagConstraints.LINE_START;
                buttonConstraints.gridy = 4;
                buttonConstraints.gridheight = 1;
                buttonConstraints.ipady = 10;

                Font buttonFont = new Font("", Font.BOLD, 14);

                enableButton = new JToggleButton("Enable");
                enableButton.setForeground(Color.decode("#088A08"));
                buttonConstraints.gridx = 0;
                enableButton.setFont(buttonFont);
                enableDisableButtonGroup.add(enableButton);
                add(enableButton, buttonConstraints);

                disableButton = new JToggleButton("Disable");
                disableButton.setSelected(true);
                disableButton.setForeground(Color.RED);
                buttonConstraints.gridx = 1;
                disableButton.setFont(buttonFont);
                enableDisableButtonGroup.add(disableButton);
                add(disableButton, buttonConstraints);
            }
        }

    }
    private final ControlPanel controlPanel;

    private class StatusPanel extends JPanel {

        private final JTextField elapsedTimeTextField;
        private final JComboBox<AlliancePosition> teamStationComboBox;
        private final ButtonGroup windowModeButtonGroup;
        private final JToggleButton floatingModeButton;
        private final JToggleButton dockedModeButton;

        public StatusPanel() {
            setLayout(new GridBagLayout());

            GridBagConstraints labelConstraints = new GridBagConstraints();
            labelConstraints.fill = GridBagConstraints.BOTH;
            labelConstraints.weighty = 1.0;
            labelConstraints.insets.right = 5;

            {
                add(new JLabel("Elapsed Time"), labelConstraints);
                elapsedTimeTextField = new JTextField();
                GridBagConstraints elapsedTimeConstraints = new GridBagConstraints();
                elapsedTimeConstraints.fill = GridBagConstraints.HORIZONTAL;
                elapsedTimeConstraints.gridx = 1;
                elapsedTimeConstraints.gridy = 0;
                elapsedTimeConstraints.gridwidth = 2;
                add(elapsedTimeTextField, elapsedTimeConstraints);
            }

            {
                labelConstraints.gridy = 1;
                add(new JLabel("Window Mode"), labelConstraints);
                GridBagConstraints windowModeConstraints = new GridBagConstraints();
                windowModeConstraints.gridx = 1;
                windowModeConstraints.gridy = 1;
                windowModeConstraints.gridwidth = 1;
                windowModeConstraints.fill = GridBagConstraints.BOTH;
                windowModeConstraints.weightx = 1.0;
                
                windowModeButtonGroup = new ButtonGroup();
                floatingModeButton = new JToggleButton(ResourceManager.getFloatingIcon());
                floatingModeButton.addActionListener((e)-> getMainWindow().setDocked(false));
                windowModeButtonGroup.add(floatingModeButton);
                add(floatingModeButton, windowModeConstraints);
                dockedModeButton = new JToggleButton(ResourceManager.getDockedIcon());
                dockedModeButton.addActionListener((e)-> getMainWindow().setDocked(true));
                windowModeConstraints.gridx = 2;
                windowModeButtonGroup.add(dockedModeButton);
                add(dockedModeButton, windowModeConstraints);
            }

            {
                labelConstraints.gridy = 2;
                add(new JLabel("Team Station"), labelConstraints);
                teamStationComboBox = new JComboBox<>(new AlliancePosition[]{
                    AlliancePosition.BLUE1,
                    AlliancePosition.BLUE2,
                    AlliancePosition.BLUE3,
                    AlliancePosition.RED1,
                    AlliancePosition.RED2,
                    AlliancePosition.RED3
                });
                GridBagConstraints teamStationConstraints = new GridBagConstraints();
                teamStationConstraints.gridx = 1;
                teamStationConstraints.gridy = 2;
                teamStationConstraints.gridwidth = 2;
                add(teamStationComboBox, teamStationConstraints);
            }
        }

    }
    private final StatusPanel statusPanel;

    private class MessagePanel extends JPanel {

        private final JTextArea messageTextArea;
        private final JLabel messageLabel;

        public MessagePanel() {
            setLayout(new GridBagLayout());

            messageLabel = new JLabel("User Messages");
            GridBagConstraints messageLabelConstraints = new GridBagConstraints();
            messageLabelConstraints.anchor = GridBagConstraints.LINE_START;
            add(messageLabel, messageLabelConstraints);

            messageTextArea = new JTextArea();
            messageTextArea.setEditable(false);
            messageTextArea.setColumns(25);
            GridBagConstraints messageConstraints = new GridBagConstraints();
            messageConstraints.fill = GridBagConstraints.BOTH;
            messageConstraints.gridy = 1;
            messageConstraints.weightx = 1.0;
            messageConstraints.weighty = 1.0;
            add(messageTextArea, messageConstraints);
        }

    }
    private final MessagePanel messagePanel;

    public OperationTab(MainWindow mainWindow) {
        super(mainWindow);

        setLayout(new GridBagLayout());

        {
            controlPanel = new ControlPanel();
            GridBagConstraints controlPanelConstraints = new GridBagConstraints();
            controlPanelConstraints.insets = new Insets(5, 5, 5, 5);
            controlPanelConstraints.gridx = 0;
            controlPanelConstraints.fill = GridBagConstraints.VERTICAL;
            add(controlPanel, controlPanelConstraints);
        }

        {
            statusPanel = new StatusPanel();
            GridBagConstraints statusPanelConstraints = new GridBagConstraints();
            statusPanelConstraints.insets = new Insets(5, 5, 5, 5);
            statusPanelConstraints.gridx = 1;
            statusPanelConstraints.fill = GridBagConstraints.VERTICAL;
            add(statusPanel, statusPanelConstraints);
        }

        {
            messagePanel = new MessagePanel();
            GridBagConstraints messagePanelConstraints = new GridBagConstraints();
            messagePanelConstraints.insets = new Insets(5, 5, 5, 5);
            messagePanelConstraints.gridx = 2;
            messagePanelConstraints.weightx = 1.0;
            messagePanelConstraints.fill = GridBagConstraints.BOTH;
            add(messagePanel, messagePanelConstraints);
        }
    }
}
