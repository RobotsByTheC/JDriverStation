/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.gui.joysticklist;

import java.util.List;
import javax.swing.AbstractListModel;
import net.java.games.input.Controller;
import org.usfirst.frc2084.jdriverstation.input.JoystickManager;
import static org.usfirst.frc2084.jdriverstation.gui.ApplicationElement.*;

/**
 *
 * @author Ben Wolsieffer
 */
public class JoystickListModel extends AbstractListModel<Controller> {

    private final List<Controller> joysticks;
    public JoystickListModel() {
        joysticks = getJoystickManager().getJoysticks();
        getJoystickManager().addJoystickListener(new JoystickManager.JoystickListener() {

            @Override
            public void joystickAdded(Controller c) {
                fireIntervalAdded(this, 0, joysticks.size());
            }

            @Override
            public void joystickRemoved(Controller c) {
                fireIntervalAdded(this, 0, joysticks.size());
            }
        });
    }

    @Override
    public int getSize() {
        return joysticks.size();
    }

    @Override
    public Controller getElementAt(int index) {
        return joysticks.get(index);
    }
    
    public void add(int index, Controller c) {
        joysticks.add(index, c);
    }
    
    public void remove(int index) {
        joysticks.remove(index);
    }
}
