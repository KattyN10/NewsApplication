package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.request.UpdatePassRequest;
import hcmute.kltn.backend.dto.response.JwtAuthResponse;
import hcmute.kltn.backend.dto.request.SignInRequest;
import hcmute.kltn.backend.dto.request.SignUpRequest;
import hcmute.kltn.backend.entity.enum_entity.Role;
import hcmute.kltn.backend.entity.User;
import hcmute.kltn.backend.repository.UserRepo;
import hcmute.kltn.backend.service.AuthService;
import hcmute.kltn.backend.service.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${DEFAULT_AVATAR}")
    private String defaultAvatar;

    @Override
    public User signUp(SignUpRequest signUpRequest) {
        User user = new User();

        if(userRepo.existsByEmail(signUpRequest.getEmail())){
            throw new RuntimeException("Email existed.");
        } else {
            user.setFirstname(signUpRequest.getFirstname());
            user.setLastname(signUpRequest.getLastname());
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            user.setDob(signUpRequest.getDob());
            user.setPremium(false);
            user.setAvatar(signUpRequest.getAvatar());
            user.setRole(Role.USER);
            user.setAvatar(defaultAvatar);
        }

        return userRepo.save(user);
    }

    @Override
    public JwtAuthResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),
                signInRequest.getPassword()));

        var user = userRepo.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));

//        String passHash = passwordEncoder.encode(signInRequest.getPassword());
//        if (user.getPassword() != passHash){
//            throw new RuntimeException("Invalid email or password.");
//        } else {
            var jwtToken = jwtService.generateToken(user);

            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setToken(jwtToken);
            return jwtAuthResponse;
//        }
    }

    @Override
    public String updatePassword(UpdatePassRequest updatePassRequest) {

        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepo.findByEmail(name)
                .orElseThrow(() -> new RuntimeException("User not found."));
        boolean oldPassMatchOldPass = passwordEncoder.matches(updatePassRequest.getOldPassword(), user.getPassword());
        boolean newPassMatchOldPass = passwordEncoder.matches(updatePassRequest.getNewPassword(), user.getPassword());
        String newPassHash = passwordEncoder.encode(updatePassRequest.getNewPassword());

        if (!oldPassMatchOldPass){
            throw new RuntimeException("Old password entered incorrectly.");
        } else if (!updatePassRequest.getNewPassword().equals(updatePassRequest.getReEnterPassword())){
            throw new RuntimeException("The re-entered password does not match.");
        } else if (newPassMatchOldPass){
            throw new RuntimeException("The new password must not be the same as the old password");
        } else {
            user.setPassword(newPassHash);
            userRepo.save(user);
            return "Update password successfully.";
        }
    }

}
