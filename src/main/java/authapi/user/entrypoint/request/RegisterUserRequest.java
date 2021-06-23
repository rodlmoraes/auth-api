package authapi.user.entrypoint.request;

import authapi.user.usecase.RegisterUser;

import javax.validation.constraints.NotBlank;

public record RegisterUserRequest(@NotBlank String name, @NotBlank String email, @NotBlank String password) {
    public RegisterUser.Input toInput() {
        return new RegisterUser.Input(name, email, password);
    }
}
