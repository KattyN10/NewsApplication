package hcmute.kltn.backend.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {
    private List<String> keyList;
}
