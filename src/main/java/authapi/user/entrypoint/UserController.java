package authapi.user.entrypoint;

import authapi.user.entrypoint.request.RegisterUserRequest;
import authapi.user.entrypoint.response.RegisterUserResponse;
import authapi.user.usecase.RegisterUser;
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

    public UserController(RegisterUser registerUser) {
        this.registerUser = registerUser;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> registerUser(@RequestBody @Valid RegisterUserRequest request) {
        RegisterUserResponse response = RegisterUserResponse.fromOutput(registerUser.execute(request.toInput()));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
