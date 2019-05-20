package com.asahaf.javacron;

import java.time.YearMonth;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Schedule {

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

    public static Schedule create(String expression) throws InvalidExpressionException {
        if (expression.isEmpty() || expression.isBlank()) {
            throw new InvalidExpressionException("empty expression");
        }
        String[] fields = expression.trim().toLowerCase().split("\\s+");
        int count = fields.length;
        if (count > 6 || count < 5) {
            throw new InvalidExpressionException(
                    "cron expression should have 6 fields for (seconds resolution) or 5 fields for (minutes resolution)");
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

    public Date next() {
        return this.next(new Date());
    }

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

    public Date[] next(int count) {
        Date[] dates = new Date[count];
        Date baseDate = new Date();
        for (int i = 0; i < dates.length; i++) {
            baseDate = this.next(baseDate);
            dates[i] = baseDate;
        }
        return dates;
    }

    public Date[] next(Date baseDate, int count) {
        Date[] dates = new Date[count];
        for (int i = 0; i < dates.length; i++) {
            baseDate = this.next(baseDate);
            dates[i] = baseDate;
        }
        return dates;
    }

    public long nextDuration(TimeUnit timeUnit) {
        Date baseDate = new Date();
        long diff = this.next(baseDate).getTime() - baseDate.getTime();
        return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
    }

    public long nextDuration(Date baseDate, TimeUnit timeUnit) {
        long diff = this.next(baseDate).getTime() - baseDate.getTime();
        return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
    }
}
