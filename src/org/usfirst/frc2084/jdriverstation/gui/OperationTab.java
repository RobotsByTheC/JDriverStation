/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.gui;

import com.tulskiy.keymaster.common.Provider;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.usfirst.frc2084.dslibrary.Robot;
import org.usfirst.frc2084.jdriverstation.communication.CommunicationManager;
import org.usfirst.frc2084.jdriverstation.resources.ResourceManager;
import static org.usfirst.frc2084.jdriverstation.gui.ApplicationElement.*;

/**
 *
 * @author Ben Wolsieffer
 */
public class OperationTab extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(OperationTab.class.getName());

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

        @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
        public ControlPanel() {
            setLayout(new MigLayout(
                    "",
                    "[grow]0[grow]",
                    "[]0[]0[]0[]0[grow, :50lp:50lp]"
            ));

            {
                modeButtonGroup = new ButtonGroup();

                CC modeButtonConstraints = new CC().grow().spanX(2).wrap();

                teleoperatedRadioButton = new JRadioButton("Teleoperated");
                teleoperatedRadioButton.setSelected(true);
                modeButtonGroup.add(teleoperatedRadioButton);
                teleoperatedRadioButton.addActionListener(
                        e -> getCommunicationManager().setMode(CommunicationManager.DriverStationMode.TELEOPERATED)
                );
                add(teleoperatedRadioButton, modeButtonConstraints);

                autonomousRadioButton = new JRadioButton("Autonomous");
                modeButtonGroup.add(autonomousRadioButton);
                autonomousRadioButton.addActionListener(
                        e -> getCommunicationManager().setMode(CommunicationManager.DriverStationMode.AUTONOMOUS)
                );
                add(autonomousRadioButton, modeButtonConstraints);

                practiceRadioButton = new JRadioButton("Practice");
                modeButtonGroup.add(practiceRadioButton);
                practiceRadioButton.addActionListener(
                        e -> getCommunicationManager().setMode(CommunicationManager.DriverStationMode.PRACTICE)
                );
                add(practiceRadioButton, modeButtonConstraints);

                testRadioButton = new JRadioButton("Test");
                modeButtonGroup.add(testRadioButton);
                testRadioButton.addActionListener(
                        e -> getCommunicationManager().setMode(CommunicationManager.DriverStationMode.TEST)
                );
                add(testRadioButton, modeButtonConstraints);
            }

            {
                enableDisableButtonGroup = new ButtonGroup();

                CC buttonConstraints = new CC().grow().pad(5, 5, 5, 5);

                Font buttonFont = new Font("", Font.BOLD, 14);

                enableButton = new JToggleButton("Enable");
                enableButton.setForeground(Color.decode("#088A08"));
                enableButton.setFont(buttonFont);
                enableButton.addActionListener(e -> getCommunicationManager().setEnabled(true));
                enableDisableButtonGroup.add(enableButton);
                add(enableButton, buttonConstraints);

                disableButton = new JToggleButton("Disable");
                disableButton.setSelected(true);
                disableButton.setForeground(Color.RED);
                disableButton.setFont(buttonFont);
                disableButton.addActionListener(e -> getCommunicationManager().setEnabled(false));
                enableDisableButtonGroup.add(disableButton);
                add(disableButton, buttonConstraints);
            }
            {
                Provider.logger.setLevel(Level.WARNING);
                Provider shortcutProvider = Provider.getCurrentProvider(true);
                shortcutProvider.register(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), e -> {
                    getCommunicationManager().setEnabled(true);
                });
                shortcutProvider.register(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), e -> {

                });
                shortcutProvider.register(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), e -> {
                    getCommunicationManager().setEnabled(false);
                });
            }
            {
                getCommunicationManager().addPropertyChangeListener(evt -> {
                    switch (evt.getPropertyName()) {
                        case CommunicationManager.ENABLED_PROPERTY:
                            JToggleButton b = ((boolean) evt.getNewValue()) ? enableButton : disableButton;
                            b.setSelected(true);
                            b.requestFocusInWindow();
                            break;
                    }
                });
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
            setLayout(new MigLayout(
                    "",
                    "[][grow]0[grow]",
                    ""
            ));

            CC labelConstraints = new CC().alignX("right").alignY("center");
            {
                add(new JLabel("Elapsed Time"), labelConstraints);
                elapsedTimeTextField = new JTextField();
                add(elapsedTimeTextField, "growx, wrap, spanx 2");
            }

            {
                add(new JLabel("Window Mode"), labelConstraints);

                windowModeButtonGroup = new ButtonGroup();
                floatingModeButton = new JToggleButton(ResourceManager.getFloatingIcon());
                floatingModeButton.setToolTipText("Float Window");
                floatingModeButton.addActionListener((e) -> getMainWindow().setDocked(false));
                windowModeButtonGroup.add(floatingModeButton);
                add(floatingModeButton, "grow, gap 0");
                dockedModeButton = new JToggleButton(ResourceManager.getDockedIcon());
                dockedModeButton.setToolTipText("Dock Window");
                dockedModeButton.addActionListener((e) -> getMainWindow().setDocked(true));
                windowModeButtonGroup.add(dockedModeButton);
                add(dockedModeButton, "grow, gap 0, wrap");
            }

            {
                add(new JLabel("Team Station"), labelConstraints);
                teamStationComboBox = new JComboBox<>(new AlliancePosition[]{
                    AlliancePosition.BLUE1,
                    AlliancePosition.BLUE2,
                    AlliancePosition.BLUE3,
                    AlliancePosition.RED1,
                    AlliancePosition.RED2,
                    AlliancePosition.RED3
                });
                teamStationComboBox.addItemListener((e) -> {
                    AlliancePosition ap = (AlliancePosition) e.getItem();
                    getCommunicationManager().setAlliance(ap.getAlliance());
                    getCommunicationManager().setPosition(ap.getPosition());
                });
                add(teamStationComboBox, "spanx 2, growx");
            }
        }

    }
    private final StatusPanel statusPanel;

    private class MessagePanel extends JPanel {

        private final JTextArea messageTextArea;
        private final JLabel messageLabel;

        public MessagePanel() {
            setLayout(new MigLayout());

            messageLabel = new JLabel("User Messages");
            add(messageLabel, "wrap");

            messageTextArea = new JTextArea();
            messageTextArea.setEditable(false);
            messageTextArea.setColumns(21);
            messageTextArea.setRows(6);
            add(messageTextArea, "");
        }

    }
    private final MessagePanel messagePanel;

    public OperationTab() {

        setLayout(new MigLayout(
                "",
                "[][grow, ::300px][shrink]",
                "grow"
        ));

        {
            controlPanel = new ControlPanel();
            add(controlPanel, "growy");
        }

        {
            statusPanel = new StatusPanel();
            add(statusPanel, "grow");
        }

        {
            messagePanel = new MessagePanel();
            add(messagePanel, "aligny top");
        }
    }
}
