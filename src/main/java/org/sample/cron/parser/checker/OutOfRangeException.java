package org.sample.cron.parser.checker;

public class OutOfRangeException extends RuntimeException {
    public OutOfRangeException(String message) {
        super(message);
    }
}