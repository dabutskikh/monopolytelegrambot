package ru.dabutskikh.monopolytelegrambot.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dabutskikh.monopolytelegrambot.command.handler.TextCommandHandler;
import ru.dabutskikh.monopolytelegrambot.command.type.TextCommand;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CommandParser {

    private final CommandProvider commandProvider;
    private final List<TextCommand> commands;

    public boolean parse(CommandContext context) {
        for (TextCommand textCommand : commands) {
            Matcher matcher = Pattern.compile(textCommand.getPattern()).matcher(context.getText());
            if (matcher.find()) {
                TextCommandHandler commandHandler = commandProvider.getByKey(textCommand.getType());
                commandHandler.execute(context);
                return true;
            }
        }
        return false;
    }
}
