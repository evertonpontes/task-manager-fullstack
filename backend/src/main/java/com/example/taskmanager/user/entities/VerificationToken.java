package com.example.taskmanager.user.entities;

import jakarta.persistence.*;
import lombok.*;
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
@Setter
public class VerificationToken {
    @EmbeddedId
    private VerificationTokenId id;
    @Column(nullable = false)
    private LocalDateTime expiredAt;
    private LocalDateTime lastSentAt;
    @CreatedDate
    private LocalDateTime createdAt;
}
