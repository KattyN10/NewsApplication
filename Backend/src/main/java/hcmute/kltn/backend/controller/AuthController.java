package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.JwtAuthResponse;
import hcmute.kltn.backend.dto.SignInRequest;
import hcmute.kltn.backend.dto.SignUpRequest;
import hcmute.kltn.backend.entity.User;
import hcmute.kltn.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<User> signUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthResponse> signIn(@RequestBody SignInRequest signInRequest){
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }

}
