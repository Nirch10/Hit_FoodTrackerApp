package foodTrackerServer.Tests;

import foodTrackerServer.Config.FoodTrackerConfigWrapper;
import foodTrackerServer.lib.*;
import foodTrackerServer.lib.DAO.*;
import foodTrackerServer.lib.Models.Record;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

public class RecordsDAOTest {
    static IRecordDAO tester;
    @BeforeClass
    public static void testSetup() throws IOException {
            tester = new HibernateRecordDAO(FoodTrackerConfigWrapper.Deserialize("./Config.json"));
    }
    @AfterClass
    public static void testCleanup() {
        // Do your cleanup here like close URL connection , releasing resources etc
    }

    //Get Tests
    @Test
    public void testGetRecord() throws UsersPlatformException, SQLException {
        Record cl = tester.getRecord(1);
        if(cl == null) throw new AssertionError("No records");
    }
    @Test
    public void testGetUsersRecords() throws UsersPlatformException {
        int userId = 1;
        Collection<Record> res = tester.getUserRecords(userId);
        if(res.size() == 0) throw new AssertionError("No records");
    }
    @Test
    public void testGetTypesRecords() throws Exception {
        int retailGuid = 1;
        Collection<Record> res = tester.getFoodTypeRecords(retailGuid);
        if(res.size() == 0) throw new AssertionError("No records");
    }
}
