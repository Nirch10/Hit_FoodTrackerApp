package foodTrackerServer.API;

import foodTrackerServer.lib.DAO.IFoodTypeDAO;
import foodTrackerServer.lib.DAO.IRecordDAO;
import foodTrackerServer.lib.DAO.IUsersDAO;
import foodTrackerServer.lib.UsersPlatformException;

public class RestModelConnector {
    private IUsersDAO usersDAO;
    private IFoodTypeDAO foodTypeDAO;
    private IRecordDAO recordsDAO;

    public RestModelConnector(IUsersDAO usersDAO, IFoodTypeDAO foodTypeDAO, IRecordDAO recordsDAO) throws UsersPlatformException {
       if(usersDAO == null || foodTypeDAO == null || recordsDAO == null)throw new UsersPlatformException("Invalid DAO");
        this.usersDAO = usersDAO;
        this.foodTypeDAO = foodTypeDAO;
        this.recordsDAO = recordsDAO;
    }

    public IRecordDAO getRecordsDAO() {
        return recordsDAO;
    }
    public IFoodTypeDAO getFoodTypeDAO() {
        return foodTypeDAO;
    }
    public IUsersDAO getUsersDAO() {
        return usersDAO;
    }
}
