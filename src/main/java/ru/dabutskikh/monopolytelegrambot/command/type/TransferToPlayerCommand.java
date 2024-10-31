package ru.dabutskikh.monopolytelegrambot.command.type;

import org.springframework.stereotype.Component;

@Component
public class TransferToPlayerCommand implements TextCommand {

    @Override
    public CommandType getType() {
        return CommandType.TRANSFER_TO_PLAYER;
    }

    @Override
    public boolean isCommand(String text) {
        return text.equals("Перевод игроку");
    }
}
