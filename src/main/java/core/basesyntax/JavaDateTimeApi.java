package core.basesyntax;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class JavaDateTimeApi {
    public static final int NO_ELEMENT_ARRAY_LENGTH = 0;
    public static final int MIN_MONTH = 1;
    public static final int MAX_MONTH = 12;
    public static final int MAX_DAY_IN_MONTH = 31;
    public static final String TIME_ZONE = "+02:00";
    public static final int START_DAY_POSITION = 0;
    public static final int END_DAY_POSITION = 2;
    public static final int START_MONTH_POSITION = 4;
    public static final int END_MONTH_POSITION = 6;

    Locale locale = new Locale("en", "US");

    /**
     * Return the current date as a String depending on a query.
     * <p>
     * The query can be passed for the whole date or for it's part:
     * - FULL - current date as a whole: year, month, day of month
     * formatted as `YYYY-MM-DD` (a default return value);
     * - YEAR - current year;
     * - MONTH - name of the current month;
     * - DAY - current day of month;
     * In any other case throw DateTimeException.
     **/
    public String todayDate(DateTimePart datePart) {
        LocalDate date = LocalDate.now();
        switch (datePart) {
            case FULL:
                return String.valueOf(date);
            case YEAR:
                return String.valueOf(date.getYear());
            case MONTH:
                return String.valueOf(date.getMonth());
            case DAY:
                return String.valueOf(date.getDayOfMonth());
            default:
                throw new DateTimeException("Can't get date!");
        }
    }

    /**
     * Given an Array of 3 elements, where
     * - 1-st element is a `year`;
     * - 2-nd element is s `month`;
     * - 3-rd element is a `day of month`;
     * <p>
     * Return Optional of a date built from these elements.
     */
    public Optional<LocalDate> getDate(Integer[] dateParams) {
        try {
            return Optional.ofNullable(LocalDate.of(dateParams[0], dateParams[1], dateParams[2]));
        } catch (DateTimeException | ArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    /**
     * Given the time and the number of hours to add, return the changed time.
     */
    public LocalTime addHours(LocalTime localTime, Integer hoursToAdd) {
        return localTime.plusHours(hoursToAdd);
    }

    /**
     * Given the time and the number of minutes to add, return the changed time.
     */
    public LocalTime addMinutes(LocalTime localTime, Integer minutesToAdd) {
        return localTime.plusMinutes(minutesToAdd);
    }

    /**
     * Given the time and the number of seconds to add, return the changed time.
     */
    public LocalTime addSeconds(LocalTime localTime, Integer secondsToAdd) {
        return localTime.plusSeconds(secondsToAdd);
    }

    /**
     * Given the date and the number of weeks to add, return the changed date.
     */
    public LocalDate addWeeks(LocalDate localDate, Integer numberOfWeeks) {
        return localDate.plusWeeks(numberOfWeeks);
    }

    /**
     * Given a random `someDate` date, return one of the following Strings:
     * - "`someDate` is after `currentDate`"
     * if `someDate` is in the future relating to the `current date`;
     * - "`someDate` is before `currentDate`"
     * if `someDate` is in the past relating to the `current date`;
     * - "`someDate` is today"
     * if `someDate` is today;
     */
    public String beforeOrAfter(LocalDate someDate) {
        LocalDate currentDate = LocalDate.now();
        if (someDate.isAfter(currentDate)) {
            return someDate + " is after " + currentDate;
        } else if (someDate.isBefore(LocalDate.now())) {
            return someDate + " is before " + currentDate;
        } else {
            return someDate + " is today";
        }
    }

    /**
     * Given a String representation of a date and some timezone,
     * return LocalDateTime in this timezone.
     */
    public LocalDateTime getDateInSpecificTimeZone(String dateInString, String zone) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateInString);
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        ZoneId zoneId = ZoneId.of(zone);
        ZoneOffset zoneOffset = zoneId.getRules().getOffset(localDateTime);
        return LocalDateTime.ofInstant(Instant.parse(dateInString), zoneOffset);
    }

    /**
     * Given some LocalDateTime, return an OffsetDateTime with the local time offset applied
     * (`+02:00` for Ukraine).
     * <p>
     * Example: we receive a LocalDateTime with a value `2019-09-06T13:17`.
     * We should return the OffsetDateTime with a value `2019-09-06T13:17+02:00`,
     * where `+02:00` is the offset for our local timezone.
     * <p>
     * OffsetDateTime is recommended to use for storing date values in a database.
     */
    public OffsetDateTime offsetDateTime(LocalDateTime localTime) {
        return localTime.atOffset(ZoneOffset.of(TIME_ZONE));
    }

    /**
     * Given a String formatted as `yyyymmdd`,
     * return Optional of this date as a LocalDate.
     */
    public Optional<LocalDate> parseDate(String date) {
        return (Integer.parseInt(date.substring(START_MONTH_POSITION, END_MONTH_POSITION))
                > MAX_MONTH) ? Optional.empty()
                : Optional.ofNullable(LocalDate.from(DateTimeFormatter.BASIC_ISO_DATE.parse(date)));
    }

    /**
     * Given a String formatted as `d MMM yyyy` (MMM - Sep, Oct, etc),
     * return Optional of this date as a LocalDate.
     */
    public Optional<LocalDate> customParseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", locale);
        return (Integer.parseInt(date.substring(START_DAY_POSITION, END_DAY_POSITION))
                > MAX_DAY_IN_MONTH) ? Optional.empty()
                : Optional.ofNullable(LocalDate.from(formatter.parse(date)));
    }

    /**
     * Given some LocalDateTime, return a String formatted as
     * `day(2-digit) month(full name in English) year(4-digit) hours(24-hour format):minutes`.
     * <p>
     * Example: "01 January 2000 18:00".
     */
    public String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", locale);
        return dateTime.format(formatter);
    }
}
