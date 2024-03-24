package hcmute.kltn.backend.service;

import hcmute.kltn.backend.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService {
    UserDetailsService userDetailsService();
}
