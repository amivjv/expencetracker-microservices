package com.expencetracker.eventproducer;

import com.expencetracker.modals.UserInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoProducer {
    private final KafkaTemplate<String, UserInfoEvent> kafkaTemplate;

    @Value("${spring.kafka.topic.user-info}")
    private String topicName;

    public UserInfoProducer(KafkaTemplate<String, UserInfoEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEventToKafkaTopic(UserInfoEvent eventData){
        Message<UserInfoEvent> message = MessageBuilder.withPayload(eventData)
                .setHeader(KafkaHeaders.TOPIC, topicName).build();
        kafkaTemplate.send(message);
    }


}
