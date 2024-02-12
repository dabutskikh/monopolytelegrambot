package ru.dabutskikh.monopolytelegrambot.command;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class CommandContext {

    Long userId;
    String username;
    String lastName;
    String firstName;
    String text;
}
