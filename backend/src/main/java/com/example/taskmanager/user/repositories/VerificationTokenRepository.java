package com.example.taskmanager.user.repositories;

import com.example.taskmanager.user.entities.VerificationToken;
import com.example.taskmanager.user.entities.VerificationTokenId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, VerificationTokenId> {
    Optional<VerificationToken> findByIdCode(String code);
    Optional<VerificationToken> findTopByIdIdentifierOrderByLastSentAtDesc(String identifier);
    void deleteAllByIdIdentifier(String identifier);
}
