package org.example;

import org.example.dao.DeviceConnectionDao;
import org.example.dao.DeviceDao;
import org.example.dao.NetworkDao;
import org.example.database.Database;
import org.example.service.NetworksService;
import org.example.ui.ConsoleController;

import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = Database.getConnection()) {
            System.out.println("Успешное подключение к базе данных!");
        } catch (Exception e) {
            System.out.println("Ошибка подключения: " + e.getMessage());
        }

        var scanner = new Scanner(System.in);
        var consoleController = new ConsoleController(scanner);

        var networkDao = new NetworkDao();
        var deviceDao = new DeviceDao();
        var connectionDao = new DeviceConnectionDao();

        var networksService = new NetworksService(consoleController, networkDao, deviceDao, connectionDao);
        networksService.process();
    }
}



