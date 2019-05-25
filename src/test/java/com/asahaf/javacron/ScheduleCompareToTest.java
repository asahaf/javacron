package com.asahaf.javacron;

import org.junit.Assert;
import org.junit.Test;

public class ScheduleCompareToTest {

    @Test
    public void testCompareTo() throws InvalidExpressionException {
        Schedule schedule1 = Schedule.create("* * * * *");
        Schedule schedule2 = Schedule.create("* * * * *");
        Assert.assertEquals(0, schedule1.compareTo(schedule2));

        schedule1 = Schedule.create("* * * * * *");
        schedule2 = Schedule.create("* * * * * *");
        Assert.assertEquals(0, schedule1.compareTo(schedule2));

        schedule1 = Schedule.create("* * * * * *");
        schedule2 = Schedule.create("* * * * *");
        Assert.assertEquals(-1, schedule1.compareTo(schedule2));

        schedule1 = Schedule.create("* * * * *");
        schedule2 = Schedule.create("* * * * * *");
        Assert.assertEquals(1, schedule1.compareTo(schedule2));

        schedule1 = Schedule.create("0 0 4 * * *");
        schedule2 = Schedule.create("0 0 4 * * *");
        Assert.assertEquals(0, schedule1.compareTo(schedule2));

        schedule1 = Schedule.create("1 1 1 1 1 *");
        schedule2 = Schedule.create("1 1 1 1 1 *");
        Assert.assertEquals(0, schedule1.compareTo(schedule2));
    }
}
