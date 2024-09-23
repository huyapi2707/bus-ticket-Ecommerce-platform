package org.huydd.bus_ticket_Ecommercial_platform.exceptions;

public class SeatNotAvailableException extends RuntimeException{
    public SeatNotAvailableException(String message) {
        super(message);
    }
}
