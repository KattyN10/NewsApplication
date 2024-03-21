package hcmute.kltn.backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SignUpRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Date dob;
    private String avatar;
}
