package authapi.user.domain.value;

import authapi.user.domain.exception.InvalidPassword;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasswordTest {
    @Test
    void whenInstantiatingAValidPasswordItReturnsThePassword() {
        String expectedValue = "valid password";

        Password instantiatedPassword = new Password(expectedValue);

        assertEquals(expectedValue, instantiatedPassword.value());
    }

    @Test
    void whenInstantiatingAInvalidPasswordItThrowsInvalidPassword() {
        String invalidValue = "pass";

        InvalidPassword exception = assertThrows(InvalidPassword.class, () -> new Password(invalidValue));

        String expectedMessage = "'" + invalidValue + "' is not a valid password.";
        assertEquals(expectedMessage, exception.getMessage());
    }
}