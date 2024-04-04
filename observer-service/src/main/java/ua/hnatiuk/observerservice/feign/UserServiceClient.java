package ua.hnatiuk.observerservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.hnatiuk.dto.SubscriptionDTO;

import java.util.List;

/**
 * @author Hnatiuk Volodymyr on 04.04.2024.
 */
@FeignClient(name = "${feign.user-service.name}", url = "${feign.user-service.url}")
public interface UserServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/subscriptions?inner_key=${carnotificator.inner-api-key}")
    List<SubscriptionDTO> findAll(@RequestParam("active") Boolean onlyActive);
}
