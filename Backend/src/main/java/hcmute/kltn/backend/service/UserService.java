package hcmute.kltn.backend.service;

import hcmute.kltn.backend.dto.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

//    @PreAuthorize("hasRole('ADMIN')")
    UserDetailsService userDetailsService();

    List<UserDTO> getAllUsers();

    UserDTO getUserById(String id);

    String deleteUser(String id);

    UserDTO updateUserInfor(String id, UserDTO userDTO);
    UserDTO updateUserAvatar(String id, UserDTO userDTO);


}
