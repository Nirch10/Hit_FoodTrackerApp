package foodTrackerServer.lib.DAO;

import com.sun.istack.internal.NotNull;
import foodTrackerServer.Config.FoodTrackerServerConfig;
import foodTrackerServer.lib.Models.Record;
import foodTrackerServer.lib.Models.User;
import foodTrackerServer.lib.QueryUtils.AbstractDbConnector;
import foodTrackerServer.lib.QueryUtils.HibernateDbConnector;
import foodTrackerServer.lib.QueryUtils.HibernateDbExecutor;
import foodTrackerServer.lib.QueryUtils.IDbExecutor;
import foodTrackerServer.lib.UsersPlatformException;
import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

public class HibernateUserDAO implements IUsersDAO {
    private String tableName = "User";
    private String guidColumn = "UserId";
    private String userNameColumn = "UserName";
    private IDbExecutor<User> executor;
    private AbstractDbConnector dbConnector;
    private IRecordDAO transactionDAO;
    private User userClassType;
    private final String hibernateConfigPath;//`"C:\\code\\Hit_ApplicationsCostManager\\il.ac.hit.costmanagerapp\\out\\production\\il.ac.hit.costmanagerapp\\costmanagerapp\\lib\\Models\\hibernate.cfg.xml";

    public HibernateUserDAO(@NotNull FoodTrackerServerConfig config){this(config,new HibernateDbExecutor<>(), new HibernateRecordDAO(config),  null);}
    public HibernateUserDAO(@NotNull FoodTrackerServerConfig config, @NotNull IDbExecutor<User> queryExecutor,
                            @NotNull IRecordDAO inputTransactionDAO, AbstractDbConnector connector){
       hibernateConfigPath = config.HibernateConfigPath;
        executor = queryExecutor;
        transactionDAO = inputTransactionDAO;
        userClassType =  new User();
        if(connector != null)
            dbConnector = connector;
        else
            dbConnector = new HibernateDbConnector(new File(hibernateConfigPath));
    }

    @Override
    public User getUser(int userGuid) throws UsersPlatformException {
        executor.openConnection(dbConnector);
        Collection<User> rs = executor.tryExecuteGetQuery(dbConnector, "SELECT * FROM " + tableName +
                " WHERE "+guidColumn+" =" + userGuid, userClassType.getClass());
        executor.closeConnection();
        if (rs == null) throw new UsersPlatformException("Query result was null");
        if (rs.size() <= 0) throw new UsersPlatformException("User {" + userGuid + "}does not exist");
        return rs.stream().findFirst().get();
    }

    @Override
    public User getUser(String userName) {
        executor.openConnection(dbConnector);
        Collection<User> rs = executor.tryExecuteGetQuery(dbConnector, "SELECT * FROM " + tableName +
                " WHERE "+userNameColumn+" =\"" + userName+"\"", userClassType.getClass());
        executor.closeConnection();
        if (rs == null) return null;
        if (rs.size() <= 0) return null;
        return rs.stream().findFirst().get();
    }

    @Override
    public Collection<User> getUsers() throws UsersPlatformException {
        Collection<User> users;
        executor.openConnection(dbConnector);
        users = executor.tryExecuteGetQuery(dbConnector, "SELECT * FROM " + tableName, userClassType.getClass());
        executor.closeConnection();
        if (users == null) throw new UsersPlatformException("Query result was null");
        return users;
    }
    @Override
    public void addUser(User user) throws UsersPlatformException, SQLException {
        executor.openConnection(dbConnector);
        boolean resultsFlag = executor.TryExecuteInsertQuery(dbConnector, user);
        executor.closeConnection();
        if (!resultsFlag) throw new UsersPlatformException("Could not insert new user");
    }
    @Override
    public void deleteUser(int userGuid) throws UsersPlatformException, SQLException {
        User user = getUser(userGuid);
        if(user == null) throw new UsersPlatformException("User {" + userGuid + "} not found");
        if(user.getUserName() == "None" || userGuid == 1)throw new UsersPlatformException("Cant delete the none object");
        User noneUser = getUser(1);
        if(noneUser == null) noneUser = new User("None","");
        Date noneDate = new Date(0,0,0);
        for (Record record : transactionDAO.getUserRecords(user.getUserId())) {
            transactionDAO.setRecordDate(record.getRecordId(),noneDate);
            transactionDAO.setRecordUser(record.getRecordId(),noneUser);
        }
        executor.openConnection(dbConnector);
        boolean resultsFlag = executor.TryExecuteDeleteQuery(dbConnector,user);
        executor.closeConnection();
        if (!resultsFlag) throw new UsersPlatformException("Could not delete user {" + userGuid + "}");
    }
    @Override
    public void setPassword(int userGuid, String newPassword) throws UsersPlatformException, SQLException {
        if(userGuid == 1)throw new UsersPlatformException("Cant update None object");
        User user = getUser(userGuid);
        user.setPassword(newPassword);
        executor.openConnection(dbConnector);
        boolean resultsFlag = executor.TryExecuteUpdateQuery(dbConnector, user);
        executor.closeConnection();
        if(!resultsFlag) throw new UsersPlatformException("Could not update Retail");
    }
}
