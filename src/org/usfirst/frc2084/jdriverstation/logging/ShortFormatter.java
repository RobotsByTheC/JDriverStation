/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Ben Wolsieffer
 */
public class ShortFormatter extends SimpleFormatter {

    public static final String DEFAULT_CLASS = "JDriverStation";

    private final DateTimeFormatter dateFormat
            = DateTimeFormatter.ofPattern("M/d/yyyy hh:mm:ss.SSS a");

    @Override
    public String format(LogRecord record) {
        StringBuilder messageBuilder = new StringBuilder();
        Instant date = Instant.ofEpochMilli(record.getMillis());
        messageBuilder.append("[");
        messageBuilder.append(dateFormat.format(LocalDateTime.ofInstant(date, ZoneId.systemDefault())));
        messageBuilder.append("] ");
        String loggerName = record.getLoggerName();
        if (loggerName != null) {
            String[] nameSections = loggerName.split("\\.");
            if (nameSections.length > 0) {
                messageBuilder.append(nameSections[nameSections.length - 1]);
            } else {
                messageBuilder.append(DEFAULT_CLASS);
            }
        } else {
            messageBuilder.append(DEFAULT_CLASS);
        }
        messageBuilder.append(" - ");
        messageBuilder.append(record.getLevel().getLocalizedName());
        messageBuilder.append(": ");
        messageBuilder.append(formatMessage(record));
        messageBuilder.append("\n");
        @SuppressWarnings("ThrowableResultIgnored")
        Throwable thrown = record.getThrown();
        if (thrown != null) {
            StringWriter sw = new StringWriter();
            try (PrintWriter pw = new PrintWriter(sw)) {
                pw.println();
                thrown.printStackTrace(pw);
            }
            messageBuilder.append(sw.getBuffer());
        }

        return messageBuilder.toString();
    }

}
