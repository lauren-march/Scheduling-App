package util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtil {

    private static final ZoneId ET_ZONE = ZoneId.of("America/New_York");
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    public static ZonedDateTime toET(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ET_ZONE);
    }

    public static ZonedDateTime toUTC(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(UTC_ZONE);
    }

    public static ZonedDateTime toLocal(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
    }
}
