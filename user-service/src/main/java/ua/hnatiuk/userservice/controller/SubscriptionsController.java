package ua.hnatiuk.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class SubscriptionsController {
    private final SubscriptionsService service;
    private final PeopleService peopleService;
    private final SubscriptionValidator validator;

    @GetMapping
    public String home(Principal principal, Model model) {
        Person person = peopleService.findByEmail(principal.getName()).get();

        if (person.getTgChatId() == null) {
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
            return "home/add-subscription";
        }
        service.addSubscription(subscription, principal);
        return "redirect:/subscriptions";
    }

    @GetMapping("/{id}/edit")
    public String getEditPage(@PathVariable("id") Long id, Model model, Principal principal) {
        Optional<Subscription> subscriptionOptional = service.findById(id);
        if (subscriptionOptional.isEmpty() ||
                !subscriptionOptional.get().getOwner().getEmail().equals(principal.getName())) {
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
            return "redirect:/error";
        }

        service.activate(subscriptionOptional.get());
        return "redirect:/subscriptions";
    }

    @DeleteMapping("/{id}")
    public String deleteSubscription(@PathVariable("id") Long id) {
        service.deleteById(id);

        return "redirect:/subscriptions";
    }
}