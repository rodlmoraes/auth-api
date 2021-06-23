package authapi.user.domain.value;

import authapi.user.domain.exception.InvalidEmail;

public record Email(String value) {
    private static final String VALID_EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    public Email {
        if (!value.matches(VALID_EMAIL_REGEX)) throw new InvalidEmail(value);
    }
}
