package ir.berimbasket.app.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Mahdi on 7/15/2017.
 * everything needed with local time
 */

public class Time {

    private static final String DATE_DELIMITER = "-";
    private static final String TIME_DELIMITER = ":";
    private static final String DATE_TIME_DELIMITER = " ";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * @param timestamp 2017-09-02 15:50:37
     */
    public static String getLocalTimeStamp(String timestamp, boolean isJalali) {
        try {
            String localZoneTimestamp = convertUTCtoLocal(timestamp);
            Calendar gregorian = convertTimeToCalendar(localZoneTimestamp);
            JalaliCalendar jalali = gregorianToJalali(gregorian);
            if (isJalali) {
                return jalali.getDay() + " " + jalali.getMonthString() + " " + jalali.getYear() + " در " +
                        gregorian.get(Calendar.HOUR_OF_DAY) + ":" + gregorian.get(Calendar.MINUTE) + ":" + gregorian.get(Calendar.SECOND);
            } else {
                String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                return monthNames[gregorian.get(Calendar.MONTH)] + " " + gregorian.get(Calendar.DAY_OF_MONTH) + " " + gregorian.get(Calendar.YEAR) +
                        " at " + gregorian.get(Calendar.HOUR_OF_DAY) + ":" + gregorian.get(Calendar.MINUTE) + " " + gregorian.get(Calendar.SECOND);
            }
        } catch (Exception e) {
            return timestamp;
        }
    }

    private static String convertUTCtoLocal(String timestamp) {
        try {
            Calendar c = convertTimeToCalendar(timestamp);
            // convert server time to local timezone
            int timezoneOffsetMinutes = (int) (getLocalTimeZone() * 60);
            c.add(Calendar.MINUTE, timezoneOffsetMinutes);
            SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);
            return format.format(c.getTime());
        } catch (Exception e) {
            return timestamp;
        }
    }

    /**
     * @return Local TimeZone in hour (eg. 3.5 or -4.5)
     */
    private static double getLocalTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        return tz.getOffset(now.getTime()) / 3600000.0;
    }

    private static Calendar convertTimeToCalendar(String timestamp) {
        String[] rootParts = timestamp.split(DATE_TIME_DELIMITER);
        String date = rootParts[0];
        String time = rootParts[1];
        String[] dateParts = date.split(DATE_DELIMITER);
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);
        String[] clockParts = time.split(TIME_DELIMITER);
        int hour = Integer.parseInt(clockParts[0]);
        int minute = Integer.parseInt(clockParts[1]);
        int second = Integer.parseInt(clockParts[2]);
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day, hour, minute, second);  // month is 0 base (we make it 1 base to enable work with Jalali)
        return c;
    }

    private static JalaliCalendar gregorianToJalali(Calendar c) {
        return new JalaliCalendar(new GregorianCalendar(
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)));
    }
}
