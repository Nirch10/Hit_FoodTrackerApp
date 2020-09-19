package foodTrackerServer.lib.DAO;

import com.sun.istack.internal.NotNull;
import foodTrackerServer.Config.FoodTrackerServerConfig;
import foodTrackerServer.lib.Models.FoodType;
import foodTrackerServer.lib.Models.Record;
import foodTrackerServer.lib.Models.User;
import foodTrackerServer.lib.QueryUtils.AbstractDbConnector;
import foodTrackerServer.lib.QueryUtils.HibernateDbConnector;
import foodTrackerServer.lib.QueryUtils.HibernateDbExecutor;
import foodTrackerServer.lib.QueryUtils.IDbExecutor;
import foodTrackerServer.lib.UsersPlatformException;
import org.hibernate.Hibernate;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class HibernateRecordDAO implements IRecordDAO {
    private String recordsTableName = "Record";
    private String recordDateColName = "DateOfRecord";
    private String recordIdColName = "RecordId";
    private String userIdColName = "UserId";
    private String foodTypeIdColName = "FoodTypeId";
    private IDbExecutor<Record> executor;
    private AbstractDbConnector dbConnector;
    private Record recordClass;
    private final String configFilePath;

    public HibernateRecordDAO(@NotNull FoodTrackerServerConfig config) {this(config, new HibernateDbExecutor() ,null);}
    public HibernateRecordDAO(@NotNull FoodTrackerServerConfig config, @NotNull IDbExecutor iQueryExecuter,
                              AbstractDbConnector abstractDbConnector){
        configFilePath = config.HibernateConfigPath;
        executor = iQueryExecuter;
        recordClass = new Record();
        if (abstractDbConnector != null)
            dbConnector = abstractDbConnector;
        else
            dbConnector = new HibernateDbConnector(new File(configFilePath));
    }

    private Collection<Record> getRecords(String query) {
        executor.openConnection(dbConnector);
        Collection<Record> results =executor.tryExecuteGetQuery(dbConnector, query, recordClass.getClass());
        results.forEach(r ->{
            Hibernate.initialize(r.getUser());
            Hibernate.initialize(r.getFoodType());
        });
        executor.closeConnection();
        return results;
    }
    @Override
    public Record getRecord(int transactionGuid) throws UsersPlatformException {
        String stringQuery = ("SELECT * FROM " + recordsTableName + " WHERE "+ recordIdColName +"=" + transactionGuid);

        Collection<Record> results = getRecords(stringQuery);
        if(results == null)throw new UsersPlatformException("Query result was null");
        if(results.size() <= 0) throw new UsersPlatformException("Transaction {"+transactionGuid + "} was not found");
        return results.stream().findFirst().get();
    }
    @Override
    public Collection<Record> getUserRecords(int userGuid) throws UsersPlatformException {
        String stringQuery = ("SELECT * FROM " + recordsTableName + " WHERE " + userIdColName + " =" + userGuid);
        Collection records = getRecords(stringQuery);
        if (records == null) throw new UsersPlatformException("Query result was null");
        return records;
    }
    @Override
    public Collection<Record> getFoodTypeRecords(int retailGuid) throws UsersPlatformException{
        String stringQuery = ("SELECT * FROM " + recordsTableName + " WHERE "+ foodTypeIdColName +"=" + retailGuid);
        Collection transactions = getRecords(stringQuery);
        if (transactions == null) throw new UsersPlatformException("Query result was null");
        return transactions;
    }
    @Override
    public Collection<Record> getUserRecords(Date from, Date to, int userGuid) throws UsersPlatformException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String stringQuery = ("SELECT * FROM " + recordsTableName + " WHERE "+ recordDateColName +" BETWEEN '" +
                formatter.format(from)+ "' and '" + formatter.format(to)+ "' and "+ userIdColName +" = "+ userGuid + "  ORDER BY " + recordDateColName + " desc");
        Collection records = getRecords(stringQuery);
        if (records == null) throw new UsersPlatformException("Query result was null");
        return records;
    }
    @Override
    public void setRecordDate(int transactionGuid, Date newDate) throws UsersPlatformException, SQLException {
        Record record = getRecord(transactionGuid);
        if(record == null)throw new UsersPlatformException("Transaction {"+transactionGuid+"} was not found");
        record.setDateOfRecord(newDate);
        executor.openConnection(dbConnector);
        executor.TryExecuteUpdateQuery(dbConnector, record);
        executor.closeConnection();
    }
    @Override
    public void setRecordFoodType(int recordId, FoodType newFoodType) throws UsersPlatformException, SQLException {
        Record record = getRecord(recordId);
        if(record == null)throw new UsersPlatformException("Transaction {"+recordId+"} was not found");
        record.setFoodType(newFoodType);
        executor.openConnection(dbConnector);
        executor.TryExecuteUpdateQuery(dbConnector, record);
        executor.closeConnection();

    }
    @Override
    public void setRecordUser(int recordId, User newUser) throws UsersPlatformException, SQLException {
        Record record = getRecord(recordId);
        if(record == null)throw new UsersPlatformException("Transaction {"+recordId+"} was not found");
        record.setUser(newUser);
        executor.openConnection(dbConnector);
        executor.TryExecuteUpdateQuery(dbConnector, record);
        executor.closeConnection();
    }
    @Override
    public void addRecord(Record record) throws SQLException, UsersPlatformException {
        executor.openConnection(dbConnector);
        boolean resultsFlag = executor.TryExecuteInsertQuery(dbConnector, record);
        executor.closeConnection();
        if (!resultsFlag) throw new UsersPlatformException("Could not update Retail");
    }
    @Override
    public void deleteRecord(int transactionGuid) throws UsersPlatformException{
        Record record = getRecord(transactionGuid);
        if(record == null)throw new UsersPlatformException("Transaction {"+transactionGuid + "} not found");
        executor.openConnection(dbConnector);
        executor.tryExecuteWildCardQuery("DELETE FROM Transaction WHERE "+ recordIdColName +" = " + record.getRecordId());
        executor.closeConnection();
    }
}
