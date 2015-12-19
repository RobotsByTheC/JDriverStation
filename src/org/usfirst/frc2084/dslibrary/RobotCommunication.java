package org.usfirst.frc2084.dslibrary;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

public class RobotCommunication {

    public static final int OUTBOUND_PORT = 1110;
    public static final int INBOUND_PORT = 1150;
    public static final int DISCONNECTION_TIMEOUT = 100;

    private static final Logger logger = Logger.getLogger(RobotCommunication.class.getName());

    private final EventListenerList listeners = new EventListenerList();

    private final Robot robot;
    private final DriverStation driverStation;

    private InetAddress robotAddress;
    private volatile boolean connected;

    private final byte[] robotData = new byte[Robot.Data.PACKET_LENGTH];
    private final DatagramSocket outboundSocket;
    private final DatagramPacket outboundPacket;
    private final Timer dataSendThread;

    private final byte[] driverStationData = new byte[DriverStation.Data.PACKET_LENGTH];
    private final DatagramSocket inboundSocket;
    private final DatagramPacket inboundPacket;
    private final Thread dataReceiveThread;

    public RobotCommunication(Robot robot, DriverStation driverStation, InetAddress robotAddress) throws SocketException {
        this.robot = robot;
        this.robotAddress = robotAddress;
        this.driverStation = driverStation;

        outboundSocket = new DatagramSocket();
        outboundPacket = new DatagramPacket(robotData, robotData.length, robotAddress, OUTBOUND_PORT);
        dataSendThread = new Timer("Data Send Thread", true);

        inboundSocket = new DatagramSocket(INBOUND_PORT);
        inboundSocket.setSoTimeout(DISCONNECTION_TIMEOUT);
        inboundPacket = new DatagramPacket(driverStationData, driverStationData.length);
        dataReceiveThread = new Thread(new DataReceiveTask(), "Data Recieve Thread");
        dataReceiveThread.setDaemon(true);
    }

    private class DataSendTask extends TimerTask {

        @Override
        public void run() {
            firePreSend();
            robot.prepareData();
            robot.getData(robotData);

            try {
                outboundSocket.send(outboundPacket);
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Unable to send data to robot:", ex);
            }
            firePostSend();
        }
    }

    private class DataReceiveTask extends Thread {

        @Override
        public void run() {
            for (;;) {
                try {
                    inboundSocket.receive(inboundPacket);
                    if (!connected) {
                        connected = true;
                        fireRobotConnected();
                    }
                    firePreReceive();
                    driverStation.setData(driverStationData);
                    firePostReceive();
                } catch (SocketTimeoutException ex) {
                    if (connected) {
                        connected = false;
                        fireRobotDisconnected();
                    }
                } catch (SocketException ex) {
                    if (inboundSocket.isClosed()) {
                        break;
                    } else {
                        logger.log(Level.WARNING, "Unable to receive data from robot:", ex);
                    }
                } catch (IOException ex) {
                    logger.log(Level.WARNING, "Unable to receive data from robot:", ex);
                }
            }
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
        dataReceiveThread.start();
        dataSendThread.schedule(new DataSendTask(), 0, 20);
    }

    public void stop() {
        dataSendThread.cancel();
        outboundSocket.close();

        inboundSocket.close();
    }

    public void addCommunicationListener(CommunicationListener cl) {
        listeners.add(CommunicationListener.class, cl);
    }

    public void removeCommunicationListener(CommunicationListener cl) {
        listeners.remove(CommunicationListener.class, cl);
    }

    private void firePreSend() {
        for (CommunicationListener cl : listeners.getListeners(CommunicationListener.class)) {
            try {
                cl.preSend();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Communication listener threw exception:", ex);
            }
        }
    }

    private void firePostSend() {
        for (CommunicationListener cl : listeners.getListeners(CommunicationListener.class)) {
            try {
                cl.postSend();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Communication listener threw exception:", ex);
            }
        }
    }

    private void firePreReceive() {
        for (CommunicationListener cl : listeners.getListeners(CommunicationListener.class)) {
            try {
                cl.preReceive();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Communication listener threw exception:", ex);
            }
        }
    }

    private void firePostReceive() {
        for (CommunicationListener cl : listeners.getListeners(CommunicationListener.class)) {
            try {
                cl.postReceive();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Communication listener threw exception:", ex);
            }
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
            try {
                cl.robotConnected();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Connection listener threw exception:", ex);
            }
        }
    }

    private void fireRobotDisconnected() {
        for (ConnectionListener cl : listeners.getListeners(ConnectionListener.class)) {
            try {
                cl.robotDisconnected();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Connection listener threw exception:", ex);
            }
        }
    }
}
