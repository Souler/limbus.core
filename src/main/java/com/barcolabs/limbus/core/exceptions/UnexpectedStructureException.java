package com.barcolabs.limbus.core.exceptions;

/**
 * Created by barbosa on 31/08/15.
 */
public class UnexpectedStructureException extends ScrapingException {
    public UnexpectedStructureException() {
        super();
    }

    public UnexpectedStructureException(String message) {
        super(message);
    }
}
