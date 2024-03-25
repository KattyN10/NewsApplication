package hcmute.kltn.backend.controller;

import hcmute.kltn.backend.dto.ApiResponse;
import hcmute.kltn.backend.dto.UserDTO;
import hcmute.kltn.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



}
