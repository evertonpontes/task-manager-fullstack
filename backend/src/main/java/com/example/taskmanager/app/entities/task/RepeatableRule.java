package com.example.taskmanager.app.entities.task;

import com.example.taskmanager.app.entities.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "repeatable_rules")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RepeatableRule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime until;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initial_task_status_id")
    private TaskStatus initialTaskStatus;
    private Integer initialTimeBlockDuration;
    private Integer dueDateShift;
    private Integer startAtDateShift;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;
}
