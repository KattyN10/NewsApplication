package hcmute.kltn.backend.service;

import hcmute.kltn.backend.dto.UserDTO;
import hcmute.kltn.backend.dto.request.UpdatePassRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();
    List<UserDTO> getAllUsers();
    UserDTO getUserById(String id);
    String deleteUser(String id);
    UserDTO updateUserInfor(String id, UserDTO userDTO);
    UserDTO updateUserAvatar(String id, MultipartFile file);
    UserDTO getMyInfor();




}
