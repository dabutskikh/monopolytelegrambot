package ru.dabutskikh.monopolytelegrambot.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dabutskikh.monopolytelegrambot.command.CommandContext;
import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;
import ru.dabutskikh.monopolytelegrambot.dto.GameDTO;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.entity.enums.GameStatus;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.keyboard.Keyboards;
import ru.dabutskikh.monopolytelegrambot.response.Response;
import ru.dabutskikh.monopolytelegrambot.service.GameService;
import ru.dabutskikh.monopolytelegrambot.service.PlayerService;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FinishGameHandler implements TextCommandHandler {

    private final PlayerService playerService;
    private final GameService gameService;

    @Override
    public CommandType getKey() {
        return CommandType.FINISH_GAME;
    }

    @Override
    public List<Response> execute(CommandContext context) {
        PlayerDTO owner = playerService.findByTelegramId(context.getUserId()).orElseThrow(() -> new UserException("Вы не зарегистрированы!"));
        if (owner.getCurrentGameId() == null) {
            throw new UserException("В данный момент вы не состоите ни в одной игре!");
        }
        GameDTO gameDto = gameService.getById(owner.getCurrentGameId());
        if (!Objects.equals(gameDto.getOwnerId(), owner.getId())) {
            throw new UserException("Вы не являетесь создатаелем игры с ID " + gameDto.getId() + "!");
        }

        List<PlayerDTO> allPlayers = playerService.findByCurrentGameId(gameDto.getId());
        allPlayers.stream()
                .peek(player -> {
                    player.setCurrentGameId(null);
                    player.setCurrentPlayerGameId(null);
                    player.setCurrentTxId(null);
                })
                .forEach(playerService::update);
        gameDto.setStatus(GameStatus.FINISHED);
        gameService.update(gameDto);
        return allPlayers.stream()
                .map(player -> new Response(player.getTelegramId(), "Игра окончена!", Keyboards.remove()))
                .toList();
    }
}
