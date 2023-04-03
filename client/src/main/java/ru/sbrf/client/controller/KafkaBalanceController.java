package ru.sbrf.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sbrf.client.kafka.service.KafkaService;
import ru.sbrf.common.messages.BalanceResponse;

@RestController
@RequestMapping("/kafka")
public class KafkaBalanceController {

    private final KafkaService kafkaService;

    public KafkaBalanceController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @GetMapping("/balance")
    public BalanceResponse getCardBalance(@RequestParam Long number, @RequestParam Integer pin) {
        return kafkaService.getCardBalance(number, pin);
    }

    @GetMapping("/replenishment")
    public BalanceResponse cardReplenishment(@RequestParam Long number, @RequestParam Integer pin, @RequestParam Long value) {
        return kafkaService.replenishCard(number, pin, value);
    }

    @GetMapping("/withdrawal")
    public BalanceResponse cardWithdrawal(@RequestParam Long number, @RequestParam Integer pin, @RequestParam Long value) {
        return kafkaService.withdrawCard(number, pin, value);
    }
}
