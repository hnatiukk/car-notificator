package ua.hnatiuk.userservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @author Hnatiuk Volodymyr on 02.04.2024.
 */
@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic mail() {
        return TopicBuilder
                .name("carnotificator.mail")
                .build();
    }
}