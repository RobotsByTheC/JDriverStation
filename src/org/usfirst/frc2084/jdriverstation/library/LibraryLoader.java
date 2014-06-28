package org.usfirst.frc2084.jdriverstation.library;

import java.io.*;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibraryLoader {

    private static final Logger logger = Logger.getLogger(LibraryLoader.class.getName());

    public static final String LIB_DIR = "./lib";
    public static final String JCOMMANDER_DIR = LIB_DIR + "/jcommander";

    private static String libPath = System.getProperty("java.library.path");
    private static URLClassLoader systemClassLoader;
    private static Method addURL;

    public static void loadJCommander() {
        loadLibraries(new File(JCOMMANDER_DIR));
    }

    public static void loadLibraries() {
        logger.log(Level.INFO, "Loading libraries...");
        loadLibraries(new File(LIB_DIR));
        setLibraryPath(libPath);
    }

    /**
     * Hack to set {@code java.library.path} to the specified value. It is not
     * good code, but it works better than anything else I have tried.
     *
     * @param path the new {@code java.library.path} value
     */
    private static void setLibraryPath(String path) {
        try {
            System.setProperty("java.library.path", path);

            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            logger.severe("Cannot load native libraries.");
        }
    }

    private static void loadJar(File file) {
        if (addURL == null || systemClassLoader == null) {
            systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            
            Method addURLTemp = null;

            try {
                addURLTemp = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addURLTemp.setAccessible(true);
            } catch (NoSuchMethodException | SecurityException e) {
                logger.log(Level.SEVERE, "Unable to load libraries:", e);
            }
            addURL = addURLTemp;
        }

        logger.log(Level.INFO, MessageFormat.format("Adding jar: {0}", file.getName()));
        try {
            addURL.invoke(systemClassLoader, file.toURI().toURL());
        } catch (MalformedURLException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.log(Level.SEVERE, "Could not load jar.");
        }
    }

    private static void loadLibraries(File directory) {

        if (directory.exists() && directory.isDirectory()) {

            libPath += File.pathSeparator + directory.getAbsolutePath();
            logger.log(Level.INFO, MessageFormat.format("Adding Folder: {0}", directory));

            File[] files = directory.listFiles();

            for (File file : files) {
                if (file.isDirectory()) {
                    loadLibraries(file);
                } else {
                    String fileName = file.getName();
                    if (fileName.endsWith(".jar")) {
                        loadJar(file);
                    }

                }
            }
        }
    }
}
