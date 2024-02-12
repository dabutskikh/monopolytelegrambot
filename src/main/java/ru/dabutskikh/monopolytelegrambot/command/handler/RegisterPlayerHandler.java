package ru.dabutskikh.monopolytelegrambot.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.dabutskikh.monopolytelegrambot.command.CommandContext;
import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.service.PlayerService;

@Component
@RequiredArgsConstructor
public class RegisterPlayerHandler implements TextCommandHandler {

    private final PlayerService playerService;

    @Transactional
    @Override
    public String execute(CommandContext context) {
        PlayerDTO player = PlayerDTO.builder()
                .telegramId(context.getUserId())
                .username(context.getUsername())
                .lastName(context.getLastName())
                .firstName(context.getFirstName())
                .build();
        playerService.create(player);
        return "Вы зарегистированы!";
    }

    @Override
    public CommandType getKey() {
        return CommandType.REGISTER_PLAYER;
    }
}
