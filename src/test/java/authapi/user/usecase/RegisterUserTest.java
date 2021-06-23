package authapi.user.usecase;

import authapi.user.domain.User;
import authapi.user.domain.exception.InvalidEmail;
import authapi.user.domain.exception.InvalidPassword;
import authapi.user.usecase.exception.EmailAlreadyRegistered;
import authapi.user.usecase.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterUserTest {
    @InjectMocks
    private RegisterUser registerUser;

    @Mock
    private UserRepository repository;

    @Test
    void whenRegisteringAUserWithValidAttributesItReturnsTheUser() {
        User expectedUser = user();

        when(repository.find(defaultEmail)).thenReturn(Optional.empty());
        when(repository.save(userWithoutId())).thenReturn(expectedUser);

        User savedUser = registerUser.execute(validInput()).user();

        assertEquals(expectedUser, savedUser);
    }

    @Test
    void whenRegisteringAUserWithAlreadyExistingEmailItThrowsEmailAlreadyRegistered() {
        when(repository.find(defaultEmail)).thenReturn(Optional.of(user()));

        EmailAlreadyRegistered exception = assertThrows(EmailAlreadyRegistered.class, () -> registerUser.execute(validInput()));

        String expectedMessage = "Email '" + defaultEmail + "' is already registered.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void whenRegisteringAUserWithInvalidEmailItThrowsInvalidEmail() {
        String invalidEmail = "Invalid Email@gmail.com";

        InvalidEmail exception = assertThrows(InvalidEmail.class, () -> registerUser.execute(new RegisterUser.Input(defaultName, invalidEmail, defaultPassword)));

        String expectedMessage = "'" + invalidEmail + "' is not a valid email.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void whenRegisteringAUserWithInvalidPasswordItThrowsInvalidPassword() {
        String invalidPassword = "ip";

        InvalidPassword exception = assertThrows(InvalidPassword.class, () -> registerUser.execute(new RegisterUser.Input(defaultName, defaultEmail, invalidPassword)));

        String expectedMessage = "'" + invalidPassword + "' is not a valid password.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    private final String defaultName = "rodrigo";
    private final String defaultEmail = "rodrigo@hotmail.com";
    private final String defaultPassword = "password";

    private RegisterUser.Input validInput() {
        return new RegisterUser.Input(defaultName, defaultEmail, defaultPassword);
    }

    private User userWithoutId() {
        return User.builder().name(defaultName).email(defaultEmail).password(defaultPassword).build();
    }

    private User user() {
        return User.builder().id(1L).name(defaultName).email(defaultEmail).password(defaultPassword).build();
    }
}
