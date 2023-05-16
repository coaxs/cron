package org.sample.cron.parser.part;

public class ParserNotFoundException extends RuntimeException {
    public ParserNotFoundException(String message) {
        super(message);
    }
}
