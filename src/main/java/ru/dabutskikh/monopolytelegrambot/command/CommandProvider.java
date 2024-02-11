package ru.dabutskikh.monopolytelegrambot.command;

import org.springframework.stereotype.Component;
import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;
import ru.dabutskikh.monopolytelegrambot.command.handler.TextCommandHandler;
import ru.dabutskikh.monopolytelegrambot.utils.Provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandProvider implements Provider<TextCommandHandler, CommandType> {

    private final Map<CommandType, TextCommandHandler> map;

    public CommandProvider(List<TextCommandHandler> handlers) {
        map = new HashMap<>();
        for (TextCommandHandler handler : handlers) {
            CommandType key = handler.getKey();
            if (map.containsKey(key)) {
                throw new RuntimeException("Для команды " + key.name() + " определено несколько обработчиков");
            }
            map.put(key, handler);
        }
    }

    @Override
    public TextCommandHandler getByKey(CommandType key) {
        return map.get(key);
    }
}
