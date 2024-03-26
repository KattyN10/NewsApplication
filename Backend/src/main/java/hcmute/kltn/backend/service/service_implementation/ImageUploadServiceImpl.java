package hcmute.kltn.backend.service.service_implementation;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import hcmute.kltn.backend.entity.enum_entity.UploadPurpose;
import hcmute.kltn.backend.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {
    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Override
    public String saveImage(MultipartFile file, UploadPurpose uploadPurpose) throws IOException {
        Cloudinary cloudinary = new Cloudinary(
                "cloudinary://"
                        + apiKey + ":"
                        + apiSecret + "@"
                        + cloudName
        );
        Map<String, String> params = new HashMap<>();
        params.put("folder", "images_upload");
        if (uploadPurpose.name() == UploadPurpose.USER_AVATAR.name()) {
            params.put("folder", "user_avatar");
        }
        else if (uploadPurpose.name() == UploadPurpose.ARTICLE_AVATAR.name()) {
            params.put("folder", "article_avatar");
        } else {
            throw new RuntimeException("Invalid upload purpose.");
        }
        File uploadedFile = File.createTempFile("temp", file.getOriginalFilename());
        file.transferTo(uploadedFile);
        Map uploadResult = cloudinary.uploader().upload(uploadedFile, params);

        String imageUrl = (String) uploadResult.get("secure_url");
        uploadedFile.delete();
        return imageUrl;
    }

//    @Override
//    public void deleteImage(String imageUrl) {
//        Cloudinary cloudinary = new Cloudinary(
//                "cloudinary://"
//                        + apiKey + ":"
//                        + apiSecret + "@"
//                        + cloudName
//        );
//        String publicId = String.valueOf(cloudinary.url().publicId(imageUrl));
//        try {
//            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
//        } catch (IOException e) {
//            throw new RuntimeException(e.getMessage());
//        }
//
//    }




}
