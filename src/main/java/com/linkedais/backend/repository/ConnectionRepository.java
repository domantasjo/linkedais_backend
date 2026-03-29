package com.linkedais.backend.repository;

import com.linkedais.backend.model.Connection;
import enums.ConnectionStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends CrudRepository<Connection,Long> {
    Optional<Connection> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
    List<Connection> findByReceiverIdAndStatus(Long receiverId, ConnectionStatus status);
    List<Connection> findBySenderIdAndStatus(Long senderId, ConnectionStatus status);
}
