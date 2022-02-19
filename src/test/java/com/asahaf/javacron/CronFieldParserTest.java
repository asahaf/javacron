package com.asahaf.javacron;

import org.junit.Test;

import java.util.BitSet;

import org.junit.Assert;

public class CronFieldParserTest {
    @Test
    public void testSecondsFieldParser() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.SECOND);
        BitSet bitSet = parser.parse("*");
        Assert.assertEquals(60, bitSet.cardinality());
        bitSet = parser.parse("6");
        Assert.assertEquals(1, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(6));
        bitSet = parser.parse("2-6");
        Assert.assertEquals(5, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(2));
        Assert.assertTrue(bitSet.get(3));
        Assert.assertTrue(bitSet.get(4));
        Assert.assertTrue(bitSet.get(5));
        Assert.assertTrue(bitSet.get(6));
        bitSet = parser.parse("2-6/3");
        Assert.assertEquals(2, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(2));
        Assert.assertTrue(bitSet.get(5));
        bitSet = parser.parse("1,5,6");
        Assert.assertEquals(3, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(1));
        Assert.assertTrue(bitSet.get(5));
        Assert.assertTrue(bitSet.get(6));
    }

    @Test(expected = InvalidExpressionException.class)
    public void testSecondsFieldParserInvalid() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.SECOND);
        parser.parse("xyz");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testSecondsFieldParserInvalidMin() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.SECOND);
        parser.parse("-1");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testSecondsFieldParserInvalidMax() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.SECOND);
        parser.parse("61");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testSecondsFieldParserInvalidRangeMin() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.SECOND);
        parser.parse("-1-6");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testSecondsFieldParserInvalidRangeMax() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.SECOND);
        parser.parse("0-61");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testSecondsFieldParserInvalidStepSize() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.SECOND);
        parser.parse("*/0");
    }

    @Test
    public void testMinutesFieldParser() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.MINUTE);
        BitSet bitSet = parser.parse("*");
        Assert.assertEquals(60, bitSet.cardinality());
        bitSet = parser.parse("6");
        Assert.assertEquals(1, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(6));
        bitSet = parser.parse("2-6");
        Assert.assertEquals(5, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(2));
        Assert.assertTrue(bitSet.get(3));
        Assert.assertTrue(bitSet.get(4));
        Assert.assertTrue(bitSet.get(5));
        Assert.assertTrue(bitSet.get(6));
        bitSet = parser.parse("2-6/3");
        Assert.assertEquals(2, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(2));
        Assert.assertTrue(bitSet.get(5));
        bitSet = parser.parse("1,5,6");
        Assert.assertEquals(3, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(1));
        Assert.assertTrue(bitSet.get(5));
        Assert.assertTrue(bitSet.get(6));

    }

    @Test(expected = InvalidExpressionException.class)
    public void testMinutesFieldParserInvalid() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.MINUTE);
        parser.parse("xyz");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testMinutesFieldParserInvalidMin() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.MINUTE);
        parser.parse("-1");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testMinutesFieldParserInvalidMax() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.MINUTE);
        parser.parse("61");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testMinutesFieldParserInvalidRangeMin() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.MINUTE);
        parser.parse("-1-6");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testMinutesFieldParserInvalidRangeMax() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.MINUTE);
        parser.parse("0-61");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testMinutesFieldParserInvalidStepSize() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.MINUTE);
        parser.parse("*/0");
    }

    @Test
    public void testHoursFieldParser() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.HOUR);
        BitSet bitSet = parser.parse("*");
        Assert.assertEquals(24, bitSet.cardinality());
        bitSet = parser.parse("6");
        Assert.assertEquals(1, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(6));
        bitSet = parser.parse("2-6");
        Assert.assertEquals(5, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(2));
        Assert.assertTrue(bitSet.get(3));
        Assert.assertTrue(bitSet.get(4));
        Assert.assertTrue(bitSet.get(5));
        Assert.assertTrue(bitSet.get(6));
        bitSet = parser.parse("2-6/3");
        Assert.assertEquals(2, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(2));
        Assert.assertTrue(bitSet.get(5));
        bitSet = parser.parse("1,5,6");
        Assert.assertEquals(3, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(1));
        Assert.assertTrue(bitSet.get(5));
        Assert.assertTrue(bitSet.get(6));

    }

    @Test(expected = InvalidExpressionException.class)
    public void testHoursFieldParserInvalid() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.HOUR);
        parser.parse("xyz");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testHoursFieldParserInvalidMin() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.HOUR);
        parser.parse("-1");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testHoursFieldParserInvalidMax() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.HOUR);
        parser.parse("24");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testHoursFieldParserInvalidRangeMin() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.HOUR);
        parser.parse("-1-6");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testHoursFieldParserInvalidRangeMax() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.HOUR);
        parser.parse("0-24");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testHoursFieldParserInvalidStepSize() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.HOUR);
        parser.parse("*/0");
    }

    @Test
    public void testDayFieldParser() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.DAY);
        BitSet bitSet = parser.parse("*");
        Assert.assertEquals(31, bitSet.cardinality());
        bitSet = parser.parse("6");
        Assert.assertEquals(1, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(5));
        bitSet = parser.parse("2-6");
        Assert.assertEquals(5, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(1));
        Assert.assertTrue(bitSet.get(2));
        Assert.assertTrue(bitSet.get(3));
        Assert.assertTrue(bitSet.get(4));
        Assert.assertTrue(bitSet.get(5));
        bitSet = parser.parse("2-6/3");
        Assert.assertEquals(2, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(1));
        Assert.assertTrue(bitSet.get(4));
        bitSet = parser.parse("1,5,6");
        Assert.assertEquals(3, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(0));
        Assert.assertTrue(bitSet.get(4));
        Assert.assertTrue(bitSet.get(5));

    }

    @Test(expected = InvalidExpressionException.class)
    public void testDaysFieldParserInvalid() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.DAY);
        parser.parse("xyz");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testDaysFieldParserInvalidMin() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.DAY);
        parser.parse("0");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testDaysFieldParserInvalidMax() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.DAY);
        parser.parse("32");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testDaysFieldParserInvalidRangeMin() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.DAY);
        parser.parse("0-6");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testDaysFieldParserInvalidRangeMax() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.DAY);
        parser.parse("1-32");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testDaysFieldParserInvalidStepSize() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.DAY);
        parser.parse("*/0");
    }

    @Test
    public void testMonthFieldParser() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.MONTH);
        BitSet bitSet = parser.parse("*");
        Assert.assertEquals(12, bitSet.cardinality());
        bitSet = parser.parse("6");
        Assert.assertEquals(1, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(5));
        bitSet = parser.parse("2-6");
        Assert.assertEquals(5, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(1));
        Assert.assertTrue(bitSet.get(2));
        Assert.assertTrue(bitSet.get(3));
        Assert.assertTrue(bitSet.get(4));
        Assert.assertTrue(bitSet.get(5));
        bitSet = parser.parse("2-6/3");
        Assert.assertEquals(2, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(1));
        Assert.assertTrue(bitSet.get(4));
        bitSet = parser.parse("1,5,6");
        Assert.assertEquals(3, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(0));
        Assert.assertTrue(bitSet.get(4));
        Assert.assertTrue(bitSet.get(5));
        bitSet = parser.parse("jAnuary,Feb,MAr,apRil,May,JunE,july,auG,sEp,OCtober,NOv,DECember");
        Assert.assertEquals(12, bitSet.cardinality());
    }

    @Test(expected = InvalidExpressionException.class)
    public void testMonthsFieldParserInvalid() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.MONTH);
        parser.parse("xyz");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testMonthsFieldParserInvalidMin() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.MONTH);
        parser.parse("0");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testMonthsFieldParserInvalidMax() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.MONTH);
        parser.parse("13");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testMonthsFieldParserInvalidRangeMin() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.MONTH);
        parser.parse("0-6");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testMonthsFieldParserInvalidRangeMax() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.MONTH);
        parser.parse("1-13");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testMonthsFieldParserInvalidStepSize() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.MONTH);
        parser.parse("*/0");
    }

    @Test
    public void testDayOfWeekFieldParser() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.DAY_OF_WEEK);
        BitSet bitSet = parser.parse("*");
        Assert.assertEquals(7, bitSet.cardinality());
        bitSet = parser.parse("6");
        Assert.assertEquals(1, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(6));
        bitSet = parser.parse("2-6");
        Assert.assertEquals(5, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(2));
        Assert.assertTrue(bitSet.get(3));
        Assert.assertTrue(bitSet.get(4));
        Assert.assertTrue(bitSet.get(5));
        Assert.assertTrue(bitSet.get(6));
        bitSet = parser.parse("2-6/3");
        Assert.assertEquals(2, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(2));
        Assert.assertTrue(bitSet.get(5));
        bitSet = parser.parse("1,5,6");
        Assert.assertEquals(3, bitSet.cardinality());
        Assert.assertTrue(bitSet.get(1));
        Assert.assertTrue(bitSet.get(5));
        Assert.assertTrue(bitSet.get(6));
        bitSet = parser.parse("sunday,mOn,tue,wedneSDay,thursday,fRi,SaturdAy");
        Assert.assertEquals(7, bitSet.cardinality());

    }

    @Test(expected = InvalidExpressionException.class)
    public void testDayOfWeeksFieldParserInvalid() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.DAY_OF_WEEK);
        parser.parse("xyz");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testDayOfWeeksFieldParserInvalidMin() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.DAY_OF_WEEK);
        parser.parse("-1");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testDayOfWeeksFieldParserInvalidMax() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.DAY_OF_WEEK);
        parser.parse("7");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testDayOfWeeksFieldParserInvalidRangeMin() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.DAY_OF_WEEK);
        parser.parse("-1-6");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testDayOfWeeksFieldParserInvalidRangeMax() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.DAY_OF_WEEK);
        parser.parse("0-7");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testDayOfWeeksFieldParserInvalidStepSize() throws InvalidExpressionException {
        CronFieldParser parser = new CronFieldParser(CronFieldType.DAY_OF_WEEK);
        parser.parse("*/0");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testUnreachableSchedule1() throws InvalidExpressionException {
        Schedule.create("* * * 31 2 *");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testUnreachableSchedule2() throws InvalidExpressionException {
        Schedule.create("* * * 31 2,4,6,9,11 *");
    }

    @Test
    public void testReachableSchedule1() throws InvalidExpressionException {
        Schedule.create("0 0 30 1 *");
    }
}
