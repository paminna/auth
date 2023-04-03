package ru.sbrf.client.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sbrf.client.config.ATMProperties;
import ru.sbrf.common.messages.BalanceRequest;
import ru.sbrf.common.messages.BalanceResponse;

@Service
public class BalanceServiceImpl implements BalanceService {

    private static final String SERVER_URL = "http://localhost:8080/api/";
    private static final String BALANCE = "balance";
    private static final String REPLENISHMENT = "replenishment";
    private static final String WITHDRAWAL = "withdrawal";

    private final ATMProperties atmProperties;
    private final RestTemplate restTemplate;

    public BalanceServiceImpl(ATMProperties atmProperties, RestTemplate restTemplate) {
        this.atmProperties = atmProperties;
        this.restTemplate = restTemplate;
    }

    private BalanceResponse sendRequestWithHeader(BalanceRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(atmProperties.getToken());
        HttpEntity<BalanceRequest> requestEntity = new HttpEntity<>(request, headers);
        return restTemplate.exchange(
                SERVER_URL + BALANCE,
                HttpMethod.POST,
                requestEntity,
                BalanceResponse.class
        ).getBody();
    }

    @Override
    public BalanceResponse getCardBalance(Long numberCard, Integer pin) {
        return sendRequestWithHeader(new BalanceRequest(numberCard, pin, BALANCE));
    }

    @Override
    public BalanceResponse replenishCard(Long numberCard, Integer pin, Long value) {
        return sendRequestWithHeader(new BalanceRequest(numberCard, pin, REPLENISHMENT, value));
    }

    @Override
    public BalanceResponse withdrawCard(Long numberCard, Integer pin, Long value) {
        return sendRequestWithHeader(new BalanceRequest(numberCard, pin, WITHDRAWAL, value));
    }
}
