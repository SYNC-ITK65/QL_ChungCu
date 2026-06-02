package com.sync.itk65.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    /**
     * Upload file lên Cloudinary và trả về URL (secure_url) của ảnh.
     *
     * @param file file ảnh cần upload
     * @return URL công khai (https) của ảnh trên Cloudinary
     * @throws IOException nếu xảy ra lỗi khi đọc file hoặc upload
     */
    String uploadFile(MultipartFile file) throws IOException;
}
