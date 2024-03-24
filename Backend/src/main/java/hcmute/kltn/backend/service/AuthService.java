package hcmute.kltn.backend.service;

import hcmute.kltn.backend.dto.JwtAuthResponse;
import hcmute.kltn.backend.dto.SignInRequest;
import hcmute.kltn.backend.dto.SignUpRequest;
import hcmute.kltn.backend.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Date;

public interface AuthService {
    User signUp(SignUpRequest signUpRequest);
    JwtAuthResponse signIn(SignInRequest signInRequest);
}
