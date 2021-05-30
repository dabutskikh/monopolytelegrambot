package ru.dabutsikh.monopolytelegrambot.service.interfaces;

import ru.dabutsikh.monopolytelegrambot.model.Transaction;
import ru.dabutsikh.monopolytelegrambot.model.Player;
import ru.dabutsikh.monopolytelegrambot.model.PlayerGame;

public interface TransactionService {

    Transaction findById(Long transactionId);

    Transaction getPreparingTransaction(PlayerGame playerGame);

    void completeTransaction(Transaction transaction);

    void dropTransaction(Transaction transaction);

    void initializeTransactionToBank(PlayerGame playerGame);

    void initializeTransactionFromBank(PlayerGame playerGame);

    void initializeTransferTransaction(PlayerGame playerGame);

    void forwardFieldTransaction(PlayerGame playerGame);

    void setRecipient(Transaction transaction, Player recipient);

    void setAmount(Transaction transaction, Integer amount);
}
