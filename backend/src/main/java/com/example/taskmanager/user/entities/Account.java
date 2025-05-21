package com.example.taskmanager.user.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "accounts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    @Column(nullable = false)
    private String provider; //e.g google, github
    private String providerAccountId;
    @CreatedDate
    private LocalDateTime createdAt;
}
