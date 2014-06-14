/* 
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ben Wolsieffer
 */
public class MainWindow extends JFrame {

    public static final Dimension DEFAULT_SIZE = new Dimension(1025, 200);

    private final JDriverStation driverStation;

    private final IndicatorPanel indicatorPanel;
    private final MessagePanel messagePanel;

    private final JTabbedPane tabs;
    private final OperationTab operationTab;
    private final DiagnosticsTab diagnosticsTab;
    private final SetupTab setupTab;
    private final IOTab ioTab;
    private final ChartsTab chartsTab;

    private boolean docked = false;

    private class TabLabel extends JLabel {

        public TabLabel(String title) {
            super(title);
            setPreferredSize(new Dimension(100, 15));
            setHorizontalAlignment(CENTER);
        }
    }

    public MainWindow(JDriverStation driverStation) {
        super("FRC Driver Station");
        this.driverStation = driverStation;

        setLayout(new MigLayout("flowy",
                "[][grow]",
                "[][grow]"));

        indicatorPanel = new IndicatorPanel();
        getContentPane().add(indicatorPanel, "grow 100 70");

        messagePanel = new MessagePanel();
        getContentPane().add(messagePanel, "grow 100 30");

        {
            tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
            tabs.addTab("Operation", operationTab = new OperationTab(this));
            tabs.addTab("Diagnostics", diagnosticsTab = new DiagnosticsTab(this));
            tabs.addTab("Setup", setupTab = new SetupTab(this));
            tabs.addTab("I/O", ioTab = new IOTab(this));
            tabs.addTab("Charts", chartsTab = new ChartsTab(this));
            for (int i = 0; i < tabs.getTabCount(); i++) {
                tabs.setTabComponentAt(i, new TabLabel(tabs.getTitleAt(i)));
            }
            getContentPane().add(tabs, " spany 2, grow, newline");
        }

        setLocationByPlatform(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setMinimumSize(getSize());
        setVisible(true);
    }

    public void setDocked(boolean docked) {
        if (this.docked != docked) {
            this.docked = docked;
            dispose();
            setUndecorated(docked);
            if (docked) {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Dimension screenSize = toolkit.getScreenSize();
                Insets screenInsets = toolkit.getScreenInsets(getGraphicsConfiguration());
                pack();
                if (toolkit.isFrameStateSupported(MAXIMIZED_HORIZ)) {
                    setExtendedState(MAXIMIZED_HORIZ);
                } else {
                    // Horizontal maximization is not supported on Windoze, so
                    // much for Java being a WORA language (I guess its WODE).
                    setSize(screenSize.width - screenInsets.left - screenInsets.right,
                            getSize().height);
                }
                setLocation(screenInsets.left, screenSize.height - getHeight() - screenInsets.bottom);
            } else {
                setExtendedState(NORMAL);
                setLocationByPlatform(true);
                pack();
            }
            setVisible(true);
        }
    }

    public boolean isDocked() {
        return docked;
    }

    public OperationTab getOperationTab() {
        return operationTab;
    }

    public DiagnosticsTab getDiagnosticsTab() {
        return diagnosticsTab;
    }

    public SetupTab getSetupTab() {
        return setupTab;
    }

    public IOTab getIOTab() {
        return ioTab;
    }

    public ChartsTab getChartsTab() {
        return chartsTab;
    }

    public IndicatorPanel getIndicatorPanel() {
        return indicatorPanel;
    }

    public MessagePanel getMessagePanel() {
        return messagePanel;
    }

    public JDriverStation getDriverStation() {
        return driverStation;
    }
}
