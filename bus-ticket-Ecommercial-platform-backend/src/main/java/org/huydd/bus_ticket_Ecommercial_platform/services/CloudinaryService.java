package org.huydd.bus_ticket_Ecommercial_platform.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    private final List<String> imageMimetypes = List.of("image/jpeg", "image/png");

    public boolean validateImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (imageMimetypes.contains(contentType)) return true;
        return false;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }

}
