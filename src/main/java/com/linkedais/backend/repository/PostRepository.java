package com.linkedais.backend.repository;

import com.linkedais.backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable  pageable);

    void deleteById(Long id);
}
