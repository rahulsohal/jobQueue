package sohal.lld.jobqueue.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import sohal.lld.jobqueue.entity.JobStatus;
import sohal.lld.jobqueue.repository.JobRepository;

import java.time.Duration;

@Component @RequiredArgsConstructor
public class JobConsumer {
    private final JobRepository repo;
    private final StringRedisTemplate redis;

    @KafkaListener(topics = "${jobs.topic}", groupId = "job-workers")
    public void onMessage(org.apache.kafka.clients.consumer.ConsumerRecord<String,String> rec) {
        String jobId = rec.key();
        String payload = rec.value();

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

