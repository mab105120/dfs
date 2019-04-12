package edu.grenoble.em.bourji;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

/**
 * Created by Moe on 4/9/19.
 */
public class TimeUtils {

    public static long hoursSince(Timestamp ts) {
        return Duration.between(fromTimeStamp(ts), utcNow()).toHours();
    }

    public static long minutesSince(Timestamp ts) {
        return Duration.between(fromTimeStamp(ts), utcNow()).toMinutes();
    }

    private static ZonedDateTime fromTimeStamp(Timestamp ts) {
        ZoneId utc = ZoneId.of("UTC");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(ts.getTime());
        return ZonedDateTime.ofInstant(c.toInstant(), utc);
    }

    private static ZonedDateTime utcNow() {
        return ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
    }
}
