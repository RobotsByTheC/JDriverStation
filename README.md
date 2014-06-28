# JDriverStation
***

### A cross platform driver station for FRC robots, written in Java.

**Warning:** This software has the ability to control a potentially dangerous robot, so please use caution and common sense. While I try to make it as safe as possible, I take no responsilibity for damage or injury caused by using this software.

**Notice:** This software is in the first stages of development and therefore has many bugs and few features. As of this writing, only one way robot communication is implemented and the GUI is incomplete. Feel free to try it out and see what I am working on, or help me implement new features and fix bugs, but do not expect a stable product yet.

### Purpose
This project was created in order to allow for the control of an FRC robot from a computer running Mac OS, GNU/Linux or Windows and also to document the communication protocol used for controlling and communicating with the robot. I plan to develop a standalone library to control FRC robots for use in other applications and to provide documentation for the protocol.

**If you're looking for a driver station for Android, look at raystubbs' [Android Driverstation](https://github.com/raystubbs/Andrid-FRC-Driverstation). His work deciphering the ds->robot protocol saved me a lot of time.**

#### Current Progress
* Similar (partially finished) GUI to the official FRC Driver Station.
* Fully functional (except for enhanced IO) Wireshark dissector for the ds->robot communication protocol. Located in `wireshark/frc_ds_dissector.lua`.
* Full control of the robot using joysticks (untested on a real robot, so use at your own risk).

#### Planned Features
* Robot battery voltage
* Virtual analog/digital IO
* LCD
* Charts
* Standalone robot communication library

#### Unlikely Features
* PC battery meter
* Network interface autoconfig
* Kinect
* FMS connection
* Cypress module support
* PC CPU usage chart
