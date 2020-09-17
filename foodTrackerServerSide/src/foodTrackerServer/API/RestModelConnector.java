package foodTrackerServer.API;

import foodTrackerServer.lib.DAO.IFoodTypeDAO;
import foodTrackerServer.lib.DAO.IRecordDAO;
import foodTrackerServer.lib.DAO.IUsersDAO;
import foodTrackerServer.lib.UsersPlatformException;

public class RestModelConnector {
    private IUsersDAO usersDAO;
    private IFoodTypeDAO retailDAO;
    private IRecordDAO transactionDAO;

    public RestModelConnector(IUsersDAO usersDAO, IFoodTypeDAO retailDAO, IRecordDAO transactionDAO) throws UsersPlatformException {
       if(usersDAO == null || retailDAO == null || transactionDAO == null)throw new UsersPlatformException("Invalid DAO");
        this.usersDAO = usersDAO;
        this.retailDAO = retailDAO;
        this.transactionDAO = transactionDAO;
    }

    public IRecordDAO getTransactionDAO() {
        return transactionDAO;
    }

    public IFoodTypeDAO getRetailDAO() {
        return retailDAO;
    }
    public IUsersDAO getUsersDAO() {
        return usersDAO;
    }
}
