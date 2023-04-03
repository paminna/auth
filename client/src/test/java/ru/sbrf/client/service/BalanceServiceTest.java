package ru.sbrf.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.sbrf.client.config.ATMProperties;
import ru.sbrf.common.messages.BalanceRequest;
import ru.sbrf.common.messages.BalanceResponse;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
class BalanceServiceTest {

    private static final String SERVER_URL = "http://localhost:8080/api/";
    private static final String BALANCE = "balance";
    private static final String REPLENISHMENT = "replenishment";
    private static final String WITHDRAWAL = "withdrawal";
    private static final String SUCCESSFULLY = "Successfully";
    private static final Long CARD_NUMBER = 1111222233334444L;
    private static final Integer CARD_PIN = 1234;
    private static final String ATM_TOKEN = "testToken";
    private final ObjectMapper mapper = new ObjectMapper();
    private final BalanceResponse serverResponse = new BalanceResponse(SUCCESSFULLY, 1000L);

    @Autowired
    BalanceService balanceService;
    @Autowired
    RestTemplate restTemplate;
    @MockBean
    ATMProperties atmProperties;

    private MockRestServiceServer mockServer;

    @Test
    void getCardBalanceTest() throws JsonProcessingException, URISyntaxException {
        BalanceRequest request = new BalanceRequest(CARD_NUMBER, CARD_PIN, BALANCE);

        initMockServer(request);

        BalanceResponse serviceResponse = balanceService.getCardBalance(CARD_NUMBER, CARD_PIN);
        verifyTest(serviceResponse);
    }

    @Test
    void replenishCardTest() throws URISyntaxException, JsonProcessingException {
        BalanceRequest request = new BalanceRequest(CARD_NUMBER, CARD_PIN, REPLENISHMENT, 100L);

        initMockServer(request);

        BalanceResponse serviceResponse = balanceService.replenishCard(CARD_NUMBER, CARD_PIN, 100L);
        verifyTest(serviceResponse);
    }

    @Test
    void withdrawCardTest() throws URISyntaxException, JsonProcessingException {
        BalanceRequest request = new BalanceRequest(CARD_NUMBER, CARD_PIN, WITHDRAWAL, 100L);

        initMockServer(request);

        BalanceResponse serviceResponse = balanceService.withdrawCard(CARD_NUMBER, CARD_PIN, 100L);
        verifyTest(serviceResponse);
    }

    private void initMockServer(BalanceRequest request) throws URISyntaxException, JsonProcessingException {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        Mockito.when(atmProperties.getToken()).thenReturn(ATM_TOKEN);
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(SERVER_URL + BALANCE)))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ATM_TOKEN))
                .andExpect(content().json(mapper.writeValueAsString(request)))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(serverResponse))
                );
    }

    private void verifyTest(BalanceResponse serviceResponse) {
        mockServer.verify();
        Assertions.assertEquals(serviceResponse, serverResponse);
    }
}
