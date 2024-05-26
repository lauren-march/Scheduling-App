package util;

import java.sql.Timestamp;
import java.time.*;

public class TimeUtil {

//    private static final ZoneId ET_ZONE = ZoneId.of("America/New_York");
//    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
//    private static final ZoneId LOCAL_ZONE = ZoneId.systemDefault();
//
//    private static final ZonedDateTime BUSINESS_START_ET = LocalDate.now().atTime(8, 0).atZone(ET_ZONE);
//    private static final ZonedDateTime BUSINESS_END_ET = LocalDate.now().atTime(22,0).atZone(ET_ZONE);
//
//    // Convert Local time to EST
//    public static ZonedDateTime toET(LocalDateTime localDateTime) {
//        return localDateTime.atZone(LOCAL_ZONE).withZoneSameInstant(ET_ZONE);
//    }
//
//    // Convert Local time to UTC
//    public static ZonedDateTime toUTC(LocalDateTime localDateTime) {
//        return localDateTime.atZone(LOCAL_ZONE).withZoneSameInstant(UTC_ZONE);
//    }
//
//    // Convert UTC to Local time
//    public static LocalDateTime fromUTCToLocal(LocalDateTime utcDateTime) {
//        return utcDateTime.atZone(UTC_ZONE).withZoneSameInstant(LOCAL_ZONE).toLocalDateTime();
//    }
private static final ZoneId ET_ZONE = ZoneId.of("America/New_York");
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    private static final ZoneId LOCAL_ZONE = ZoneId.systemDefault();

    private static final ZonedDateTime BUSINESS_START_ET = LocalDate.now().atTime(8, 0).atZone(ET_ZONE);
    private static final ZonedDateTime BUSINESS_END_ET = LocalDate.now().atTime(22,0).atZone(ET_ZONE);

    // Get the current time in UTC
    public static LocalDateTime utcNow() {
        return ZonedDateTime.now(UTC_ZONE).toLocalDateTime();
    }

    // Convert LocalDateTime to EST
    public static ZonedDateTime toET(LocalDateTime localDateTime) {
        return localDateTime.atZone(LOCAL_ZONE).withZoneSameInstant(ET_ZONE);
    }

    // Convert LocalDateTime to UTC
    public static ZonedDateTime toUTC(LocalDateTime localDateTime) {
        return localDateTime.atZone(LOCAL_ZONE).withZoneSameInstant(UTC_ZONE);
    }

    // Convert UTC to LocalDateTime
    public static LocalDateTime fromUTCToLocal(LocalDateTime utcDateTime) {
        return utcDateTime.atZone(UTC_ZONE).withZoneSameInstant(LOCAL_ZONE).toLocalDateTime();
    }

    // Convert Timestamp to LocalDateTime
    public static LocalDateTime timestampToLocal(Timestamp timestamp) {
        return timestamp.toLocalDateTime().atZone(UTC_ZONE).withZoneSameInstant(LOCAL_ZONE).toLocalDateTime();
    }

    // Convert LocalDateTime to Timestamp
    public static Timestamp localToTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime.atZone(LOCAL_ZONE).withZoneSameInstant(UTC_ZONE).toLocalDateTime());
    }

    public static boolean isWithinBusinessHours(ZonedDateTime start, ZonedDateTime end) {
        ZonedDateTime startET = toET(start.toLocalDateTime());
        ZonedDateTime endET = toET(end.toLocalDateTime());

        boolean isStartWithinBusinessHours = !startET.isBefore(BUSINESS_START_ET) && !startET.isAfter(BUSINESS_END_ET);
        boolean isEndWithinBusinessHours = !endET.isBefore(BUSINESS_START_ET) && !endET.isAfter(BUSINESS_END_ET);

        return isStartWithinBusinessHours && isEndWithinBusinessHours;
    }

}
