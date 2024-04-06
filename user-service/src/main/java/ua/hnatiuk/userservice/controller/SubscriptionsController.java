package ua.hnatiuk.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.model.entity.Subscription;
import ua.hnatiuk.userservice.service.PeopleService;
import ua.hnatiuk.userservice.service.SubscriptionsService;
import ua.hnatiuk.userservice.util.SubscriptionValidator;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author Hnatiuk Volodymyr on 22.03.2024.
 */
@Controller
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionsController {
    private final SubscriptionsService service;
    private final PeopleService peopleService;
    private final SubscriptionValidator validator;

    @GetMapping
    public String home(Principal principal, Model model) {
        log.debug("Returning home page");

        Person person = peopleService.findByEmail(principal.getName()).get();

        if (person.getTgChatId() == null) {
            log.debug("Redirecting to account settings due to missing telegram id");
            return "redirect:/account?no_tg";
        }

        List<Subscription> activeSubscriptions = new LinkedList<>();
        List<Subscription> disabledSubscriptions = new LinkedList<>();

        peopleService.initSubscriptions(person);
        person.getSubscriptions()
                .forEach(subscription -> {
                    if (subscription.getIsActive()) {
                        activeSubscriptions.add(subscription);
                    } else disabledSubscriptions.add(subscription);
                });

        model.addAttribute("activeSubscriptions", activeSubscriptions);
        model.addAttribute("disabledSubscriptions", disabledSubscriptions);

        return "home/subscriptions";
    }

    @GetMapping("/add")
    public String getAddSubscriptionPage(@ModelAttribute("subscription") Subscription subscription,
                                         Model model) {
        log.debug("Returning add subscription page");

        model.addAttribute("brands", service.getBrands());
        model.addAttribute("models", service.getModels());
        return "home/add-subscription";
    }

    @PostMapping
    public String addSubscription(@ModelAttribute("subscription") @Valid Subscription subscription,
                                  BindingResult bindingResult,
                                  Principal principal) {
        validator.validate(subscription, bindingResult);

        if (bindingResult.hasErrors()) {
            log.warn("Could not validate subscription to add");
            return "home/add-subscription";
        }

        service.addSubscription(subscription, principal.getName());
        return "redirect:/subscriptions";
    }

    @GetMapping("/{id}/edit")
    public String getEditPage(@PathVariable("id") Long id, Model model, Principal principal) {
        log.debug("Returning edit page");

        Optional<Subscription> subscriptionOptional = service.findById(id);
        if (subscriptionOptional.isEmpty() ||
                !subscriptionOptional.get().getOwner().getEmail().equals(principal.getName())) {
            log.warn("Rejected editing non existing subscription or from third user");
            return "redirect:/error";
        }
        model.addAttribute("subscription", subscriptionOptional.get());
        model.addAttribute("brands", service.getBrands());
        model.addAttribute("models", service.getModels());

        return "home/edit-subscription";
    }

    @PatchMapping("/{id}")
    public String editSubscription(@ModelAttribute("subscription") @Valid Subscription subscription,
                                   BindingResult bindingResult) {
        validator.validate(subscription, bindingResult);

        if (bindingResult.hasErrors()) {
            log.warn("Could not validate subscription to edit");
            return "home/edit-subscription";
        }

        service.update(subscription);
        return "redirect:/subscriptions";
    }

    @GetMapping("/{id}/disable")
    public String disableSubscription(@PathVariable("id") Long id, Principal principal) {
        Optional<Subscription> subscriptionOptional = service.findById(id);

        if (subscriptionOptional.isEmpty() ||
                !subscriptionOptional.get().getOwner().getEmail().equals(principal.getName())) {
            log.warn("Rejected disabling non existing subscription or from third user");
            return "redirect:/error";
        }

        service.disable(subscriptionOptional.get());
        return "redirect:/subscriptions";
    }

    @GetMapping("/{id}/activate")
    public String activateSubscription(@PathVariable("id") Long id, Principal principal) {
        Optional<Subscription> subscriptionOptional = service.findById(id);

        if (subscriptionOptional.isEmpty() ||
                !subscriptionOptional.get().getOwner().getEmail().equals(principal.getName())) {
            log.warn("Rejected activating non existing subscription or from third user");
            return "redirect:/error";
        }

        service.activate(subscriptionOptional.get());
        return "redirect:/subscriptions";
    }

    @DeleteMapping("/{id}")
    public String deleteSubscription(@PathVariable("id") Long id, Principal principal) {
        Optional<Subscription> subscriptionOptional = service.findById(id);

        if (subscriptionOptional.isEmpty() ||
                !subscriptionOptional.get().getOwner().getEmail().equals(principal.getName())) {
            log.warn("Rejected deleting non existing subscription or from third user");
            return "redirect:/error";
        }

        service.deleteById(id);

        return "redirect:/subscriptions";
    }
}
