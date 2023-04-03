package ru.sbrf.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.sbrf.client.config.ATMProperties;
import ru.sbrf.common.messages.AuthenticationRequest;
import ru.sbrf.common.messages.AuthenticationResponse;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
class AuthenticationServiceTest {

    private static final String SERVER_AUTH_URL = "http://localhost:8080/auth";
    private static final String ATM_UUID = "testUUID";
    private static final String ATM_PASSWORD = "testPASSWORD";
    private static final String ATM_TOKEN = "testToken";
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    RestTemplate restTemplate;
    @MockBean
    ATMProperties atmProperties;

    @Test
    void authenticateTest() throws URISyntaxException, JsonProcessingException {
        Mockito.when(atmProperties.getUUID()).thenReturn(ATM_UUID);
        Mockito.when(atmProperties.getPassword()).thenReturn(ATM_PASSWORD);
        AuthenticationRequest request = new AuthenticationRequest(ATM_UUID, ATM_PASSWORD);
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(SERVER_AUTH_URL)))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(request)))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(new AuthenticationResponse(ATM_TOKEN)))
                );

        authenticationService.authenticate();
        mockServer.verify();
        Mockito.verify(atmProperties, Mockito.times(1)).setToken(ATM_TOKEN);
    }
}
