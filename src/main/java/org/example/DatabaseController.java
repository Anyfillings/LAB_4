package org.example;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/database")
public class DatabaseController {

    private final IDatabase database;
    private final RestTemplate restTemplate;
    private final ReplicationConfig replicationConfig;

    public DatabaseController(IDatabase database, RestTemplate restTemplate, ReplicationConfig replicationConfig) {
        this.database = database;
        this.restTemplate = restTemplate;
        this.replicationConfig = replicationConfig;
    }

    // Регистрация мастера
    @PostMapping("/register-master")
    public String registerMaster(@RequestBody Map<String, String> data) {
        String masterUrl = data.get("masterUrl");
        replicationConfig.setMasterUrl(masterUrl);
        return "Master registered: " + masterUrl;
    }

    // Проверка роли текущего сервера
    @GetMapping("/role")
    public String getRole() {
        return "Current role: " + replicationConfig.getRole();
    }

    // Создание новой таблицы
    @PostMapping("/{tableName}/create")
    public String createTable(@PathVariable String tableName) {
        if (database.containsTable(tableName)) {
            return "Table " + tableName + " already exists.";
        }
        database.createTable(tableName); // Локальное создание таблицы
        replicate("/database/" + tableName + "/create", null); // Реплицируем создание таблицы (только мастер)
        return "Table " + tableName + " created successfully.";
    }



    // Добавление или обновление записи
    @PostMapping("/{tableName}")
    public String putValue(@PathVariable String tableName, @RequestBody Map<String, String> data) {
        if (!database.containsTable(tableName)) return "Table does not exist.";

        database.getTable(tableName).put(data.get("key"), data.get("value")); // Локальное добавление записи
        replicate("/database/" + tableName, data); // Репликация (только мастер)
        return "Record added successfully.";
    }


    // Получение значения
    @GetMapping("/{tableName}/{key}")
    public String getValue(@PathVariable String tableName, @PathVariable String key) {
        if (database.containsTable(tableName) && database.getTable(tableName).containsKey(key)) {
            return database.getTable(tableName).get(key);
        }
        return "Record not found.";
    }


    // Проверка доступности мастера
    @GetMapping("/check-master")
    public String checkMaster() {
        try {
            String masterUrl = replicationConfig.getMasterUrl();
            restTemplate.getForObject(masterUrl + "/database/role", String.class);
            return "Master is reachable.";
        } catch (Exception e) {
            return "Master is unreachable.";
        }
    }

    // Назначение нового мастера
    @PostMapping("/elect-new-master")
    public String electNewMaster() {
        if (!"master".equals(replicationConfig.getRole())) {
            replicationConfig.setMasterUrl("http://localhost:" + replicationConfig.getReplicationHosts().get(0));
            // Локально обновляем роль сервера на master
            replicationConfig.setRole("master");
            return "This server is now the master.";
        }
        return "This server is already the master.";
    }


    // Метод репликации данных
    private void replicate(String path, Object request) {
        // Только мастер отправляет репликационные запросы
        if (!"master".equals(replicationConfig.getRole())) {
            return; // Если это не мастер, репликация не выполняется
        }

        for (String host : replicationConfig.getReplicationHosts()) {
            String url = host + path;
            try {
                System.out.println("Репликация данных на: " + url);
                restTemplate.postForObject(url, request, String.class);
            } catch (Exception e) {
                System.err.println("Ошибка репликации на " + host + ": " + e.getMessage());
            }
        }
    }

}
