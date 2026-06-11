package akendo.orderservice.messaging.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, String key, String payload) {
        kafkaTemplate.send(topic, key, payload).join();
    }
}
