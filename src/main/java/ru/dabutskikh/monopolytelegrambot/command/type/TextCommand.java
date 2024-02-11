package ru.dabutskikh.monopolytelegrambot.command.type;

import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;

public interface TextCommand {

    CommandType getType();
    String getPattern();
}
