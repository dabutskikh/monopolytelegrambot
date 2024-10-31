package ru.dabutskikh.monopolytelegrambot.state_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerGameDTO;
import ru.dabutskikh.monopolytelegrambot.dto.TxDTO;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameState;
import ru.dabutskikh.monopolytelegrambot.entity.enums.TxStatus;
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

@Component
@RequiredArgsConstructor
public class ConfirmToPlayerHandler implements PlayerGameStateHandler {

    private final PlayerService playerService;
    private final PlayerGameService playerGameService;
    private final TxService txService;

    @Override
    public PlayerGameState getKey() {
        return PlayerGameState.CONFIRM_TO_PLAYER;
    }

    @Override
    public List<Response> execute(GameMoveContext context) {
        TxDTO txDto = txService.getById(context.getPlayer().getCurrentTxId());
        List<Response> responses = switch (context.getCommand().getText()) {
            case "ОК" -> commitTx(context, txDto);
            case "Отмена" -> rollbackTx(context, txDto);
            default -> throw new UserException("Некорректный ввод. Попробуйте еще раз");
        };
        PlayerDTO playerDto = context.getPlayer();
        playerDto.setCurrentTxId(null);
        playerService.update(playerDto);

        return responses;
    }

    private List<Response> commitTx(GameMoveContext context, TxDTO txDto) {
        PlayerGameDTO playerGameDto = context.getPlayerGame();
        BigDecimal amount = txDto.getAmount();
        playerGameDto.setMoney(playerGameDto.getMoney().subtract(amount));
        playerGameDto.setState(PlayerGameState.DEFAULT);
        PlayerDTO playerDto = context.getPlayer();

        Long targetPlayerId = txDto.getTargetId();
        PlayerDTO targetPlayerDto = playerService.findById(targetPlayerId)
                .orElseThrow(() -> new UserException("Игрок с ID " + targetPlayerId + " не найден"));
        PlayerGameDTO targetPlayerGameDto = playerGameService.getById(targetPlayerDto.getCurrentPlayerGameId());
        targetPlayerGameDto.setMoney(targetPlayerGameDto.getMoney().add(amount));

        txDto.setStatus(TxStatus.COMPLETED);
        playerGameService.update(playerGameDto);
        playerGameService.update(targetPlayerGameDto);
        txService.update(txDto);

        String username = playerDto.getUsername();
        String toYourselfMessage = String.format("""
                        Вы заплатили игроку %s %s
                        Ваш баланс: %s
                        """,
                targetPlayerDto.getUsername(),
                amount,
                playerGameDto.getMoney()
        );
        String toTargetPlayerMessage = String.format("""
                Игрок %s перевел Вам %s
                Ваш баланс: %s
                """,
                username, amount, targetPlayerGameDto.getMoney());
        return List.of(new Response(playerDto.getTelegramId(), toYourselfMessage, Keyboards.getDefault()),
                new Response(targetPlayerDto.getTelegramId(), toTargetPlayerMessage));
    }

    private List<Response> rollbackTx(GameMoveContext context, TxDTO txDto) {
        PlayerGameDTO playerGameDto = context.getPlayerGame();
        playerGameDto.setState(PlayerGameState.DEFAULT);
        txDto.setStatus(TxStatus.DROPPED);
        playerGameService.update(playerGameDto);
        txService.update(txDto);
        return Collections.singletonList(new Response(context.getPlayer().getTelegramId(), "Операция отменена", Keyboards.getDefault()));
    }
}
