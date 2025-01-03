package org.example;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;

// Интерфейс для работы с базой данных
public interface IDatabase extends Serializable {

    void createTable(String tableName); // Создание новой таблицы

    void deleteTable(String tableName); // Удаление таблицы

    boolean containsTable(String tableName); // Проверка наличия таблицы

    ITable getTable(String tableName); // Получение таблицы по имени

    void saveToFile(Path filePath) throws IOException; // Сохранение базы данных в файл

    void loadFromFile(Path filePath) throws IOException, ClassNotFoundException; // Загрузка базы данных из файла
}
