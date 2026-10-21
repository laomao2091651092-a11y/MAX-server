package com.sweet.photos.controller;

import com.sweet.photos.entity.Photo;
import com.sweet.photos.service.PhotoService;
import org.springframework.beans.factory.annotation.Value;
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

    /** 上传密码（从 application.yml 读） */
    @Value("${upload.token}")
    private String uploadToken;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    /** 校验上传密码 */
    private boolean checkToken(String token) {
        return token != null && token.equals(uploadToken);
    }

    /** 查询全部照片 GET /api/photos */
    @GetMapping
    public List<Photo> list() {
        return photoService.findAll();
    }

    /**
     * 上传照片 POST /api/photos/upload
     * Header: X-Upload-Token = 密码
     * 参数：file=文件, title=标题, category=portrait/landscape, description=描述
     */
    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestHeader(value = "X-Upload-Token", required = false) String token,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "description", required = false) String description) {
        // 密码校验
        if (!checkToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("code", 401, "message", "密码错误，无权上传"));
        }
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

    /** 删除照片 DELETE /api/photos/{id}（也需要密码） */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @RequestHeader(value = "X-Upload-Token", required = false) String token,
            @PathVariable Long id) {
        if (!checkToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("code", 401, "message", "密码错误，无权删除"));
        }
        photoService.delete(id);
        return ResponseEntity.ok(Map.of("code", 0, "message", "删除成功"));
    }

    /** 切换精选展示 PUT /api/photos/{id}/featured?featured=true|false */
    @PutMapping("/{id}/featured")
    public ResponseEntity<?> toggleFeatured(
            @RequestHeader(value = "X-Upload-Token", required = false) String token,
            @PathVariable Long id,
            @RequestParam boolean featured) {
        if (!checkToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("code", 401, "message", "密码错误"));
        }
        Photo p = photoService.toggleFeatured(id, featured);
        return ResponseEntity.ok(Map.of("code", 0, "data", p));
    }

    /** 修改照片标题 PUT /api/photos/{id}/title?title=新名字 */
    @PutMapping("/{id}/title")
    public ResponseEntity<?> rename(
            @RequestHeader(value = "X-Upload-Token", required = false) String token,
            @PathVariable Long id,
            @RequestParam String title) {
        if (!checkToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("code", 401, "message", "密码错误"));
        }
        Photo p = photoService.rename(id, title);
        return ResponseEntity.ok(Map.of("code", 0, "data", p));
    }
}
