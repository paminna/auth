package ru.sbrf.client.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.sbrf.common.messages.BalanceRequest;
import ru.sbrf.common.messages.BalanceResponse;

import java.util.LinkedList;
import java.util.Queue;

@Service
@Slf4j
public class KafkaServiceImpl implements KafkaService {

    private static final long TIMEOUT_MILLISECONDS = 300;
    private static final String BALANCE = "balance";
    private static final String REPLENISHMENT = "replenishment";
    private static final String WITHDRAWAL = "withdrawal";

    private final KafkaTemplate<Long, BalanceRequest> kafkaStarshipTemplate;
    private final ObjectMapper objectMapper;

    Queue<BalanceResponse> responseQueue = new LinkedList<>();

    public KafkaServiceImpl(KafkaTemplate<Long, BalanceRequest> kafkaStarshipTemplate, ObjectMapper objectMapper) {
        this.kafkaStarshipTemplate = kafkaStarshipTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void send(BalanceRequest request) {
        kafkaStarshipTemplate.send("client.request", request);
    }

    @Override
    @KafkaListener(id = "Response", topics = {"server.response"}, containerFactory = "singleFactory")
    public void consume(BalanceResponse response) {
        log.info("=> consumed {}", writeValueAsString(response));
        responseQueue.add(response);
    }

    private String writeValueAsString(BalanceResponse response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Writing value to JSON failed: " + response.toString());
        }
    }

    @SneakyThrows
    @Override
    public BalanceResponse getCardBalance(Long numberCard, Integer pin) {
        send(new BalanceRequest(numberCard, pin, BALANCE));
        return getResponse();
    }

    @SneakyThrows
    @Override
    public BalanceResponse replenishCard(Long numberCard, Integer pin, Long value) {
        send(new BalanceRequest(numberCard, pin, REPLENISHMENT, value));
        return getResponse();
    }

    @SneakyThrows
    @Override
    public BalanceResponse withdrawCard(Long numberCard, Integer pin, Long value) {
        send(new BalanceRequest(numberCard, pin, WITHDRAWAL, value));
        return getResponse();
    }

    private BalanceResponse getResponse() throws InterruptedException {
        Thread.sleep(TIMEOUT_MILLISECONDS);
        return responseQueue.remove();
    }
}
