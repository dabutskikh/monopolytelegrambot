package ru.dabutskikh.monopolytelegrambot.config.game;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@PropertySource("classpath:game.properties")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class GameConfig {

    @Value("${game.start-money}")
    BigDecimal startMoney;

    @Value("${game.forward-money}")
    BigDecimal forwardMoney;
}
