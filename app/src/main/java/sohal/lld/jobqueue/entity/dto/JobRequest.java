package sohal.lld.jobqueue.entity.dto;

import lombok.Getter;
import lombok.Setter;
import sohal.lld.jobqueue.entity.Priority;

import java.time.LocalDateTime;

@Setter
@Getter
public class JobRequest {
    private String type;
    private String payload;
    private Priority priority = Priority.HIGH;
    private int maxRetries = 3;
    private LocalDateTime scheduledAt;

}
