package ru.dabutskikh.monopolytelegrambot.command.type;

import org.springframework.stereotype.Component;

@Component
public class TransferToBankCommand implements TextCommand {

    @Override
    public CommandType getType() {
        return CommandType.TRANSFER_TO_BANK;
    }

    @Override
    public boolean isCommand(String text) {
        return text.equals("Заплатить банку");
    }
}
