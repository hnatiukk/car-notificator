package ua.hnatiuk.observerservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.hnatiuk.observerservice.feign.params.AutoRiaRequestParams;
import ua.hnatiuk.dto.car.CarDTO;

/**
 * @author Hnatiuk Volodymyr on 26.03.2024.
 */
@FeignClient(name = "${feign.autoria.name}", url = "${feign.autoria.url}")
public interface AutoRiaClient {
    @RequestMapping(method = RequestMethod.GET, value = "/search?api_key=${autoria.apikey}&top=1", consumes = "application/json")
    String search(@SpringQueryMap AutoRiaRequestParams params, @RequestParam("countpage") int searchCount);
    @RequestMapping(method = RequestMethod.GET, value = "/info?api_key=${autoria.apikey}", consumes = "application/json")
    CarDTO searchCar(@RequestParam("auto_id") int carId);
}