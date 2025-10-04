package cc.dreamcode.chat.utils;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeParser {

    // np. "1d2h30m10s"
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d+)([smhd])");

    public static long parseToMillis(String input) {
        Matcher matcher = TIME_PATTERN.matcher(input.toLowerCase());
        long millis = 0;

        while (matcher.find()) {
            long value = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "s": millis += TimeUnit.SECONDS.toMillis(value); break;
                case "m": millis += TimeUnit.MINUTES.toMillis(value); break;
                case "h": millis += TimeUnit.HOURS.toMillis(value); break;
                case "d": millis += TimeUnit.DAYS.toMillis(value); break;
            }
        }

        return millis;
    }
}
