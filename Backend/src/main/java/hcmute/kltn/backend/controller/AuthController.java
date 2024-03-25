package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.ApiResponse;
import hcmute.kltn.backend.dto.JwtAuthResponse;
import hcmute.kltn.backend.dto.SignInRequest;
import hcmute.kltn.backend.dto.SignUpRequest;
import hcmute.kltn.backend.entity.User;
import hcmute.kltn.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<User>> signUp(@RequestBody SignUpRequest signUpRequest) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setData(authService.signUp(signUpRequest));
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> signIn(@RequestBody SignInRequest signInRequest){
        ApiResponse<JwtAuthResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(authService.signIn(signInRequest));
        return ResponseEntity.ok(apiResponse);
    }

}
