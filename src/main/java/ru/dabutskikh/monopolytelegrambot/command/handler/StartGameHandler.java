package ru.dabutskikh.monopolytelegrambot.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.dabutskikh.monopolytelegrambot.command.CommandContext;
import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;
import ru.dabutskikh.monopolytelegrambot.dto.GameDTO;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.keyboard.Keyboards;
import ru.dabutskikh.monopolytelegrambot.response.Response;
import ru.dabutskikh.monopolytelegrambot.service.GameService;
import ru.dabutskikh.monopolytelegrambot.service.PlayerService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StartGameHandler implements TextCommandHandler {

    private final GameService gameService;
    private final PlayerService playerService;

    @Override
    public List<Response> execute(CommandContext context) {
        GameDTO game = gameService.startGame(context.getUserId());
        List<PlayerDTO> players = playerService.findByCurrentGameId(game.getId());
        String message = String.format("""
                Игра началась!
                Сумма, выданная Вам банком в начале игры: %s
                Сумма, выдаваемая банком после прохождения поля ВПЕРЕД: %s
                """,
                game.getStartMoney(),
                game.getForwardMoney()
        );
        return players.stream()
                .map(player -> new Response(player.getTelegramId(), message, Keyboards.getDefault()))
                .toList();
    }

    @Override
    public CommandType getKey() {
        return CommandType.START_GAME;
    }
}
