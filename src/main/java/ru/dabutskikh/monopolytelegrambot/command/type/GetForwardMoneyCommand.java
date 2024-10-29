package ru.dabutskikh.monopolytelegrambot.command.type;

import org.springframework.stereotype.Component;

@Component
public class GetForwardMoneyCommand implements TextCommand {

    @Override
    public CommandType getType() {
        return CommandType.GET_FORWARD_MONEY;
    }

    @Override
    public boolean isCommand(String text) {
        return text.equals("Пройти поле ВПЕРЕД");
    }
}
