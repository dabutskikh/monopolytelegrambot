package ru.dabutskikh.monopolytelegrambot.command.type;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ConnectGameCommand implements TextCommand {

    @Override
    public CommandType getType() {
        return CommandType.CONNECT_GAME;
    }

    @Override
    public boolean isCommand(String text) {
        Matcher matcher = Pattern.compile("^/connect (\\d+)$").matcher(text);
        return matcher.find();
    }
}
