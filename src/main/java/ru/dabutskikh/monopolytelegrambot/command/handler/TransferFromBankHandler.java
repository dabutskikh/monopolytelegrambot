package ru.dabutskikh.monopolytelegrambot.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dabutskikh.monopolytelegrambot.command.CommandContext;
import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransferFromBankHandler implements TextCommandHandler {

    private final PlayerService playerService;
    private final PlayerGameService playerGameService;
    private final TxService txService;

    @Override
    public CommandType getKey() {
        return CommandType.TRANSFER_FROM_BANK;
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
        Long txId = txService.create(TxDTO.builder()
                .gameId(playerDto.getCurrentGameId())
                .type(TxType.BANK_TO_PLAYER)
                .status(TxStatus.CREATED)
                .ownerId(playerDto.getId())
                .targetId(playerDto.getId())
                .build());
        playerDto.setCurrentTxId(txId);
        playerService.update(playerDto);
        playerGameDto.setState(PlayerGameState.SELECT_MONEY_AMOUNT_FROM_BANK);
        playerGameService.update(playerGameDto);
        return Collections.singletonList(new Response(context.getUserId(),
                """
                        Получение денег из банка
                        Введите сумму
                        """,
                Keyboards.remove()
        ));
    }
}
