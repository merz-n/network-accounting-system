
import org.example.dao.DeviceConnectionDao;
import org.example.dao.DeviceDao;
import org.example.dao.NetworkDao;
import org.example.model.Device;
import org.example.model.DeviceConnection;
import org.example.model.Network;
import org.junit.jupiter.api.*;

        import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeviceConnectionDaoTest {

    private NetworkDao networkDao;
    private DeviceDao deviceDao;
    private DeviceConnectionDao connectionDao;
    private Network testNetwork;

    @BeforeEach
    void setUp() throws SQLException {
        networkDao = new NetworkDao();
        deviceDao = new DeviceDao();
        connectionDao = new DeviceConnectionDao();

        // создаём тестовую сеть
        testNetwork = networkDao.save(new Network("ConnTest_" + UUID.randomUUID(), "тестовая сеть соединений"));
    }

    @Test
    void testSaveAndGetAllConnections() throws SQLException {
        // создаём два устройства
        Device device1 = createRandomDevice();
        Device device2 = createRandomDevice();

        // создаём соединение между ними
        DeviceConnection connection = new DeviceConnection("Ethernet", "active");
        connection.setDeviceFromId(device1.getId());
        connection.setDeviceToId(device2.getId());

        DeviceConnection saved = connectionDao.save(connection);
        assertNotNull(saved.getId());
        assertEquals("Ethernet", saved.getType());

        // проверяем, что оно есть в списке всех соединений
        List<DeviceConnection> all = connectionDao.getAllConnections();
        assertTrue(all.stream().anyMatch(c -> c.getId() == saved.getId()));
    }

    @Test
    void testRemoveConnection() throws SQLException {
        // создаём два устройства
        Device device1 = createRandomDevice();
        Device device2 = createRandomDevice();

        // соединение
        DeviceConnection connection = new DeviceConnection("WiFi", "inactive");
        connection.setDeviceFromId(device1.getId());
        connection.setDeviceToId(device2.getId());
        DeviceConnection saved = connectionDao.save(connection);

        // удаляем
        connectionDao.remove(saved);

        List<DeviceConnection> all = connectionDao.getAllConnections();
        assertTrue(all.stream().noneMatch(c -> c.getId() == saved.getId()));
    }

    private Device createRandomDevice() throws SQLException {
        String name = "Dev_" + UUID.randomUUID().toString().substring(0, 5);
        String ip = "10.0.0." + new Random().nextInt(255);
        String mac = UUID.randomUUID().toString().substring(0, 12).replaceAll("(.{2})", "$1:").replaceAll(":$", "").toUpperCase();
        String type = new Random().nextBoolean() ? "Router" : "Switch";
        String status = new Random().nextBoolean() ? "active" : "inactive";

        Device device = new Device(name, ip, mac, type, status, testNetwork.getId());
        return deviceDao.save(device);
    }
}
