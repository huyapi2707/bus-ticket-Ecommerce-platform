package org.huydd.bus_ticket_Ecommercial_platform.exceptions;

public class NoContentException extends RuntimeException{
    private String message;
    public NoContentException(String message) {
        super(message);
    }
}
