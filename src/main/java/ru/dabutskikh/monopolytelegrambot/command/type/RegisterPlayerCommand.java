package ru.dabutskikh.monopolytelegrambot.command.type;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegisterPlayerCommand implements TextCommand {

    @Override
    public CommandType getType() {
        return CommandType.REGISTER_PLAYER;
    }

    @Override
    public boolean isCommand(String text) {
        Matcher matcher = Pattern.compile("^/start$").matcher(text);
        return matcher.find();
    }
}
