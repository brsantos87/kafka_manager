package br.com.tools.kafkamanager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaMessageService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendToKafka(String message, String topicName) {
        kafkaTemplate.send(topicName,message);
    }
}
