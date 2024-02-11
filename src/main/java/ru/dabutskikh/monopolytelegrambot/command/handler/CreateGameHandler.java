package ru.dabutskikh.monopolytelegrambot.command.handler;

import org.springframework.stereotype.Component;
import ru.dabutskikh.monopolytelegrambot.command.CommandContext;
import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;

@Component
public class CreateGameHandler implements TextCommandHandler {

    @Override
    public void execute(CommandContext context) {
        System.out.println("CreateGameHandler: ");
        System.out.println(context.getUserId());
        System.out.println(context.getUsername());
        System.out.println(context.getLastName());
        System.out.println(context.getFirstName());
        System.out.println(context.getText());
    }

    @Override
    public CommandType getKey() {
        return CommandType.CREATE_GAME;
    }
}
