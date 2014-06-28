/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.input;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.ControllerEvent;
import net.java.games.input.ControllerListener;
import org.usfirst.frc2084.dslibrary.Joystick;
import static org.usfirst.frc2084.jdriverstation.gui.ApplicationElement.*;
import org.usfirst.frc2084.jdriverstation.preferences.PreferencesManager;

/**
 *
 * @author Ben Wolsieffer
 */
public class JoystickManager {

    private static final Logger logger = Logger.getLogger(JoystickManager.class.getName());

    static {
        Logger jinputLogger = Logger.getLogger(ControllerEnvironment.class.getName());
        jinputLogger.setLevel(Level.OFF);
    }

    public static interface JoystickListener extends EventListener {

        public void joystickAdded(Controller c);

        public void joystickRemoved(Controller c);

    }

    private ControllerEnvironment controllerEnvironment;
    private HashMap<Controller, Integer> joystickIds = new HashMap<>();
    private List<Integer> joystickPositions = new ArrayList<>();
    private List<Controller> joysticks = new ArrayList<Controller>() {

        @Override
        public void add(int i, Controller e) {
            int id = getJoystickId(e);
            joystickPositions.add(i, id);
            getPreferencesManager().setJoystickPositions(joystickPositions);
            super.add(i, e);
        }

        @Override
        public boolean add(Controller j) {
            PreferencesManager pm = getPreferencesManager();
            int id = getJoystickId(j);
            int pos = pm.getJoystickPosition(id);
            if (pos != -1 && pos <= size()) {
                joystickPositions.add(pos, id);
                super.add(pos, j);
            } else {
                joystickPositions.add(id);
                return super.add(j);
            }
            return true;
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof Controller) {
                joystickPositions.remove(getJoystickId((Controller) o));
                getPreferencesManager().setJoystickPositions(joystickPositions);
                return super.remove(o);
            } else {
                return false;
            }
        }

        @Override
        public Controller remove(int index) {
            joystickPositions.remove(index);
            return super.remove(index);
        }

    };
    private final ControllerListener controllerListener;
    private final EventListenerList joystickListeners = new EventListenerList();

    public JoystickManager() {
        controllerEnvironment = ControllerEnvironment.getDefaultEnvironment();
        initializeJoysticks();

        controllerListener = new ControllerListener() {

            @Override
            public void controllerRemoved(ControllerEvent ev) {
                Controller c = ev.getController();
                Controller.Type t = c.getType();
                if (t.equals(Controller.Type.STICK) || t.equals(Controller.Type.GAMEPAD)) {
                    fireJoystickRemoved(c);
                    joysticks.remove(ev.getController());
                    joystickIds.remove(c);
                }
            }

            @Override
            public void controllerAdded(ControllerEvent ev) {
                Controller c = ev.getController();
                Controller.Type t = c.getType();
                if (t.equals(Controller.Type.STICK) || t.equals(Controller.Type.GAMEPAD)) {
                    joystickIds.put(c, joystickIds.size());
                    joysticks.add(ev.getController());
                    fireJoystickAdded(c);
                }
            }
        };
        controllerEnvironment.addControllerListener(controllerListener);

        getCommunicationManager().setJoystickUpdater(robotJoysticks -> {
            for (int j = 0; j < Math.min(joysticks.size(), robotJoysticks.size()); j++) {
                Controller dsJoystick = joysticks.get(j);
                Joystick robotJoystick = robotJoysticks.get(j);

                if (dsJoystick.poll()) {

                    Component[] components = dsJoystick.getComponents();

                    int axisIndex = 1;
                    int buttonIndex = 1;
                    for (Component c : components) {
                        if (c.isAnalog() && axisIndex <= Joystick.NUM_AXES) {
                            robotJoystick.setAxis(axisIndex++, c.getPollData());
                        } else if (buttonIndex <= Joystick.NUM_BUTTONS) {
                            robotJoystick.setButton(buttonIndex++, c.getPollData() == 1.0f);
                        }
                    }
                } else {
                    fireJoystickRemoved(dsJoystick);
                    joysticks.remove(j);
                }
            }
        });
    }

    private void initializeJoysticks() {
        Controller[] controllers = controllerEnvironment.getControllers();
        for (int i = 0; i < controllers.length; i++) {
            Controller j = controllers[i];
            Controller.Type t = j.getType();
            if (t == Controller.Type.GAMEPAD || t == Controller.Type.STICK) {
                joystickIds.put(j, i);
                joysticks.add(j);
            }
        }
        getPreferencesManager().setJoystickPositions(joystickPositions);
        joysticks.stream().forEach(this::fireJoystickAdded);
    }

    public List<Controller> getJoysticks() {
        return joysticks;
    }

    @SuppressWarnings("CallToThreadRun")
    public boolean rescan() {
        joysticks.clear();
        joystickIds.clear();
        joystickPositions.clear();
        joysticks.stream().forEach(this::fireJoystickRemoved);
        controllerEnvironment.removeControllerListener(controllerListener);

        Collection<Thread> shutdownHooks = ControllerEnvironmentHack.getShutdownHooks();
        if (shutdownHooks != null) {
            Class linuxEnvClass = ControllerEnvironmentHack.getLinuxEnvironmentShutdownHookClass();
            if (linuxEnvClass != null) {
                Thread linuxEnvHook = null;
                for (Thread hook : shutdownHooks) {
                    if (linuxEnvClass.isAssignableFrom(hook.getClass())) {
                        linuxEnvHook = hook;
                    }
                }
                if (linuxEnvHook != null) {
                    linuxEnvHook.run();
                    Runtime.getRuntime().removeShutdownHook(linuxEnvHook);
                }
            }
        }

        ControllerEnvironment ce = ControllerEnvironmentHack.newDefaultControllerEnvironment();
        if (ce == null) {
            return false;
        }

        ce.addControllerListener(controllerListener);
        ControllerEnvironmentHack.setDefaultEnvironment(ce);

        controllerEnvironment = ce;
        initializeJoysticks();

        return true;
    }

    public void addJoystickListener(JoystickListener jl) {
        joystickListeners.add(JoystickListener.class, jl);
    }

    public void removeJoystickListener(JoystickListener jl) {
        joystickListeners.remove(JoystickListener.class, jl);
    }

    private int getJoystickId(Controller j) {
        return joystickIds.get(j);
    }

    private void fireJoystickAdded(Controller c) {
        for (JoystickListener jl : joystickListeners.getListeners(JoystickListener.class)) {
            jl.joystickAdded(c);
        }
    }

    private void fireJoystickRemoved(Controller c) {
        for (JoystickListener jl : joystickListeners.getListeners(JoystickListener.class)) {
            jl.joystickRemoved(c);
        }
    }

    /**
     * Helper class for rescanning for joysticks. It makes heavy use of
     * reflection and dubious code to reinitialize JInput. If the faintest hint
     * of a {@link SecurityManager} is in place this will fail.
     */
    @SuppressWarnings("StaticNonFinalUsedInInitialization")
    private static class ControllerEnvironmentHack {

        private static final Logger logger = Logger.getLogger(ControllerEnvironmentHack.class.getName());

        private static Field defaultEnvironment;
        private static Constructor<ControllerEnvironment> defaultControllerEnvironmentConstructor;
        private static Field shutdownHooks;
        private static Class linuxEnvironmentShutdownHookClass;

        static {
            try {
                defaultEnvironment = ControllerEnvironment.class.getDeclaredField("defaultEnvironment");
                defaultEnvironment.setAccessible(true);

                Class defaultControllerEnvironmentClass = Class.forName("net.java.games.input.DefaultControllerEnvironment");
                defaultControllerEnvironmentConstructor
                        = (Constructor<ControllerEnvironment>) defaultControllerEnvironmentClass.getDeclaredConstructors()[0];
                defaultControllerEnvironmentConstructor.setAccessible(true);

                try {
                    Class applicationShutdownHooksClass = Class.forName("java.lang.ApplicationShutdownHooks");
                    shutdownHooks = applicationShutdownHooksClass.getDeclaredField("hooks");
                    shutdownHooks.setAccessible(true);

                    linuxEnvironmentShutdownHookClass = Class.forName("net.java.games.input.LinuxEnvironmentPlugin$ShutdownHook");
                } catch (NoSuchFieldException | SecurityException | ClassNotFoundException ex) {
                    logger.log(Level.WARNING, "Unable to access shutdown hooks, the appliation may close slowly or misbehave on Linux:", ex);
                }
            } catch (NoSuchFieldException | SecurityException | ClassNotFoundException ex) {
                logger.log(Level.WARNING, "Unable to enable joystick reloading support:", ex);
            }
        }

        public static void setDefaultEnvironment(ControllerEnvironment ce) {
            try {
                defaultEnvironment.set(null, ce);
            } catch (IllegalArgumentException | NullPointerException | IllegalAccessException ex) {
                logger.log(Level.WARNING, "Unable to reinitialize ControllerEnvironment:", ex);
            }
        }

        public static ControllerEnvironment newDefaultControllerEnvironment() {
            try {
                return defaultControllerEnvironmentConstructor.newInstance();
            } catch (InstantiationException | NullPointerException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                logger.log(Level.WARNING, "Unable to create new DefaultControllerEnvironment:", ex);
            }
            return null;
        }

        public static Collection<Thread> getShutdownHooks() {
            try {
                return (Collection<Thread>) ((Map) shutdownHooks.get(null)).keySet();
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                logger.log(Level.WARNING, "Unable to get Java shutdown hooks:", ex);
            }
            return null;
        }

        public static Class getLinuxEnvironmentShutdownHookClass() {
            return linuxEnvironmentShutdownHookClass;
        }
    }
}
