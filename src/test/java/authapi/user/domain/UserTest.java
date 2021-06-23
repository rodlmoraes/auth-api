package authapi.user.domain;

import authapi.user.domain.exception.InvalidEmail;
import authapi.user.domain.exception.InvalidPassword;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private final Long defaultId = 1L;
    private final String defaultName = "rodrigo";
    private final String defaultEmail = "rodrigo@hotmail.com";
    private final String defaultPassword = "password";

    @Test
    void whenInstantiatingAUserWithValidAttributesItReturnsTheUser() {
        User instantiatedUser = User.builder().id(defaultId).name(defaultName).email(defaultEmail).password(defaultPassword).build();

        assertEquals(defaultId, instantiatedUser.id());
        assertEquals(defaultName, instantiatedUser.name());
        assertEquals(defaultEmail, instantiatedUser.email());
        assertEquals(defaultPassword, instantiatedUser.password());
    }

    @Test
    void whenInstantiatingAUserWithoutIdItReturnsTheUser() {
        User instantiatedUser = User.builder().name(defaultName).email(defaultEmail).password(defaultPassword).build();

        assertNull(instantiatedUser.id());
        assertEquals(defaultName, instantiatedUser.name());
        assertEquals(defaultEmail, instantiatedUser.email());
        assertEquals(defaultPassword, instantiatedUser.password());
    }

    @Test
    void whenInstantiatingAUserWithInvalidEmailItThrowsInvalidEmail() {
        String invalidEmail = "r0dr1go@hotmail.com.";

        InvalidEmail exception = assertThrows(InvalidEmail.class, () -> User.builder().id(defaultId).name(defaultName).email(invalidEmail).password(defaultPassword).build());

        String expectedMessage = "'" + invalidEmail + "' is not a valid email.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void whenInstantiatingAUserWithInvalidPasswordItThrowsInvalidPassword() {
        String invalidPassword = "pass";

        InvalidPassword exception = assertThrows(InvalidPassword.class, () -> User.builder().id(defaultId).name(defaultName).email(defaultEmail).password(invalidPassword).build());

        String expectedMessage = "'" + invalidPassword + "' is not a valid password.";
        assertEquals(expectedMessage, exception.getMessage());
    }
}