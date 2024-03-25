package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.UserDTO;
import hcmute.kltn.backend.entity.User;
import hcmute.kltn.backend.repository.UserRepo;
import hcmute.kltn.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public List<UserDTO> getAllUsers() {
        List<User> allUsers = userRepo.findAll();
        return allUsers.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public String deleteUser(String id) {
        return null;
    }

    @Override
    public UserDTO updateUserInfor(String id, UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO updateUserAvatar(String id, UserDTO userDTO) {
        return null;
    }
}

