package br.com.tools.kafkamanager.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class KafkaConfig {

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapAddress;

    @Value("${kafka.properties.sasl.mechanism}")
    private String saslMechanism;

    @Value("${kafka.security.user}")
    private String user;

    @Value("${kafka.protocol}")
    private String securityProtocol;

    @Value("${kafka.security.password}")
    private String password;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {

        var deserializer = new JsonDeserializer<>(String.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "json");

        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"" + user + "\" password=\"" + password + "\";";

        //configProps.put("sasl.mechanism", saslMechanism);
        //configProps.put("security.protocol", securityProtocol);
        //configProps.put("sasl.jaas.config", jaasTemplate);

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"" + user
                + "\" password=\"" + password + "\";";

        configProps.put("sasl.mechanism", saslMechanism);
        configProps.put("security.protocol", securityProtocol);
        configProps.put("sasl.jaas.config", jaasTemplate);
        configProps.put("max.poll.interval.ms", "75000");
        configProps.put("max.poll.records", "300");
        configProps.put("session.timeout.ms", "30000");

        return new DefaultKafkaProducerFactory(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
