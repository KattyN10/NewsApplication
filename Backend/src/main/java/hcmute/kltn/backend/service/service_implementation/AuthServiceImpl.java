package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.JwtAuthResponse;
import hcmute.kltn.backend.dto.SignInRequest;
import hcmute.kltn.backend.dto.SignUpRequest;
import hcmute.kltn.backend.entity.Role;
import hcmute.kltn.backend.entity.User;
import hcmute.kltn.backend.repository.UserRepo;
import hcmute.kltn.backend.service.AuthService;
import hcmute.kltn.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public User signUp(SignUpRequest signUpRequest) {
        User user = new User();

        user.setFirstname(signUpRequest.getFirstname());
        user.setLastname(signUpRequest.getLastname());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setDob(signUpRequest.getDob());
        user.setPremium(0);
        user.setAvatar(signUpRequest.getAvatar());
        user.setRole(Role.USER);

        return userRepo.save(user);
    }

    @Override
    public JwtAuthResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),
                signInRequest.getPassword()));

        var user = userRepo.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));

        var jwtToken = jwtService.generateToken(user);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setToken(jwtToken);
        return jwtAuthResponse;
    }
}
