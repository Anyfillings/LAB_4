package org.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Реализация интерфейса ITable
public class Table implements ITable {

    // Потокобезопасное хранилище данных таблицы
    private final Map<String, String> storage = new ConcurrentHashMap<>();

    @Override
    public void put(String key, String value) {
        // Добавляем или обновляем значение
        storage.put(key, value);
    }

    @Override
    public String get(String key) {
        // Получаем значение по ключу
        return storage.get(key);
    }

    @Override
    public void delete(String key) {
        // Удаляем значение по ключу
        storage.remove(key);
    }

    @Override
    public boolean containsKey(String key) {
        // Проверяем наличие ключа
        return storage.containsKey(key);
    }
}
