package authapi.user.database;

import authapi.user.database.jpa.UserJpaRepository;
import authapi.user.domain.User;
import authapi.user.usecase.port.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MappedUserRepository implements UserRepository {
    private final UserJpaRepository repository;

    public MappedUserRepository(UserJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> find(String email) {
        return repository.findByEmail(email).map(UserEntity::toUser);
    }

    @Override
    public User save(User user) {
        return repository.save(UserEntity.from(user)).toUser();
    }
}
