package util;

import java.time.*;

/**
 * This class is used to handle all time-related methods for time conversions and time validation.
 */
public class TimeUtil {

    private static final ZoneId ET_ZONE = ZoneId.of("America/New_York");

    /**
     * This method gets the current local time zone from systemDefault.
     * @return the current local time zone.
     */
    public static ZoneId getLocalZone() {
        return ZoneId.systemDefault();
    }

    /**
     * This method converts local date/time to EST.
     * @param localDateTime parameter used in conversion.
     * @return returns time in EST.
     */
    public static ZonedDateTime toEST(LocalDateTime localDateTime) {
        return localDateTime.atZone(getLocalZone()).withZoneSameInstant(ET_ZONE);
    }

    /**
     * This method compares time to business hours for business hours validation.
     * @param start parameter for comparison of EST business hours.
     * @param end parameter for comparison of EST business hours.
     * @return returns boolean values for isStartWithinBusinessHours && isEndWithinBusinessHours.
     */
    public static boolean isWithinBusinessHours(ZonedDateTime start, ZonedDateTime end) {
        ZonedDateTime businessStartET = ZonedDateTime.of(start.toLocalDate(), LocalTime.of(8, 0), ET_ZONE);
        ZonedDateTime businessEndET = businessStartET.plusHours(14);

        ZonedDateTime startET = toEST(start.toLocalDateTime());
        ZonedDateTime endET = toEST(end.toLocalDateTime());

        boolean isStartWithinBusinessHours = !startET.isBefore(businessStartET) && !startET.isAfter(businessEndET);
        boolean isEndWithinBusinessHours = !endET.isBefore(businessStartET) && !endET.isAfter(businessEndET);

        return isStartWithinBusinessHours && isEndWithinBusinessHours;
    }
}
