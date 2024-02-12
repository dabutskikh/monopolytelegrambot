package ru.dabutskikh.monopolytelegrambot.command.handler;

import ru.dabutskikh.monopolytelegrambot.command.CommandContext;
import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;
import ru.dabutskikh.monopolytelegrambot.utils.Providable;

public interface TextCommandHandler extends Providable<CommandType> {
    String execute(CommandContext context);
}
