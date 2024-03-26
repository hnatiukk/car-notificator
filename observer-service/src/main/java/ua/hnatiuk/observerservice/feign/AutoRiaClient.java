package ua.hnatiuk.observerservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Hnatiuk Volodymyr on 26.03.2024.
 */
@FeignClient(name = "${feign.autoria.name}", url = "${feign.autoria.url}")
public interface AutoRiaClient {
    @RequestMapping(method = RequestMethod.GET, value = "/search?api_key=${autoria.apikey}&{params}", consumes = "application/json")
    String search(@PathVariable("params") String params);
}