package ru.dabutskikh.monopolytelegrambot.command.type;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SetUsernameCommand implements TextCommand {

    @Override
    public CommandType getType() {
        return CommandType.SET_USERNAME;
    }

    @Override
    public boolean isCommand(String text) {
        Matcher matcher = Pattern.compile("^/setusername ([А-Яа-я\\w\\s]+)$").matcher(text.trim());
        return matcher.find();
    }
}
