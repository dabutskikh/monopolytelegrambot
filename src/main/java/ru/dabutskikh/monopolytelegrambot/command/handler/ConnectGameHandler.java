package ru.dabutskikh.monopolytelegrambot.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import ru.dabutskikh.monopolytelegrambot.command.CommandContext;
import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.response.Response;
import ru.dabutskikh.monopolytelegrambot.service.GameService;
import ru.dabutskikh.monopolytelegrambot.service.PlayerService;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ConnectGameHandler implements TextCommandHandler {

    private final GameService gameService;
    private final PlayerService playerService;

    @Override
    public List<Response> execute(CommandContext context) {
        Matcher matcher = Pattern.compile("^/connect (\\d+)$").matcher(context.getText());
        if (!matcher.find()) {
            throw new UserException("Ошибка при парсинге команды /connect");
        }
        Long gameId = Long.parseLong(matcher.group(1));
        gameService.joinToGame(context.getUserId(), gameId);
        List<PlayerDTO> players = playerService.findByCurrentGameId(gameId);
        String username = players.stream()
                .filter(player -> Objects.equals(player.getTelegramId(), context.getUserId()))
                .map(PlayerDTO::getUsername)
                .findFirst()
                .orElseThrow(() -> new UserException("Игрок не найден"));
        String toYourselfMessage = "Вы присоединились к игре с ID " + gameId;
        String toOtherPlayersMessage = "Игрок " + username + " присоединился и игре";
        return players.stream()
                .map(PlayerDTO::getTelegramId)
                .map(playerId ->
                        new Response(
                                playerId,
                                Objects.equals(playerId, context.getUserId())
                                        ? toYourselfMessage
                                        : toOtherPlayersMessage,
                                new ReplyKeyboardRemove(true))
                ).toList();
    }

    @Override
    public CommandType getKey() {
        return CommandType.CONNECT_GAME;
    }
}
