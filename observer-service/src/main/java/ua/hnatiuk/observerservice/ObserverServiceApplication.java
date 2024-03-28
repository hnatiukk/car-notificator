package ua.hnatiuk.observerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
//@EnableAsync
public class ObserverServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ObserverServiceApplication.class, args);
    }

}
