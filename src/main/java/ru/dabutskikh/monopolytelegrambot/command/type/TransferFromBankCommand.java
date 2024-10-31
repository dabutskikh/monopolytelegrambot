package ru.dabutskikh.monopolytelegrambot.command.type;

import org.springframework.stereotype.Component;

@Component
public class TransferFromBankCommand implements TextCommand {

    @Override
    public CommandType getType() {
        return CommandType.TRANSFER_FROM_BANK;
    }

    @Override
    public boolean isCommand(String text) {
        return text.equals("Получить из банка");
    }
}
