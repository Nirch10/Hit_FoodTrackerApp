package foodTrackerServer.Tests;

import foodTrackerServer.Config.FoodTrackerServerConfigWrapper;
import foodTrackerServer.lib.*;
import foodTrackerServer.lib.DAO.*;
import foodTrackerServer.lib.Models.FoodType;
import foodTrackerServer.lib.Models.Record;
import foodTrackerServer.lib.Models.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

public class TransactionDAOUnitTest {
    static IRecordDAO tester;
    @BeforeClass
    public static void testSetup() throws IOException {
            tester = new HnetMySqlRecordDAO(FoodTrackerServerConfigWrapper.Deserialize("./Config.json"));
    }
    @AfterClass
    public static void testCleanup() {
        // Do your cleanup here like close URL connection , releasing resources etc
    }

    //Get Tests
    @Test
    public void testGetTransaction() throws UsersPlatformException, SQLException {
        int transactionGuid = 1;
        Record cl = tester.getRecord(transactionGuid);
        if(cl == null) throw new AssertionError("No transaction found");
        System.out.println(cl.getRecordId() + " :  "  + cl.getCalories() +", "
                + cl.getDateOfRecord()  +  ", " + cl.getDescription() +  ", " + cl.getUser().getUserName() +  ", " +
                cl.getFoodType().getName());
    }
    @Test
    public void testGetTransactionByUser() throws UsersPlatformException {
        int userId = 1;
        Collection<Record> res = tester.getUserRecords(userId);
        if(res.size() == 0) throw new AssertionError("No transaction found");
        res.forEach(cl -> System.out.println(cl.getRecordId() + " :  " + cl.getCalories() +", "
                + cl.getDateOfRecord()  +  ", " + cl.getDescription() +  ", " + cl.getUser().getUserName() +  ", " +
                cl.getFoodType().getName()));
    }
    @Test
    public void testGetTransactionByRetail() throws Exception {
        int retailGuid = 1;
        Collection<Record> res = tester.getFoodTypeRecords(retailGuid);
        if(res.size() == 0) throw new AssertionError("No transaction found");
        res.forEach(cl -> System.out.println(cl.getRecordId() + " :  " + cl.getCalories() +", "
                + cl.getDateOfRecord()  +  ", " + cl.getDescription() +  ", " + cl.getUser().getUserName() +  ", " +
                cl.getFoodType().getName()));
    }
    @Test
    public void testGetTransactionByDateRange() throws UsersPlatformException {
//        LocalDate d1 = LocalDate.of(2020, 05, 10);
//        LocalDate d2 = LocalDate.of(2020, 05, 15);
        Date d1 = new Date(2020 -1900,00,01);
        Date d2 = new Date(2020 - 1900,05,31);
        Collection<Record> res = tester.getUserRecords(d1, d2,1);
        if(res.size() == 0) throw new AssertionError("No transaction found");
        res.forEach(cl -> System.out.println(cl.getRecordId() + " :  "  + cl.getCalories() +", "
                + cl.getDateOfRecord()  +  ", " + cl.getDescription() +  ", " + cl.getUser().getUserName() +  ", " +
                cl.getFoodType().getName()));
    }

    //Update Tests
    @Test
    public void testUpdateTransaction() throws IOException {
        LocalDate d = LocalDate.of(2020, 05, 10);
        String desc = "Lord Of The Rings";
        IFoodTypeDAO retailDAO = new HibernateFoodTypeDAO(FoodTrackerServerConfigWrapper.Deserialize("./Config.json"));
        IUsersDAO usersDAO = new HibernateUserDAO(FoodTrackerServerConfigWrapper.Deserialize("./Config.json"));
    }
    @Test
    public void testUpdateTransactionUser() throws UsersPlatformException, SQLException, IOException {
        IUsersDAO usersDAO = new HibernateUserDAO(FoodTrackerServerConfigWrapper.Deserialize("./Config.json"));
        int userGuid = 8;
        User user = usersDAO.getUser(userGuid);
        int newUserGuid = 4;
        User newUser = usersDAO.getUser(newUserGuid);
        Collection<Record> transactionsByUser = tester.getUserRecords(userGuid);
        for (Record t :
                transactionsByUser) {
            tester.setRecordUser(t.getRecordId(), newUser);
        }
    }
    //Insert Tests
    @Test
    public void testInsertTransaction() throws UsersPlatformException, IOException {
        IFoodTypeDAO retailDAO = new HibernateFoodTypeDAO(FoodTrackerServerConfigWrapper.Deserialize("./Config.json"));
        IUsersDAO usersDAO = new HibernateUserDAO(FoodTrackerServerConfigWrapper.Deserialize("./Config.json"));
        Collection<User> users = usersDAO.getUsers();
        Collection<FoodType> retails = retailDAO.getFoodTypes();
        boolean isIncome = false;
        double price = 1.5;
        for (User user : users) {
            for (FoodType foodType : retails) {
                isIncome = !isIncome;
                price *= 1.5;
//                try {
//                    //tester.addRecord();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    throw new AssertionError();
//                } catch (UsersPlatformException e) {
//                    e.printStackTrace();
//                    throw new AssertionError();
//                }

            }
        }


        }
    //Delete Tests
    @Test
    public void testDeleteTransaction(){
       int transactionGuid = 1;
        try{
            tester.deleteRecord(transactionGuid);
        } catch (UsersPlatformException e) {
            e.printStackTrace();
            throw new AssertionError();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }
}
