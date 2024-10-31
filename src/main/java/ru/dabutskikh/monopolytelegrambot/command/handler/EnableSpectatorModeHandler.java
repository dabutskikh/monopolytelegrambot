package ru.dabutskikh.monopolytelegrambot.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dabutskikh.monopolytelegrambot.command.CommandContext;
import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerGameDTO;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameState;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameStatus;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.keyboard.Keyboards;
import ru.dabutskikh.monopolytelegrambot.response.Response;
import ru.dabutskikh.monopolytelegrambot.service.PlayerGameService;
import ru.dabutskikh.monopolytelegrambot.service.PlayerService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EnableSpectatorModeHandler implements TextCommandHandler {

    private final PlayerService playerService;
    private final PlayerGameService playerGameService;

    @Override
    public CommandType getKey() {
        return CommandType.SPECTATOR_MODE_ON;
    }

    @Override
    public List<Response> execute(CommandContext context) {
        PlayerDTO playerDto = playerService.findByTelegramId(context.getUserId())
                .orElseThrow(() -> new UserException("Вы не зарегистрированы"));
        Long playerGameId = Optional.ofNullable(playerDto.getCurrentPlayerGameId())
                .orElseThrow(() -> new UserException("Вы не состоите ни в одной игре"));
        PlayerGameDTO playerGameDto = playerGameService.getById(playerGameId);
        if (playerGameDto.getStatus().equals(PlayerGameStatus.DISABLED)) {
            throw new UserException("Вы наблюдате за игрой");
        }
        if (!playerGameDto.getState().equals(PlayerGameState.DEFAULT)) {
            throw new UserException("Выполнение операции в данный момент невозможно, так как не закончена предыдущая");
        }
        Integer money = playerGameDto.getMoney();
        if (money > 0) {
            throw new UserException(String.format("""
                    Вы не банкрот
                    Ваш баланс: %s
                    """, 
                    money
            ), Keyboards.getDefault());
        }
        playerGameDto.setStatus(PlayerGameStatus.DISABLED);
        playerGameDto.setState(null);
        playerGameDto.setMoney(null);
        playerGameService.update(playerGameDto);
        List<PlayerDTO> players = playerService.findByCurrentGameId(playerDto.getCurrentGameId());
        String username = players.stream()
                .filter(player -> Objects.equals(player.getTelegramId(), context.getUserId()))
                .map(PlayerDTO::getUsername)
                .findFirst()
                .orElseThrow(() -> new UserException("Игрок не найден"));
        String toYourselfMessage = "Вы наблюдаете за игрой";
        String toOtherPlayersMessage = "Игрок " + username + " обанкротился.";
        return players.stream()
                .map(PlayerDTO::getTelegramId)
                .map(playerId -> new Response(
                        playerId,
                        Objects.equals(playerId, context.getUserId())
                                ? toYourselfMessage
                                : toOtherPlayersMessage,
                        Objects.equals(playerId, context.getUserId())
                                ? Keyboards.remove()
                                : null
                )).toList();
    }
}
