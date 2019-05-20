package com.asahaf.javacron;

public class InvalidExpressionException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidExpressionException() {
        super();
    }

    public InvalidExpressionException(String message) {
        super(message);
    }

    public InvalidExpressionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidExpressionException(Throwable cause) {
        super(cause);
    }
}
