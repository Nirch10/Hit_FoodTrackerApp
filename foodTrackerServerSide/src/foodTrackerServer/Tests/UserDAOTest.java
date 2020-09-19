package foodTrackerServer.Tests;

import com.mysql.jdbc.AssertionFailedException;
import foodTrackerServer.Config.FoodTrackerConfigWrapper;
import foodTrackerServer.lib.DAO.HibernateUserDAO;
import foodTrackerServer.lib.DAO.IUsersDAO;
import foodTrackerServer.lib.Models.User;
import foodTrackerServer.lib.UsersPlatformException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Scanner;

public class UserDAOTest {
    static IUsersDAO tester;
    @BeforeClass
    public static void testSetup() throws IOException {
            tester = new HibernateUserDAO(FoodTrackerConfigWrapper.Deserialize("./Config.json"));
    }
    @AfterClass
    public static void testCleanup() {
    }
    //Get Tests
    @Test
    public void testGetUser() throws UsersPlatformException {
        testGetUser(1);
    }
    @Test
    public void testGetAllUsers() throws UsersPlatformException {
        Collection<User> users = tester.getUsers();
        if (users.size() == 0) throw new AssertionError("no users");
    }

    private void testGetUser(int userGuid) throws UsersPlatformException {
        User res;
        res= tester.getUser(userGuid);
        if (res == null) throw new AssertionError("User not found");
    }
    //Insert Tests
    @Test
    public void testInsertUser() {
        Scanner scanner = new Scanner(System.in);
        String[] names = {"None","Royi", "Ran", "Haim"};
        String[] passwords= {"***","111", "222", "abc123"};
        for (int i =0;i< names.length;i++) {
            try {
                tester.addUser(new User(names[i],passwords[i])) ;
            } catch (UsersPlatformException e) {
                throw new AssertionError(e.getMessage());
            } catch (SQLException e) {
                throw new AssertionError(e.getMessage());
            }
        }
    }
    //Delete Tests
    @Test
    public void testDeleteUser(){
        int userGuid = 2;
        try {
            User u = tester.getUser(userGuid);
            if (u == null) throw new ValueException("User doesnt exist");
            tester.deleteUser(u.getUserId());

        } catch (UsersPlatformException e) {
            throw new AssertionError(e.getMessage());
        } catch (SQLException e) {
            throw new AssertionError(e.getMessage());
        }
    }
    //Update Tests
    @Test
    public void testSetPassword(){
        String newPass = "456";
        int userGuid = 1;
        try {
            if (tester.getUser(userGuid) == null) throw new AssertionFailedException(new Exception("User was not found"));
            tester.setPassword(userGuid, newPass);
            User u1 = tester.getUser(userGuid);
        } catch (UsersPlatformException e) {
            throw new AssertionError(e.getMessage());
        } catch (SQLException e) {
            throw new AssertionError(e.getMessage());
        }
    }
}