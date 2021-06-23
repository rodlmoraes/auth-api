package authapi.user.domain.exception;

public class InvalidEmail extends RuntimeException {
    public InvalidEmail(String email) {
        super(String.format("'%s' is not a valid email.", email));
    }
}
