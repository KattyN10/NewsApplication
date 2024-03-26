package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.response.ApiResponse;
import hcmute.kltn.backend.dto.UserDTO;
import hcmute.kltn.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get-all-users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        ApiResponse<List<UserDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.getAllUsers());
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/get-user")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@RequestParam("userId") String userId) {
        ApiResponse<UserDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.getUserById(userId));
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/get-my-infor")
    public ResponseEntity<ApiResponse<UserDTO>> getMyInfor(){
        ApiResponse<UserDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.getMyInfor());
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/update-user-infor")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserInfor(
            @RequestParam("userId") String userId,
            @RequestBody UserDTO userDTO) {
        ApiResponse<UserDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.updateUserInfor(userId, userDTO));
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<ApiResponse<String>> deleteUser(@RequestParam("userId") String userId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(userService.deleteUser(userId));
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/update-user-avatar")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserAvatar(
            @RequestParam("userId") String userId,
            @RequestParam("file") MultipartFile file ) {
        ApiResponse<UserDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.updateUserAvatar(userId, file));
        return ResponseEntity.ok(apiResponse);
    }



}
