package hcmute.kltn.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T> {
    private int code = 1000;
    private String message;
    private T data;
}
