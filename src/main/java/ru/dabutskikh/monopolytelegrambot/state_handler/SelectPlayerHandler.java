package ru.dabutskikh.monopolytelegrambot.state_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerGameDTO;
import ru.dabutskikh.monopolytelegrambot.dto.TxDTO;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameState;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.keyboard.Keyboards;
import ru.dabutskikh.monopolytelegrambot.response.Response;
import ru.dabutskikh.monopolytelegrambot.service.PlayerGameService;
import ru.dabutskikh.monopolytelegrambot.service.PlayerService;
import ru.dabutskikh.monopolytelegrambot.service.TxService;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SelectPlayerHandler implements PlayerGameStateHandler {

    private final PlayerService playerService;
    private final PlayerGameService playerGameService;
    private final TxService txService;

    @Override
    public PlayerGameState getKey() {
        return PlayerGameState.SELECT_PLAYER;
    }

    @Override
    public List<Response> execute(GameMoveContext context) {
        Long gameId = context.getPlayer().getCurrentGameId();
        List<PlayerDTO> activePlayers = playerService.findActiveGamePlayers(gameId);
        PlayerDTO targenPlayerDto = activePlayers.stream()
                .filter(player -> player.getUsername().equals(context.getCommand().getText()))
                .findFirst()
                .orElseThrow(() -> new UserException(
                        "Игрок " + context.getCommand().getText() + " не найден",
                        Keyboards.getWithRows(activePlayers.stream()
                                .filter(player -> !player.getId().equals(context.getPlayer().getId()))
                                .map(PlayerDTO::getUsername)
                                .toList()
                        )
                ));
        TxDTO txDto = txService.getById(context.getPlayer().getCurrentTxId());
        txDto.setTargetId(targenPlayerDto.getId());
        txService.update(txDto);
        PlayerGameDTO playerGameDto = context.getPlayerGame();
        playerGameDto.setState(PlayerGameState.SELECT_MONEY_AMOUNT_TO_PLAYER);
        playerGameService.update(playerGameDto);
        return Collections.singletonList(new Response(context.getCommand().getUserId(),
                String.format("""
                        Перевод игроку %s
                        Введите сумму
                        """,
                        targenPlayerDto.getUsername()
                ),
                Keyboards.remove()
        ));
    }
}
