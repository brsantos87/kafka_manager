package br.com.tools.kafkamanager.resource;

import br.com.tools.kafkamanager.model.MessageKafka;
import br.com.tools.kafkamanager.service.KafkaMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaApi {

    @Autowired
    private KafkaMessageService service;

    @PostMapping(value = "/send")
    public ResponseEntity send(
            @RequestBody MessageKafka message) {
        service.sendToKafka(message.getMessage(),message.getTopic());
        return ResponseEntity.ok("{\"status\":\"success\"}");
    }
}
