package org.usfirst.frc2084.dslibrary;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.event.EventListenerList;

public class RobotCommunication {

    private final EventListenerList listeners = new EventListenerList();

    public static final int OUTBOUND_PORT = 1110;
    public static final int INBOUND_PORT = 1150;

    private final Timer communicationThread;

    private final Robot robot;
    private final DriverStation driverStation;

    private InetAddress robotAddress;
    private boolean connected;

    private final byte[] robotData = new byte[Robot.Data.PACKET_LENGTH];
    private final DatagramSocket outboundSocket;
    private final DatagramPacket outboundPacket;

    private final byte[] driverStationData = new byte[DriverStation.Data.PACKET_LENGTH];
    private final DatagramSocket inboundSocket;
    private final DatagramPacket inboundPacket;

    public RobotCommunication(Robot robot, InetAddress robotAddress, DriverStation driverStation) throws SocketException {
        this.robot = robot;
        this.robotAddress = robotAddress;
        this.driverStation = driverStation;

        outboundSocket = new DatagramSocket();
        outboundPacket = new DatagramPacket(robotData, robotData.length, robotAddress, OUTBOUND_PORT);

        inboundSocket = new DatagramSocket(INBOUND_PORT);
        inboundPacket = new DatagramPacket(driverStationData, driverStationData.length);

        communicationThread = new Timer("Communication Thread");
    }

    private class CommunicationTask extends TimerTask {

        @Override
        public void run() {
            firePreSend();
            robot.prepareData();
            robot.getData(robotData);

            try {
                outboundSocket.send(outboundPacket);
            } catch (IOException ex) {
            }
            firePostSend();
        }
    }

    public InetAddress getRobotAddress() {
        return robotAddress;
    }

    public void setRobotAddress(InetAddress robotAddress) {
        this.robotAddress = robotAddress;
        outboundPacket.setAddress(robotAddress);
    }
    
    public DriverStation getDriverStation() {
        return driverStation;
    }

    public boolean isConnected() {
        return connected;
    }

    public void start() {
        // Temporary until real connection detection is implemented.
        connected = true;
        fireRobotConnected();
        communicationThread.schedule(new CommunicationTask(), 0, 20);
    }

    public void stop() {
        communicationThread.cancel();
        outboundSocket.close();
    }

    public void addCommunicationListener(CommunicationListener cl) {
        listeners.add(CommunicationListener.class, cl);
    }

    public void removeCommunicationListener(CommunicationListener cl) {
        listeners.remove(CommunicationListener.class, cl);
    }

    private void firePreSend() {
        for (CommunicationListener cl : listeners.getListeners(CommunicationListener.class)) {
            cl.preSend();
        }
    }

    private void firePostSend() {
        for (CommunicationListener cl : listeners.getListeners(CommunicationListener.class)) {
            cl.postSend();
        }
    }

    private void firePreReceive() {
        for (CommunicationListener cl : listeners.getListeners(CommunicationListener.class)) {
            cl.preReceive();
        }
    }

    private void firePostReceive() {
        for (CommunicationListener cl : listeners.getListeners(CommunicationListener.class)) {
            cl.postReceive();
        }
    }
    
    public void addConnectionListener(ConnectionListener cl) {
        listeners.add(ConnectionListener.class, cl);
    }
    
    public void removeConnectionListener(ConnectionListener cl) {
        listeners.add(ConnectionListener.class, cl);
    }
    
    private void fireRobotConnected() {
        for (ConnectionListener cl : listeners.getListeners(ConnectionListener.class)) {
            cl.robotConnected();
        }
    }
    
    private void fireRobotDisconnected() {
        for (ConnectionListener cl : listeners.getListeners(ConnectionListener.class)) {
            cl.robotDisconnected();
        }
    }
}
