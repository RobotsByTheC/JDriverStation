/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ben Wolsieffer
 */
public class IndicatorPanel extends JPanel {

    private class VoltagePanel extends JPanel {

        private final JLabel voltsLabel;
        private final JLabel numberLabel;

        public VoltagePanel() {
            setLayout(new BorderLayout());

            Font voltageFont = new Font("Sans", Font.PLAIN, 17);

            voltsLabel = new JLabel("Volts", JLabel.LEFT);
            voltsLabel.setFont(voltageFont);
            add(voltsLabel, BorderLayout.LINE_START);

            numberLabel = new JLabel("00.00", JLabel.RIGHT);
            numberLabel.setFont(voltageFont);
            add(numberLabel, BorderLayout.LINE_END);
        }

        public void setVoltage(float voltage) {
            numberLabel.setText(String.format("%2.2f", voltage));
        }
    }

    private final VoltagePanel voltagePanel;
    private final Indicator communicationsIndicator;
    private final Indicator robotCodeIndicator;
    private final Indicator joysticksIndicator;

    public IndicatorPanel() {
        setPreferredSize(new Dimension(175, 0));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        setLayout(new MigLayout("flowy",
                "10%[grow]10%",
                ""));

        {
            voltagePanel = new VoltagePanel();
            add(voltagePanel, "grow 100 50");
        }
        {
            CC indicatorConstraints = new CC().growY(20f);

            communicationsIndicator = new Indicator("Communications", Indicator.Color.RED);
            add(communicationsIndicator, indicatorConstraints);

            robotCodeIndicator = new Indicator("Robot Code", Indicator.Color.RED);
            add(robotCodeIndicator, indicatorConstraints);

            joysticksIndicator = new Indicator("Joysticks", Indicator.Color.RED);
            add(joysticksIndicator, indicatorConstraints);
        }
    }

}
