package sohal.lld.jobqueue.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@Component @RequiredArgsConstructor
public class JobProducer {
    @Autowired
    private final KafkaTemplate<String,String> kafka;
    @org.springframework.beans.factory.annotation.Value("${jobs.topic}") String topic;

    public void send(String key, String payload) {
        kafka.send(topic, key, payload);
    }
}

