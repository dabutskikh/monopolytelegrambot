package ru.dabutskikh.monopolytelegrambot.command.type;

import org.springframework.stereotype.Component;

@Component
public class EnableSpectatorModeCommand implements TextCommand {

    @Override
    public CommandType getType() {
        return CommandType.SPECTATOR_MODE_ON;
    }

    @Override
    public boolean isCommand(String text) {
        return text.equals("Auf Wiedersehen");
    }
}
