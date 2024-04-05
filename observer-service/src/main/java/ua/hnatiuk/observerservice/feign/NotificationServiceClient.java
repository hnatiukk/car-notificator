package ua.hnatiuk.observerservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.hnatiuk.dto.MessageDTO;

/**
 * @author Hnatiuk Volodymyr on 26.03.2024.
 */
@FeignClient(name = "${feign.notification-service.name}", url = "${feign.notification-service.url}")
public interface NotificationServiceClient {
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/send")
    void sendNotification(@RequestBody MessageDTO messageDTO);
}
