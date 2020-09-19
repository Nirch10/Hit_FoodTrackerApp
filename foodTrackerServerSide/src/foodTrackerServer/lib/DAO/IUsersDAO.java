package foodTrackerServer.lib.DAO;

import foodTrackerServer.lib.Models.User;
import foodTrackerServer.lib.UsersPlatformException;
import java.sql.SQLException;
import java.util.Collection;

public interface IUsersDAO {
    User getUser(int userId) throws UsersPlatformException;
    User getUser(String userName) throws UsersPlatformException;
    Collection <User> getUsers() throws UsersPlatformException;
    void addUser(User user) throws UsersPlatformException, SQLException;
    void deleteUser(int id) throws UsersPlatformException, SQLException;
    void setPassword(int id , String password) throws UsersPlatformException, SQLException;;
}