package ru.dabutskikh.monopolytelegrambot.utils;

public interface Provider<ProvidedType extends Providable<KeyType>, KeyType> {

    ProvidedType getByKey(KeyType key);
}
