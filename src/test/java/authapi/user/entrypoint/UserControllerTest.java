package authapi.user.entrypoint;

import authapi.user.domain.User;
import authapi.user.domain.exception.InvalidEmail;
import authapi.user.domain.exception.InvalidPassword;
import authapi.user.entrypoint.request.RegisterUserRequest;
import authapi.user.entrypoint.response.RegisterUserResponse;
import authapi.user.usecase.RegisterUser;
import authapi.user.usecase.exception.EmailAlreadyRegistered;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    private UserController controller;

    @Mock
    private RegisterUser registerUser;

    @Test
    void whenRegisteringAUserWithValidAttributesItReturnsTheRegisterUserResponse() {
        when(registerUser.execute(validInput())).thenReturn(output());

        ResponseEntity<RegisterUserResponse> response = controller.registerUser(validRequest());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(defaultId, Objects.requireNonNull(response.getBody()).id());
        assertEquals(defaultName, Objects.requireNonNull(response.getBody()).name());
        assertEquals(defaultEmail, Objects.requireNonNull(response.getBody()).email());
        assertEquals(defaultPassword, Objects.requireNonNull(response.getBody()).password());
    }

    @Test
    void whenRegisteringAUserWithAlreadyExistingEmailItThrowsEmailAlreadyRegistered() {
        when(registerUser.execute(validInput())).thenThrow(new EmailAlreadyRegistered(defaultEmail));

        EmailAlreadyRegistered exception = assertThrows(EmailAlreadyRegistered.class, () -> controller.registerUser(validRequest()));

        String expectedMessage = "Email '" + defaultEmail + "' is already registered.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void whenRegisteringAUserWithInvalidEmailItThrowsInvalidEmail() {
        String invalidEmail = "Invalid Email@gmail.com";

        when(registerUser.execute(new RegisterUser.Input(defaultName, invalidEmail, defaultPassword))).thenThrow(new InvalidEmail(invalidEmail));

        InvalidEmail exception = assertThrows(InvalidEmail.class, () -> controller.registerUser(new RegisterUserRequest(defaultName, invalidEmail, defaultPassword)));

        String expectedMessage = "'" + invalidEmail + "' is not a valid email.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void whenRegisteringAUserWithInvalidPasswordItThrowsInvalidPassword() {
        String invalidPassword = "ip";

        when(registerUser.execute(new RegisterUser.Input(defaultName, defaultEmail, invalidPassword))).thenThrow(new InvalidPassword(invalidPassword));

        InvalidPassword exception = assertThrows(InvalidPassword.class, () -> controller.registerUser(new RegisterUserRequest(defaultName, defaultEmail, invalidPassword)));

        String expectedMessage = "'" + invalidPassword + "' is not a valid password.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    private final Long defaultId = 1L;
    private final String defaultName = "rodrigo";
    private final String defaultEmail = "rodrigo@hotmail.com";
    private final String defaultPassword = "password";

    private RegisterUser.Input validInput() {
        return new RegisterUser.Input(defaultName, defaultEmail, defaultPassword);
    }

    private RegisterUser.Output output() {
        return new RegisterUser.Output(user());
    }

    private RegisterUserRequest validRequest() {
        return new RegisterUserRequest(defaultName, defaultEmail, defaultPassword);
    }

    private User user() {
        return User.builder().id(defaultId).name(defaultName).email(defaultEmail).password(defaultPassword).build();
    }
}
