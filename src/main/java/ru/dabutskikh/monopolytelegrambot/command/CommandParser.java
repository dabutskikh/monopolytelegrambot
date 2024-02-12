package ru.dabutskikh.monopolytelegrambot.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dabutskikh.monopolytelegrambot.command.handler.TextCommandHandler;
import ru.dabutskikh.monopolytelegrambot.command.type.TextCommand;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommandParser {

    private final CommandProvider commandProvider;
    private final List<TextCommand> commands;

    public String parse(CommandContext context) {
        try {
            for (TextCommand textCommand : commands) {
                if (textCommand.isCommand(context.getText())) {
                    TextCommandHandler commandHandler = commandProvider.getByKey(textCommand.getType());
                    return commandHandler.execute(context);
                }
            }
            throw new UserException("Некорректная комманда. Попробуйте снова");
        } catch (UserException e) {
            return e.getMessage();
        }
    }
}
