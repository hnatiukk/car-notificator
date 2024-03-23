package ua.hnatiuk.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.model.entity.Subscription;
import ua.hnatiuk.userservice.service.PeopleService;
import ua.hnatiuk.userservice.service.SubscriptionsService;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Hnatiuk Volodymyr on 22.03.2024.
 */
@Controller
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionsController {
    private final SubscriptionsService service;

    @GetMapping
    public String home(Principal principal, Model model) {
        List<Subscription> activeSubscriptions = new LinkedList<>();
        List<Subscription> disabledSubscriptions = new LinkedList<>();

        service.findAllByEmail(principal.getName())
                        .forEach(subscription -> {
                            if (subscription.getIsActive()){
                                activeSubscriptions.add(subscription);
                            }
                            else disabledSubscriptions.add(subscription);
                        });

        model.addAttribute("activeSubscriptions", activeSubscriptions);
        model.addAttribute("disabledSubscriptions", disabledSubscriptions);

        return "home/subscriptions";
    }
    @GetMapping("/add")
    public String getAddSubscriptionPage() {
        return "home/add-subscription";
    }
}
