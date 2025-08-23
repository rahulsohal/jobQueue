package sohal.lld.jobqueue.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sohal.lld.jobqueue.entity.Job;
import sohal.lld.jobqueue.entity.JobStatus;
import sohal.lld.jobqueue.entity.Priority;
import sohal.lld.jobqueue.entity.dto.JobRequest;
import sohal.lld.jobqueue.repository.JobRepository;
import sohal.lld.jobqueue.service.JobService;

import javax.print.attribute.standard.JobPriority;
import java.time.LocalDateTime;

@RestController @RequestMapping("/jobs")
public class JobController {
    private final JobService svc;
    public JobController(JobService svc){ this.svc = svc; }

    public record CreateJobReq(
            @NotBlank String type,
            String payload,
            Priority priority,
            @Min(0) int maxRetries,
            LocalDateTime scheduledAt
    ) {}

    @PostMapping public Job create(@RequestBody CreateJobReq req){
        return svc.create(req.type(), req.payload(), req.priority(), req.maxRetries(), req.scheduledAt());
    }

    @GetMapping("/{id}") public java.util.Optional<Job> get(@PathVariable String id){
        return svc.get(id);
    }
}
