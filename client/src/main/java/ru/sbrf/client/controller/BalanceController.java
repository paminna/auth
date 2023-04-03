package ru.sbrf.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sbrf.client.service.BalanceService;
import ru.sbrf.common.messages.BalanceResponse;

@RestController
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/balance")
    public BalanceResponse getCardBalance(@RequestParam Long number, @RequestParam Integer pin) {
        return balanceService.getCardBalance(number, pin);
    }

    @GetMapping("/replenishment")
    public BalanceResponse cardReplenishment(@RequestParam Long number, @RequestParam Integer pin, @RequestParam Long value) {
        return balanceService.replenishCard(number, pin, value);
    }

    @GetMapping("/withdrawal")
    public BalanceResponse cardWithdrawal(@RequestParam Long number, @RequestParam Integer pin, @RequestParam Long value) {
        return balanceService.withdrawCard(number, pin, value);
    }
}
