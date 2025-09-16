package org.example.dao;

import org.example.model.Network;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.database.Database;

public class NetworkDao {

    private Connection getConnection() throws SQLException {
        return Database.getConnection();
    }

    private static final String INSERT_NETWORK_SQL =
            "INSERT INTO networks.network (name, description) VALUES (?, ?) RETURNING id, name, description, created_at";

    private static final String SELECT_ALL_NETWORKS_SQL =
            "SELECT * FROM networks.network";

    private static final String DELETE_NETWORK_SQL =
            "DELETE FROM networks.network WHERE id = ?";

    private static final String UPDATE_NETWORK_SQL =
            "UPDATE networks.network SET name=?, description=? WHERE id=? RETURNING id, name, description, created_at";
    private static final String SELECT_BY_NAME_SQL =
            "SELECT * FROM networks.network WHERE name ILIKE ?";
    private static final String SELECT_BY_DESCRIPTION_SQL =
            "SELECT * FROM networks.network WHERE description ILIKE ?";



    public Network save(Network networkToSave) throws SQLException {
        try (var connection = getConnection(); var statement = connection.prepareStatement(INSERT_NETWORK_SQL)) {
            statement.setString(1, networkToSave.getName());
            statement.setString(2, networkToSave.getDescription());

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            return mapNetwork(resultSet);
        }
    }

    public List<Network> getAllNetworks() throws SQLException {
        try (var connection = getConnection(); var statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SELECT_ALL_NETWORKS_SQL);
            List<Network> networks = new ArrayList<>();

            while (resultSet.next()) {
                networks.add(mapNetwork(resultSet));
            }

            return networks;

        }
    }

    public Network update(Network networkToUpdate) throws SQLException {
        try (var connection = getConnection(); var statement = connection.prepareStatement(UPDATE_NETWORK_SQL)) {
            statement.setString(1, networkToUpdate.getName());
            statement.setString(2, networkToUpdate.getDescription());
            statement.setLong(3, networkToUpdate.getId());


            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return mapNetwork(resultSet);
        }
    }

    public Network mapNetwork(ResultSet resultSet) throws SQLException {
        return new Network(resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getTimestamp("created_at"));
    }
    public void remove(Network network) throws SQLException {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(DELETE_NETWORK_SQL)) {
            statement.setLong(1, network.getId());
            statement.execute();
        }
    }
    public List<Network> findByName(String name) throws SQLException {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(SELECT_BY_NAME_SQL)) {
            statement.setString(1, "%" + name + "%");
            var resultSet = statement.executeQuery();

            List<Network> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(mapNetwork(resultSet));
            }
            return result;
        }
    }

    public List<Network> findByDescription(String description) throws SQLException {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(SELECT_BY_DESCRIPTION_SQL)) {
            statement.setString(1, "%" + description + "%");
            var resultSet = statement.executeQuery();

            List<Network> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(mapNetwork(resultSet));
            }
            return result;
        }
    }
}
