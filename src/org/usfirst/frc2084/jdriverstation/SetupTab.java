/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation;

import java.awt.GridBagConstraints;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ben Wolsieffer
 */
public class SetupTab extends DriverStationTab {

    private class TeamNumberPanel extends JPanel {

        private final JLabel teamNumberLabel;
        private final JTextField teamNumberField;

        public TeamNumberPanel() {
            setLayout(new MigLayout("flowy"));

            teamNumberLabel = new JLabel("Team Number");
            add(teamNumberLabel);
            teamNumberField = new JTextField();
            add(teamNumberField, "growx");
        }

    }
    private final TeamNumberPanel teamNumberPanel;

    private class PracticePanel extends JPanel {

        private final JLabel titleLabel;

        private final JLabel countdownLabel;
        private final JTextField countdownField;
        private final JLabel autonomousLabel;
        private final JTextField autonomousField;
        private final JLabel delayLabel;
        private final JTextField delayField;

        private final JLabel teleoperatedLabel;
        private final JTextField teleoperatedField;
        private final JLabel endGameLabel;
        private final JTextField endGameField;
        private final JLabel soundEffectsLabel;
        private final JCheckBox soundEffectsCheckBox;

        public PracticePanel() {
            setLayout(new MigLayout("wrap 4, flowy"));
            {
                titleLabel = new JLabel("Practice Round Timing (seconds)");
                add(titleLabel, "spanx 4");
            }
            {
                CC leftLabelConstraints = new CC().alignX("right");

                countdownLabel = new JLabel("Countdown", JLabel.RIGHT);
                add(countdownLabel, leftLabelConstraints);

                autonomousLabel = new JLabel("Autonomous", JLabel.RIGHT);
                add(autonomousLabel, leftLabelConstraints);

                delayLabel = new JLabel("Delay", JLabel.RIGHT);
                add(delayLabel, leftLabelConstraints);
            }
            {
                CC fieldConstraints = new CC().growX().width("50::");

                countdownField = new JTextField();
                add(countdownField, fieldConstraints);

                autonomousField = new JTextField();
                add(autonomousField, fieldConstraints);

                delayField = new JTextField();
                add(delayField, fieldConstraints);

                teleoperatedField = new JTextField();
                add(teleoperatedField, fieldConstraints);

                endGameField = new JTextField();
                add(endGameField, fieldConstraints);

                soundEffectsCheckBox = new JCheckBox();
                add(soundEffectsCheckBox, "right");
            }
            {
                CC rightLabelConstraints = new CC().alignX("left");

                teleoperatedLabel = new JLabel("Teleoperated", JLabel.LEFT);
                add(teleoperatedLabel, rightLabelConstraints);

                endGameLabel = new JLabel("End Game", JLabel.LEFT);
                add(endGameLabel, rightLabelConstraints);

                soundEffectsLabel = new JLabel("Sound Effects", JLabel.LEFT);
                add(soundEffectsLabel, rightLabelConstraints);
            }
        }
    }
    private final PracticePanel practicePanel;

    public SetupTab(MainWindow mainWindow) {
        super(mainWindow);

        setLayout(new MigLayout("",
                "[]20px[grow]",
                ""));

        {
            teamNumberPanel = new TeamNumberPanel();
            add(teamNumberPanel, "top, left");
        }
        {
            practicePanel = new PracticePanel();
            add(practicePanel, "grow");
        }
    }

}
