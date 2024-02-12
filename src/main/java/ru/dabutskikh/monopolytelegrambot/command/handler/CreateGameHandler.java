package ru.dabutskikh.monopolytelegrambot.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.dabutskikh.monopolytelegrambot.command.CommandContext;
import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;
import ru.dabutskikh.monopolytelegrambot.dto.GameDTO;
import ru.dabutskikh.monopolytelegrambot.service.GameService;

@Component
@RequiredArgsConstructor
public class CreateGameHandler implements TextCommandHandler {

    private final GameService gameService;

    @Transactional
    @Override
    public String execute(CommandContext context) {
        GameDTO game = gameService.create(GameDTO.builder().ownerTelegramId(context.getUserId()).build());
        return "Игра с ID " + game.getId() + " создана";
    }

    @Override
    public CommandType getKey() {
        return CommandType.CREATE_GAME;
    }
}
