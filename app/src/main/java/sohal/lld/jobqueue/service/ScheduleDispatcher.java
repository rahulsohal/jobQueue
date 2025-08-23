package sohal.lld.jobqueue.service;

import sohal.lld.jobqueue.entity.JobStatus;
import sohal.lld.jobqueue.kafka.JobProducer;
import sohal.lld.jobqueue.repository.JobRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component @RequiredArgsConstructor
public class ScheduleDispatcher {
    private final JobRepository repo;
    private final JobProducer producer;

    @Scheduled(fixedDelayString = "${jobs.scheduler.poll-ms}")
    public void dispatch() {
        var now = LocalDateTime.now();
        var jobs = repo.findDispatchable(JobStatus.PENDING, now, PageRequest.of(0, 50)); // batch of 50
        jobs.forEach(j -> {
            producer.send(String.valueOf(j.getJobId()), j.getPayload());
            j.setStatus(JobStatus.QUEUED);
            repo.save(j);
        });
    }
}

