package authapi.user.domain.value;

import authapi.user.domain.exception.InvalidEmail;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {
    @Test
    void whenInstantiatingAValidEmailItReturnsTheEmail() {
        String expectedValue = "r0dr1go@hotmail.com.br";

        Email instantiatedEmail = new Email(expectedValue);

        assertEquals(expectedValue, instantiatedEmail.value());
    }

    @Test
    void whenInstantiatingAInvalidEmailItThrowsInvalidEmail() {
        String invalidValue = "r0dr1go.@hotmail.com.br";

        InvalidEmail exception = assertThrows(InvalidEmail.class, () -> new Email(invalidValue));

        String expectedMessage = "'" + invalidValue + "' is not a valid email.";
        assertEquals(expectedMessage, exception.getMessage());
    }
}