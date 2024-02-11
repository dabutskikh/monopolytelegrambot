package ru.dabutskikh.monopolytelegrambot.command.type;

import org.springframework.stereotype.Component;

@Component
public class CreateGameCommand implements TextCommand {

    @Override
    public CommandType getType() {
        return CommandType.CREATE_GAME;
    }

    @Override
    public String getPattern() {
        return "^/create$";
    }
}
