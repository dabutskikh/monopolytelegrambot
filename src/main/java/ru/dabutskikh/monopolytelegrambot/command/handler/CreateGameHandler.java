package ru.dabutskikh.monopolytelegrambot.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import ru.dabutskikh.monopolytelegrambot.command.CommandContext;
import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;
import ru.dabutskikh.monopolytelegrambot.dto.GameDTO;
import ru.dabutskikh.monopolytelegrambot.keyboard.Keyboards;
import ru.dabutskikh.monopolytelegrambot.response.Response;
import ru.dabutskikh.monopolytelegrambot.service.GameService;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateGameHandler implements TextCommandHandler {

    private final GameService gameService;

    @Override
    public List<Response> execute(CommandContext context) {
        GameDTO game = gameService.create(GameDTO.builder().ownerId(context.getUserId()).build());
        return Collections.singletonList(new Response(context.getUserId(), "Игра с ID " + game.getId() + " создана", Keyboards.remove()));
    }

    @Override
    public CommandType getKey() {
        return CommandType.CREATE_GAME;
    }
}
