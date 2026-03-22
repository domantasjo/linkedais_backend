package com.linkedais.backend.repository;

import com.linkedais.backend.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    void  deleteByPostIdAndUserId(Long postId, Long userId);
    int countByPostId(Long postId);
}
