package com.example.taskmanager.user.repositories;

import com.example.taskmanager.user.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    Optional<Session> findBySessionToken(String sessionToken);
    void deleteBySessionToken(String sessionToken);
}
