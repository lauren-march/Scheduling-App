package util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtil {

    private static final ZoneId ET_ZONE = ZoneId.of("America/New_York");
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    private static final LocalTime BUSINESS_START = LocalTime.of(8, 0);
    private static final LocalTime BUSINESS_END = LocalTime.of(22, 0);

    public static ZonedDateTime toET(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ET_ZONE);
    }

    public static ZonedDateTime toUTC(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(UTC_ZONE);
    }

    public static ZonedDateTime toLocal(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
    }

    public static boolean isWithinBusinessHours(LocalDateTime start, LocalDateTime end) {
        ZonedDateTime startET = toET(start);
        ZonedDateTime endET = toET(end);

        return !startET.toLocalTime().isBefore(BUSINESS_START) && !endET.toLocalTime().isAfter(BUSINESS_END);
    }

    public static LocalDateTime fromUTCToLocal(LocalDateTime utcDateTime) {
        return utcDateTime.atZone(UTC_ZONE).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime fromETToLocal(LocalDateTime etDateTime) {
        return etDateTime.atZone(ET_ZONE).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime fromLocalToET(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ET_ZONE).toLocalDateTime();
    }

    public static LocalDateTime fromLocalToUTC(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(UTC_ZONE).toLocalDateTime();
    }
}
