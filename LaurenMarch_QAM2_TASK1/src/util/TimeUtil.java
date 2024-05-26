package util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtil {

    private static final ZoneId ET_ZONE = ZoneId.of("America/New_York");
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    private static final ZoneId LOCAL_ZONE = ZoneId.systemDefault();

    private static final LocalTime BUSINESS_START_ET = LocalTime.of(8, 0);
    private static final LocalTime BUSINESS_END_ET = LocalTime.of(22, 0);

    public static ZonedDateTime toET(LocalDateTime localDateTime) {
        return localDateTime.atZone(LOCAL_ZONE).withZoneSameInstant(ET_ZONE);
    }

    public static ZonedDateTime toUTC(LocalDateTime localDateTime) {
        return localDateTime.atZone(LOCAL_ZONE).withZoneSameInstant(UTC_ZONE);
    }

    public static ZonedDateTime toLocal(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(LOCAL_ZONE);
    }

    public static boolean isWithinBusinessHours(ZonedDateTime start, ZonedDateTime end) {
        LocalTime startET = toET(start.toLocalDateTime()).toLocalTime();
        LocalTime endET = toET(end.toLocalDateTime()).toLocalTime();

        boolean isStartWithinBusinessHours = !startET.isBefore(BUSINESS_START_ET) && !startET.isAfter(BUSINESS_END_ET);
        boolean isEndWithinBusinessHours = !endET.isBefore(BUSINESS_START_ET) && !endET.isAfter(BUSINESS_END_ET);

        return isStartWithinBusinessHours && isEndWithinBusinessHours;
    }

    public static LocalDateTime fromUTCToLocal(LocalDateTime utcDateTime) {
        return utcDateTime.atZone(UTC_ZONE).withZoneSameInstant(LOCAL_ZONE).toLocalDateTime();
    }

    public static ZonedDateTime fromUTCToLocal(ZonedDateTime utcDateTime) {
        return utcDateTime.withZoneSameInstant(LOCAL_ZONE);
    }

    public static ZonedDateTime fromLocalToUTC(ZonedDateTime localDateTime) {
        return localDateTime.withZoneSameInstant(UTC_ZONE);
    }
}
