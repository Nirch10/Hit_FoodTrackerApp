package foodTrackerServer.lib.DAO;

import com.sun.istack.internal.NotNull;
import foodTrackerServer.Config.FoodTrackerServerConfig;
import foodTrackerServer.lib.Models.FoodType;
import foodTrackerServer.lib.Models.Record;
import foodTrackerServer.lib.QueryUtils.AbstractDbConnector;
import foodTrackerServer.lib.QueryUtils.HibernateDbConnector;
import foodTrackerServer.lib.QueryUtils.HibernateDbExecutor;
import foodTrackerServer.lib.QueryUtils.IDbExecutor;
import foodTrackerServer.lib.UsersPlatformException;
import java.io.File;
import java.sql.SQLException;
import java.util.Collection;

public class HibernateFoodTypeDAO implements IFoodTypeDAO {
    private String guidColumn = "RecordId";
    private String tableName = "Record";
    private IDbExecutor<FoodType> executor;
    private AbstractDbConnector dbConnector;
    private IRecordDAO transactionDAO;
    private FoodType FoodType;
    private final String filePath;

    public HibernateFoodTypeDAO(@NotNull FoodTrackerServerConfig config){this(config,new HibernateDbExecutor<>(),
            new HnetMySqlRecordDAO(config), null);}
    public HibernateFoodTypeDAO(@NotNull FoodTrackerServerConfig config, @NotNull IDbExecutor<FoodType> queryExecutor,
                                @NotNull IRecordDAO inputTransactionDAO, AbstractDbConnector connector){
        filePath = config.HibernateConfigPath;
        executor = queryExecutor;
        FoodType =  new FoodType();
        transactionDAO = inputTransactionDAO;
        if(connector != null)
            dbConnector = connector;
        else
            dbConnector = new HibernateDbConnector(new File(filePath));
    }

    @Override
    public FoodType getFoodTypeId(@NotNull int retailGuid) throws UsersPlatformException {
        executor.openConnection(dbConnector);
        Collection<FoodType> queryResults = executor.tryExecuteGetQuery(dbConnector,
                "SELECT * FROM " + tableName + " WHERE "+guidColumn+"=" + retailGuid, FoodType.getClass());
        executor.closeConnection();

        if (queryResults == null)throw new UsersPlatformException("Query result was null");
        if (queryResults.size() <= 0)throw new UsersPlatformException("Retail does not exist");

        return queryResults.stream().findFirst().get();
    }
    @Override
    public Collection<FoodType> getFoodTypes() throws UsersPlatformException {
        executor.openConnection(dbConnector);
        Collection<FoodType> retails = executor.tryExecuteGetQuery(dbConnector, "SELECT * FROM " + tableName, FoodType.getClass());
        executor.closeConnection();
        if (retails == null)throw new UsersPlatformException("Query result was null");

        return retails;
    }
    @Override
    public void setFoodTypeName(@NotNull int retailGuid, @NotNull String retailNewName) throws UsersPlatformException, SQLException {
        if(retailGuid == 1)throw new UsersPlatformException("Cant update None object");
        FoodType foodTypeToSet = getFoodTypeId(retailGuid);
        if(foodTypeToSet == null)throw new UsersPlatformException("Retail {"+retailGuid + "} not found");
        foodTypeToSet.setName(retailNewName);
        executor.openConnection(dbConnector);
        boolean resultsFlag = executor.TryExecuteUpdateQuery(dbConnector, foodTypeToSet);
        executor.closeConnection();
        if(!resultsFlag)throw new UsersPlatformException("Could not update Retail {" + retailGuid + "} name");
    }
    @Override
    public void addFoodType(@NotNull FoodType foodType) throws UsersPlatformException, SQLException {
        executor.openConnection(dbConnector);
        boolean resultsFlag = executor.TryExecuteInsertQuery(dbConnector, foodType);
        executor.closeConnection();
        if (!resultsFlag) throw new UsersPlatformException("Could not Insert new Retail");
    }
    @Override
    public void deleteFoodType(@NotNull int retailGuid) throws UsersPlatformException, SQLException {
        FoodType rt = getFoodTypeId(retailGuid);
        if(rt.getName() == "None" || retailGuid == 1)throw new UsersPlatformException("Cant delete the none object");
        FoodType noneRetail = getFoodTypeId(1);
        if(noneRetail == null)noneRetail = new FoodType(1,"None");
        for (Record record : transactionDAO.getFoodTypeRecords(rt.getTypeId())) {
            transactionDAO.setRecordFoodType(record.getRecordId(), noneRetail);
        }
        executor.openConnection(dbConnector);
        boolean resultsFlag = executor.TryExecuteDeleteQuery(dbConnector,rt);
        executor.closeConnection();
        if (!resultsFlag) throw new UsersPlatformException("Could not delete Retail {"+retailGuid+"}");
    }
}
