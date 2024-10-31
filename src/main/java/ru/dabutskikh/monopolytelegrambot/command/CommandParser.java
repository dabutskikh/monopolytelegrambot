package ru.dabutskikh.monopolytelegrambot.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import ru.dabutskikh.monopolytelegrambot.command.handler.TextCommandHandler;
import ru.dabutskikh.monopolytelegrambot.command.type.TextCommand;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerGameDTO;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameState;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameStatus;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.keyboard.Keyboards;
import ru.dabutskikh.monopolytelegrambot.response.Response;
import ru.dabutskikh.monopolytelegrambot.service.PlayerGameService;
import ru.dabutskikh.monopolytelegrambot.service.PlayerService;
import ru.dabutskikh.monopolytelegrambot.state_handler.GameMoveContext;
import ru.dabutskikh.monopolytelegrambot.state_handler.PlayerGameStateHandler;
import ru.dabutskikh.monopolytelegrambot.state_handler.PlayerGameStateProvider;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommandParser {

    private final CommandProvider commandProvider;
    private final PlayerGameStateProvider stateProvider;
    private final List<TextCommand> commands;
    private final PlayerService playerService;
    private final PlayerGameService playerGameService;

    public List<Response> parse(CommandContext context) {
        try {
            for (TextCommand textCommand : commands) {
                if (textCommand.isCommand(context.getText())) {
                    TextCommandHandler commandHandler = commandProvider.getByKey(textCommand.getType());
                    return commandHandler.execute(context);
                }
            }
            PlayerDTO playerDto = playerService.findByTelegramId(context.getUserId())
                    .orElseThrow(() -> new UserException("Вы не зарегистрированы"));
            Long playerGameId = Optional.ofNullable(playerDto.getCurrentPlayerGameId())
                    .orElseThrow(() -> new UserException("Вы не состоите ни в одной игре"));
            PlayerGameDTO playerGameDto = playerGameService.getById(playerGameId);
            if (playerGameDto.getStatus().equals(PlayerGameStatus.DISABLED)) {
                throw new UserException("Вы наблюдате за игрой");
            }
            if (playerGameDto.getState().equals(PlayerGameState.DEFAULT)) {
                throw new UserException("Выполнение операции в данный момент невозможно, так как не закончена предыдущая");
            }
            return stateProvider.getByKey(playerGameDto.getState()).execute(
                    GameMoveContext.builder().player(playerDto).playerGame(playerGameDto).command(context).build());
        } catch (UserException e) {
            return Collections.singletonList(new Response(context.getUserId(), e.getMessage(), Keyboards.remove()));
        }
    }
}
