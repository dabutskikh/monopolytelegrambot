package ru.dabutskikh.monopolytelegrambot.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import ru.dabutskikh.monopolytelegrambot.command.CommandContext;
import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;
import ru.dabutskikh.monopolytelegrambot.config.game.GameConfig;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerGameDTO;
import ru.dabutskikh.monopolytelegrambot.dto.TxDTO;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameState;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameStatus;
import ru.dabutskikh.monopolytelegrambot.entity.enums.TxStatus;
import ru.dabutskikh.monopolytelegrambot.entity.enums.TxType;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.keyboard.Keyboards;
import ru.dabutskikh.monopolytelegrambot.response.Response;
import ru.dabutskikh.monopolytelegrambot.service.PlayerGameService;
import ru.dabutskikh.monopolytelegrambot.service.PlayerService;
import ru.dabutskikh.monopolytelegrambot.service.TxService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetForwardMoneyHandler implements TextCommandHandler {

    private final PlayerService playerService;
    private final PlayerGameService playerGameService;
    private final TxService txService;
    private final GameConfig gameConfig;

    @Override
    public CommandType getKey() {
        return CommandType.GET_FORWARD_MONEY;
    }

    @Transactional
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
        BigDecimal updatedMoney = playerGameDto.getMoney().add(gameConfig.getForwardMoney());
        playerGameDto.setMoney(updatedMoney);
        playerGameService.update(playerGameDto);
        txService.create(TxDTO.builder()
                .gameId(playerDto.getCurrentGameId())
                .type(TxType.FORWARD_FIELD)
                .status(TxStatus.COMPLETED)
                .ownerId(playerDto.getId())
                .targetId(playerDto.getId())
                .amount(gameConfig.getForwardMoney())
                .build());

        List<PlayerDTO> players = playerService.findByCurrentGameId(playerDto.getCurrentGameId());
        String username = playerDto.getUsername();
        String toYourselfMessage = String.format("""
                        Вы прошли поле ВПЕРЕД!
                        Ваш баланс: %s
                        """,
                updatedMoney
        );
        String toOtherPlayersMessage = "Игрок " + username + " прошел поле ВПЕРЕД.";
        return players.stream()
                .map(PlayerDTO::getTelegramId)
                .map(playerId -> new Response(
                        playerId,
                        Objects.equals(playerId, context.getUserId())
                                ? toYourselfMessage
                                : toOtherPlayersMessage,
                        Objects.equals(playerId, context.getUserId())
                                ? Keyboards.getDefault()
                                : null
                )).toList();
    }
}
