package com.linkedais.backend.repository;


import com.linkedais.backend.model.Message;
import com.linkedais.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    /**
     * Retrieves the full conversation between two users, ordered chronologically.
     */
    @Query("""
            SELECT m FROM Message m
            WHERE m.deleted = false
              AND (
                    (m.sender = :userA AND m.receiver = :userB)
                 OR (m.sender = :userB AND m.receiver = :userA)
              )
            ORDER BY m.createdAt ASC
            """)
    List<Message> findConversation(@Param("userA") User userA,
                                   @Param("userB") User userB);

    /**
     * Retrieves all direct replies to a given parent message.
     * Excludes soft-deleted replies.
     */
    @Query("""
            SELECT m FROM Message m
            WHERE m.parentMessage.id = :parentId
              AND m.deleted = false
            ORDER BY m.createdAt ASC
            """)
    List<Message> findReplies(@Param("parentId") Long parentId);

    List<Message> findBySenderOrderByCreatedAtAsc(User sender);
    List<Message> findByReceiverOrderByCreatedAtAsc(User receiver);
}
