package sohal.lld.jobqueue.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name="jobs")
@NoArgsConstructor @Getter @Setter @AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    private String type;
    @Lob
    private String payload;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private int maxRetries;
    private int retryCount;
    private LocalDateTime scheduledAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Job(String type, String payload, Priority priority, JobStatus status, int maxRetries, LocalDateTime scheduledAt) {
        this.type = type;
        this.payload = payload;
        this.priority = priority;
        this.status = status;
        this.maxRetries = maxRetries;
        this.scheduledAt = scheduledAt;
    }
}
