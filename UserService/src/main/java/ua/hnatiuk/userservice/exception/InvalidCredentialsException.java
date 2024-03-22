package ua.hnatiuk.userservice.exception;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
