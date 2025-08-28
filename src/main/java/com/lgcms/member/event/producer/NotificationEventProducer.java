package com.lgcms.member.event.producer;

import com.lgcms.member.common.kafka.dto.KafkaEvent;
import com.lgcms.member.event.dto.NotificationEventDto.RoleModified;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationEventProducer {
    @Value("${spring.application.name}")
    private String applicationName;
    private final String notificationTopic = "NOTIFICATION";
    private final String roleModifiedType = "ROLE_MODIFIED";

    private final KafkaTemplate kafkaTemplate;

    public void roleModified(RoleModified roleModified) {
        KafkaEvent kafkaEvent = KafkaEvent.builder()
            .eventId(applicationName + UUID.randomUUID().toString())
            .eventTime(LocalDateTime.now().toString())
            .eventType(roleModifiedType)
            .data(roleModified)
            .build();

        kafkaTemplate.send(notificationTopic, kafkaEvent);
    }
}
