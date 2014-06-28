/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.gui;

import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;
import org.usfirst.frc2084.jdriverstation.logging.ShortFormatter;
import org.usfirst.frc2084.jdriverstation.logging.TextAreaHandler;

/**
 *
 * @author Ben Wolsieffer
 */
public class DiagnosticsTab extends JPanel {

    private class IndicatorPanel extends JPanel {

        public IndicatorPanel() {
            setLayout(new MigLayout());
        }

    }
    private final IndicatorPanel indicatorPanel;

    private class MessagePanel extends JPanel {

        private final JLabel messageLabel;
        private final JTextArea messageArea;

        public MessagePanel() {
            setLayout(new MigLayout(
                    "flowy",
                    "[grow]",
                    "[][grow]"
            ));

            messageLabel = new JLabel("Messages");
            add(messageLabel);
            
            messageArea = new JTextArea();
            messageArea.setEditable(false);

            Logger appLogger = Logger.getLogger("org.usfirst.frc2084");
            TextAreaHandler textAreaHandler = new TextAreaHandler(messageArea);
            textAreaHandler.setFormatter(new ShortFormatter());
            appLogger.addHandler(textAreaHandler);

            add(messageArea, "grow");
        }

    }
    private final MessagePanel messagePanel;

    public DiagnosticsTab() {
        setLayout(new MigLayout(
                "",
                "[shrink][grow]",
                "[grow]"
        ));

        indicatorPanel = new IndicatorPanel();
        add(indicatorPanel, "");

        messagePanel = new MessagePanel();
        add(messagePanel, "grow");
    }
}
