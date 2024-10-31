package ru.dabutskikh.monopolytelegrambot.state_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerGameDTO;
import ru.dabutskikh.monopolytelegrambot.dto.TxDTO;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameState;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.keyboard.Keyboards;
import ru.dabutskikh.monopolytelegrambot.response.Response;
import ru.dabutskikh.monopolytelegrambot.service.PlayerGameService;
import ru.dabutskikh.monopolytelegrambot.service.TxService;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SelectMoneyFromBankHandler implements PlayerGameStateHandler {

    private final TxService txService;
    private final PlayerGameService playerGameService;

    @Override
    public PlayerGameState getKey() {
        return PlayerGameState.SELECT_MONEY_AMOUNT_FROM_BANK;
    }

    @Override
    public List<Response> execute(GameMoveContext context) {
        int amount;
        try {
            amount = Integer.parseInt(context.getCommand().getText());
            if (amount < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new UserException("Введено некорректное значение суммы. Попробуйте еще раз");
        }
        TxDTO txDto = txService.getById(context.getPlayer().getCurrentTxId());
        txDto.setAmount(amount);
        txService.update(txDto);
        PlayerGameDTO playerGameDto = context.getPlayerGame();
        playerGameDto.setState(PlayerGameState.CONFIRM_FROM_BANK);
        playerGameService.update(playerGameDto);
        return Collections.singletonList(new Response(context.getCommand().getUserId(), "Подтвердите операцию", Keyboards.getConfirmKeyboard()));
    }
}
