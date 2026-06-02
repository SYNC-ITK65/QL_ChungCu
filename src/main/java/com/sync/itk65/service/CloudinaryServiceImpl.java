package com.sync.itk65.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        // Upload file lên Cloudinary vào thư mục "canho"
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "canho",           // Thư mục lưu trữ trên Cloudinary
                        "resource_type", "image"      // Chỉ cho phép upload ảnh
                ));

        // Trả về đường dẫn HTTPS an toàn của ảnh
        return uploadResult.get("secure_url").toString();
    }
}
