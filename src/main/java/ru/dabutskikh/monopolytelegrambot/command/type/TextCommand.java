package ru.dabutskikh.monopolytelegrambot.command.type;

public interface TextCommand {

    CommandType getType();
    boolean isCommand(String text);
}
