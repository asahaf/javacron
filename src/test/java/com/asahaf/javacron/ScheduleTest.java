package com.asahaf.javacron;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ScheduleTest {

    static DateFormat dateFormatter;
    static {
        dateFormatter = new SimpleDateFormat("y-MM-dd HH:mm:ss");
    }

    @Parameterized.Parameters
    public static Collection getTestCases() {
        return Arrays.asList(new Object[][] {

                // Second resolution all asterisks

                { "2019-01-01 00:00:00", "* * * * * *", "2019-01-01 00:00:01" },
                { "2019-01-01 00:00:09", "* * * * * *", "2019-01-01 00:00:10" },
                { "2019-01-01 00:00:29", "* * * * * *", "2019-01-01 00:00:30" },
                { "2019-01-01 00:00:37", "* * * * * *", "2019-01-01 00:00:38" },
                // Second rollover
                { "2019-01-01 00:58:59", "* * * * * *", "2019-01-01 00:59:00" },
                { "2019-01-01 11:59:59", "* * * * * *", "2019-01-01 12:00:00" },
                // Minute rollover
                { "2019-01-01 00:59:59", "* * * * * *", "2019-01-01 01:00:00" },
                { "2019-01-01 11:59:59", "* * * * * *", "2019-01-01 12:00:00" },
                // Hour rollover
                { "2019-01-01 23:59:59", "* * * * * *", "2019-01-02 00:00:00" },
                // Month rollover
                { "2019-01-01 23:59:59", "* * * * * *", "2019-01-02 00:00:00" },
                { "2019-02-28 23:59:59", "* * * * * *", "2019-03-01 00:00:00" },
                // Year rollover
                { "2019-12-31 23:59:59", "* * * * * *", "2020-01-01 00:00:00" },
                // Leap year
                { "2020-02-28 23:59:59", "* * * * * *", "2020-02-29 00:00:00" },

                // Minute resolution all asterisks

                { "2019-01-01 00:00:00", "* * * * *", "2019-01-01 00:01:00" },
                { "2019-01-01 00:09:00", "* * * * *", "2019-01-01 00:10:00" },
                { "2019-01-01 00:29:00", "* * * * *", "2019-01-01 00:30:00" },
                { "2019-01-01 00:37:00", "* * * * *", "2019-01-01 00:38:00" },
                // Minute rollover
                { "2019-01-01 00:59:00", "* * * * *", "2019-01-01 01:00:00" },
                { "2019-01-01 11:59:00", "* * * * *", "2019-01-01 12:00:00" },
                // Hour rollover
                { "2019-01-01 23:59:00", "* * * * *", "2019-01-02 00:00:00" },
                // Month rollover
                { "2019-01-01 23:59:00", "* * * * *", "2019-01-02 00:00:00" },
                { "2019-02-28 23:59:00", "* * * * *", "2019-03-01 00:00:00" },
                // Year rollover
                { "2019-12-31 23:59:00", "* * * * *", "2020-01-01 00:00:00" },
                // Leap year
                { "2020-02-28 23:59:00", "* * * * *", "2020-02-29 00:00:00" },

                // Second tests

                // Restricted second
                { "2019-01-01 00:00:00", "44 * * * * *", "2019-01-01 00:00:44" },
                { "2019-01-01 00:00:44", "44 * * * * *", "2019-01-01 00:01:44" },
                { "2019-01-01 00:00:45", "44 * * * * *", "2019-01-01 00:01:44" },

                // test seconds range
                { "2019-01-01 00:00:00", "0-20 * * * * *", "2019-01-01 00:00:01" },
                { "2019-01-01 00:00:19", "0-20 * * * * *", "2019-01-01 00:00:20" },
                { "2019-01-01 00:00:14", "0-20 * * * * *", "2019-01-01 00:00:15" },
                { "2019-01-01 00:00:20", "0-20 * * * * *", "2019-01-01 00:01:00" },
                { "2019-01-01 00:00:59", "0-20 * * * * *", "2019-01-01 00:01:00" },

                // test seconds list
                { "2019-01-01 00:00:00", "1,5,20,59 * * * * *", "2019-01-01 00:00:01" },
                { "2019-01-01 00:00:01", "1,5,20,59 * * * * *", "2019-01-01 00:00:05" },
                { "2019-01-01 00:00:05", "1,5,20,59 * * * * *", "2019-01-01 00:00:20" },
                { "2019-01-01 00:00:58", "1,5,20,59 * * * * *", "2019-01-01 00:00:59" },
                { "2019-01-01 00:00:59", "1,5,20,59 * * * * *", "2019-01-01 00:01:01" },

                // test seconds step/every
                { "2019-01-01 00:00:00", "*/3 * * * * *", "2019-01-01 00:00:03" },
                { "2019-01-01 00:00:02", "*/3 * * * * *", "2019-01-01 00:00:03" },
                { "2019-01-01 00:00:03", "*/3 * * * * *", "2019-01-01 00:00:06" },
                { "2019-01-01 00:00:30", "*/3 * * * * *", "2019-01-01 00:00:33" },
                { "2019-01-01 00:00:44", "*/3 * * * * *", "2019-01-01 00:00:45" },
                { "2019-01-01 00:00:58", "*/3 * * * * *", "2019-01-01 00:01:00" },
                { "2019-01-01 00:00:59", "*/3 * * * * *", "2019-01-01 00:01:00" },

                { "2019-01-01 00:00:00", "40/3 * * * * *", "2019-01-01 00:00:40" },
                { "2019-01-01 00:00:40", "40/3 * * * * *", "2019-01-01 00:00:43" },
                { "2019-01-01 00:00:47", "40/3 * * * * *", "2019-01-01 00:00:49" },
                { "2019-01-01 00:00:49", "40/3 * * * * *", "2019-01-01 00:00:52" },
                { "2019-01-01 00:00:52", "40/3 * * * * *", "2019-01-01 00:00:55" },
                { "2019-01-01 00:00:58", "40/3 * * * * *", "2019-01-01 00:01:40" },
                { "2019-01-01 00:00:59", "40/3 * * * * *", "2019-01-01 00:01:40" },

                { "2019-01-01 00:00:00", "40-58/3 * * * * *", "2019-01-01 00:00:40" },
                { "2019-01-01 00:00:40", "40-58/3 * * * * *", "2019-01-01 00:00:43" },
                { "2019-01-01 00:00:47", "40-58/3 * * * * *", "2019-01-01 00:00:49" },
                { "2019-01-01 00:00:49", "40-58/3 * * * * *", "2019-01-01 00:00:52" },
                { "2019-01-01 00:00:52", "40-58/3 * * * * *", "2019-01-01 00:00:55" },
                { "2019-01-01 00:00:58", "40-58/3 * * * * *", "2019-01-01 00:01:40" },
                { "2019-01-01 00:00:59", "40-58/3 * * * * *", "2019-01-01 00:01:40" },

                // test seconds range and list
                { "2019-01-01 00:00:00", "1,0-20,5,30,20,59 * * * * *", "2019-01-01 00:00:01" },
                { "2019-01-01 00:00:01", "1,0-20,5,30,20,59 * * * * *", "2019-01-01 00:00:02" },
                { "2019-01-01 00:00:05", "1,0-20,5,30,20,59 * * * * *", "2019-01-01 00:00:06" },
                { "2019-01-01 00:00:25", "1,0-20,5,30,20,59 * * * * *", "2019-01-01 00:00:30" },
                { "2019-01-01 00:00:58", "1,0-20,5,30,20,59 * * * * *", "2019-01-01 00:00:59" },
                { "2019-01-01 00:00:59", "1,0-20,5,30,20,59 * * * * *", "2019-01-01 00:01:00" },

                // test seconds range ,list and step
                { "2019-01-01 00:00:00", "1,0-20,5,30,20,59,15-21/3 * * * * *", "2019-01-01 00:00:01" },
                { "2019-01-01 00:00:01", "1,0-20,5,30,20,59,15-21/3 * * * * *", "2019-01-01 00:00:02" },
                { "2019-01-01 00:00:05", "1,0-20,5,30,20,59,15-21/3 * * * * *", "2019-01-01 00:00:06" },
                { "2019-01-01 00:00:14", "1,0-20,5,30,20,59,15-21/3 * * * * *", "2019-01-01 00:00:15" },
                { "2019-01-01 00:00:25", "1,0-20,5,30,20,59,15-21/3 * * * * *", "2019-01-01 00:00:30" },
                { "2019-01-01 00:00:58", "1,0-20,5,30,20,59,15-21/3 * * * * *", "2019-01-01 00:00:59" },
                { "2019-01-01 00:00:59", "1,0-20,5,30,20,59,15-21/3 * * * * *", "2019-01-01 00:01:00" },

                // Minutes tests

                // Restricted minute
                { "2019-01-01 00:00:00", "44 * * * *", "2019-01-01 00:44:00" },

                // test minutes range
                { "2019-01-01 00:00:00", "0-20 * * * *", "2019-01-01 00:01:00" },
                { "2019-01-01 00:19:00", "0-20 * * * *", "2019-01-01 00:20:00" },
                { "2019-01-01 00:14:00", "0-20 * * * *", "2019-01-01 00:15:00" },
                { "2019-01-01 00:20:00", "0-20 * * * *", "2019-01-01 01:00:00" },
                { "2019-01-01 00:59:00", "0-20 * * * *", "2019-01-01 01:00:00" },

                // test minutes list
                { "2019-01-01 00:00:00", "1,5,20,59 * * * *", "2019-01-01 00:01:00" },
                { "2019-01-01 00:01:00", "1,5,20,59 * * * *", "2019-01-01 00:05:00" },
                { "2019-01-01 00:05:00", "1,5,20,59 * * * *", "2019-01-01 00:20:00" },
                { "2019-01-01 00:58:00", "1,5,20,59 * * * *", "2019-01-01 00:59:00" },
                { "2019-01-01 00:59:00", "1,5,20,59 * * * *", "2019-01-01 01:01:00" },

                // test minutes step/every
                { "2019-01-01 00:00:00", "*/3 * * * *", "2019-01-01 00:03:00" },
                { "2019-01-01 00:02:00", "*/3 * * * *", "2019-01-01 00:03:00" },
                { "2019-01-01 00:03:00", "*/3 * * * *", "2019-01-01 00:06:00" },
                { "2019-01-01 00:30:00", "*/3 * * * *", "2019-01-01 00:33:00" },
                { "2019-01-01 00:44:00", "*/3 * * * *", "2019-01-01 00:45:00" },
                { "2019-01-01 00:58:00", "*/3 * * * *", "2019-01-01 01:00:00" },
                { "2019-01-01 00:59:00", "*/3 * * * *", "2019-01-01 01:00:00" },

                { "2019-01-01 00:00:00", "40/3 * * * *", "2019-01-01 00:40:00" },
                { "2019-01-01 00:40:00", "40/3 * * * *", "2019-01-01 00:43:00" },
                { "2019-01-01 00:47:00", "40/3 * * * *", "2019-01-01 00:49:00" },
                { "2019-01-01 00:49:00", "40/3 * * * *", "2019-01-01 00:52:00" },
                { "2019-01-01 00:52:00", "40/3 * * * *", "2019-01-01 00:55:00" },
                { "2019-01-01 00:58:00", "40/3 * * * *", "2019-01-01 01:40:00" },
                { "2019-01-01 00:59:00", "40/3 * * * *", "2019-01-01 01:40:00" },

                { "2019-01-01 00:00:00", "40-58/3 * * * *", "2019-01-01 00:40:00" },
                { "2019-01-01 00:40:00", "40-58/3 * * * *", "2019-01-01 00:43:00" },
                { "2019-01-01 00:47:00", "40-58/3 * * * *", "2019-01-01 00:49:00" },
                { "2019-01-01 00:49:00", "40-58/3 * * * *", "2019-01-01 00:52:00" },
                { "2019-01-01 00:52:00", "40-58/3 * * * *", "2019-01-01 00:55:00" },
                { "2019-01-01 00:58:00", "40-58/3 * * * *", "2019-01-01 01:40:00" },
                { "2019-01-01 00:59:00", "40-58/3 * * * *", "2019-01-01 01:40:00" },

                // test minutes range and list
                { "2019-01-01 00:00:00", "1,0-20,5,30,20,59 * * * *", "2019-01-01 00:01:00" },
                { "2019-01-01 00:01:00", "1,0-20,5,30,20,59 * * * *", "2019-01-01 00:02:00" },
                { "2019-01-01 00:05:00", "1,0-20,5,30,20,59 * * * *", "2019-01-01 00:06:00" },
                { "2019-01-01 00:25:00", "1,0-20,5,30,20,59 * * * *", "2019-01-01 00:30:00" },
                { "2019-01-01 00:58:00", "1,0-20,5,30,20,59 * * * *", "2019-01-01 00:59:00" },
                { "2019-01-01 00:59:00", "1,0-20,5,30,20,59 * * * *", "2019-01-01 01:00:00" },

                // test minutes range ,list and step
                { "2019-01-01 00:00:00", "1,0-20,5,30,20,59,15-21/3 * * * *", "2019-01-01 00:01:00" },
                { "2019-01-01 00:01:00", "1,0-20,5,30,20,59,15-21/3 * * * *", "2019-01-01 00:02:00" },
                { "2019-01-01 00:05:00", "1,0-20,5,30,20,59,15-21/3 * * * *", "2019-01-01 00:06:00" },
                { "2019-01-01 00:14:00", "1,0-20,5,30,20,59,15-21/3 * * * *", "2019-01-01 00:15:00" },
                { "2019-01-01 00:25:00", "1,0-20,5,30,20,59,15-21/3 * * * *", "2019-01-01 00:30:00" },
                { "2019-01-01 00:58:00", "1,0-20,5,30,20,59,15-21/3 * * * *", "2019-01-01 00:59:00" },
                { "2019-01-01 00:59:00", "1,0-20,5,30,20,59,15-21/3 * * * *", "2019-01-01 01:00:00" },

                // Minutes tests - seconds resolution

                // Restricted minute - seconds resolution
                { "2019-01-01 00:00:00", "0 44 * * * *", "2019-01-01 00:44:00" },
                { "2019-01-01 00:44:05", "20 44 * * * *", "2019-01-01 00:44:20" },
                { "2019-01-01 00:44:21", "20 44 * * * *", "2019-01-01 01:44:20" },
                { "2019-01-01 00:45:05", "20 44 * * * *", "2019-01-01 01:44:20" },

                // test minutes range - seconds resolution
                { "2019-01-01 00:00:00", "0 0-20 * * * *", "2019-01-01 00:01:00" },
                { "2019-01-01 00:19:00", "0 0-20 * * * *", "2019-01-01 00:20:00" },
                { "2019-01-01 00:14:00", "0 0-20 * * * *", "2019-01-01 00:15:00" },
                { "2019-01-01 00:20:00", "0 0-20 * * * *", "2019-01-01 01:00:00" },
                { "2019-01-01 00:59:00", "0 0-20 * * * *", "2019-01-01 01:00:00" },

                // test minutes list - seconds resolution
                { "2019-01-01 00:00:00", "0 1,5,20,59 * * * *", "2019-01-01 00:01:00" },
                { "2019-01-01 00:01:02", "0 1,5,20,59 * * * *", "2019-01-01 00:05:00" },
                { "2019-01-01 00:05:05", "0 1,5,20,59 * * * *", "2019-01-01 00:20:00" },
                { "2019-01-01 00:58:00", "0 1,5,20,59 * * * *", "2019-01-01 00:59:00" },
                { "2019-01-01 00:59:00", "0 1,5,20,59 * * * *", "2019-01-01 01:01:00" },

                // test minutes step/every - seconds resolution
                { "2019-01-01 00:00:00", "0 */3 * * * *", "2019-01-01 00:03:00" },
                { "2019-01-01 00:02:00", "0 */3 * * * *", "2019-01-01 00:03:00" },
                { "2019-01-01 00:03:00", "0 */3 * * * *", "2019-01-01 00:06:00" },
                { "2019-01-01 00:30:02", "0 */3 * * * *", "2019-01-01 00:33:00" },
                { "2019-01-01 00:44:00", "0 */3 * * * *", "2019-01-01 00:45:00" },
                { "2019-01-01 00:58:00", "0 */3 * * * *", "2019-01-01 01:00:00" },
                { "2019-01-01 00:59:00", "0 */3 * * * *", "2019-01-01 01:00:00" },

                { "2019-01-01 00:00:00", "0 40/3 * * * *", "2019-01-01 00:40:00" },
                { "2019-01-01 00:40:00", "0 40/3 * * * *", "2019-01-01 00:43:00" },
                { "2019-01-01 00:47:00", "0 40/3 * * * *", "2019-01-01 00:49:00" },
                { "2019-01-01 00:49:00", "0 40/3 * * * *", "2019-01-01 00:52:00" },
                { "2019-01-01 00:52:00", "0 40/3 * * * *", "2019-01-01 00:55:00" },
                { "2019-01-01 00:58:00", "0 40/3 * * * *", "2019-01-01 01:40:00" },
                { "2019-01-01 00:59:00", "0 40/3 * * * *", "2019-01-01 01:40:00" },

                { "2019-01-01 00:00:00", "0 40-58/3 * * * *", "2019-01-01 00:40:00" },
                { "2019-01-01 00:40:00", "0 40-58/3 * * * *", "2019-01-01 00:43:00" },
                { "2019-01-01 00:47:00", "0 40-58/3 * * * *", "2019-01-01 00:49:00" },
                { "2019-01-01 00:49:00", "0 40-58/3 * * * *", "2019-01-01 00:52:00" },
                { "2019-01-01 00:52:00", "0 40-58/3 * * * *", "2019-01-01 00:55:00" },
                { "2019-01-01 00:58:00", "0 40-58/3 * * * *", "2019-01-01 01:40:00" },
                { "2019-01-01 00:59:00", "0 40-58/3 * * * *", "2019-01-01 01:40:00" },

                // test minutes range and list - seconds resolution
                { "2019-01-01 00:00:00", "0 1,0-20,5,30,20,59 * * * *", "2019-01-01 00:01:00" },
                { "2019-01-01 00:01:00", "0 1,0-20,5,30,20,59 * * * *", "2019-01-01 00:02:00" },
                { "2019-01-01 00:05:00", "0 1,0-20,5,30,20,59 * * * *", "2019-01-01 00:06:00" },
                { "2019-01-01 00:25:00", "0 1,0-20,5,30,20,59 * * * *", "2019-01-01 00:30:00" },
                { "2019-01-01 00:58:00", "0 1,0-20,5,30,20,59 * * * *", "2019-01-01 00:59:00" },
                { "2019-01-01 00:59:00", "0 1,0-20,5,30,20,59 * * * *", "2019-01-01 01:00:00" },

                // test minutes range ,list and step - seconds resolution
                { "2019-01-01 00:00:00", "0 1,0-20,5,30,20,59,15-21/3 * * * *", "2019-01-01 00:01:00" },
                { "2019-01-01 00:01:00", "0 1,0-20,5,30,20,59,15-21/3 * * * *", "2019-01-01 00:02:00" },
                { "2019-01-01 00:05:00", "0 1,0-20,5,30,20,59,15-21/3 * * * *", "2019-01-01 00:06:00" },
                { "2019-01-01 00:14:00", "0 1,0-20,5,30,20,59,15-21/3 * * * *", "2019-01-01 00:15:00" },
                { "2019-01-01 00:25:00", "0 1,0-20,5,30,20,59,15-21/3 * * * *", "2019-01-01 00:30:00" },
                { "2019-01-01 00:58:00", "0 1,0-20,5,30,20,59,15-21/3 * * * *", "2019-01-01 00:59:00" },
                { "2019-01-01 00:59:00", "0 1,0-20,5,30,20,59,15-21/3 * * * *", "2019-01-01 01:00:00" },

                // Hours tests

                // Restricted hour
                { "2019-01-01 00:00:00", "* * 14 * * *", "2019-01-01 14:00:00" },
                { "2019-01-01 00:09:00", "* * 13 * * *", "2019-01-01 13:00:00" },
                { "2019-01-04 02:38:00", "2 39 5 * * *", "2019-01-04 05:39:02" },
                { "2019-01-04 05:38:00", "2 39 5 * * *", "2019-01-04 05:39:02" },
                { "2019-01-04 06:38:00", "2 39 5 * * *", "2019-01-05 05:39:02" },
                { "2019-01-04 02:40:00", "2 39 5 * * *", "2019-01-04 05:39:02" },
                { "2019-01-04 05:40:00", "2 39 5 * * *", "2019-01-05 05:39:02" },
                { "2019-01-04 06:40:00", "2 39 5 * * *", "2019-01-05 05:39:02" },

                // test hours range
                { "2019-01-01 00:02:05", "0 0 0-20 * * *", "2019-01-01 01:00:00" },
                { "2019-01-01 19:08:06", "0 0 0-20 * * *", "2019-01-01 20:00:00" },
                { "2019-01-01 14:15:15", "0 0 0-20 * * *", "2019-01-01 15:00:00" },
                { "2019-01-01 20:22:04", "0 0 0-20 * * *", "2019-01-02 00:00:00" },
                { "2019-01-01 23:05:11", "0 0 0-20 * * *", "2019-01-02 00:00:00" },

                // test hours list
                { "2019-01-01 00:03:00", "0 0 1,5,20 * * *", "2019-01-01 01:00:00" },
                { "2019-01-01 01:00:04", "0 0 1,5,20 * * *", "2019-01-01 05:00:00" },
                { "2019-01-01 05:08:40", "0 0 1,5,20 * * *", "2019-01-01 20:00:00" },
                { "2019-01-01 22:00:09", "0 0 1,5,20 * * *", "2019-01-02 01:00:00" },
                { "2019-01-02 23:09:00", "0 0 1,5,20 * * *", "2019-01-03 01:00:00" },

                // test hours step/every
                { "2019-01-01 00:03:01", "0 0 */3 * * *", "2019-01-01 03:00:00" },
                { "2019-01-01 02:00:00", "0 0 */3 * * *", "2019-01-01 03:00:00" },
                { "2019-01-01 03:01:00", "0 0 */3 * * *", "2019-01-01 06:00:00" },
                { "2019-01-01 14:00:00", "0 0 */3 * * *", "2019-01-01 15:00:00" },
                { "2019-01-01 21:00:00", "0 0 */3 * * *", "2019-01-02 00:00:00" },
                { "2019-01-01 23:00:00", "0 0 */3 * * *", "2019-01-02 00:00:00" },

                { "2019-01-01 00:00:00", "0 0 12/3 * * *", "2019-01-01 12:00:00" },
                { "2019-01-01 12:00:00", "0 0 12/3 * * *", "2019-01-01 15:00:00" },
                { "2019-01-01 19:00:00", "0 0 12/3 * * *", "2019-01-01 21:00:00" },
                { "2019-01-01 22:00:00", "0 0 12/3 * * *", "2019-01-02 12:00:00" },
                { "2019-01-01 23:00:00", "0 0 12/3 * * *", "2019-01-02 12:00:00" },

                { "2019-01-01 00:00:00", "0 0 12-23/3 * * *", "2019-01-01 12:00:00" },
                { "2019-01-01 12:00:00", "0 0 12-23/3 * * *", "2019-01-01 15:00:00" },
                { "2019-01-01 19:00:00", "0 0 12-23/3 * * *", "2019-01-01 21:00:00" },
                { "2019-01-01 22:00:00", "0 0 12-23/3 * * *", "2019-01-02 12:00:00" },
                { "2019-01-01 23:00:00", "0 0 12-23/3 * * *", "2019-01-02 12:00:00" },

                { "2019-01-01 06:00:00", "0 0 8/1 * * *", "2019-01-01 08:00:00" },
                { "2019-01-01 08:00:00", "0 0 8/1 * * *", "2019-01-01 09:00:00" },
                { "2019-01-01 06:00:00", "0 0 8-23/1 * * *", "2019-01-01 08:00:00" },
                { "2019-01-01 08:00:00", "0 0 8-23/1 * * *", "2019-01-01 09:00:00" },

                // test hours range and list
                { "2019-01-01 00:00:00", "0 0 1,0-9,5,23 * * *", "2019-01-01 01:00:00" },
                { "2019-01-01 01:00:00", "0 0 1,0-9,5,23 * * *", "2019-01-01 02:00:00" },
                { "2019-01-01 05:00:00", "0 0 1,0-9,5,23 * * *", "2019-01-01 06:00:00" },
                { "2019-01-01 09:00:00", "0 0 1,0-9,5,23 * * *", "2019-01-01 23:00:00" },
                { "2019-01-01 23:00:00", "0 0 1,0-9,5,23 * * *", "2019-01-02 00:00:00" },

                // test hours range ,list and step
                { "2019-01-01 00:00:00", "0 0 1,0-9,5,23,15-21/3 * * *", "2019-01-01 01:00:00" },
                { "2019-01-01 01:00:00", "0 0 1,0-9,5,23,15-21/3 * * *", "2019-01-01 02:00:00" },
                { "2019-01-01 05:00:00", "0 0 1,0-9,5,23,15-21/3 * * *", "2019-01-01 06:00:00" },
                { "2019-01-01 14:00:00", "0 0 1,0-9,5,23,15-21/3 * * *", "2019-01-01 15:00:00" },
                { "2019-01-01 19:00:00", "0 0 1,0-9,5,23,15-21/3 * * *", "2019-01-01 21:00:00" },
                { "2019-01-01 22:00:00", "0 0 1,0-9,5,23,15-21/3 * * *", "2019-01-01 23:00:00" },
                { "2019-01-01 23:00:00", "0 0 1,0-9,5,23,15-21/3 * * *", "2019-01-02 00:00:00" },

                // days tests

                // restricted day
                { "2019-01-01 00:00:00", "* * 8 14 * *", "2019-01-14 08:00:00" },
                { "2019-01-01 00:09:00", "* * 8 13 * *", "2019-01-13 08:00:00" },
                { "2019-01-03 02:38:00", "2 39 8 5 * *", "2019-01-05 08:39:02" },
                { "2019-01-04 05:38:00", "2 39 8 5 * *", "2019-01-05 08:39:02" },
                { "2019-01-05 06:38:00", "2 39 8 5 * *", "2019-01-05 08:39:02" },
                { "2019-01-06 02:40:00", "2 39 8 5 * *", "2019-02-05 08:39:02" },
                { "2019-01-12 05:40:00", "2 39 8 5 * *", "2019-02-05 08:39:02" },
                { "2019-01-11 06:40:00", "2 39 8 5 * *", "2019-02-05 08:39:02" },

                // test days range
                { "2019-01-01 00:02:05", "0 0 0 1-20 * *", "2019-01-02 00:00:00" },
                { "2019-01-07 19:08:06", "0 0 0 1-20 * *", "2019-01-08 00:00:00" },
                { "2019-01-19 14:15:15", "0 0 0 1-20 * *", "2019-01-20 00:00:00" },
                { "2019-01-20 20:22:04", "0 0 0 1-20 * *", "2019-02-01 00:00:00" },
                { "2019-01-22 23:05:11", "0 0 0 1-20 * *", "2019-02-01 00:00:00" },

                // test days list
                { "2019-01-01 00:03:00", "0 0 0 1,5,20 * *", "2019-01-05 00:00:00" },
                { "2019-01-05 01:00:04", "0 0 0 1,5,20 * *", "2019-01-20 00:00:00" },
                { "2019-01-07 05:08:40", "0 0 0 1,5,20 * *", "2019-01-20 00:00:00" },
                { "2019-01-19 22:00:09", "0 0 0 1,5,20 * *", "2019-01-20 00:00:00" },
                { "2019-01-20 23:09:00", "0 0 0 1,5,20 * *", "2019-02-01 00:00:00" },
                { "2019-01-21 23:09:00", "0 0 0 1,5,20 * *", "2019-02-01 00:00:00" },

                // test days step/every
                { "2019-01-01 00:03:01", "0 0 0 */3 * *", "2019-01-04 00:00:00" },
                { "2019-01-03 02:00:00", "0 0 0 */3 * *", "2019-01-04 00:00:00" },
                { "2019-01-05 03:01:00", "0 0 0 */3 * *", "2019-01-07 00:00:00" },
                { "2019-01-10 14:00:00", "0 0 0 */3 * *", "2019-01-13 00:00:00" },
                { "2019-01-30 21:00:00", "0 0 0 */3 * *", "2019-01-31 00:00:00" },
                { "2019-01-31 23:00:00", "0 0 0 */3 * *", "2019-02-01 00:00:00" },

                { "2019-01-01 00:00:00", "0 0 0 12/3 * *", "2019-01-12 00:00:00" },
                { "2019-01-04 12:00:00", "0 0 0 12/3 * *", "2019-01-12 00:00:00" },
                { "2019-01-12 19:00:00", "0 0 0 12/3 * *", "2019-01-15 00:00:00" },
                { "2019-01-15 22:00:00", "0 0 0 12/3 * *", "2019-01-18 00:00:00" },
                { "2019-01-30 23:00:00", "0 0 0 12/3 * *", "2019-02-12 00:00:00" },

                { "2019-01-01 00:00:00", "0 0 0 12-23/3 * *", "2019-01-12 00:00:00" },
                { "2019-01-04 12:00:00", "0 0 0 12-23/3 * *", "2019-01-12 00:00:00" },
                { "2019-01-12 19:00:00", "0 0 0 12-23/3 * *", "2019-01-15 00:00:00" },
                { "2019-01-15 22:00:00", "0 0 0 12-23/3 * *", "2019-01-18 00:00:00" },
                { "2019-01-23 22:00:00", "0 0 0 12-23/3 * *", "2019-02-12 00:00:00" },
                { "2019-01-30 23:00:00", "0 0 0 12-23/3 * *", "2019-02-12 00:00:00" },

                // test days range and list
                { "2019-01-01 00:00:00", "0 0 0 1,1-9,5,23 * *", "2019-01-02 00:00:00" },
                { "2019-01-04 01:00:00", "0 0 0 1,1-9,5,23 * *", "2019-01-05 00:00:00" },
                { "2019-01-22 05:00:00", "0 0 0 1,1-9,5,23 * *", "2019-01-23 00:00:00" },
                { "2019-01-23 09:00:00", "0 0 0 1,1-9,5,23 * *", "2019-02-01 00:00:00" },
                { "2019-01-31 23:00:00", "0 0 0 1,1-9,5,23 * *", "2019-02-01 00:00:00" },

                // test days range ,list and step
                { "2019-01-01 00:00:00", "0 0 0 1,1-9,5,23,15-21/3 * *", "2019-01-02 00:00:00" },
                { "2019-01-05 01:00:00", "0 0 0 1,1-9,5,23,15-21/3 * *", "2019-01-06 00:00:00" },
                { "2019-01-09 05:00:00", "0 0 0 1,1-9,5,23,15-21/3 * *", "2019-01-15 00:00:00" },
                { "2019-01-13 14:00:00", "0 0 0 1,1-9,5,23,15-21/3 * *", "2019-01-15 00:00:00" },
                { "2019-01-15 19:00:00", "0 0 0 1,1-9,5,23,15-21/3 * *", "2019-01-18 00:00:00" },
                { "2019-01-21 22:00:00", "0 0 0 1,1-9,5,23,15-21/3 * *", "2019-01-23 00:00:00" },
                { "2019-01-28 23:00:00", "0 0 0 1,1-9,5,23,15-21/3 * *", "2019-02-01 00:00:00" },

                // Day of month tests
                { "2019-01-07 00:00:00", "30  *  1 * *", "2019-02-01 00:30:00" },
                { "2019-02-01 00:30:00", "30  *  1 * *", "2019-02-01 01:30:00" },

                { "2019-01-01 00:00:00", "10  * 22    * *", "2019-01-22 00:10:00" },
                { "2019-01-01 00:00:00", "30 23 19    * *", "2019-01-19 23:30:00" },
                { "2019-01-01 00:00:00", "30 23 21    * *", "2019-01-21 23:30:00" },
                { "2019-01-01 00:01:00", " *  * 21    * *", "2019-01-21 00:00:00" },
                { "2019-07-10 00:00:00", " *  * 30,31 * *", "2019-07-30 00:00:00" },

                // test months

                // Restricted month
                { "2019-01-01 00:00:00", "* * * * 4 *", "2019-04-01 00:00:00" },
                { "2019-01-01 00:09:00", "* * * * 2 *", "2019-02-01 00:00:00" },
                { "2019-01-04 02:40:00", "0 39 5 3 7 *", "2019-07-03 05:39:00" },
                { "2019-12-01 00:00:00", "* * * * 1 *", "2020-01-01 00:00:00" },

                // test months range
                { "2019-01-01 00:02:05", "0 0 1 1-8 *", "2019-02-01 00:00:00" },
                { "2019-06-01 19:08:06", "0 0 1 1-8 *", "2019-07-01 00:00:00" },
                { "2019-07-01 14:15:15", "0 0 1 1-8 *", "2019-08-01 00:00:00" },
                { "2019-08-01 20:22:04", "0 0 1 1-8 *", "2020-01-01 00:00:00" },
                { "2019-10-01 23:05:11", "0 0 1 1-8 *", "2020-01-01 00:00:00" },

                // test months list
                { "2019-01-01 00:03:00", "0 0 1 1,5,8 *", "2019-05-01 00:00:00" },
                { "2019-02-01 01:00:04", "0 0 1 1,5,8 *", "2019-05-01 00:00:00" },
                { "2019-05-01 05:08:40", "0 0 1 1,5,8 *", "2019-08-01 00:00:00" },
                { "2019-08-01 22:00:09", "0 0 1 1,5,8 *", "2020-01-01 00:00:00" },
                { "2019-10-02 23:09:00", "0 0 1 1,5,8 *", "2020-01-01 00:00:00" },

                // test months step/every
                { "2019-01-01 00:03:01", "0 0 1 */3 *", "2019-04-01 00:00:00" },
                { "2019-02-01 02:00:00", "0 0 1 */3 *", "2019-04-01 00:00:00" },
                { "2019-04-01 03:01:00", "0 0 1 */3 *", "2019-07-01 00:00:00" },
                { "2019-06-01 14:00:00", "0 0 1 */3 *", "2019-07-01 00:00:00" },
                { "2019-07-01 14:00:00", "0 0 1 */3 *", "2019-10-01 00:00:00" },
                { "2019-09-01 21:00:00", "0 0 1 */3 *", "2019-10-01 00:00:00" },
                { "2019-12-01 23:00:00", "0 0 1 */3 *", "2020-01-01 00:00:00" },

                { "2019-01-01 00:00:00", "0 0 1 5/3 *", "2019-05-01 00:00:00" },
                { "2019-04-01 12:00:00", "0 0 1 5/3 *", "2019-05-01 00:00:00" },
                { "2019-05-01 19:00:00", "0 0 1 5/3 *", "2019-08-01 00:00:00" },
                { "2019-07-01 22:00:00", "0 0 1 5/3 *", "2019-08-01 00:00:00" },
                { "2019-09-01 23:00:00", "0 0 1 5/3 *", "2019-11-01 00:00:00" },
                { "2019-12-01 23:00:00", "0 0 1 5/3 *", "2020-05-01 00:00:00" },

                { "2019-01-01 00:00:00", "0 0 1 5-12/3 *", "2019-05-01 00:00:00" },
                { "2019-04-01 12:00:00", "0 0 1 5-12/3 *", "2019-05-01 00:00:00" },
                { "2019-05-01 19:00:00", "0 0 1 5-12/3 *", "2019-08-01 00:00:00" },
                { "2019-07-01 22:00:00", "0 0 1 5-12/3 *", "2019-08-01 00:00:00" },
                { "2019-09-01 23:00:00", "0 0 1 5-12/3 *", "2019-11-01 00:00:00" },
                { "2019-12-01 23:00:00", "0 0 1 5-12/3 *", "2020-05-01 00:00:00" },

                // test months range and list
                { "2019-01-01 00:00:00", "0 0 1 1,1-9,5,12 *", "2019-02-01 00:00:00" },
                { "2019-03-01 01:00:00", "0 0 1 1,1-9,5,12 *", "2019-04-01 00:00:00" },
                { "2019-04-01 01:00:00", "0 0 1 1,1-9,5,12 *", "2019-05-01 00:00:00" },
                { "2019-05-01 05:00:00", "0 0 1 1,1-9,5,12 *", "2019-06-01 00:00:00" },
                { "2019-09-01 09:00:00", "0 0 1 1,1-9,5,12 *", "2019-12-01 00:00:00" },
                { "2019-12-01 23:00:00", "0 0 1 1,1-9,5,12 *", "2020-01-01 00:00:00" },

                // test months range ,list and step
                { "2019-01-01 00:00:00", "0 0 1 1,1-3,5,12,8-12/3 *", "2019-02-01 00:00:00" },
                { "2019-02-01 01:00:00", "0 0 1 1,1-3,5,12,8-12/3 *", "2019-03-01 00:00:00" },
                { "2019-05-01 05:00:00", "0 0 1 1,1-3,5,12,8-12/3 *", "2019-08-01 00:00:00" },
                { "2019-08-01 14:00:00", "0 0 1 1,1-3,5,12,8-12/3 *", "2019-11-01 00:00:00" },
                { "2019-09-01 19:00:00", "0 0 1 1,1-3,5,12,8-12/3 *", "2019-11-01 00:00:00" },
                { "2019-11-01 22:00:00", "0 0 1 1,1-3,5,12,8-12/3 *", "2019-12-01 00:00:00" },
                { "2019-12-01 23:00:00", "0 0 1 1,1-3,5,12,8-12/3 *", "2020-01-01 00:00:00" },

                // test January
                { "2016-01-01 00:00:00", "0 0 10,30,31 * *", "2016-01-10 00:00:00" },
                { "2016-01-10 00:00:00", "0 0 10,30,31 * *", "2016-01-30 00:00:00" },
                { "2016-01-30 00:00:00", "0 0 10,30,31 * *", "2016-01-31 00:00:00" },
                { "2016-01-31 00:00:00", "0 0 10,30,31 * *", "2016-02-10 00:00:00" },

                // test February
                { "2016-02-10 00:00:00", "0 0 10,30,31 * *", "2016-03-10 00:00:00" },
                { "2016-02-10 00:00:00", "0 0 10,29,30,31 * *", "2016-02-29 00:00:00" },
                { "2019-02-10 00:00:00", "0 0 10,29,30,31 * *", "2019-03-10 00:00:00" },
                { "2019-02-28 23:59:59", "* * * 3 *", "2019-03-01 00:00:00" },
                { "2020-02-29 23:59:59", "* * * 3 *", "2020-03-01 00:00:00" },

                // test Mar
                { "2016-03-10 00:00:00", "0 0 10,30,31 * *", "2016-03-30 00:00:00" },
                { "2016-03-30 00:00:00", "0 0 10,30,31 * *", "2016-03-31 00:00:00" },
                { "2016-03-31 00:00:00", "0 0 10,30,31 * *", "2016-04-10 00:00:00" },
                { "2019-03-31 23:59:59", "* * * 4 *", "2019-04-01 00:00:00" },

                // test April
                { "2016-04-10 00:00:00", "0 0 10,30,31 * *", "2016-04-30 00:00:00" },
                { "2016-04-30 00:00:00", "0 0 10,30,31 * *", "2016-05-10 00:00:00" },
                { "2019-04-30 23:59:59", "* * * 5 *", "2019-05-01 00:00:00" },

                // test May
                { "2016-05-10 00:00:00", "0 0 10,30,31 * *", "2016-05-30 00:00:00" },
                { "2016-05-30 00:00:00", "0 0 10,30,31 * *", "2016-05-31 00:00:00" },
                { "2016-05-31 00:00:00", "0 0 10,30,31 * *", "2016-06-10 00:00:00" },

                // test June
                { "2016-06-10 00:00:00", "0 0 10,30,31 * *", "2016-06-30 00:00:00" },
                { "2016-06-30 00:00:00", "0 0 10,30,31 * *", "2016-07-10 00:00:00" },

                // test July
                { "2016-07-10 00:00:00", "0 0 10,30,31 * *", "2016-07-30 00:00:00" },
                { "2016-07-30 00:00:00", "0 0 10,30,31 * *", "2016-07-31 00:00:00" },
                { "2016-07-31 00:00:00", "0 0 10,30,31 * *", "2016-08-10 00:00:00" },

                // test August
                { "2016-08-10 00:00:00", "0 0 10,30,31 * *", "2016-08-30 00:00:00" },
                { "2016-08-30 00:00:00", "0 0 10,30,31 * *", "2016-08-31 00:00:00" },
                { "2016-08-31 00:00:00", "0 0 10,30,31 * *", "2016-09-10 00:00:00" },

                // test September
                { "2016-09-10 00:00:00", "0 0 10,30,31 * *", "2016-09-30 00:00:00" },
                { "2016-09-30 00:00:00", "0 0 10,30,31 * *", "2016-10-10 00:00:00" },

                // test October
                { "2016-10-10 00:00:00", "0 0 10,30,31 * *", "2016-10-30 00:00:00" },
                { "2016-10-30 00:00:00", "0 0 10,30,31 * *", "2016-10-31 00:00:00" },
                { "2016-10-31 00:00:00", "0 0 10,30,31 * *", "2016-11-10 00:00:00" },

                // test November
                { "2016-11-10 00:00:00", "0 0 10,30,31 * *", "2016-11-30 00:00:00" },
                { "2016-11-30 00:00:00", "0 0 10,30,31 * *", "2016-12-10 00:00:00" },

                // test December
                { "2016-12-10 00:00:00", "0 0 10,30,31 * *", "2016-12-30 00:00:00" },
                { "2016-12-30 00:00:00", "0 0 10,30,31 * *", "2016-12-31 00:00:00" },
                { "2016-12-31 00:00:00", "0 0 10,30,31 * *", "2017-01-10 00:00:00" },

                // leap year tests
                { "2016-02-29 00:00:00", "0 0 0 29 2 *", "2020-02-29 00:00:00" },
                { "2016-02-29 00:00:00", "0 0 0 28 2 *", "2017-02-28 00:00:00" },
                // leap year dividable by 100 and 400
                { "2000-01-01 00:00:00", "0 0 0 29 2 *", "2000-02-29 00:00:00" },
                { "2000-02-29 00:00:00", "0 0 0 29 2 *", "2004-02-29 00:00:00" },

                // more year tests
                { "2016-01-01 00:00:00", "0 0 0 28 2 *", "2016-02-28 00:00:00" },
                { "2016-02-28 00:00:00", "0 0 0 28 2 *", "2017-02-28 00:00:00" },
                { "2018-02-28 00:00:00", "0 0 0 28 2 *", "2019-02-28 00:00:00" },
                { "2019-02-28 00:00:00", "0 0 0 28 2 *", "2020-02-28 00:00:00" },
                { "2017-02-28 00:00:00", "0 0 0 28 2 *", "2018-02-28 00:00:00" },

                // day of week tests
                { "2019-01-12 00:00:00", "0 0 6 * * 0", "2019-01-13 06:00:00" },
                { "2019-01-12 00:00:00", "0 0 6 * * 1", "2019-01-14 06:00:00" },
                { "2019-01-12 00:00:00", "0 0 6 * * 2", "2019-01-15 06:00:00" },
                { "2019-01-12 00:00:00", "0 0 12 * * 3", "2019-01-16 12:00:00" },
                { "2019-01-24 00:00:00", "0 0 12 * * 4", "2019-01-24 12:00:00" },

                { "2019-01-01 09:45:00", "0 2 9 * * 3", "2019-01-02 09:02:00" },
                { "2019-01-02 09:10:00", "0 2 9 * * 3", "2019-01-09 09:02:00" },
                { "2019-01-17 05:11:00", "0 2 9 * * 3", "2019-01-23 09:02:00" },
                { "2019-01-11 12:07:00", "0 2 9 * * 3", "2019-01-16 09:02:00" },
                { "2019-01-19 13:12:00", "0 2 9 * * 3", "2019-01-23 09:02:00" },
                { "2019-01-25 22:11:00", "0 2 9 * * 3", "2019-01-30 09:02:00" },

                { "2019-01-13 00:08:00", "0 * * * * 3", "2019-01-16 00:00:00" },
                { "2019-01-14 08:00:00", "0 9 23 2 * 3", "2019-01-16 23:09:00" },
                { "2019-01-16 11:39:00", "0 9 23 3 * 3", "2019-01-16 23:09:00" },

                // intersect day with day of week
                { "2019-01-01 00:00:00", "0 0 0 * * 3", "2019-01-02 00:00:00" },
                { "2019-01-01 00:00:00", "0 0 0 * * 6", "2019-01-05 00:00:00" },
                { "2019-01-01 00:00:00", "0 0 0 */5 * 2", "2019-02-26 00:00:00" },
                { "2019-01-01 00:00:00", "0 0 0 */5 * */5", "2019-01-06 00:00:00" },
                { "2019-01-01 00:00:00", "0 0 0 */5 2 */5", "2019-02-01 00:00:00" },
                { "2019-01-01 00:00:00", "0 0 0 29 2 */4", "2024-02-29 00:00:00" },
                { "2019-01-01 00:00:00", "0 0 0 29 2 */5", "2032-02-29 00:00:00" },

                // union day with day of week
                { "2019-01-01 00:00:00", "0 0 0 4 * 3", "2019-01-02 00:00:00" },
                { "2019-01-01 00:00:00", "0 0 0 23 * 6", "2019-01-05 00:00:00" },
                { "2019-01-01 00:00:00", "0 0 0 12 * 2", "2019-01-08 00:00:00" },
                { "2019-01-01 00:00:00", "0 0 0 5 * 5", "2019-01-04 00:00:00" },
                { "2019-01-01 00:00:00", "0 0 0 5 2 5", "2019-02-01 00:00:00" },
                { "2019-01-01 00:00:00", "0 0 0 29 2 4", "2019-02-07 00:00:00" },
                { "2019-01-01 00:00:00", "0 0 0 29 2 5", "2019-02-01 00:00:00" },

        });
    }

    private String baseDateStr;
    private String expression;
    private String expectedDateStr;

    public ScheduleTest(String baseDate, String expression, String scheduledDate) {
        try {
            this.baseDateStr = baseDate;
            this.expression = expression;
            this.expectedDateStr = scheduledDate;
        } catch (Exception ex) {
            Assert.fail(ex.toString());
        }
    }

    @Test
    public void testSchedule() {
        try {
            Date baseDate = dateFormatter.parse(this.baseDateStr);
            Schedule schedule = Schedule.create(this.expression);
            Date resultDate = schedule.next(baseDate);
            Assert.assertEquals(this.expectedDateStr, dateFormatter.format(resultDate));
        } catch (Exception ex) {
            Assert.fail(ex.toString());
        }
    }

    @Test
    public void testScheduleDuration() {
        try {
            Date baseDate = dateFormatter.parse(this.baseDateStr);
            Date expectedDate = dateFormatter.parse(this.expectedDateStr);
            long expectedDuration = expectedDate.getTime() - baseDate.getTime();
            Schedule schedule = Schedule.create(this.expression);
            long result = schedule.nextDuration(baseDate, TimeUnit.MILLISECONDS);
            Assert.assertEquals(expectedDuration, result);
            result = schedule.nextDuration(baseDate, TimeUnit.SECONDS);
            Assert.assertEquals(TimeUnit.SECONDS.convert(expectedDuration, TimeUnit.MILLISECONDS), result);
        } catch (Exception ex) {
            Assert.fail(ex.toString());
        }
    }
}
