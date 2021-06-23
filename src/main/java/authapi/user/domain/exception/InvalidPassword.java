package authapi.user.domain.exception;

public class InvalidPassword extends RuntimeException {
    public InvalidPassword(String password) {
        super(String.format("'%s' is not a valid password.", password));
    }
}
