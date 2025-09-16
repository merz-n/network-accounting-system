import org.example.dao.NetworkDao;
import org.example.model.Network;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NetworkDaoTest {

    private NetworkDao dao;

    @BeforeEach
    void setUp() {
        dao = new NetworkDao();
    }

    @Test
    void testSaveAndFindByName() throws SQLException {
        String name = "JUnit-Net-" + java.util.UUID.randomUUID();
        Network network = new Network(name, "Test network");

        dao.save(network);
        List<Network> found = dao.findByName(name);

        assertEquals(1, found.size());
        Network result = found.get(0);

        assertEquals(name, result.getName());
        assertEquals("Test network", result.getDescription());
    }

    @Test
    void testUpdateNetwork() throws SQLException {
        String initialName = "ToUpdate-" + java.util.UUID.randomUUID();
        Network net = dao.save(new Network(initialName, "Initial"));

        String newName = "Updated-" + java.util.UUID.randomUUID();
        net.setName(newName);
    }

    @Test
    void testRemoveNetwork() throws SQLException {
        Network net = dao.save(new Network("ToDelete", "To be removed"));
        dao.remove(net);

        List<Network> found = dao.findByName("ToDelete");
        assertTrue(found.isEmpty());
    }
}
