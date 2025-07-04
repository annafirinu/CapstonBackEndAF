package it.epicode.CapstonBackEndAF.exception;


public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
