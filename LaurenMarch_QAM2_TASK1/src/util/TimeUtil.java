package util;

import java.sql.Timestamp;
import java.time.*;

/**
 * This class is used to handle all time-related methods for time conversions and time validation.
 */
public class TimeUtil {

    private static final ZoneId ET_ZONE = ZoneId.of("America/New_York");
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    /**
     * This method gets the current local time zone.
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
     * This method converts Timestamp in UTC to local time.
     * @param timestamp parameter used in conversion.
     * @return returns time in local time.
     */
    public static LocalDateTime timestampToLocal(Timestamp timestamp) {
        return timestamp.toLocalDateTime().atZone(UTC_ZONE).withZoneSameInstant(getLocalZone()).toLocalDateTime();
    }

    /**
     * This method converts local time to Timestamp in UTC.
     * @param localDateTime parameter used in conversion.
     * @return returns Timestamp in UTC.
     */
    public static Timestamp localToTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime.atZone(getLocalZone()).withZoneSameInstant(UTC_ZONE).toLocalDateTime());
    }

    /**
     * This method compares time to business hours for business hours validation.
     * @param start parameter for comparison of EST business hours.
     * @param end parameter for comparison of EST business hours.
     * @return returns boolean values for isStartWithinBusinessHours && isEndWithinBusinessHours.
     */
    public static boolean isWithinBusinessHours(LocalDateTime start, LocalDateTime end) {
        ZonedDateTime businessStartET = LocalDate.now().atTime(8, 0).atZone(ET_ZONE);
        ZonedDateTime businessEndET = LocalDate.now().atTime(22, 0).atZone(ET_ZONE);

        ZonedDateTime startET = toEST(start);
        ZonedDateTime endET = toEST(end);

        boolean isStartWithinBusinessHours = !startET.isBefore(businessStartET) && !startET.isAfter(businessEndET);
        boolean isEndWithinBusinessHours = !endET.isBefore(businessStartET) && !endET.isAfter(businessEndET);

        return isStartWithinBusinessHours && isEndWithinBusinessHours;
    }
}
