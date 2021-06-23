package authapi.integration.entrypoint;

import authapi.global.ApiExceptionHandler;
import authapi.user.database.MappedUserRepository;
import authapi.user.domain.User;
import authapi.user.entrypoint.UserController;
import authapi.user.entrypoint.request.RegisterUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {
    private static final String USER_API_URL = "/users";
    private static final String REGISTER_USER_API_URL = USER_API_URL + "/register";

    @Autowired
    private UserController controller;

    @Autowired
    private MappedUserRepository repository;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    void whenRegisteringAUserWithValidAttributesItReturnsTheRegisterUserResponse() throws Exception {
        mockMvc.perform(post(REGISTER_USER_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(validRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(defaultName)))
                .andExpect(jsonPath("$.email", is(defaultEmail)))
                .andExpect(jsonPath("$.password", is(defaultPassword)));

        Optional<User> savedUser = repository.find(defaultEmail);

        assertTrue(savedUser.isPresent());
        assertEquals(defaultName, savedUser.get().name());
        assertEquals(defaultEmail, savedUser.get().email());
        assertEquals(defaultPassword, savedUser.get().password());
    }

    @Test
    void whenRegisteringAUserWithAlreadyExistingEmailItReturnsEmailAlreadyRegisteredMessage() throws Exception {
        repository.save(userWithoutId());

        String expectedMessage = "Email '" + defaultEmail + "' is already registered.";

        mockMvc.perform(post(REGISTER_USER_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(validRequest())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is(expectedMessage)));
    }

    @Test
    void whenRegisteringAUserWithInvalidEmailItReturnsInvalidEmailMessage() throws Exception {
        String invalidEmail = "Invalid Email@gmail.com";

        String expectedMessage = "'" + invalidEmail + "' is not a valid email.";

        mockMvc.perform(post(REGISTER_USER_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new RegisterUserRequest(defaultName, invalidEmail, defaultPassword))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", is(expectedMessage)));

        Optional<User> savedUser = repository.find(invalidEmail);

        assertFalse(savedUser.isPresent());
    }

    @Test
    void whenRegisteringAUserWithInvalidPasswordItReturnsInvalidPasswordMessage() throws Exception {
        String invalidPassword = "ip";

        String expectedMessage = "'" + invalidPassword + "' is not a valid password.";

        mockMvc.perform(post(REGISTER_USER_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new RegisterUserRequest(defaultName, defaultEmail, invalidPassword))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", is(expectedMessage)));

        Optional<User> savedUser = repository.find(defaultEmail);

        assertFalse(savedUser.isPresent());
    }

    @Test
    void whenRegisteringAUserWithBlankNameItReturnsArgumentErrorMessage() throws Exception {
        mockMvc.perform(post(REGISTER_USER_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new RegisterUserRequest("", defaultEmail, defaultPassword))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message", is("must not be blank")))
                .andExpect(jsonPath("$[0].field", is("name")));

        Optional<User> savedUser = repository.find(defaultEmail);

        assertFalse(savedUser.isPresent());
    }

    @Test
    void whenRegisteringAUserWithBlankEmailItReturnsArgumentErrorMessage() throws Exception {
        mockMvc.perform(post(REGISTER_USER_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new RegisterUserRequest(defaultName, "", defaultPassword))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message", is("must not be blank")))
                .andExpect(jsonPath("$[0].field", is("email")));

        Optional<User> savedUser = repository.find("");

        assertFalse(savedUser.isPresent());
    }

    @Test
    void whenRegisteringAUserWithBlankPasswordItReturnsArgumentErrorMessage() throws Exception {
        mockMvc.perform(post(REGISTER_USER_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new RegisterUserRequest(defaultName, defaultEmail, ""))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message", is("must not be blank")))
                .andExpect(jsonPath("$[0].field", is("password")));

        Optional<User> savedUser = repository.find(defaultEmail);

        assertFalse(savedUser.isPresent());
    }

    private final String defaultName = "rodrigo";
    private final String defaultEmail = "rodrigo@hotmail.com";
    private final String defaultPassword = "password";

    private RegisterUserRequest validRequest() {
        return new RegisterUserRequest(defaultName, defaultEmail, defaultPassword);
    }

    private User userWithoutId() {
        return User.builder().name(defaultName).email(defaultEmail).password(defaultPassword).build();
    }
}
