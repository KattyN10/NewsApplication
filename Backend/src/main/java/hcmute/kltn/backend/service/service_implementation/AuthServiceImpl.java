package hcmute.kltn.backend.service.service_implementation;

import com.cloudinary.Cloudinary;
import hcmute.kltn.backend.dto.JwtAuthResponse;
import hcmute.kltn.backend.dto.SignInRequest;
import hcmute.kltn.backend.dto.SignUpRequest;
import hcmute.kltn.backend.entity.enum_entity.Role;
import hcmute.kltn.backend.entity.User;
import hcmute.kltn.backend.repository.UserRepo;
import hcmute.kltn.backend.service.AuthService;
import hcmute.kltn.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


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
            user.setAvatar("???");
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

    public String saveImage(MultipartFile file, String typeImage) throws IOException {
        Cloudinary cloudinary = new Cloudinary(
                "cloudinary://"
                        + apiKey + ":"
                        + apiSecret + "@"
                        + cloudName
        );
        Map<String, String> params = new HashMap<>();
        params.put("folder", "images_upload");
        if (typeImage.equals("user")) {
            params.put("folder", "user_avatar");
        }
        if (typeImage.equals("article")) {
            params.put("folder", "article_avatar");
        } else {
            throw new RuntimeException("Invalid input type.");
        }
        File uploadedFile = File.createTempFile("temp", file.getOriginalFilename());
        file.transferTo(uploadedFile);
        Map uploadResult = cloudinary.uploader().upload(uploadedFile, params);

        String imageUrl = (String) uploadResult.get("secure_url");
        uploadedFile.delete();
        return imageUrl;
    }
}
