package ru.dabutskikh.monopolytelegrambot.command.handler;

import ru.dabutskikh.monopolytelegrambot.command.CommandContext;
import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;
import ru.dabutskikh.monopolytelegrambot.response.Response;
import ru.dabutskikh.monopolytelegrambot.utils.Providable;

import java.util.List;

public interface TextCommandHandler extends Providable<CommandType> {
    List<Response> execute(CommandContext context);
}
