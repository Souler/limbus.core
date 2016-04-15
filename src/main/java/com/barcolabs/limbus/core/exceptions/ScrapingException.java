package com.barcolabs.limbus.core.exceptions;

public class ScrapingException extends Exception {
    public ScrapingException() {
        super();
    }

    public ScrapingException(String message) {
        super(message);
    }

    public ScrapingException(String message, Throwable cause) {
        super(message, cause);
    }
}
