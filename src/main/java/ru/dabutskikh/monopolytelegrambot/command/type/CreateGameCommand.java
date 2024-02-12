package ru.dabutskikh.monopolytelegrambot.command.type;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CreateGameCommand implements TextCommand {

    @Override
    public CommandType getType() {
        return CommandType.CREATE_GAME;
    }

    @Override
    public boolean isCommand(String text) {
        Matcher matcher = Pattern.compile("^/create$").matcher(text);
        return matcher.find();
    }
}
