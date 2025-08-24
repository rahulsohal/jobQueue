package sohal.lld.jobqueue.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sohal.lld.jobqueue.entity.Job;
import sohal.lld.jobqueue.entity.JobStatus;
import sohal.lld.jobqueue.repository.JobRepository;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobConsumer {
    private final JobRepository repo;
    private final StringRedisTemplate redis;

    @KafkaListener(topics = "jobs.dispatch", concurrency = "1")
    public void onMessage(ConsumerRecord<String, String> rec) {
        String jobId = rec.key();
        String payload = rec.value();

        log.info("Received and processing job: {}", jobId);
        // Idempotency: set processing lock with TTL 5 min; skip if present
        String lockKey = "job:lock:" + jobId;
        Boolean acquired = redis.opsForValue().setIfAbsent(lockKey, "1", Duration.ofMinutes(5));
        if (Boolean.FALSE.equals(acquired)) return;

        repo.findById(Long.valueOf(jobId)).ifPresent(job -> {
            job.setStatus(JobStatus.RUNNING);
            repo.save(job);
            try {
                // simulate work per job type; replace with real handlers
                Thread.sleep(500);
                job.setStatus(JobStatus.SUCCESS);
            } catch (Exception e) {
                int rc = job.getRetryCount() + 1;
                job.setRetryCount(rc);
                if (rc > job.getMaxRetries()) job.setStatus(JobStatus.FAILED);
                else job.setStatus(JobStatus.PENDING); // will re-dispatch
            }
            repo.save(job);
            redis.delete(lockKey);
        });
    }
}

