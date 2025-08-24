CREATE TABLE IF NOT EXISTS jobs (
  job_id varchar(64) PRIMARY KEY,
  type varchar(100) NOT NULL,
  payload text,
  priority varchar(20) NOT NULL,
  status varchar(20) NOT NULL,
  max_retries int NOT NULL,
  retry_count int NOT NULL,
  scheduled_at timestamp NOT NULL,
  completed_at timestamp NULL,
  created_at timestamp NOT NULL,
  updated_at timestamp NULL
);
