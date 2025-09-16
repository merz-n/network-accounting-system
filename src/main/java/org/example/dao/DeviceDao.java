package org.example.dao;

import org.example.model.Device;
import org.example.model.Network;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.database.Database.getConnection;



public class DeviceDao {
    private static final String INSERT_DEVICE_SQL =
            "INSERT INTO networks.device (name, ip_address, mac_address, type, status, network_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING id, name, ip_address, mac_address, type, status, network_id, created_at";

    private static final String SELECT_ALL_DEVICES_SQL =
            "SELECT * FROM networks.device";

    private static final String SELECT_DEVICES_OF_NETWORK_SQL =
            "SELECT * FROM networks.device WHERE network_id = ?";

    private static final String SELECT_DEVICES_BY_NAME_SQL =
            "SELECT * FROM networks.device WHERE name ILIKE ?";

    private static final String SELECT_DEVICES_BY_IP_SQL =
            "SELECT * FROM networks.device WHERE ip_address = ?";

    private static final String SELECT_DEVICES_BY_TYPE_SQL =
            "SELECT * FROM networks.device WHERE type ILIKE ?";

    private static final String SELECT_DEVICES_BY_STATUS_SQL =
            "SELECT * FROM networks.device WHERE status ILIKE ?";

    public List<Device> getDevicesOf(Network network) throws SQLException {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(SELECT_DEVICES_OF_NETWORK_SQL)) {
            statement.setLong(1, network.getId());
            ResultSet resultSet = statement.executeQuery();

            List<Device> devices = new ArrayList<>();
            while (resultSet.next()) {
                devices.add(mapDevice(resultSet));
            }
            return devices;
        }
    }

    public List<Device> getAllDevices() throws SQLException {
        try (var connection = getConnection();
             var statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_DEVICES_SQL);
            List<Device> devices = new ArrayList<>();

            while (resultSet.next()) {
                devices.add(mapDevice(resultSet));
            }

            return devices;
        }
    }

    public List<Device> findByName(String name) throws SQLException {
        return findByField(SELECT_DEVICES_BY_NAME_SQL, "%" + name + "%");
    }

    public List<Device> findByIp(String ip) throws SQLException {
        return findByField(SELECT_DEVICES_BY_IP_SQL, ip);
    }

    public List<Device> findByType(String type) throws SQLException {
        return findByField(SELECT_DEVICES_BY_TYPE_SQL, "%" + type + "%");
    }

    public List<Device> findByStatus(String status) throws SQLException {
        return findByField(SELECT_DEVICES_BY_STATUS_SQL, "%" + status + "%");
    }

    private List<Device> findByField(String sql, String value) throws SQLException {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(sql)) {

            statement.setString(1, value);
            ResultSet resultSet = statement.executeQuery();
            List<Device> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(mapDevice(resultSet));
            }
            return result;
        }
    }

    private Device mapDevice(ResultSet resultSet) throws SQLException {
        return new Device(
                resultSet.getLong("id"),
                resultSet.getLong("network_id"),
                resultSet.getString("name"),
                resultSet.getString("ip_address"),
                resultSet.getString("mac_address"),
                resultSet.getString("type"),
                resultSet.getString("status"),
                resultSet.getTimestamp("created_at")
        );
    }

    public Device save(Device deviceToSave) throws SQLException {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(INSERT_DEVICE_SQL)) {

            statement.setString(1, deviceToSave.getName());
            statement.setString(2, deviceToSave.getIpAddress());
            statement.setString(3, deviceToSave.getMacAddress());
            statement.setString(4, deviceToSave.getType());
            statement.setString(5, deviceToSave.getStatus());
            statement.setLong(6, deviceToSave.getNetworkId());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapDevice(resultSet);
            } else {
                throw new SQLException("Failed to insert device: no data returned.");
            }

        }
    }
}
