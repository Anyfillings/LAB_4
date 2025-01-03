package org.example;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.HashMap;
import static java.nio.file.StandardOpenOption.CREATE;

// Реализация интерфейса IDatabase
public class Database implements IDatabase {

    // Хранилище таблиц
    private final HashMap<String, ITable> tables = new HashMap<>();

    @Override
    public void createTable(String tableName) {
        // Создаем новую таблицу, если она еще не существует
        if (!tables.containsKey(tableName)) {
            tables.put(tableName, new Table());
        } else {
            throw new IllegalArgumentException("Table " + tableName + " already exists.");
        }
    }

    @Override
    public void deleteTable(String tableName) {
        // Удаляем таблицу
        tables.remove(tableName);
    }

    @Override
    public boolean containsTable(String tableName) {
        // Проверяем, существует ли таблица
        return tables.containsKey(tableName);
    }

    @Override
    public ITable getTable(String tableName) {
        // Возвращаем таблицу по имени или выбрасываем исключение
        if (tables.containsKey(tableName)) {
            return tables.get(tableName);
        } else {
            throw new IllegalArgumentException("Table " + tableName + " does not exist.");
        }
    }

    @Override
    public void saveToFile(Path filePath) throws IOException {
        // Сохраняем объект базы данных в файл
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath, CREATE))) {
            oos.writeObject(this);
        }
    }

    @Override
    public void loadFromFile(Path filePath) throws IOException, ClassNotFoundException {
        // Загружаем объект базы данных из файла
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            Database loaded = (Database) ois.readObject();
            this.tables.clear();
            this.tables.putAll(loaded.tables); // Переносим данные из файла в текущую БД
        }
    }
}
