package ru.dabutskikh.monopolytelegrambot.command.type;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FinishGameCommand implements TextCommand {

    @Override
    public CommandType getType() {
        return CommandType.FINISH_GAME;
    }

    @Override
    public boolean isCommand(String text) {
        Matcher matcher = Pattern.compile("^/finish$").matcher(text);
        return matcher.find();
    }
}
