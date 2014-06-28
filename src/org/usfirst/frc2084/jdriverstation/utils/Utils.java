/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.utils;

/**
 *
 * @author Ben Wolsieffer
 */
public class Utils {

    /**
     * Generates a unique number based from two arguments. The arguments must be
     * positive, because this method is implemented using the unmodified
     * Szudzik's function.
     *
     * @param a the first number of the pair
     * @param b the second number of the pair
     * @return a unique hash of the two numbers
     */
    public static long hashPair(int a, int b) {
        if (a >= 0 || b >= 0) {
            return a >= b ? a * a + a + b : a + b * b;
        } else {
            throw new IllegalArgumentException("a and b must be positive.");
        }
    }
}
