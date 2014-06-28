/* 
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import net.miginfocom.swing.MigLayout;
import static org.usfirst.frc2084.jdriverstation.gui.ApplicationElement.getPreferencesManager;

/**
 *
 * @author Ben Wolsieffer
 */
public class MainWindow extends JFrame {

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

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public MainWindow() {
        super("FRC Driver Station");

        Container cp = getContentPane();

        setLayout(new MigLayout("flowy",
                "[shrink 0][grow]",
                "[][grow]"
        ));

        indicatorPanel = new IndicatorPanel();
        cp.add(indicatorPanel, "grow 100 70");

        messagePanel = new MessagePanel();
        cp.add(messagePanel, "grow 100 30");

        {
            tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
            tabs.addTab("Operation", operationTab = new OperationTab());
            tabs.addTab("Diagnostics", diagnosticsTab = new DiagnosticsTab());
            tabs.addTab("Setup", setupTab = new SetupTab());
            tabs.addTab("I/O", ioTab = new IOTab());
            tabs.addTab("Charts", chartsTab = new ChartsTab());
            for (int i = 0; i < tabs.getTabCount(); i++) {
                tabs.setTabComponentAt(i, new TabLabel(tabs.getTitleAt(i)));
            }
            cp.add(tabs, " spany 2, grow, newline");
        }

        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                if (!docked) {
                    getPreferencesManager().setWindowSize(cp.getSize());
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                if (!docked) {
                    getPreferencesManager().setWindowLocation(getLocation());
                }
            }

        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        loadSizeAndLocation();

        boolean prefDocked = getPreferencesManager().isWindowDocked();
        setDocked(prefDocked);

        setVisible(true);
    }

    private void loadSizeAndLocation() {
        Point location = getPreferencesManager().getWindowLocation();
        if (location != null) {
            setLocation(location);
        } else {
            setLocationByPlatform(true);
        }
        Container cp = getContentPane();
        Dimension minSize = getMinimumSize();
        Dimension size = getPreferencesManager().getWindowSize();
        if (size != null) {
            if (size.width < minSize.width) {
                size.width = minSize.width;
            }
            if (size.height < minSize.height) {
                size.height = minSize.height;
            }
            cp.setPreferredSize(size);
        }
        pack();
    }

    public void setDocked(boolean docked) {
        if (this.docked != docked) {
            this.docked = docked;
            getPreferencesManager().setWindowDocked(docked);
            dispose();
            setUndecorated(docked);
            if (docked) {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Dimension screenSize = toolkit.getScreenSize();
                Insets screenInsets = toolkit.getScreenInsets(getGraphicsConfiguration());
                if (toolkit.isFrameStateSupported(MAXIMIZED_HORIZ)) {
                    setExtendedState(MAXIMIZED_HORIZ);
                } else {
                    Container cp = getContentPane();
                    // Horizontal maximization is not supported on Windoze, so
                    // much for Java being a WORA language (I guess its WODE).
                    cp.setPreferredSize(new Dimension(
                            screenSize.width - screenInsets.left - screenInsets.right,
                            cp.getSize().height)
                    );
                    pack();
                }
                setLocation(screenInsets.left, screenSize.height - getHeight() - screenInsets.bottom);
            } else {
                setExtendedState(NORMAL);
                loadSizeAndLocation();
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
}
