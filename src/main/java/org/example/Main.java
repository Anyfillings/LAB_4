package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Класс для тестирования функциональности базы данных
public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Logger logger = LoggerFactory.getLogger(Main.class);

        IDatabase db = new Database();

        // Создаем таблицы
        db.createTable("Users");
        db.createTable("Products");

        // Добавляем данные в таблицу "Users"
        ITable usersTable = db.getTable("Users");
        usersTable.put("Alice", "25");
        usersTable.put("Bob", "30");

        // Добавляем данные в таблицу "Products"
        ITable productsTable = db.getTable("Products");
        productsTable.put("Laptop", "1000");
        productsTable.put("Phone", "500");

        // Сохраняем базу данных на диск
        Path path = Paths.get("src/database.ser");
        db.saveToFile(path);
        logger.info("База данных сохранена.");

        // Загружаем базу данных с диска
        db = new Database();
        db.loadFromFile(path);
        logger.info("База данных загружена. Users: Alice = {}", db.getTable("Users").get("Alice"));
    }
}
