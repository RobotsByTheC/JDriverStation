package org.usfirst.frc2084.jdriverstation.communication;

//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.net.SocketException;
//import java.net.UnknownHostException;

public class PacketSender extends Thread {

//    private int packetIndex;
//    private DatagramSocket sock;
//    private OutboundPacket cRioPacket = new OutboundPacket();
//    private DatagramPacket packet;
//    private boolean isRobotNet = true;

//    public PacketSender() {
//        byte[] dsAddress = InetAddress.getLocalHost().getAddress();
//
//        //Set up the datagram packet.
//        InetAddress crioAddr;
//        try {
//            crioAddr = InetAddress.getByAddress(new byte[]{10, dsAddress[2], dsAddress[3], 2});
//            packet = new DatagramPacket(cRioPacket.data, 1024, crioAddr, 1110);
//            if (dsAddress[0] != 10 && dsAddress[1] <= 99 && dsAddress[3] <= 99) {
//                isRobotNet = false;
//            }
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//
//        //Make a socket
//        try {
//            sock = new DatagramSocket();
//        } catch (SocketException e) {
//        }
//
//        //Set up static fields in the packet.
//        cRioPacket.setAlliance('R');
//        cRioPacket.setPosition(0x36);
//        int teamNum = Integer.parseInt("" + dsAddress[1] + dsAddress[2]);
//        cRioPacket.setTeam(teamNum);
//
//    }
//
//    //Thread function.
//    @Override
//    public void run() {
//        //Set all of the fields in the packet and send it, then wait for 20 ms.
//        try {
//            while (isRobotNet) {
//                for (int i = 0; i < 8; i++) {
//                    cRioPacket.setButton(i, ui.buttons[i]);
//                    cRioPacket.setDigitalIn(i, true);
//                }
//                cRioPacket.setIndex(packetIndex);
//                cRioPacket.setJoystick(ui.joy1X, ui.joy1Y, OutboundPacket.JOY1);
//                cRioPacket.setAuto(ui.auto);
//                cRioPacket.setEnabled(ui.enabled);
//                cRioPacket.makeCRC();
//                packet.setData(cRioPacket.data);
//                sock.send(packet);
//                cRioPacket.clearCRC();  //Took me a while to find this bug
//                //have to clear the CRC otherwise the
//                //next CRC generated will take the
//                //previous one into account.
//                packetIndex++;
//                Thread.sleep(20);
//            }
//        } catch (IOException e) {
//        } catch (InterruptedException e) {
//        }
//
//    }
}
