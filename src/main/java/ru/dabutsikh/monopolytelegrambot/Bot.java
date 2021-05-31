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

import java.util.List;
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
        Player player = playerService.saveOrUpdate(user.getId(), user.getUserName());
        String command = update.getMessage().getText();
        try {
            Matcher connectGameMatcher = Pattern.compile("^/connect (\\d+)$").matcher(command);
            Matcher startMoneyMatcher = Pattern.compile("^/setstartmoney (\\d+)$").matcher(command);
            Matcher forwardMoneyMatcher = Pattern.compile("^/setforwardmoney (\\d+)$").matcher(command);
            Matcher forwardMoneyTimeMatcher = Pattern.compile("^/setforwardmoneytime (\\d+)$").matcher(command);

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
                Game game = playerGameService.startCreatedGame(player);
                List<PlayerGame> playerGames = playerGameService.getActiveAndSpectatorPlayerGamesByGame(game);
                for (PlayerGame playerGame : playerGames) {
                    player = playerGame.getId().getPlayer();
                    messages.sendMessage(player, "Игра началась");
                }
            // Завершение игры
            } else if ("/finish".equals(command)) {
                Game game = playerGameService.finishGame(player);
                List<PlayerGame> playerGames = playerGameService.getActiveAndSpectatorPlayerGamesByGame(game);
                for (PlayerGame playerGame : playerGames) {
                    player = playerGame.getId().getPlayer();
                    messages.sendMessage(player, "Игра закончилась");
                }
            // Установка начальной суммы денег
            } else if (startMoneyMatcher.find()) {
                Integer startMoney = Integer.parseInt(startMoneyMatcher.group(1));
                Game game = playerGameService.setStartMoneyInCreatedGame(player, startMoney);
                List<PlayerGame> playerGames = playerGameService.getActiveAndSpectatorPlayerGamesByGame(game);
                for (PlayerGame playerGame : playerGames) {
                    player = playerGame.getId().getPlayer();
                    messages.sendMessage(player, "Установлена начальная сумма денег: " + startMoney + ".");
                }
            // Установка суммы денег, выдаваемой после прохождения поля ВПЕРЕД
            } else if (forwardMoneyMatcher.find()) {
                Integer forwardMoney = Integer.parseInt(forwardMoneyMatcher.group(1));
                Game game = playerGameService.setForwardMoneyInCreatedGame(player, forwardMoney);
                List<PlayerGame> playerGames = playerGameService.getActiveAndSpectatorPlayerGamesByGame(game);
                for (PlayerGame playerGame : playerGames) {
                    player = playerGame.getId().getPlayer();
                    messages.sendMessage(player, "Установлена выдаваемая сумма денег при прохождении поля ВПЕРЕД: " + forwardMoney + ".");
                }
            // Установка времени, в течение которого производится выплата после прохождения поля ВПЕРЕД
            } else if (forwardMoneyTimeMatcher.find()) {
                Integer forwardMoneyTime = Integer.parseInt(forwardMoneyTimeMatcher.group(1));
                Game game = playerGameService.setForwardMoneyTimeInCreatedGame(player, forwardMoneyTime);
                List<PlayerGame> playerGames = playerGameService.getActiveAndSpectatorPlayerGamesByGame(game);
                for (PlayerGame playerGame : playerGames) {
                    player = playerGame.getId().getPlayer();
                    messages.sendMessage(player,
                            "Установлено время, в течение которого производится выплата после прохождения поля ВПЕРЕД: " + forwardMoneyTime + " мин."
                    );
                }
            } else if (playerGameService.getProcessingGameByPlayer(player) != null) {

            }


        } catch (RuntimeException e) {
            messages.sendMessage(player, e.getMessage());
        }
    }
}
