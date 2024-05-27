package util;

import java.sql.Timestamp;
import java.time.*;

/**
 * This class is used to handle all time related methods for time conversions and time validation.
 */
public class TimeUtil {

    private static final ZoneId ET_ZONE = ZoneId.of("America/New_York");
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    private static final ZoneId LOCAL_ZONE = ZoneId.systemDefault();

    private static final ZonedDateTime BUSINESS_START_ET = LocalDate.now().atTime(8, 0).atZone(ET_ZONE);
    private static final ZonedDateTime BUSINESS_END_ET = LocalDate.now().atTime(22,0).atZone(ET_ZONE);

    /**
     * This method converts local date/time to EST.
     * @param localDateTime parameter used in conversion.
     * @return returns time in EST.
     */
    public static ZonedDateTime toEST(LocalDateTime localDateTime) {
        return localDateTime.atZone(LOCAL_ZONE).withZoneSameInstant(ET_ZONE);
    }

    /**
     * This method converts Timestamp in UTC to local time.
     * @param timestamp parameter used in conversion.
     * @return returns time in local time.
     */
    public static LocalDateTime timestampToLocal(Timestamp timestamp) {
        return timestamp.toLocalDateTime().atZone(UTC_ZONE).withZoneSameInstant(LOCAL_ZONE).toLocalDateTime();
    }

    /**
     * This method converts local time to Timestamp in UTC.
     * @param localDateTime parameter used in conversion.
     * @return returns Timestamp in UTC.
     */
    public static Timestamp localToTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime.atZone(LOCAL_ZONE).withZoneSameInstant(UTC_ZONE).toLocalDateTime());
    }

    /**
     * This method compares time to business hours for business hours validation.
     * @param start parameter for comparison of EST business hours.
     * @param end parameter for comparison of EST business hours.
     * @return returns boolean values for isStartWithinBusinessHours && isEndWithinBusinessHours.
     */
    public static boolean isWithinBusinessHours(ZonedDateTime start, ZonedDateTime end) {
        ZonedDateTime startET = toEST(start.toLocalDateTime());
        ZonedDateTime endET = toEST(end.toLocalDateTime());

        boolean isStartWithinBusinessHours = !startET.isBefore(BUSINESS_START_ET) && !startET.isAfter(BUSINESS_END_ET);
        boolean isEndWithinBusinessHours = !endET.isBefore(BUSINESS_START_ET) && !endET.isAfter(BUSINESS_END_ET);

        return isStartWithinBusinessHours && isEndWithinBusinessHours;
    }
}
