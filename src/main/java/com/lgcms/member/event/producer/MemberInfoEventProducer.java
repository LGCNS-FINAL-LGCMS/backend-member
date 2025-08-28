package com.lgcms.member.event.producer;

import com.lgcms.member.common.kafka.dto.KafkaEvent;
import com.lgcms.member.event.dto.MemberInfoDto.MemberQuited;
import com.lgcms.member.event.dto.MemberInfoDto.NicknameModified;
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
public class MemberInfoEventProducer {
    @Value("${spring.application.name}")
    private String applicationName;
    private final String memberInfoTopic = "MEMBER_INFO";
    private final String nicknameModifiedType = "NICKNAME_MODIFIED";
    private final String memberQuitedType = "MEMBER_QUITED";

    private final KafkaTemplate kafkaTemplate;

    public void memberModified(NicknameModified nicknameModified) {
        KafkaEvent kafkaEvent = KafkaEvent.builder()
            .eventId(applicationName + UUID.randomUUID().toString())
            .eventTime(LocalDateTime.now().toString())
            .eventType(nicknameModifiedType)
            .data(nicknameModified)
            .build();

        kafkaTemplate.send(memberInfoTopic, kafkaEvent);
    }

    public void memberQuitedType(MemberQuited memberQuited) {
        KafkaEvent kafkaEvent = KafkaEvent.builder()
            .eventId(applicationName + UUID.randomUUID().toString())
            .eventTime(LocalDateTime.now().toString())
            .eventType(memberQuitedType)
            .data(memberQuited)
            .build();

        kafkaTemplate.send(memberInfoTopic, kafkaEvent);
    }
}
