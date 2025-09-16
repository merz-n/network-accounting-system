package org.example.ui;

import org.example.model.Device;
import org.example.model.DeviceConnection;
import org.example.model.Model;
import org.example.model.Network;
import org.example.validator.UserAction;

import java.util.List;
import java.util.Scanner;

public class ConsoleController {
    private final Scanner scanner;

    public ConsoleController(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getUserChoice() {
        System.out.println("Enter: ");
        for (var action : UserAction.values()) {
            System.out.println(action.getCode() + " " + action.getDescription());
        }

        int code = scanner.nextInt();
        scanner.nextLine();
        return code;
    }

    public <T extends Model> T selectOf(List<T> models, String type) {
        System.out.println("Select " + type + " by id:");

        while (true) {
            models.forEach(System.out::println);

            int modelId = scanner.nextInt();
            scanner.nextLine();

            T selectedModel = null;
            for (var model : models) {
                if (model.getId() == modelId) {
                    selectedModel = model;
                    break;
                }
            }

            if (selectedModel == null)
                System.out.println("id was incorrect; try again");
            else
                return selectedModel;
        }
    }

    public Device readNewDevice() {
        String name = enterStringField("device", "name");
        String ipAddress = enterStringField("device", "ip");
        String macAddress = enterStringField("device", "mac");
        String status = enterStringField("device", "status");
        String type = enterStringField("device", "type");
        return new Device(name, ipAddress, macAddress, type,status);
    }

    public DeviceConnection readNewConnection() {
        String type = enterStringField("connection", "type");
        String status = enterStringField("connection", "status");
        return new DeviceConnection(type, status);
    }

    public Network readNetwork() {
        return updateFields(new Network());
    }

    public Network updateFields(Network network) {
        System.out.println("Enter network name: ");
        String name = scanner.next();
        scanner.nextLine();

        System.out.println("Enter network description: ");
        String description = scanner.nextLine();

        network.setName(name);
        network.setDescription(description);
        return network;
    }

    public void print(Network network) {
        System.out.println(network.toString());
    }

    public void print(Device device) {
        System.out.println(device.toString());
    }

    public void print(DeviceConnection deviceConnection) {
        System.out.println(deviceConnection.toString());
    }

    public void printError(String message) {
        System.out.println(message);
    }


    public String enterStringField(String entityName, String fieldName) {
        return enterString("Enter " + entityName + " " + fieldName + ":");
    }

    public String enterString(String request) {
        System.out.println(request);
        return scanner.nextLine();
    }
}
