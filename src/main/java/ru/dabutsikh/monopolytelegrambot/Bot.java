package ru.dabutsikh.monopolytelegrambot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.dabutsikh.monopolytelegrambot.model.Game;
import ru.dabutsikh.monopolytelegrambot.model.Player;
import ru.dabutsikh.monopolytelegrambot.model.PlayerGame;
import ru.dabutsikh.monopolytelegrambot.model.PlayerGameStatus;
import ru.dabutsikh.monopolytelegrambot.service.interfaces.GameService;
import ru.dabutsikh.monopolytelegrambot.service.interfaces.PlayerGameService;
import ru.dabutsikh.monopolytelegrambot.service.interfaces.PlayerService;
import ru.dabutsikh.monopolytelegrambot.service.interfaces.TransactionService;
import ru.dabutsikh.monopolytelegrambot.util.Messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("Bot")
@NoArgsConstructor
@Getter
@PropertySource("classpath:application.properties")
public class Bot extends TelegramLongPollingBot {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerGameService playerGameService;

    @Autowired
    private GameService gameService;

    @Autowired
    private Messages messages;

    @Autowired
    private TransactionService transactionService;

    @Value("${bot.username}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        User user = update.getMessage().getFrom();
        Player player = playerService.saveOrUpdate(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName());
        String command = update.getMessage().getText();
        try {
            Pattern connectGamePattern = Pattern.compile("^/connect (\\d+)$");
            Matcher connectGameMatcher = Pattern.compile("^/connect (\\d+)$").matcher(command);

            Pattern startMoneyPattern = Pattern.compile("^/startmoney (\\d+)$");
            Matcher startMoneyMatcher = startMoneyPattern.matcher(command);

            Pattern forwardMoneyPattern = Pattern.compile("^/forwardmoney (\\d+)$");
            Matcher forwardMoneyMatcher = forwardMoneyPattern.matcher(command);

            Pattern forwardMoneyTimePattern = Pattern.compile("^/forwardmoneytime (\\d+)$");
            Matcher forwardMoneyTimeMatcher = forwardMoneyTimePattern.matcher(command);

            // Создание игры
            if ("/create".equals(command)) {
                Game game = gameService.create(player);
                playerGameService.joinGame(game, player);
                messages.sendMessage(player,
                        "Игра создана! Id новой игры: " + game.getId() + "."
                );
            // Отключение от текущей игры
            } else if ("/disconnect".equals(command)) {
                PlayerGame playerGame = playerGameService.getProcessingGameByPlayer(player);
                playerGameService.quitGame(playerGame);
                messages.sendMessage(player,
                        "Вы вышли из игры с id " + playerGame.getId().getGame().getId() + "."
                );
            // Присоединение к игре с указанным id
            } else if (connectGameMatcher.find()) { // /connect {id}
                Long gameId = Long.parseLong(connectGameMatcher.group(1));
                Game game = gameService.findById(gameId);
                PlayerGame playerGame = playerGameService.joinGame(game, player);
                String role = PlayerGameStatus.ACTIVE.equals(playerGame.getStatus()) ? "Игрок" : "Наблюдатель";
                messages.sendMessage(
                        player, "Вы вошли в игру c id " + gameId + ". Ваша роль: " + role + "."
                );
            // Старт игры
            } else if ("/start".equals(command)) {
                PlayerGame playerGame = playerGameService.getProcessingGameByPlayer(player);
                if (playerGame == null) {
                    throw new RuntimeException("Вы не состоите ни в одной игре.");
                }
                Game game = playerGame.getId().getGame();
                gameService.start(player, game);
                messages.sendMessage(player,
                        "Игра началась"
                );
            // Завершение игры
            } else if ("/finish".equals(command)) {
                PlayerGame playerGame = playerGameService.getProcessingGameByPlayer(player);
                Game game = playerGame.getId().getGame();
                gameService.finish(player, game);
                messages.sendMessage(player,
                        "Игра завершена"
                );
            // Установка начальной суммы денег
            } else if (startMoneyMatcher.find()) {
                PlayerGame playerGame = playerGameService.getProcessingGameByPlayer(player);
                Game game = playerGame.getId().getGame();
                Integer startMoney = Integer.parseInt(startMoneyMatcher.group(1));
                gameService.setStartMoney(player, game, startMoney);
                messages.sendMessage(player,
                        "Установлена начальная сумма денег: " + startMoney + ".");
            // Установка суммы денег, выдаваемой после прохождения поля ВПЕРЕД
            } else if (forwardMoneyMatcher.find()) {
                PlayerGame playerGame = playerGameService.getProcessingGameByPlayer(player);
                Game game = playerGame.getId().getGame();
                Integer forwardMoney = Integer.parseInt(forwardMoneyMatcher.group(1));
                gameService.setForwardMoney(player, game, forwardMoney);
                messages.sendMessage(player,
                        "Установлена выдаваемая сумма денег при прохождении поля ВПЕРЕД: " + forwardMoney + ".");
            // Установка времени, в течение которого производится выплата после прохождения поля ВПЕРЕД
            } else if (forwardMoneyTimeMatcher.find()) {
                PlayerGame playerGame = playerGameService.getProcessingGameByPlayer(player);
                Game game = playerGame.getId().getGame();
                Integer forwardMoneyTime = Integer.parseInt(forwardMoneyTimeMatcher.group(1));
                gameService.setForwardMoneyTime(player, game, forwardMoneyTime);
                messages.sendMessage(player,
                        "Установлено время, в течение которого производится выплата после прохождения поля ВПЕРЕД: " + forwardMoneyTime + " мин.");
            }
        } catch (RuntimeException e) {
            messages.sendMessage(player, e.getMessage());
        }
    }
}
