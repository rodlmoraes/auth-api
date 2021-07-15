package authapi.user.entrypoint;

import authapi.user.entrypoint.request.LoginRequest;
import authapi.user.entrypoint.request.RegisterUserRequest;
import authapi.user.entrypoint.response.LoginResponse;
import authapi.user.entrypoint.response.RegisterUserResponse;
import authapi.user.usecase.Login;
import authapi.user.usecase.RegisterUser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final RegisterUser registerUser;
    private final Login login;

    public UserController(RegisterUser registerUser, Login login) {
        this.registerUser = registerUser;
        this.login = login;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> registerUser(@RequestBody @Valid RegisterUserRequest request) {
        RegisterUserResponse response = RegisterUserResponse.fromOutput(registerUser.execute(request.toInput()));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        Login.Output output = login.execute(request.toInput());

        LoginResponse response = LoginResponse.fromOutput(output);

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.AUTHORIZATION, output.token()).body(response);
    }
}
