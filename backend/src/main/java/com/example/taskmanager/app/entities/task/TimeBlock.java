package com.example.taskmanager.app.entities.task;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "scheduled_works")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TimeBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(precision = 20, scale = 0)
    private BigDecimal rank;
    private LocalDateTime startAt;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private Integer duration;
    private Integer spentTime;
    private Integer manualSpentTime;
    private Boolean billable;
    private String comment;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;
}
