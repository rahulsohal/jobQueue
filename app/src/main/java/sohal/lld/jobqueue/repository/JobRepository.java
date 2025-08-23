package sohal.lld.jobqueue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sohal.lld.jobqueue.entity.Job;
import sohal.lld.jobqueue.entity.JobStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("""
     SELECT j FROM Job j
     WHERE j.status = :status AND j.scheduledAt <= :now
     ORDER BY CASE j.priority
                WHEN 'CRITICAL' THEN 1
                WHEN 'HIGH' THEN 2
                WHEN 'MEDIUM' THEN 3
                ELSE 4 END, j.scheduledAt ASC
     """)
    List<Job> findDispatchable(JobStatus status, LocalDateTime now, org.springframework.data.domain.Pageable pageable);
}
