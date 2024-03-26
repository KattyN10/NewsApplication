package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.UserDTO;
import hcmute.kltn.backend.entity.User;
import hcmute.kltn.backend.entity.enum_entity.UploadPurpose;
import hcmute.kltn.backend.repository.UserRepo;
import hcmute.kltn.backend.service.ImageUploadService;
import hcmute.kltn.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final ImageUploadService imageUploadService;
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public String deleteUser(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepo.deleteById(user.getId());
        return "Deleted user with email:" + user.getEmail() + ".";
    }

    @Override
    public UserDTO updateUserInfor(String id, UserDTO userDTO) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(!userDTO.getFirstname().isEmpty()) {
            user.setFirstname(userDTO.getFirstname());
        }
        if(!userDTO.getLastname().isEmpty()) {
            user.setLastname(userDTO.getLastname());
        }
        if(!userDTO.getEmail().isEmpty()) {
            if (userRepo.existsByEmail(userDTO.getEmail())){
                throw new RuntimeException("Existed email.");
            } else {
                user.setEmail(userDTO.getEmail());
            }
        }
        if(userDTO.getDob() != null) {
            user.setDob(userDTO.getDob());
        }

        userRepo.save(user);
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updateUserAvatar(String id, MultipartFile file) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        try {
            String imageUrl = imageUploadService.saveImage(file, UploadPurpose.USER_AVATAR);
            user.setAvatar(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while uploading the image");
        }
        userRepo.save(user);
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO getMyInfor() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepo.findByEmail(name)
                .orElseThrow(() -> new RuntimeException("User not found."));
        return modelMapper.map(user, UserDTO.class);
    }

}

