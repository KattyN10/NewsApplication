package hcmute.kltn.backend.service;

import hcmute.kltn.backend.dto.JwtAuthResponse;
import hcmute.kltn.backend.dto.SignInRequest;
import hcmute.kltn.backend.dto.SignUpRequest;
import hcmute.kltn.backend.entity.User;

public interface AuthService {
    User signUp(SignUpRequest signUpRequest);
    JwtAuthResponse signIn(SignInRequest signInRequest);
}
