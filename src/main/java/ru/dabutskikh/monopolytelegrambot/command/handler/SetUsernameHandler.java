package ru.dabutskikh.monopolytelegrambot.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dabutskikh.monopolytelegrambot.command.CommandContext;
import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.response.Response;
import ru.dabutskikh.monopolytelegrambot.service.PlayerService;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class SetUsernameHandler implements TextCommandHandler {

    private final PlayerService playerService;

    @Override
    public CommandType getKey() {
        return CommandType.SET_USERNAME;
    }

    @Override
    public List<Response> execute(CommandContext context) {
        Matcher matcher = Pattern.compile("^/setusername ([А-Яа-я\\w\\s]+)$").matcher(context.getText());
        if (!matcher.find()) {
            throw new UserException("Ошибка при парсинге команды /setusername");
        }
        PlayerDTO playerDto = playerService.findByTelegramId(context.getUserId())
                .orElseThrow(() -> new UserException("Вы не зарегистрированы"));
        String newUsername = matcher.group(1);
        if (playerService.findByUsername(newUsername).isPresent()) {
            throw new UserException("Логин уже занят");
        }
        playerDto.setUsername(newUsername);
        playerService.update(playerDto);
        return Collections.singletonList(new Response(context.getUserId(), String.format("Ваш новый логин: %s", newUsername)));
    }
}
