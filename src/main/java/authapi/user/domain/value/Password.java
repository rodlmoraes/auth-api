package authapi.user.domain.value;

import authapi.user.domain.exception.InvalidPassword;

public record Password(String value) {
    public Password {
        if (value.length() < 6) throw new InvalidPassword(value);
    }
}
