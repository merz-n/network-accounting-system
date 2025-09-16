package org.example.dao;
import org.example.model.DeviceConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.database.Database.getConnection;

public class DeviceConnectionDao {
    private static final String INSERT_CONNECTION_SQL =
            "INSERT INTO networks.connection (connection_type, status, device_from_id, device_to_id) " +
                    "VALUES (?, ?, ?, ?) RETURNING id, connection_type, status, device_from_id, device_to_id, created_at";
    private static final String SELECT_ALL_CONNECTIONS_SQL =
            "SELECT * FROM networks.connection";

    private static final String DELETE_CONNECTION_SQL =
            "DELETE FROM networks.connection WHERE id = ?";

    public DeviceConnection save(DeviceConnection connectionToSave) throws SQLException {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(INSERT_CONNECTION_SQL)) {

            statement.setString(1, connectionToSave.getType());
            statement.setString(2, connectionToSave.getStatus());
            statement.setLong(3, connectionToSave.getDeviceFromId());
            statement.setLong(4, connectionToSave.getDeviceToId());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapConnection(resultSet);
            } else {
                throw new SQLException("Не удалось сохранить соединение устройств");
            }
        }
    }
    private DeviceConnection mapConnection(ResultSet resultSet) throws SQLException {
        return new DeviceConnection(
                resultSet.getLong("id"),
                resultSet.getLong("device_from_id"),
                resultSet.getLong("device_to_id"),
                resultSet.getString("connection_type"),
                resultSet.getString("status"),
                resultSet.getTimestamp("created_at")
        );
    }
    public List<DeviceConnection> getAllConnections() throws SQLException {
        try (var connection = getConnection();
             var statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SELECT_ALL_CONNECTIONS_SQL);
            List<DeviceConnection> connections = new ArrayList<>();

            while (resultSet.next()) {
                connections.add(mapConnection(resultSet));
            }

            return connections;
        }
    }

    public void remove(DeviceConnection connection) throws SQLException {
        try (var connectionDb = getConnection();
             var statement = connectionDb.prepareStatement(DELETE_CONNECTION_SQL)) {

            statement.setLong(1, connection.getId());
            statement.executeUpdate();
        }
    }
}
