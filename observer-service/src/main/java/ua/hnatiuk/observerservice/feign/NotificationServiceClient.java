package ua.hnatiuk.observerservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Hnatiuk Volodymyr on 26.03.2024.
 */
@FeignClient(name = "${feign.notification-service.name}", url = "${feign.notification-service.url}")
public interface NotificationServiceClient {
    @RequestMapping(method = RequestMethod.POST, value = "/send")
    void sendNotification(@RequestParam("chat_id") Long chatId,
                          @RequestBody String message,
                          @RequestParam("photo_url") String photoUrl);
}
