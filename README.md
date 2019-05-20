[![Build Status](https://travis-ci.org/asahaf/javacron.svg?branch=master)](https://travis-ci.org/asahaf/javacron)

JavaCron
============

JavaCron is a java library which provides functionality for parsing crontab expression 
and calculating the next run, based on current or specified date time.

## Features
* Parsing crontab expression
* Calculating next run date time, based on current or specified time
* Calculating next run in number of milliseconds, seconds, or any specified TimeUnit
* Support 6 fields expressions for seconds resolution

Note: JavaCron doesn't provide a scheduling functionality. It only parses and calculates
next run(s).

### 5 fields expression
```
┌───────────── minute (0 - 59)
│ ┌───────────── hour (0 - 23)
│ │ ┌───────────── day of the month (1 - 31)
│ │ │ ┌───────────── month (1 - 12 or Jan/January - Dec/December)
│ │ │ │ ┌───────────── day of the week (0 - 6 or Sun/Sunday - Sat/Saturday)
│ │ │ │ │                                   
│ │ │ │ │
│ │ │ │ │
* * * * *
```

### 6 fields expression (including seconds)
```
┌───────────── second (0 - 59)
│ ┌───────────── minute (0 - 59)
│ │ ┌───────────── hour (0 - 23)
│ │ │ ┌───────────── day of the month (1 - 31)
│ │ │ │ ┌───────────── month (1 - 12 or Jan/January - Dec/December)
│ │ │ │ │ ┌───────────── day of the week (0 - 6 or Sun/Sunday - Sat/Saturday)
│ │ │ │ │ │                                   
│ │ │ │ │ │
│ │ │ │ │ │
* * * * * *
```

## Hello, world
```java
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.asahaf.javacron

public class App {
    public static void main(String[] args) {
        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("y-MM-dd HH:mm:ss");

            // base date 2019-01-01 04:04:02
            Date baseDate = dateFormatter.parse("2019-01-01 04:04:02");

            // evey minute
            Schedule schedule1 = Schedule.create("* * * * *");
            System.out.println(dateFormatter.format(schedule1.next(baseDate))); // 2019-01-01 04:05:00

            // evey second
            Schedule schedule2 = Schedule.create("* * * * * *");
            System.out.println(dateFormatter.format(schedule2.next(baseDate))); // 2019-01-01 04:04:03

            // at hour 1, 5, and 23 every day and evey month
            Schedule schedule3 = Schedule.create("0 0 1,5,23 * * *");
            System.out.println(dateFormatter.format(schedule3.next(baseDate))); // 2019-01-01 05:00:00

            // at 00:00:00 on 29th or Feb if it's on every 4th day-of-week.
            Schedule schedule4 = Schedule.create("0 0 0 29 2 */4");
            System.out.println(dateFormatter.format(schedule4.next(baseDate))); // 2024-02-29 00:00:00

            // Calculating the next 5 runs
            Date[] nextRuns = schedule3.next(baseDate, 5);
            for (Date run : nextRuns) {
                System.out.println(dateFormatter.format(run));
                // 2019-01-01 05:00:00
                // 2019-01-01 23:00:00
                // 2019-01-02 01:00:00
                // 2019-01-02 05:00:00
                // 2019-01-02 23:00:00
            }

            // calculate time from base time to next run in milliseconds
            long duration = schedule3.nextDuration(baseDate, TimeUnit.MILLISECONDS);
            System.out.println(String.format("next run is after %d milliseconds", duration));
            // next run is after 3358000 milliseconds

            /*
             * next and nextDuration can be called without specifying a base date. when base
             * date is not specified, the current date will be used e.g. schedule.next()
             * schedule.next(5) schedule.nextDuration()
             */

        } catch (InvalidExpressionException ex) {
            System.out.println(ex);
        } catch (ParseException ex) {
            System.out.println(ex);
        }
    }
}
```
