package foodTrackerServer.Tests;

import com.mysql.jdbc.AssertionFailedException;
import foodTrackerServer.Config.FoodTrackerServerConfigWrapper;
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
            tester = new HibernateUserDAO(FoodTrackerServerConfigWrapper.Deserialize("./Config.json"));
    }
    @AfterClass
    public static void testCleanup() {
        // Do your cleanup here like close URL connection , releasing resources etc
    }
    //Get Tests
    @Test
    public void testGetUser() throws UsersPlatformException {
        testGetUser(1);
    }
    @Test
    public void testGetAllUsers() throws UsersPlatformException {
        Collection<User> users = tester.getUsers();
        if (users.size() == 0) throw new AssertionError("empty list");
        users.forEach(u -> System.out.println(u.getUserId() + " : " + u.getUserName() ));
    }
    @Test
    public void testGetUsersOneByOne() throws UsersPlatformException {
        Collection<User> users = tester.getUsers();
        users.forEach(user -> {
            try {
                testGetUser(user.getUserId());
            } catch (UsersPlatformException e) {
                e.printStackTrace();
            }
        });
    }
    private void testGetUser(int userGuid) throws UsersPlatformException {
        User res;
        res= tester.getUser(userGuid);
        if (res == null) throw new AssertionError("User not found");
        System.out.println(res.getUserId() + " : " + res.getUserName());
    }
    //Insert Tests
    @Test
    public void testInsertUser() {
        Scanner scanner = new Scanner(System.in);
        String[] names = {"None","Achi", "Oren", "Haim"};
        String[] passwords= {"***","abc", "  ", "abc 123"};
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
        int userGuid = 6;
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
            if (u1.getPassword() == newPass) throw new AssertionFailedException(new Exception("Did not change password"));
        } catch (UsersPlatformException e) {
            throw new AssertionError(e.getMessage());
        } catch (SQLException e) {
            throw new AssertionError(e.getMessage());
        }
    }
}