package com.asahaf.javacron;

import java.time.YearMonth;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Schedule class represents a parsed crontab expression.
 *
 * <p>
 * The schedule class cannot be instantiated using a constructor, a Schedule
 * object can be obtain by using the static {@link create} method, which parses
 * a crontab expression and creates a Schedule object.
 *
 * @author Ahmed AlSahaf
 */
public class Schedule implements Comparable<Schedule> {

    private enum DaysAndDaysOfWeekRelation {
        INTERSECT, UNION
    }

    private final static CronFieldParser SECONDS_FIELD_PARSER = new CronFieldParser(CronFieldType.SECOND);
    private final static CronFieldParser MINUTES_FIELD_PARSER = new CronFieldParser(CronFieldType.MINUTE);
    private final static CronFieldParser HOURS_FIELD_PARSER = new CronFieldParser(CronFieldType.HOUR);
    private final static CronFieldParser DAYS_FIELD_PARSER = new CronFieldParser(CronFieldType.DAY);
    private final static CronFieldParser MONTHS_FIELD_PARSER = new CronFieldParser(CronFieldType.MONTH);
    private final static CronFieldParser DAY_OF_WEEK_FIELD_PARSER = new CronFieldParser(CronFieldType.DAY_OF_WEEK);

    private Schedule() {
    }

    private String expression;
    private boolean hasSecondsField;
    private DaysAndDaysOfWeekRelation daysAndDaysOfWeekRelation;
    private BitSet seconds;
    private BitSet minutes;
    private BitSet hours;
    private BitSet days;
    private BitSet months;
    private BitSet daysOfWeek;
    private BitSet daysOf5Weeks;

    /**
     * Parses crontab expression and create a Schedule object representing that
     * expression.
     *
     * The expression string can be 5 fields expression for minutes resolution.
     *
     * <pre>
     *  ┌───────────── minute (0 - 59)
     *  │ ┌───────────── hour (0 - 23)
     *  │ │ ┌───────────── day of the month (1 - 31)
     *  │ │ │ ┌───────────── month (1 - 12 or Jan/January - Dec/December)
     *  │ │ │ │ ┌───────────── day of the week (0 - 6 or Sun/Sunday - Sat/Saturday)
     *  │ │ │ │ │
     *  │ │ │ │ │
     *  │ │ │ │ │
     * "* * * * *"
     * </pre>
     *
     * or 6 fields expression for higher, seconds resolution.
     *
     * <pre>
     *  ┌───────────── second (0 - 59)
     *  │ ┌───────────── minute (0 - 59)
     *  │ │ ┌───────────── hour (0 - 23)
     *  │ │ │ ┌───────────── day of the month (1 - 31)
     *  │ │ │ │ ┌───────────── month (1 - 12 or Jan/January - Dec/December)
     *  │ │ │ │ │ ┌───────────── day of the week (0 - 6 or Sun/Sunday - Sat/Saturday)
     *  │ │ │ │ │ │
     *  │ │ │ │ │ │
     *  │ │ │ │ │ │
     * "* * * * * *"
     * </pre>
     *
     * @param expression a crontab expression string used to create Schedule.
     * @return Schedule object created based on the supplied crontab expression.
     * @throws InvalidExpressionException if the provided crontab expression is
     *                                    invalid. The crontab expression is
     *                                    considered invalid if it is not properly
     *                                    formed, like empty string or contains less
     *                                    than 5 fields or more than 6 field. It's
     *                                    also invalid if the values in a field are
     *                                    beyond the allowed values range of that
     *                                    field. Non-occurring schedules like "0 0
     *                                    30 2 *" is considered invalid too, as Feb
     *                                    never has 30 days and a schedule like this
     *                                    never occurs.
     */
    public static Schedule create(String expression) throws InvalidExpressionException {
        if (expression.isEmpty()) {
            throw new InvalidExpressionException("empty expression");
        }
        String[] fields = expression.trim().toLowerCase().split("\\s+");
        int count = fields.length;
        if (count > 6 || count < 5) {
            throw new InvalidExpressionException(
                    "crontab expression should have 6 fields for (seconds resolution) or 5 fields for (minutes resolution)");
        }
        Schedule schedule = new Schedule();
        schedule.hasSecondsField = count == 6;
        String token;
        int index = 0;
        if (schedule.hasSecondsField) {
            token = fields[index++];
            schedule.seconds = Schedule.SECONDS_FIELD_PARSER.parse(token);
        } else {
            schedule.seconds = new BitSet(1);
            schedule.seconds.set(0);
        }
        token = fields[index++];
        schedule.minutes = Schedule.MINUTES_FIELD_PARSER.parse(token);

        token = fields[index++];
        schedule.hours = Schedule.HOURS_FIELD_PARSER.parse(token);

        token = fields[index++];
        schedule.days = Schedule.DAYS_FIELD_PARSER.parse(token);
        boolean daysStartWithAsterisk = false;
        if (token.startsWith("*"))
            daysStartWithAsterisk = true;

        token = fields[index++];
        schedule.months = Schedule.MONTHS_FIELD_PARSER.parse(token);

        token = fields[index++];
        schedule.daysOfWeek = Schedule.DAY_OF_WEEK_FIELD_PARSER.parse(token);
        boolean daysOfWeekStartAsterisk = false;
        if (token.startsWith("*"))
            daysOfWeekStartAsterisk = true;
        schedule.daysOf5Weeks = generateDaysOf5Weeks(schedule.daysOfWeek);

        schedule.daysAndDaysOfWeekRelation = (daysStartWithAsterisk || daysOfWeekStartAsterisk)
                ? DaysAndDaysOfWeekRelation.INTERSECT
                : DaysAndDaysOfWeekRelation.UNION;

        if (!schedule.canScheduleActuallyOccur())
            throw new InvalidExpressionException(
                    "schedule can not occur. the specified months do not have the day 30th or the day 31st");
        schedule.expression = expression.trim();
        return schedule;
    }

    /**
     * Calculates the next occurrence based on the current time.
     *
     * @return Date object of the next occurrence.
     */
    public Date next() {
        return this.next(new Date());
    }

    /**
     * Calculates the next occurrence based on provided base time.
     *
     * @param baseDate Date object based on which calculating the next occurrence.
     * @return Date object of the next occurrence.
     */
    public Date next(Date baseDate) {
        int baseSecond = baseDate.getSeconds();
        int baseMinute = baseDate.getMinutes();
        int baseHour = baseDate.getHours();
        int baseDay = baseDate.getDate();
        int baseMonth = baseDate.getMonth();
        int baseYear = baseDate.getYear();

        int second = baseSecond;
        int minute = baseMinute;
        int hour = baseHour;
        int day = baseDay;
        int month = baseMonth;
        int year = baseYear;

        if (this.hasSecondsField) {
            second++;
            second = this.seconds.nextSetBit(second);
            if (second < 0) {
                second = this.seconds.nextSetBit(0);
                minute++;
            }
        } else {
            minute++;
        }

        minute = this.minutes.nextSetBit(minute);
        if (minute < 0) {
            hour++;
            second = this.seconds.nextSetBit(0);
            minute = this.minutes.nextSetBit(0);
        } else if (minute > baseMinute) {
            second = this.seconds.nextSetBit(0);
        }

        hour = this.hours.nextSetBit(hour);
        if (hour < 0) {
            day++;
            second = this.seconds.nextSetBit(0);
            minute = this.minutes.nextSetBit(0);
            hour = this.hours.nextSetBit(0);
        } else if (hour > baseHour) {
            second = this.seconds.nextSetBit(0);
            minute = this.minutes.nextSetBit(0);
        }

        int candidateDay;
        int candidateMonth;
        while (true) {
            candidateMonth = this.months.nextSetBit(month);
            if (candidateMonth < 0) {
                year++;
                second = this.seconds.nextSetBit(0);
                minute = this.minutes.nextSetBit(0);
                hour = this.hours.nextSetBit(0);
                day = 1;
                candidateMonth = this.months.nextSetBit(0);
            } else if (candidateMonth > month) {
                second = this.seconds.nextSetBit(0);
                minute = this.minutes.nextSetBit(0);
                hour = this.hours.nextSetBit(0);
                day = 1;
            }
            month = candidateMonth;
            BitSet adjustedDaysSet = getUpdatedDays(year, month);
            candidateDay = adjustedDaysSet.nextSetBit(day - 1) + 1;
            if (candidateDay < 1) {
                month++;
                second = this.seconds.nextSetBit(0);
                minute = this.minutes.nextSetBit(0);
                hour = this.hours.nextSetBit(0);
                day = 1;
                continue;
            } else if (candidateDay > day) {
                second = this.seconds.nextSetBit(0);
                minute = this.minutes.nextSetBit(0);
                hour = this.hours.nextSetBit(0);
            }
            day = candidateDay;
            return new Date(year, month, day, hour, minute, second);
        }
    }

    /**
     * Calculates the next N occurrences based on current time.
     *
     * @param count number of next occurrences to calculate.
     * @return Array of Date objects of the next N occurrences.
     */
    public Date[] next(int count) {
        Date[] dates = new Date[count];
        Date baseDate = new Date();
        for (int i = 0; i < dates.length; i++) {
            baseDate = this.next(baseDate);
            dates[i] = baseDate;
        }
        return dates;
    }

    /**
     * Calculates the next N occurrences based on provided base time.
     *
     * @param baseDate Date object based on which calculating the next occurrence.
     * @param count    number of next occurrences to calculate.
     * @return Array of Date objects of the next N occurrences.
     */
    public Date[] next(Date baseDate, int count) {
        Date[] dates = new Date[count];
        for (int i = 0; i < dates.length; i++) {
            baseDate = this.next(baseDate);
            dates[i] = baseDate;
        }
        return dates;
    }

    /**
     * Calculates the number of time units from the current time to the next
     * occurrence.
     *
     * @param timeUnit time unit in which the returned value should be.
     * @return number of time units from the current time to the next occurrence.
     */
    public long nextDuration(TimeUnit timeUnit) {
        Date baseDate = new Date();
        long diff = this.next(baseDate).getTime() - baseDate.getTime();
        return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
    }

    /**
     * Calculates the number of time units from the provided base time to the next
     * occurrence.
     *
     * @param baseDate Date object based on which calculating the next occurrence.
     * @param timeUnit time unit in which the returned value should be.
     * @return number of time units from the provided base time to the next
     *         occurrence.
     */
    public long nextDuration(Date baseDate, TimeUnit timeUnit) {
        long diff = this.next(baseDate).getTime() - baseDate.getTime();
        return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
    }

    /**
     * Compare two {@code Schedule} objects based on next occurrence.
     *
     * The next occurrences are calculated based on the current time.
     *
     * @param anotherSchedule the {@code Schedule} to be compared.
     * @return the value {@code 0} if this {@code Schedule} next occurrence is equal
     *         to the argument {@code Schedule} next occurrence; a value less than
     *         {@code 0} if this {@code Schedule} next occurrence is before the
     *         argument {@code Schedule} next occurrence; and a value greater than
     *         {@code 0} if this {@code Schedule} next occurrence is after the
     *         argument {@code Schedule} next occurrence.
     */
    @Override
    public int compareTo(Schedule anotherSchedule) {
        if (anotherSchedule == this) {
            return 0;
        }

        Date baseDate = new Date();
        Long anotherDuration = Long.valueOf(anotherSchedule.nextDuration(baseDate, TimeUnit.SECONDS));
        Long thisDuration = Long.valueOf(this.nextDuration(baseDate, TimeUnit.SECONDS));
        return thisDuration.compareTo(anotherDuration);
    }

    /**
     * Compares this object against the specified object. The result is {@code true}
     * if and only if the argument is not {@code null} and is a {@code Schedule}
     * object that whose seconds, minutes, hours, days, months, and days of
     * weeks sets are equal to those of this schedule.
     * 
     * The expression string used to create the schedule is not considered, as two
     * different expressions may produce same schedules.
     *
     * @param obj the object to compare with
     * @return {@code true} if the objects are the same; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Schedule))
            return false;
        if (this == obj)
            return true;

        Schedule schedule = (Schedule) obj;
        return this.seconds.equals(schedule.seconds) && this.minutes.equals(schedule.minutes)
                && this.hours.equals(schedule.hours) && this.days.equals(schedule.days)
                && this.months.equals(schedule.months) && this.daysOfWeek.equals(schedule.daysOfWeek);
    }

    public static boolean isLeapYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }

    public int getNumberOfFields() {
        return hasSecondsField ? 6 : 5;
    }

    public String getExpression() {
        return expression;
    }

    private boolean canScheduleActuallyOccur() {
        if (this.daysAndDaysOfWeekRelation == DaysAndDaysOfWeekRelation.UNION || this.days.nextSetBit(0) < 29)
            return true;

        int aYear = new Date().getYear();
        for (int dayIndex = 29; dayIndex < 31; dayIndex++) {
            if (!this.days.get(dayIndex))
                continue;

            for (int monthIndex = 0; monthIndex < 12; monthIndex++) {
                if (!this.months.get(monthIndex))
                    continue;

                if (dayIndex + 1 <= YearMonth.of(aYear, monthIndex + 1).lengthOfMonth())
                    return true;
            }
        }
        return false;
    }

    private static BitSet generateDaysOf5Weeks(BitSet daysOfWeek) {
        int weekLength = 7;
        int setLength = weekLength + 31;
        BitSet bitSet = new BitSet(setLength);
        for (int i = 0; i < setLength; i += weekLength) {
            for (int j = 0; j < weekLength; j++) {
                bitSet.set(j + i, daysOfWeek.get(j));
            }
        }
        return bitSet;
    }

    private BitSet getUpdatedDays(int year, int month) {
        Date date = new Date(year, month, 1);
        int daysOf5WeeksOffset = date.getDay();
        BitSet updatedDays = new BitSet(31);
        updatedDays.or(this.days);
        BitSet monthDaysOfWeeks = this.daysOf5Weeks.get(daysOf5WeeksOffset, daysOf5WeeksOffset + 31);
        if (this.daysAndDaysOfWeekRelation == DaysAndDaysOfWeekRelation.INTERSECT) {
            updatedDays.and(monthDaysOfWeeks);
        } else {
            updatedDays.or(monthDaysOfWeeks);
        }
        int i;
        if (month == 1 /* Feb */) {
            i = 28;
            if (isLeapYear(year)) {
                i++;
            }
        } else {
            // We cannot use lengthOfMonth method with the month Feb
            // because it returns incorrect number of days for years
            // that are dividable by 400 like the year 2000, a bug??
            i = YearMonth.of(year, month + 1).lengthOfMonth();
        }
        // remove days beyond month length
        for (; i < 31; i++) {
            updatedDays.set(i, false);
        }
        return updatedDays;
    }
}
