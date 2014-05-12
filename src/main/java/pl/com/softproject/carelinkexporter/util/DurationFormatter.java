/*
 * Copyright 2014-05-11 the original author or authors.
 */
package pl.com.softproject.carelinkexporter.util;

import java.time.Duration;
import java.time.LocalTime;

/**
 *
 * @author Adrian Lapierre <adrian@softproject.com.pl>
 */
public class DurationFormatter {

    public static Duration parse(String text) {
        return Duration.between(LocalTime.MIDNIGHT, LocalTime.parse(text));
    }

    public static String format(Duration duration) {
        if(duration != null) {
            long hours = duration.toHours(); 
            long minutes = duration.minusHours(hours).toMinutes(); 
            return hours + "h " + minutes + "m"; 
        } else return null;
    }

}
