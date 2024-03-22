package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.repository.UserRepo;
import hcmute.kltn.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws RuntimeException {
                return userRepo.findByEmail(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
            }
        };
    }
}
