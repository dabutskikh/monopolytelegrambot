package ru.dabutskikh.monopolytelegrambot.config.bot;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:bot.properties")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class BotConfig {

    @Value("${bot.username}")
    String username;

    @Value("${bot.token}")
    String token;
}
