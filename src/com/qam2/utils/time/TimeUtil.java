package com.qam2.utils.time;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Provides static utility methods to convert and format times and dates.
 * @author Alex Hanson
 */
public abstract class TimeUtil {

    /**
     * @param zt ZonedDateTime to be formatted.
     * @return A string in the format yyyy-MM-dd HH:mm:ss
     */
    public static String toDateTimeString(ZonedDateTime zt) {
        return zt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * @param zt ZonedDateTime to be formatted.
     * @return A string in the format yyyy-MM-dd HH:mm:ss z
     */
    public static String toDateTimeStringWithZone(ZonedDateTime zt) {
        return zt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));
    }

    /**
     * @param zt ZonedDateTime to be formatted.
     * @return String in the format hh:mm a
     */
    public static String toTimeString(ZonedDateTime zt) {
        return zt.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    /**
     * @param lt LocalTime to be formatted.
     * @return String in the format hh:mm a
     */
    public static String toTimeString(LocalTime lt) {
        return lt.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    /**
     * @param zt ZonedDateTime to be formatted.
     * @return String in the format MM/dd/yy hh:mm a
     */
    public static String toAppointmentTimeDisplay(ZonedDateTime zt) { return zt.format(DateTimeFormatter.ofPattern("MM/dd/yy  hh:mm a")); }

    /**
     * @param zt ZonedDateTime to be converted.
     * @param tz TimeZone to convert to.
     * @return ZonedDateTime instance converted to the given TimeZone.
     */
    public static ZonedDateTime convertTime(ZonedDateTime zt, TimeZone tz) {
        return ZonedDateTime.ofInstant(zt.toInstant(), tz.getID());
    }

    /**
     * @return ZonedDateTime representing the current date and time zoned to UTC.
     */
    public static ZonedDateTime getUTCDateTimeNow() {
        return ZonedDateTime.ofInstant(ZonedDateTime.now().toInstant(), TimeZone.UTC.getID());
    }

    /**
     * Converts String of the format [hh:mm a] to LocalTime object.
     * @param time The String to be converted.
     * @return LocalTime for the given String.
     */
    public static LocalTime parseLocalTime(String time) {

        String[] tokens = time.split(":");
        int hr = Integer.parseInt(tokens[0]);

        tokens = tokens[1].split(" ");

        int min = Integer.parseInt(tokens[0]);

        String timeOfDay = tokens[1];

        if (hr != 12 && timeOfDay.compareTo("PM") == 0) {
            hr += 12;
        }else if(hr == 12 && timeOfDay.compareTo("AM") == 0) {
            hr = 0;
        }

        return LocalTime.of(hr, min);
    }

    /**
     * Converts Strings of the format [yyyy-MM-dd HH:mm:ss] to a ZonedDatedTime zoned to UTC.
     * @param dateTimeString The String to be converted.
     * @return ZonedDateTime for the given String.
     */
    public static ZonedDateTime parseUTCZonedDateTime(String dateTimeString) {

        dateTimeString = dateTimeString.split("\\.")[0];
        String [] dateTime = dateTimeString.split(" ");
        String [] date = dateTime[0].split("-");
        String [] time = dateTime[1].split(":");

        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);

        int hour = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);
        int seconds = Integer.parseInt(time[2]);

        return ZonedDateTime.of(LocalDateTime.of(year, month, day, hour, minutes, seconds), TimeZone.UTC.getID());
    }
}
