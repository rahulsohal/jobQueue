package sohal.lld.jobqueue.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import sohal.lld.jobqueue.entity.JobStatus;
import sohal.lld.jobqueue.kafka.JobProducer;
import sohal.lld.jobqueue.repository.JobRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableScheduling
public class ScheduleDispatcher {
    private final JobRepository repo;
    private final JobProducer producer;

    @Scheduled(fixedDelayString = "${jobs.scheduler.poll-ms}")
    public void dispatch() {
        var now = LocalDateTime.now();
        var jobs = repo.findDispatchable(JobStatus.PENDING, now, PageRequest.of(0, 50)); // batch of 50

        jobs.forEach(j -> {
            log.info("Scheduler triggered. Creating new job message with ID: {}", j.getJobId());
            producer.send(String.valueOf(j.getJobId()), j.getPayload());
            j.setStatus(JobStatus.QUEUED);
            repo.save(j);
        });
    }
}

