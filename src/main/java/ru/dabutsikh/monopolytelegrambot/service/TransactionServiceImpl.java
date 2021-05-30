package ru.dabutsikh.monopolytelegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dabutsikh.monopolytelegrambot.model.*;
import ru.dabutsikh.monopolytelegrambot.repository.TransactionRepository;
import ru.dabutsikh.monopolytelegrambot.service.interfaces.GameService;
import ru.dabutsikh.monopolytelegrambot.service.interfaces.TransactionService;
import ru.dabutsikh.monopolytelegrambot.service.interfaces.PlayerGameService;
import ru.dabutsikh.monopolytelegrambot.service.interfaces.PlayerService;

import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    GameService gameService;

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerGameService playerGameService;

    @Override
    public Transaction findById(Long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(
                () -> new RuntimeException("Транзакция с id " + transactionId + " не найдена")
        );
    }

    @Override
    public Transaction getPreparingTransaction(PlayerGame playerGame) {
        List<Transaction> result = transactionRepository.getPreparingTransaction(
                playerGame.getId().getGame().getId(),
                playerGame.getId().getPlayer().getId()
        );
        return result.size() != 0 ? result.get(0) : null;
    }

    @Override
    public void completeTransaction(Transaction transaction) {
        Game game = transaction.getGame();
        Player payer = transaction.getPayer();
        Player recipient = transaction.getRecipient();
        Integer amount = transaction.getAmount();
        if (payer != null) {
            PlayerGame payerGame = playerGameService.getPlayerGameByGameAndPlayer(game, payer);
            playerGameService.writeOffMoney(payerGame, amount);
        }
        if (recipient != null) {
            PlayerGame recipientGame = playerGameService.getPlayerGameByGameAndPlayer(game, recipient);
            playerGameService.chargeMoney(recipientGame, amount);
        }
        transaction.setState(TransactionState.COMPLETED);
        transaction.setDate(new Date());
        transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public void dropTransaction(Transaction transaction) {
        transaction.setState(TransactionState.DROPPED);
        transaction.setDate(new Date());
        transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public void initializeTransactionToBank(PlayerGame playerGame) {
        Game game = playerGame.getId().getGame();
        Player owner = playerGame.getId().getPlayer();
        Transaction transaction = new Transaction(game, owner);
        transaction.setType(TransactionType.PLAYER_TO_BANK);
        transaction.setPayer(owner);
        transaction.setState(TransactionState.PREPARING);
        transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public void initializeTransactionFromBank(PlayerGame playerGame) {
        Game game = playerGame.getId().getGame();
        Player owner = playerGame.getId().getPlayer();
        Transaction transaction = new Transaction(game, owner);
        transaction.setType(TransactionType.BANK_TO_PLAYER);
        transaction.setRecipient(owner);
        transaction.setState(TransactionState.PREPARING);
        transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public void initializeTransferTransaction(PlayerGame playerGame) {
        Game game = playerGame.getId().getGame();
        Player owner = playerGame.getId().getPlayer();
        Transaction transaction = new Transaction(game, owner);
        transaction.setType(TransactionType.PLAYER_TO_PLAYER);
        transaction.setPayer(owner);
        transaction.setState(TransactionState.PREPARING);
        transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public void forwardFieldTransaction(PlayerGame playerGame) {
        Game game = playerGame.getId().getGame();
        Player owner = playerGame.getId().getPlayer();
        Transaction transaction = new Transaction(game, owner);
        transaction.setType(TransactionType.PASSING_FORWARD_FIELD);
        transaction.setRecipient(owner);
        playerGameService.chargeMoney(playerGame, game.getForwardMoney());
        transaction.setState(TransactionState.COMPLETED);
        transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public void setRecipient(Transaction transaction, Player recipient) {
        transaction.setRecipient(recipient);
        transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public void setAmount(Transaction transaction, Integer amount) {
        transaction.setAmount(amount);
        transactionRepository.saveAndFlush(transaction);
    }
}
