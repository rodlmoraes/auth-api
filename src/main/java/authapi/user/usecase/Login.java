package authapi.user.usecase;

import authapi.global.UseCase;
import authapi.user.domain.User;
import authapi.user.security.JwtTokenUtil;
import authapi.user.security.UserAuthDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class Login implements UseCase<Login.Input, Login.Output> {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil tokenUtil;

    public Login(AuthenticationManager authenticationManager, JwtTokenUtil tokenUtil) {
        this.authenticationManager = authenticationManager;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public Output execute(Input input) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.email(), input.password())
        );

        User user = ((UserAuthDetails) authentication.getPrincipal()).toUser();

        return new Output(user, tokenUtil.generateAccessToken(user));
    }

    public static record Input(String email, String password) implements UseCase.Input {}
    public static record Output(User user, String token) implements UseCase.Output {}
}
