package com.sweet.photos.service;

import com.sweet.photos.entity.Photo;
import com.sweet.photos.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 照片业务逻辑层
 */
@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    /** 查全部照片 */
    public List<Photo> findAll() {
        return photoRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * 上传照片：保存文件到磁盘 + 记录到数据库
     */
    public Photo upload(MultipartFile file, String title, String category, String description) throws IOException {
        // 1. 确保上传目录存在
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // 2. 生成唯一文件名（防止重名覆盖）
        String original = file.getOriginalFilename();
        String ext = (original != null && original.contains("."))
                ? original.substring(original.lastIndexOf("."))
                : ".jpg";
        String filename = UUID.randomUUID().toString().replace("-", "") + ext;

        // 3. 保存文件到磁盘
        File dest = new File(uploadDir + filename);
        file.transferTo(dest);

        // 4. 保存记录到数据库
        Photo photo = new Photo();
        photo.setFilename(filename);
        photo.setOriginalName(original);
        photo.setTitle(title != null ? title : "未命名");
        photo.setCategory(category != null ? category : "portrait");
        photo.setDescription(description);
        return photoRepository.save(photo);
    }

    /** 删除照片：删数据库记录 + 删磁盘文件 */
    public void delete(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("照片不存在: " + id));
        // 删磁盘文件
        File f = new File(uploadDir + photo.getFilename());
        if (f.exists()) f.delete();
        // 删数据库记录
        photoRepository.deleteById(id);
    }

    /** 切换精选展示状态 */
    public Photo toggleFeatured(Long id, boolean featured) {
        Photo p = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("照片不存在: " + id));
        p.setIsFeatured(featured);
        return photoRepository.save(p);
    }

    /** 修改照片标题 */
    public Photo rename(Long id, String title) {
        Photo p = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("照片不存在: " + id));
        p.setTitle(title);
        return photoRepository.save(p);
    }
}
