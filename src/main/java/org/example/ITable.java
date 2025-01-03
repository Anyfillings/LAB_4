package org.example;

import java.io.Serializable;

// Интерфейс для работы с таблицами
public interface ITable extends Serializable {

    void put(String key, String value); // Добавление или обновление значения

    String get(String key); // Получение значения по ключу

    void delete(String key); // Удаление значения по ключу

    boolean containsKey(String key); // Проверка наличия ключа
}
