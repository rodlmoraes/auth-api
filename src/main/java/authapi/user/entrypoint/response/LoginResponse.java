package authapi.user.entrypoint.response;

import authapi.user.domain.User;
import authapi.user.usecase.Login;

public record LoginResponse(Long id, String name, String email) {
    public static LoginResponse fromOutput(Login.Output output) {
        User user = output.user();

        return new LoginResponse(user.id(), user.name(), user.email());
    }
}
