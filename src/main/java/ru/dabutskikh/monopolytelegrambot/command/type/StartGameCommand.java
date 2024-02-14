package ru.dabutskikh.monopolytelegrambot.command.type;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StartGameCommand implements TextCommand {

    @Override
    public CommandType getType() {
        return CommandType.START_GAME;
    }

    @Override
    public boolean isCommand(String text) {
        Matcher matcher = Pattern.compile("^/begin$").matcher(text);
        return matcher.find();
    }
}
