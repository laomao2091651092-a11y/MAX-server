package com.sweet.photos.controller;

import com.sweet.photos.entity.Photo;
import com.sweet.photos.service.PhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 照片接口控制器
 */
@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    /** 查询全部照片 GET /api/photos */
    @GetMapping
    public List<Photo> list() {
        return photoService.findAll();
    }

    /**
     * 上传照片 POST /api/photos/upload
     * 参数：file=文件, title=标题, category=portrait/landscape, description=描述
     */
    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "description", required = false) String description) {
        try {
            Photo saved = photoService.upload(file, title, category, description);
            return ResponseEntity.ok(Map.of(
                    "code", 0,
                    "message", "上传成功",
                    "data", saved
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("code", 1, "message", "上传失败: " + e.getMessage()));
        }
    }

    /** 删除照片 DELETE /api/photos/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        photoService.delete(id);
        return ResponseEntity.ok(Map.of("code", 0, "message", "删除成功"));
    }
}
