package authapi.user.security;

import authapi.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class UserAuthDetails implements UserDetails {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public static UserAuthDetails from(User user) {
        return new UserAuthDetails(
                user.id(),
                user.name(),
                user.email(),
                user.password()
        );
    }

    private UserAuthDetails(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User toUser() {
        return User.builder().id(id).name(name).email(email).password(password).build();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
