package authapi.user.entrypoint.response;

import authapi.user.domain.User;
import authapi.user.usecase.RegisterUser;

public record RegisterUserResponse(Long id, String name, String email, String password) {
    public static RegisterUserResponse fromOutput(RegisterUser.Output output) {
        User user = output.user();

        return new RegisterUserResponse(user.id(), user.name(), user.email(), user.password());
    }
}
