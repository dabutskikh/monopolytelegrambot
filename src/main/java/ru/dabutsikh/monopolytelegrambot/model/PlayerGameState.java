package ru.dabutsikh.monopolytelegrambot.model;

public enum PlayerGameState {
    INITIALIZE,
    DEFAULT,
    SELECT_RECIPIENT,
    SELECT_MONEY_TRANSFER,
    SELECT_MONEY_FROM_BANK,
    SELECT_MONEY_TO_BANK,
    CONFIRM_TRANSFER,
    CONFIRM_FROM_BANK,
    CONFIRM_TO_BANK
}
