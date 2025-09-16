import org.example.dao.DeviceDao;
import org.example.dao.NetworkDao;
import org.example.model.Device;
import org.example.model.Network;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeviceDaoTest {

    private NetworkDao networkDao;
    private DeviceDao deviceDao;
    private Network testNetwork;

    @BeforeEach
    void setUp() throws SQLException {
        networkDao = new NetworkDao();
        deviceDao = new DeviceDao();

        // создаём тестовую сеть
        String randomName = "DeviceTestNet_" + java.util.UUID.randomUUID();
        testNetwork = networkDao.save(new Network(randomName, "тестовая сеть для устройств"));
    }

    @Test
    void testSaveDeviceAndFindByIp() throws SQLException {
        String macAddress = UUID.randomUUID().toString().substring(0, 12).replaceAll("(.{2})", "$1:").replaceAll(":$", "").toUpperCase();
        String ip = "192.168.1." + new Random().nextInt(255);
        Device device = new Device("Test Device", ip, macAddress, "Router", "Active", testNetwork.getId());

        deviceDao.save(device);

        List<Device> found = deviceDao.findByIp(ip);
        assertEquals(1, found.size());
        assertEquals(macAddress, found.get(0).getMacAddress());

    }


    @Test
    void testFindByNameAndType() throws SQLException {
        Device device = new Device("Printer", "192.168.1.2", null, "Printer", "inactive");
        device.setNetworkId(testNetwork.getId());
        deviceDao.save(device);

        List<Device> byName = deviceDao.findByName("Printer");
        assertFalse(byName.isEmpty());

        List<Device> byType = deviceDao.findByType("Printer");
        assertFalse(byType.isEmpty());
    }

    @Test
    void testFindByStatus() throws SQLException {
        Device device = new Device("Switch1", "192.168.1.3", null, "Switch", "active");
        device.setNetworkId(testNetwork.getId());
        deviceDao.save(device);

        List<Device> activeDevices = deviceDao.findByStatus("active");
        assertTrue(activeDevices.stream().anyMatch(d -> d.getName().equals("Switch1")));
    }
}