package authapi.user.entrypoint.request;

import authapi.user.usecase.Login;

import javax.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String email, @NotBlank String password) {
    public Login.Input toInput() {
        return new Login.Input(email, password);
    }
}
