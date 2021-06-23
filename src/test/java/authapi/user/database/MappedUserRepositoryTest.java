package authapi.user.database;

import authapi.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.PersistenceException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ComponentScan("authapi.user.database")
class MappedUserRepositoryTest {
    @Autowired
    private MappedUserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private Long currentId;
    private final String defaultName = "rodrigo";
    private final String defaultEmail = "rodrigo@hotmail.com";
    private final String defaultPassword = "password";
    private final String differentEmail = "new@email.com";

    @BeforeEach
    void persistTestUser() {
        currentId = entityManager.persistAndGetId(new UserEntity(null, defaultName, defaultEmail, defaultPassword), Long.class);
    }

    @Test
    void whenFindIsCalledWithExistingEmailItReturnsTheUser() {
        Optional<User> foundUser = repository.find(defaultEmail);

        assertEquals(foundUser, Optional.of(userWith(currentId)));
    }

    @Test
    void whenFindIsCalledWithNonExistingEmailItReturnsEmptyOptional() {
        Optional<User> foundUser = repository.find("not existing");

        assertEquals(Optional.empty(), foundUser);
    }

    @Test
    void whenSaveIsCalledWithValidUserItReturnsTheUser() {
        User savedUser = repository.save(User.builder().name(defaultName).email(differentEmail).password(defaultPassword).build());

        User expectedUser = User.builder().id(savedUser.id()).name(defaultName).email(differentEmail).password(defaultPassword).build();

        assertEquals(expectedUser, savedUser);
        assertEquals(expectedUser, entityManager.find(UserEntity.class, savedUser.id()).toUser());
    }

    @Test
    void whenSaveIsCalledWithUserWithAlreadyExistingEmailItThrowsPersistenceException() {
        assertThrows(PersistenceException.class, () -> {
            repository.save(userWithoutId());
            entityManager.flush();
        });
    }

    @Test
    void whenSaveIsCalledWithUserWithoutNameItThrowsPersistenceException() {
        assertThrows(PersistenceException.class, () -> {
            repository.save(User.builder().email(differentEmail).password(defaultPassword).build());
            entityManager.flush();
        });
    }

    private User userWith(Long id) {
        return User.builder().id(id).name(defaultName).email(defaultEmail).password(defaultPassword).build();
    }

    private User userWithoutId() {
        return User.builder().name(defaultName).email(defaultEmail).password(defaultPassword).build();
    }
}
