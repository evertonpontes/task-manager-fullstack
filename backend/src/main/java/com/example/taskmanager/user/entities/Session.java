package com.example.taskmanager.user.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "sessions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    @Column(unique = true, nullable = false)
    private String sessionToken;
    @Column(nullable = false)
    private LocalDateTime expiredAt;
    @CreatedDate
    private LocalDateTime createdAt;
}
