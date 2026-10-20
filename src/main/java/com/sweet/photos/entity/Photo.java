package com.sweet.photos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 照片表实体 - 对应数据库 photos 表
 */
@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 存储文件名（UUID.jpg，避免重名） */
    private String filename;

    /** 原始文件名（用户上传时的名字） */
    private String originalName;

    /** 照片标题 */
    private String title;

    /** 分类：portrait=竖屏  landscape=横屏 */
    private String category;

    /** 照片描述/情话 */
    @Column(length = 500)
    private String description;

    /** 上传时间 */
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
