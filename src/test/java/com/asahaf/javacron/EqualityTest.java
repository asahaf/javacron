package com.asahaf.javacron;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.junit.Assert;

@RunWith(Parameterized.class)
public class EqualityTest {

    @Parameterized.Parameters
    public static Collection<?> getTestCase() {
        return Arrays.asList(new Object[][] {

                { "* * * * *", "* * * * *", true }, { "0 * * * * *", "* * * * *", true },
                { "0 0 0 1 1 0", "0 0 0 1 1 0", true }, { "0 0 0 1 1 0", "0 0 0 1 1 0", true },
                { "0,20,40 0 0 1 1 0", "*/20 0 0 1 1 0", true }, { "0 0,20,40 0 1 1 0", "0 */20 0 1 1 0", true },
                { "0 0 */10 1 1 0", "0 0 0,10,20 1 1 0", true }, { "0 0 0 */20 1 0", "0 0 0 1,21 1 0", true },
                { "0 0 0 1 1,6,11 0", "0 0 0 1 */5 0", true }, { "0 0 0 1 1 0,2,4,6", "0 0 0 1 1 */2", true },
                { "0 0 0 1 1 0", "1 0 0 1 1 0", false }, { "0 0 0 1 1 0", "0 1 0 1 1 0", false },
                { "0 0 0 1 1 0", "0 0 1 1 1 0", false }, { "0 0 0 1 1 0", "0 0 0 2 1 0", false },
                { "0 0 0 1 1 0", "0 0 0 1 2 0", false }, { "0 0 0 1 1 0", "0 0 0 1 1 1", false }

        });
    }

    private String first;
    private String second;
    private boolean expectedResult;

    public EqualityTest(String first, String second, boolean expectedResult) {
        this.first = first;
        this.second = second;
        this.expectedResult = expectedResult;
    }

    @Test
    public void testEquality() {
        try {
            Schedule first = Schedule.create(this.first);
            Schedule second = Schedule.create(this.second);
            Assert.assertEquals(this.expectedResult, first.equals(second));

        } catch (Exception exp) {
            Assert.fail(exp.toString());
        }
    }
}
