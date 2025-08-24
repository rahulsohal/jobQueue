package sohal.lld.jobqueue.kafka;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;


@Component
@RequiredArgsConstructor
public class JobProducer {
    private static final Logger log = LoggerFactory.getLogger(JobProducer.class);

    @Autowired
    private final KafkaTemplate<String,String> kafka;
    @Value("${jobs.topic}")
    String topic;

    public void send(String key, String payload) {
        log.info("Sending the job to topic {} , key: {}", topic, key);
        kafka.send(topic, key, payload);
    }
}

