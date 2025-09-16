package org.example.validator;

import java.util.Optional;

public enum UserAction {
    ADD_NETWORK(1, "Add network"),
    ADD_DEVICE(2, "Add device"),
    ADD_CONNECTION(3, "Add connection"),
    REMOVE_NETWORK(4, "Remove network"),
    REMOVE_CONNECTION(5, "Remove connection"),
    EDIT_NETWORK(6, "Edit network"),

    FIND_DEVICE_BY_NAME(10, "Найти устройство по имени"),
    FIND_DEVICE_BY_IP(11, "Найти устройство по IP-адресу"),
    FIND_DEVICE_BY_TYPE(12, "Найти устройство по типу"),
    FIND_DEVICE_BY_STATUS(13, "Найти устройство по статусу"),

    SHOW_DEVICES_AND_CONNECTIONS(14, "Показать устройства и подключения в выбранной сети"),

    SHOW_NETWORKS_AND_DEVICES(15, "Показать все сети и подключённые к ним устройства"),
    SHOW_ACTIVE_DEVICES_REPORT(16, "Отчёт: количество активных устройств в каждой сети"),
    EXIT(0, "Exit");

    private final int code;
    private final String description;

    UserAction(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<UserAction> valueOf(int code) {
        for (UserAction action : values()) {
            if (action.code == code)
                return Optional.of(action);
        }
        return Optional.empty();
    }
}
