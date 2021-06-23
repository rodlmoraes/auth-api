package authapi.user.usecase.exception;

public class EmailAlreadyRegistered extends RuntimeException {
    public EmailAlreadyRegistered(String email) {
        super(String.format("Email '%s' is already registered.", email));
    }
}
