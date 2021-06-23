package authapi.user.database;

import authapi.user.domain.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    public UserEntity(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserEntity() {}

    public User toUser() {
        return User.builder().id(id).name(name).email(email).password(password).build();
    }

    public static UserEntity from(User user) {
        return new UserEntity(user.id(), user.name(), user.email(), user.password());
    }
}
