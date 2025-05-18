package com.example.taskmanager.user.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "verification_tokens")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VerificationToken {
    @EmbeddedId
    private VerificationTokenId id;
    @Column(nullable = false)
    private LocalDateTime expiredAt;
    private LocalDateTime lastSentAt;
    @CreatedDate
    private LocalDateTime createdAt;
}
