package com.example.taskmanager.app.entities.task;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "sub_tasks")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubTask {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Long rank;
    private String title;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private LocalDateTime dueDate;
    private Integer estimatedTime;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_task_id")
    private Task parentTask;
}
