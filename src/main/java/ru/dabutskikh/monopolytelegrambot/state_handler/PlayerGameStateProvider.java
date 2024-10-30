package ru.dabutskikh.monopolytelegrambot.state_handler;

import org.springframework.stereotype.Component;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PlayerGameStateProvider {

    private final Map<PlayerGameState, PlayerGameStateHandler> handlerMap;

    public PlayerGameStateProvider(List<PlayerGameStateHandler> handlers) {
        handlerMap = new HashMap<>();
        for (PlayerGameStateHandler handler : handlers) {
            if (handlerMap.containsKey(handler.getKey())) {
                throw new RuntimeException("Для состояния " + handler.getKey() + " определено несколько обрабочиков");
            }
            handlerMap.put(handler.getKey(), handler);
        }
    }

    public PlayerGameStateHandler getByKey(PlayerGameState value) {
        return Optional.ofNullable(handlerMap.get(value)).orElseThrow(
                () -> new RuntimeException("Для состояния " + value + " не определен обрабочик"));
    }
}
