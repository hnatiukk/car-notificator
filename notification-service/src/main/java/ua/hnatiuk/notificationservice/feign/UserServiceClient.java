package ua.hnatiuk.notificationservice.feign;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.hnatiuk.dto.PersonDTO;

/**
 * @author Hnatiuk Volodymyr on 04.04.2024.
 */
@FeignClient(name = "${feign.user-service.name}", url = "${feign.user-service.url}")
public interface UserServiceClient {
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/people/assign-tg-chat-id?inner_key=${carnotificator.inner-api-key}")
    Response assignTgChatId(@RequestBody PersonDTO personDTO);
}
