package ru.sbrf.client.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ATMProperties {

    @Value("${atm.uuid}")
    private String UUID;
    @Value("${atm.password}")
    private String password;

    private String token;
}
