package sohal.lld.jobqueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
public class JobqueueApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobqueueApplication.class, args);
	}

}
