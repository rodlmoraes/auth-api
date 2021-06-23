package authapi.user.usecase;

import authapi.global.UseCase;
import authapi.user.domain.User;
import authapi.user.usecase.exception.EmailAlreadyRegistered;
import authapi.user.usecase.port.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class RegisterUser implements UseCase<RegisterUser.Input, RegisterUser.Output> {
    private final UserRepository repository;

    public RegisterUser(UserRepository repository) {
        this.repository = repository;
    }

    public Output execute(Input userInput) {
        String email = userInput.email();

        User userToSave = User.builder()
                .name(userInput.name())
                .email(email)
                .password(userInput.password())
                .build();

        if (repository.find(email).isPresent()) throw new EmailAlreadyRegistered(email);

        return new Output(repository.save(userToSave));
    }

    public static record Input(String name, String email, String password) implements UseCase.Input {}
    public static record Output(User user) implements UseCase.Output {}
}
