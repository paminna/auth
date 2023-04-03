package ru.sbrf.client.kafka.service;

import ru.sbrf.common.messages.BalanceRequest;
import ru.sbrf.common.messages.BalanceResponse;

public interface KafkaService {

    void send(BalanceRequest request);

    void consume(BalanceResponse response);

    BalanceResponse getCardBalance(Long numberCard, Integer pin);

    BalanceResponse replenishCard(Long numberCard, Integer pin, Long value);

    BalanceResponse withdrawCard(Long numberCard, Integer pin, Long value);
}
