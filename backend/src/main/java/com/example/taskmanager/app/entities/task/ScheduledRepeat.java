package com.example.taskmanager.app.entities.task;

import com.example.taskmanager.app.entities.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "scheduled_works")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScheduledRepeat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "sort_index", precision = 10, scale = 5)
    private BigDecimal sortIndex;
    @Enumerated(EnumType.STRING)
    private RepeatTypeEnum type;
    private String action;
    private LocalDate end;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_status_id")
    private TaskStatus defaultStatus;
    private LocalDateTime dueDate;
    private LocalDate statingFrom;
    private Boolean skipWeekends;
    private LocalTime estimatedTime;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;
}
