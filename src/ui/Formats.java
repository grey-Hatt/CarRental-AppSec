package ui;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/** Small formatting helpers shared by the screens. */
public final class Formats {

    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private Formats() {
    }

    /** "2026-09-19 17:42", or "-" when there is no date (for example a car that is not returned yet). */
    public static String dateTime(Timestamp timestamp) {
        return timestamp == null ? "-" : DATE_TIME.format(timestamp.toLocalDateTime());
    }

    /** "available" becomes "Available". */
    public static String capitalise(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return Character.toUpperCase(text.charAt(0)) + text.substring(1).toLowerCase();
    }
}
