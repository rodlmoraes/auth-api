package authapi.user.usecase.port;

import authapi.user.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> find(String email);
    User save(User user);
}
