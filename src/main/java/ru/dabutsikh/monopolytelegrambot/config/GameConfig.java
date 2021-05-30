package ru.dabutsikh.monopolytelegrambot.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@PropertySource("classpath:application.properties")
public class GameConfig {

    @Value("${game.startmoney}")
    private Integer startMoney;

    @Value("${game.forwardmoney}")
    private Integer forwardMoney;

    @Value("${game.forwardmoneytime}")
    private Integer forwartMoneyTime;
}
