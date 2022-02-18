package com.asahaf.javacron;

import java.util.BitSet;
import java.util.Map;
import java.util.TreeMap;

class CronFieldParser {

    private static Map<String, Integer> MONTHS_NAMES;
    private static Map<String, Integer> DAYS_OF_WEEK_NAMES;

    static {
        CronFieldParser.MONTHS_NAMES = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
        CronFieldParser.MONTHS_NAMES.put("January", 1);
        CronFieldParser.MONTHS_NAMES.put("Jan", 1);
        CronFieldParser.MONTHS_NAMES.put("February", 2);
        CronFieldParser.MONTHS_NAMES.put("Feb", 2);
        CronFieldParser.MONTHS_NAMES.put("March", 3);
        CronFieldParser.MONTHS_NAMES.put("Mar", 3);
        CronFieldParser.MONTHS_NAMES.put("April", 4);
        CronFieldParser.MONTHS_NAMES.put("Apr", 4);
        CronFieldParser.MONTHS_NAMES.put("May", 5);
        CronFieldParser.MONTHS_NAMES.put("June", 6);
        CronFieldParser.MONTHS_NAMES.put("Jun", 6);
        CronFieldParser.MONTHS_NAMES.put("July", 7);
        CronFieldParser.MONTHS_NAMES.put("Jul", 7);
        CronFieldParser.MONTHS_NAMES.put("August", 8);
        CronFieldParser.MONTHS_NAMES.put("Aug", 8);
        CronFieldParser.MONTHS_NAMES.put("September", 9);
        CronFieldParser.MONTHS_NAMES.put("Sep", 9);
        CronFieldParser.MONTHS_NAMES.put("October", 10);
        CronFieldParser.MONTHS_NAMES.put("Oct", 10);
        CronFieldParser.MONTHS_NAMES.put("November", 11);
        CronFieldParser.MONTHS_NAMES.put("Nov", 11);
        CronFieldParser.MONTHS_NAMES.put("December", 12);
        CronFieldParser.MONTHS_NAMES.put("Dec", 12);

        CronFieldParser.DAYS_OF_WEEK_NAMES = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
        CronFieldParser.DAYS_OF_WEEK_NAMES.put("Sunday", 0);
        CronFieldParser.DAYS_OF_WEEK_NAMES.put("Sun", 0);
        CronFieldParser.DAYS_OF_WEEK_NAMES.put("Monday", 1);
        CronFieldParser.DAYS_OF_WEEK_NAMES.put("Mon", 1);
        CronFieldParser.DAYS_OF_WEEK_NAMES.put("Tuesday", 2);
        CronFieldParser.DAYS_OF_WEEK_NAMES.put("Tue", 2);
        CronFieldParser.DAYS_OF_WEEK_NAMES.put("Wednesday", 3);
        CronFieldParser.DAYS_OF_WEEK_NAMES.put("Wed", 3);
        CronFieldParser.DAYS_OF_WEEK_NAMES.put("Thursday", 4);
        CronFieldParser.DAYS_OF_WEEK_NAMES.put("Thu", 4);
        CronFieldParser.DAYS_OF_WEEK_NAMES.put("Friday", 5);
        CronFieldParser.DAYS_OF_WEEK_NAMES.put("Fri", 5);
        CronFieldParser.DAYS_OF_WEEK_NAMES.put("Saturday", 6);
        CronFieldParser.DAYS_OF_WEEK_NAMES.put("sat", 6);
    }

    private CronFieldType fieldType;
    private Map<String, Integer> choices;
    private int length;
    private int maxAllowedValue;
    private int minAllowedValue;
    private String fieldName;

    CronFieldParser(CronFieldType fieldType) {
        this.fieldType = fieldType;
        switch (fieldType) {
            case SECOND:
            case MINUTE:
                this.fieldName = this.fieldType.toString().toLowerCase();
                this.length = 60;
                this.maxAllowedValue = 59;
                this.minAllowedValue = 0;
                break;
            case HOUR:
                this.fieldName = this.fieldType.toString().toLowerCase();
                this.length = 24;
                this.maxAllowedValue = 23;
                this.minAllowedValue = 0;
                break;
            case DAY:
                this.fieldName = this.fieldType.toString().toLowerCase();
                this.length = 31;
                this.maxAllowedValue = 31;
                this.minAllowedValue = 1;
                break;
            case MONTH:
                this.choices = CronFieldParser.MONTHS_NAMES;
                this.fieldName = this.fieldType.toString().toLowerCase();
                this.length = 12;
                this.maxAllowedValue = 12;
                this.minAllowedValue = 1;
                break;
            case DAY_OF_WEEK:
                this.choices = CronFieldParser.DAYS_OF_WEEK_NAMES;
                this.fieldName = "day of week";
                this.length = 7;
                this.maxAllowedValue = 6;
                this.minAllowedValue = 0;
                break;
        }
    }

    public CronFieldType getFieldType() {
        return fieldType;
    }

    public int getMaxAllowedValue() {
        return maxAllowedValue;
    }

    public int getMinAllowedValue() {
        return minAllowedValue;
    }

    public int getLength() {
        return this.length;
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private int getChoiceNumber(String choice) {
        if (!this.choices.containsKey(choice)) {
            return -1;
        }
        return this.choices.get(choice);
    }

    private int parseValue(String token) {
        int value = -1;
        if (this.choices != null) {
            if (this.isInteger(token)) {
                value = Integer.parseInt(token);
            } else {
                value = this.getChoiceNumber(token);
            }
        } else {
            value = Integer.parseInt(token);
        }
        return value;
    }

    public BitSet parse(String token) throws InvalidExpressionException {
        // This is when last day of the month is specified
        if (this.fieldType == CronFieldType.DAY_OF_WEEK) {
            if (token.length() == 2 && token.endsWith("l")) {
                return this.parseLiteral(token.substring(0, 1));
            }
        }
        if (token.indexOf(",") > -1) {
            BitSet bitSet = new BitSet(this.length);
            String[] items = token.split(",");
            for (String item : items) {
                bitSet.or(this.parse(item));
            }
            return bitSet;
        }

        if (token.indexOf("/") > -1)
            return this.parseStep(token);

        if (token.indexOf("-") > -1)
            return this.parseRange(token);

        if (token.equalsIgnoreCase("*"))
            return this.parseAsterisk();

        return this.parseLiteral(token);
    }

    private BitSet parseStep(String token) throws InvalidExpressionException {
        try {
            String[] tokenParts = token.split("/");
            if (tokenParts.length != 2) {
                throw new InvalidExpressionException(String.format("invalid %s field: \"%s\"", this.fieldName, token));
            }
            String stepSizePart = tokenParts[1];
            int stepSize = this.parseValue(stepSizePart);
            if (stepSize < 1) {
                throw new InvalidExpressionException(
                        String.format("invalid %s field: \"%s\". minimum allowed step (every) value is \"1\"",
                                this.fieldName, token));
            }
            String numSetPart = tokenParts[0];
            if (!numSetPart.contains("-") && numSetPart != "*" && isInteger(numSetPart)) {
                // if number is a single digit, it should be a range starts with that
                // number and ends with the maximum allowed value for the field type
                numSetPart = String.format("%s-%d", numSetPart, this.maxAllowedValue);
            }
            BitSet numSet = this.parse(numSetPart);
            BitSet stepsSet = new BitSet(this.length);
            for (int i = numSet.nextSetBit(0); i < this.length; i += stepSize) {
                stepsSet.set(i);
            }
            stepsSet.and(numSet);
            return stepsSet;
        } catch (NumberFormatException ex) {
            throw new InvalidExpressionException(String.format("invalid %s field: \"%s\"", this.fieldName, token), ex);
        }
    }

    private BitSet parseRange(String token) throws InvalidExpressionException {
        String[] rangeParts = token.split("-");
        if (rangeParts.length != 2) {
            throw new InvalidExpressionException(String.format("invalid %s field: \"%s\"", this.fieldName, token));
        }
        try {
            int from = this.minAllowedValue;
            int to = this.maxAllowedValue;

            from = this.parseValue(rangeParts[0]);
            if (from < 0) {
                throw new InvalidExpressionException(
                        String.format("invalid %s field: \"%s\".", this.fieldName, token, this.minAllowedValue));
            }
            if (from < this.minAllowedValue) {
                throw new InvalidExpressionException(
                        String.format("invalid %s field: \"%s\". minimum allowed value for %s field is \"%d\"",
                                this.fieldName, token, this.fieldName, this.minAllowedValue));
            }

            to = this.parseValue(rangeParts[1]);
            if (to < 0) {
                throw new InvalidExpressionException(
                        String.format("invalid %s field: \"%s\".", this.fieldName, token, this.minAllowedValue));
            }
            if (to > this.maxAllowedValue) {
                throw new InvalidExpressionException(
                        String.format("invalid %s field: \"%s\". maximum allowed value for %s field is \"%d\"",
                                this.fieldName, token, this.fieldName, this.maxAllowedValue));
            }
            if (to < from) {
                throw new InvalidExpressionException(String.format(
                        "invalid %s field: \"%s\". the start of range value must be less than or equal the end value",
                        this.fieldName, token));
            }

            BitSet bitSet = new BitSet(this.length);
            int fromIndex = from - this.minAllowedValue;
            int toIndex = to - this.minAllowedValue;
            for (int i = fromIndex; i <= toIndex; i++) {
                bitSet.set(i);
            }
            return bitSet;
        } catch (NumberFormatException ex) {
            throw new InvalidExpressionException(String.format("invalid %s field: \"%s\"", this.fieldName, token), ex);
        }
    }

    private BitSet parseAsterisk() {
        BitSet bitSet = new BitSet(this.length);
        bitSet.set(0, this.length);
        return bitSet;
    }

    private BitSet parseLiteral(String token) throws InvalidExpressionException {
        BitSet bitSet = new BitSet(this.length);
        try {
            int number = this.parseValue(token);
            if (number < 0) {
                throw new InvalidExpressionException(
                        String.format("invalid %s field: \"%s\".", this.fieldName, token, this.minAllowedValue));
            }
            if (number < this.minAllowedValue) {
                throw new InvalidExpressionException(
                        String.format("invalid %s field: \"%s\". minimum allowed value for %s field is \"%d\"",
                                this.fieldName, token, this.fieldName, this.minAllowedValue));
            }
            if (number > this.maxAllowedValue) {
                throw new InvalidExpressionException(
                        String.format("invalid %s field: \"%s\". maximum allowed value for %s field is \"%d\"",
                                this.fieldName, token, this.fieldName, this.maxAllowedValue));
            }
            bitSet.set(number - this.minAllowedValue);
        } catch (NumberFormatException ex) {
            throw new InvalidExpressionException(String.format("invalid %s field: \"%s\"", this.fieldName, token), ex);
        }
        return bitSet;
    }
}
