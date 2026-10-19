package com.expencetracker.serializer;

import com.expencetracker.eventproducer.UserInfoEvent;
import io.jsonwebtoken.io.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import tools.jackson.databind.ObjectMapper;

public class UserInfoEventSerializer implements Serializer<UserInfoEvent> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, UserInfoEvent data) {

        if (data == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException(
                    "Error serializing UserInfoEvent", e
            );
        }
    }
}
