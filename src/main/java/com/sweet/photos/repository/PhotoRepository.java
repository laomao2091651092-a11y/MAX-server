package com.sweet.photos.repository;

import com.sweet.photos.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 照片数据访问层 - 自动实现增删改查
 */
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    /** 按上传时间倒序查全部（最新的在前面） */
    List<Photo> findAllByOrderByCreatedAtDesc();

    /** 按分类查（竖屏/横屏） */
    List<Photo> findByCategoryOrderByCreatedAtDesc(String category);

    /** 查精选展示的照片（画廊页用） */
    List<Photo> findByIsFeaturedTrueOrderByCreatedAtDesc();
}
