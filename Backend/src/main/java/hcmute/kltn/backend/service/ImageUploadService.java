package hcmute.kltn.backend.service;

import hcmute.kltn.backend.entity.enum_entity.UploadPurpose;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageUploadService {
    String saveImage(MultipartFile file, UploadPurpose uploadPurpose) throws IOException;
//    void deleteImage(String imageUrl);
}
