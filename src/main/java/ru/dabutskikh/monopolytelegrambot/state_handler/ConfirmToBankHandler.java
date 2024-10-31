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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ConfirmToBankHandler implements PlayerGameStateHandler {

    private final PlayerService playerService;
    private final PlayerGameService playerGameService;
    private final TxService txService;

    @Override
    public PlayerGameState getKey() {
        return PlayerGameState.CONFIRM_TO_BANK;
    }

    @Override
    public List<Response> execute(GameMoveContext context) {
        TxDTO txDto = txService.getById(context.getPlayer().getCurrentTxId());
        List<Response> responses = switch (context.getCommand().getText()) {
            case "ОК" -> commitTx(context, txDto);
            case "Отмена" -> rollbackTx(context, txDto);
            default -> throw new UserException("Некорректный ввод. Попробуйте еще раз", Keyboards.getConfirmKeyboard());
        };
        PlayerDTO playerDto = context.getPlayer();
        playerDto.setCurrentTxId(null);
        playerService.update(playerDto);

        return responses;
    }

    private List<Response> commitTx(GameMoveContext context, TxDTO txDto) {
        PlayerGameDTO playerGameDto = context.getPlayerGame();
        Integer amount = txDto.getAmount();
        playerGameDto.setMoney(playerGameDto.getMoney() - amount);
        playerGameDto.setState(PlayerGameState.DEFAULT);
        PlayerDTO playerDto = context.getPlayer();
        txDto.setStatus(TxStatus.COMPLETED);
        playerGameService.update(playerGameDto);
        txService.update(txDto);

        List<PlayerDTO> players = playerService.findByCurrentGameId(playerDto.getCurrentGameId());
        String username = playerDto.getUsername();
        String toYourselfMessage = String.format("""
                        Вы заплатили банку %s
                        Ваш баланс: %s
                        """,
                amount,
                playerGameDto.getMoney()
        );
        String toOtherPlayersMessage = String.format("Игрок %s заплатил банку %s", username, amount);
        return players.stream()
                .map(PlayerDTO::getTelegramId)
                .map(playerId -> new Response(
                        playerId,
                        Objects.equals(playerId, context.getCommand().getUserId())
                                ? toYourselfMessage
                                : toOtherPlayersMessage,
                        Objects.equals(playerId, context.getCommand().getUserId())
                                ? Keyboards.getDefault()
                                : null
                )).toList();
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
