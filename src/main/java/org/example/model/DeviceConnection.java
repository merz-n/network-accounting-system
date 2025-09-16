package org.example.model;

import java.util.Date;

public class DeviceConnection implements Model {

    private long id;
    private long deviceFromId;
    private long deviceToId;
    private String type;
    private String status;
    private Date createdAt;

    public DeviceConnection(long id, long deviceFromId, long deviceToId, String type, String status, Date createdAt) {
        this.id = id;
        this.deviceFromId = deviceFromId;
        this.deviceToId = deviceToId;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
    }

    public DeviceConnection(String type, String status) {
        this.type = type;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDeviceFromId() {
        return deviceFromId;
    }

    public void setDeviceFromId(long deviceFromId) {
        this.deviceFromId = deviceFromId;
    }

    public long getDeviceToId() {
        return deviceToId;
    }

    public void setDeviceToId(long deviceToId) {
        this.deviceToId = deviceToId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "DeviceConnection{" +
                "id=" + id +
                ", deviceFromId=" + deviceFromId +
                ", deviceToId=" + deviceToId +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
