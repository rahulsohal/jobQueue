package sohal.lld.jobqueue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sohal.lld.jobqueue.entity.Job;
import sohal.lld.jobqueue.entity.JobStatus;
import sohal.lld.jobqueue.entity.Priority;
import sohal.lld.jobqueue.repository.JobRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public Job create(String type, String payload, Priority priority, int maxRetries, LocalDateTime when) {
        var job = Job.builder()
                .type(type)
                .payload(payload)
                .priority(priority == null ? Priority.MEDIUM : priority)
                .status(JobStatus.PENDING)
                .maxRetries(maxRetries <= 0 ? 3 : maxRetries)
                .retryCount(0)
                .scheduledAt(when == null ? LocalDateTime.now() : when)
                .build();
        return jobRepository.save(job);
    }

    public java.util.Optional<Job> get(String id){ return jobRepository.findById(Long.valueOf(id)); }
}
