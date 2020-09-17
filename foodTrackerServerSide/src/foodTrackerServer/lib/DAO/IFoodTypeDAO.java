package foodTrackerServer.lib.DAO;

import foodTrackerServer.lib.Models.FoodType;
import foodTrackerServer.lib.UsersPlatformException;
import java.sql.SQLException;
import java.util.Collection;

public interface IFoodTypeDAO {
    FoodType getFoodTypeId(int guid) throws UsersPlatformException;
    Collection<FoodType> getFoodTypes() throws UsersPlatformException;
    void setFoodTypeName(int guid, String newName) throws SQLException, UsersPlatformException;
    void addFoodType(FoodType foodType) throws UsersPlatformException, SQLException;
    void deleteFoodType(int guid) throws UsersPlatformException, SQLException;
}
