package ru.sbrf.client.service;

import ru.sbrf.common.messages.BalanceResponse;

public interface BalanceService {

    BalanceResponse getCardBalance(Long numberCard, Integer pin);

    BalanceResponse replenishCard(Long numberCard, Integer pin, Long value);

    BalanceResponse withdrawCard(Long numberCard, Integer pin, Long value);
}
