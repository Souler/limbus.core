package com.barcolabs.limbus.core.exceptions;

public class UnexpectedStructureException extends ScrapingException {

    public UnexpectedStructureException(String message) {
        super(message);
    }

    public UnexpectedStructureException(String message, String body) {
        super(message, new UnexpectedStructureException(body));
    }
}
