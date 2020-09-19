package foodTrackerServer.lib.DAO;

import com.sun.istack.internal.NotNull;
import foodTrackerServer.lib.Models.FoodType;
import foodTrackerServer.lib.Models.Record;
import foodTrackerServer.lib.Models.User;
import foodTrackerServer.lib.UsersPlatformException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

public interface IRecordDAO {
    Record getRecord(int transaction_id) throws UsersPlatformException, SQLException;
    Collection<Record> getUserRecords(int userId) throws UsersPlatformException;
    Collection<Record> getFoodTypeRecords(int retailId) throws UsersPlatformException;
    Collection<Record> getUserRecords(Date from, Date to, int userGuid) throws UsersPlatformException;
    void setRecordFoodType(@NotNull int recordId, @NotNull FoodType newFoodType) throws UsersPlatformException, SQLException;
    void setRecordDate(@NotNull int recordId, @NotNull Date newDate) throws UsersPlatformException, SQLException;
    void setRecordUser(@NotNull int recordId, @NotNull User newUser) throws UsersPlatformException, SQLException;
    void addRecord(@NotNull Record record) throws SQLException, UsersPlatformException;
    void deleteRecord(@NotNull int guid) throws UsersPlatformException, SQLException;
}