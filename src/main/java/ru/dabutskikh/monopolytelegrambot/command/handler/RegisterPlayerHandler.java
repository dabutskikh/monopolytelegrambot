package ru.dabutskikh.monopolytelegrambot.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import ru.dabutskikh.monopolytelegrambot.command.CommandContext;
import ru.dabutskikh.monopolytelegrambot.command.type.CommandType;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.keyboard.Keyboards;
import ru.dabutskikh.monopolytelegrambot.response.Response;
import ru.dabutskikh.monopolytelegrambot.service.PlayerService;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RegisterPlayerHandler implements TextCommandHandler {

    private final PlayerService playerService;

    @Override
    public List<Response> execute(CommandContext context) {
        PlayerDTO player = PlayerDTO.builder()
                .telegramId(context.getUserId())
                .username(context.getUsername())
                .lastName(context.getLastName())
                .firstName(context.getFirstName())
                .build();
        playerService.create(player);
        return Collections.singletonList(new Response(context.getUserId(), "Вы зарегистированы!", Keyboards.remove()));
    }

    @Override
    public CommandType getKey() {
        return CommandType.REGISTER_PLAYER;
    }
}
