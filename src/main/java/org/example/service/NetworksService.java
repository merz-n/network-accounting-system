package org.example.service;

import org.example.dao.DeviceConnectionDao;
import org.example.dao.DeviceDao;
import org.example.dao.NetworkDao;
import org.example.model.Device;
import org.example.model.DeviceConnection;
import org.example.model.Network;
import org.example.ui.ConsoleController;

import java.sql.SQLException;
import java.util.List;

public class NetworksService {

    private final NetworkDao networkDao;
    private final DeviceDao deviceDao;
    private final DeviceConnectionDao connectionDao;
    private final ConsoleController console;

    public NetworksService(ConsoleController console, NetworkDao networkDao, DeviceDao deviceDao, DeviceConnectionDao connectionDao) {
        this.networkDao = networkDao;
        this.deviceDao = deviceDao;
        this.connectionDao = connectionDao;
        this.console = console;
    }
    public Network addNetwork(Network network) throws SQLException {
        return networkDao.save(network);
    }

    public List<Network> getAllNetworks() throws SQLException {
        return networkDao.getAllNetworks();
    }

    public Network updateNetwork(Network network) throws SQLException {
        return networkDao.update(network);
    }

    public void removeNetwork(Network network) throws SQLException {
        networkDao.remove(network);
    }


    public Device addDevice(Device device) throws SQLException {
        return deviceDao.save(device);
    }

    public List<Device> getAllDevices() throws SQLException {
        return deviceDao.getAllDevices();
    }

    public List<Device> getDevicesOf(Network network) throws SQLException {
        return deviceDao.getDevicesOf(network);
    }


    public DeviceConnection addConnection(DeviceConnection connection) throws SQLException {
        return connectionDao.save(connection);
    }

    public List<DeviceConnection> getAllConnections() throws SQLException {
        return connectionDao.getAllConnections();
    }

    public void removeConnection(DeviceConnection connection) throws SQLException {
        connectionDao.remove(connection);
    }
    private void findDeviceByName() {
        try {
            String name = console.enterString("Введите имя устройства для поиска:");
            List<Device> devices = deviceDao.findByName(name);
            devices.forEach(console::print);
        } catch (Exception e) {
            console.printError("Ошибка при поиске по имени: " + e.getMessage());
        }
    }

    private void findDeviceByIp() {
        try {
            String ip = console.enterString("Введите IP-адрес для поиска:");
            List<Device> devices = deviceDao.findByIp(ip);
            devices.forEach(console::print);
        } catch (Exception e) {
            console.printError("Ошибка при поиске по IP: " + e.getMessage());
        }
    }

    private void findDeviceByType() {
        try {
            String type = console.enterString("Введите тип устройства для поиска:");
            List<Device> devices = deviceDao.findByType(type);
            devices.forEach(console::print);
        } catch (Exception e) {
            console.printError("Ошибка при поиске по типу: " + e.getMessage());
        }
    }

    private void findDeviceByStatus() {
        try {
            String status = console.enterString("Введите статус устройства для поиска:");
            List<Device> devices = deviceDao.findByStatus(status);
            devices.forEach(console::print);
        } catch (Exception e) {
            console.printError("Ошибка при поиске по статусу: " + e.getMessage());
        }
    }

    private void showDevicesAndConnections() {
        try {
            var networks = getAllNetworks();
            var selectedNetwork = console.selectOf(networks, "network");

            var devices = getDevicesOf(selectedNetwork);
            console.enterString("Устройства в выбранной сети (нажмите Enter для продолжения):");
            for (Device device : devices) {
                console.print(device);
            }
            var allConnections = getAllConnections();
            List<Long> deviceIds = new java.util.ArrayList<>();
            for (Device device : devices) {
                deviceIds.add(device.getId());
            }
            List<DeviceConnection> filteredConnections = new java.util.ArrayList<>();
            for (DeviceConnection connection : allConnections) {
                if (deviceIds.contains(connection.getDeviceFromId()) || deviceIds.contains(connection.getDeviceToId())) {
                    filteredConnections.add(connection);
                }
            }
            console.enterString("Подключения устройств в этой сети (нажмите Enter):");
            for (DeviceConnection connection : filteredConnections) {
                console.print(connection);
            }
        } catch (Exception e) {
            console.printError("Ошибка при отображении устройств и подключений: " + e.getMessage());
        }
    }

    private void showNetworksAndDevices() {
        try {
            List<Network> networks = getAllNetworks();
            for (Network network : networks) {
                console.enterString("Сеть: " + network.getName() + " (нажмите Enter для просмотра устройств)");
                console.print(network);

                List<Device> devices = getDevicesOf(network);
                if (devices.isEmpty()) {
                    console.enterString("Нет подключённых устройств. Нажмите Enter, чтобы продолжить.");
                } else {
                    for (Device device : devices) {
                        console.print(device);
                    }
                }
            }
        } catch (Exception e) {
            console.printError("Ошибка при отображении сетей и устройств: " + e.getMessage());
        }
    }

    private void showActiveDevicesReport() {
        try {
            List<Network> networks = getAllNetworks();
            for (Network network : networks) {
                List<Device> devices = getDevicesOf(network);
                int activeCount = 0;

                for (Device device : devices) {
                    if ("active".equalsIgnoreCase(device.getStatus())) {
                        activeCount++;
                    }
                }

                System.out.println("Сеть: " + network.getName() + " | Активных устройств: " + activeCount);
            }

            console.enterString("Отчёт завершён. Нажмите Enter, чтобы вернуться в меню.");
        } catch (Exception e) {
            console.printError("Ошибка при генерации отчёта: " + e.getMessage());
        }
    }



    public void process() {
        while (true) {
            int code = console.getUserChoice();

            try {
                var actionOpt = org.example.validator.UserAction.valueOf(code);
                if (actionOpt.isEmpty()) {
                    console.printError("Unknown command.");
                    continue;
                }

                var action = actionOpt.get();
                switch (action) {
                    case ADD_NETWORK -> {
                        var network = console.readNetwork();
                        var saved = addNetwork(network);
                        console.print(saved);
                    }
                    case EDIT_NETWORK -> {
                        var allNetworks = getAllNetworks();
                        var selected = console.selectOf(allNetworks, "network");
                        var updated = console.updateFields(selected);
                        var saved = updateNetwork(updated);
                        console.print(saved);
                    }
                    case REMOVE_NETWORK -> {
                        var allNetworks = getAllNetworks();
                        var selected = console.selectOf(allNetworks, "network");
                        removeNetwork(selected);
                    }
                    case ADD_DEVICE -> {
                        var allNetworks = getAllNetworks();
                        var selected = console.selectOf(allNetworks, "network");
                        var device = console.readNewDevice();
                        device.setNetworkId(selected.getId());
                        var saved = addDevice(device);
                        console.print(saved);
                    }
                    case ADD_CONNECTION -> {
                        var allDevices = getAllDevices();
                        var deviceFrom = console.selectOf(allDevices, "device from");
                        var deviceTo = console.selectOf(allDevices, "device to");
                        var connection = console.readNewConnection();
                        connection.setDeviceFromId(deviceFrom.getId());
                        connection.setDeviceToId(deviceTo.getId());
                        var saved = addConnection(connection);
                        console.print(saved);
                    }
                    case REMOVE_CONNECTION -> {
                        var allConnections = getAllConnections();
                        var selected = console.selectOf(allConnections, "connection");
                        removeConnection(selected);
                    }
                    case FIND_DEVICE_BY_NAME -> {
                        findDeviceByName();
                    }
                    case FIND_DEVICE_BY_IP -> {
                        findDeviceByIp();
                    }
                    case FIND_DEVICE_BY_TYPE -> {
                        findDeviceByType();
                    }
                    case FIND_DEVICE_BY_STATUS -> {
                        findDeviceByStatus();
                    }
                    case SHOW_DEVICES_AND_CONNECTIONS -> {
                        showDevicesAndConnections();
                    }
                    case SHOW_NETWORKS_AND_DEVICES -> {
                        showNetworksAndDevices();
                    }
                    case SHOW_ACTIVE_DEVICES_REPORT -> {
                        showActiveDevicesReport();
                    }

                    case EXIT -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                }

            } catch (SQLException e) {
                console.printError("Database error: " + e.getMessage());
            }
        }
    }
}
