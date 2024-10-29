package ru.dabutskikh.monopolytelegrambot.command.type;

import org.springframework.stereotype.Component;

@Component
public class MoneyBalanceCommand implements TextCommand {

    @Override
    public CommandType getType() {
        return CommandType.MONEY_BALANCE;
    }

    @Override
    public boolean isCommand(String text) {
        return text.equals("Баланс");
    }
}
