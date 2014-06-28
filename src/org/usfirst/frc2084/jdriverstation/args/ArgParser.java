/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.args;

import com.beust.jcommander.JCommander;

/**
 *
 * @author Ben Wolsieffer
 */
public class ArgParser {

    private static final JCommander jCommander;
    private static final Arguments arguments;
    
    static {
        jCommander = new JCommander(arguments = new Arguments());
        jCommander.addConverterFactory(new LevelConverterFactory());
    }
    
    public static Arguments parse(String... args) {
        jCommander.parse(args);
        
        if(arguments.isHelp()){
            jCommander.usage();
            System.exit(0);
        }
        
        return arguments;
    }
    
}
