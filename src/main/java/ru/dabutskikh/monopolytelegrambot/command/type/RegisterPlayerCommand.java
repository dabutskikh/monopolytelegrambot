package ru.dabutskikh.monopolytelegrambot.command.type;

import org.springframework.stereotype.Component;

@Component
public class RegisterPlayerCommand implements TextCommand {

    @Override
    public CommandType getType() {
        return CommandType.REGISTER_PLAYER;
    }

    @Override
    public String getPattern() {
        return "^/start$";
    }
}
