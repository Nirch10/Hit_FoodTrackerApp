package foodTrackerServer.lib.DAO;

import com.sun.istack.internal.NotNull;
import foodTrackerServer.Config.FoodTrackerConfig;
import foodTrackerServer.lib.Models.FoodType;
import foodTrackerServer.lib.Models.Record;
import foodTrackerServer.lib.Query.AbstractDbConnector;
import foodTrackerServer.lib.Query.HibernateDbConnector;
import foodTrackerServer.lib.Query.HibernateDbExecutor;
import foodTrackerServer.lib.Query.IDbExecutor;
import foodTrackerServer.lib.UsersPlatformException;
import java.io.File;
import java.sql.SQLException;
import java.util.Collection;

public class HibernateFoodTypeDAO implements IFoodTypeDAO {
    private String guidColumn = "TypeId";
    private String tableName = "FoodType";
    private IDbExecutor<FoodType> executor;
    private AbstractDbConnector dbConnector;
    private IRecordDAO transactionDAO;
    private FoodType FoodType;
    private final String filePath;

    public HibernateFoodTypeDAO(@NotNull FoodTrackerConfig config){this(config,new HibernateDbExecutor<>(),
            new HibernateRecordDAO(config), null);}
    public HibernateFoodTypeDAO(@NotNull FoodTrackerConfig config, @NotNull IDbExecutor<FoodType> queryExecutor,
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
    public FoodType getFoodTypeId(@NotNull int foodTypeId) throws UsersPlatformException {
        executor.openConnection(dbConnector);
        Collection<FoodType> queryResults = executor.tryExecuteGetQuery(dbConnector,
                "SELECT * FROM " + tableName + " WHERE "+guidColumn+"=" + foodTypeId, FoodType.getClass());
        executor.closeConnection();

        if (queryResults == null)throw new UsersPlatformException("Query result was null");
        if (queryResults.size() <= 0)throw new UsersPlatformException("type does not exist");

        return queryResults.stream().findFirst().get();
    }
    @Override
    public Collection<FoodType> getFoodTypes() throws UsersPlatformException {
        executor.openConnection(dbConnector);
        Collection<FoodType> foodTypes = executor.tryExecuteGetQuery(dbConnector, "SELECT * FROM " + tableName, FoodType.getClass());
        executor.closeConnection();
        if (foodTypes == null)throw new UsersPlatformException("Query result was null");

        return foodTypes;
    }
    @Override
    public void setFoodTypeName(@NotNull int foodTypeId, @NotNull String typeName) throws UsersPlatformException, SQLException {
        if(foodTypeId == 1)throw new UsersPlatformException("Cant update None object");
        FoodType foodTypeToSet = getFoodTypeId(foodTypeId);
        if(foodTypeToSet == null)throw new UsersPlatformException("type {"+foodTypeId + "} not found");
        foodTypeToSet.setName(typeName);
        executor.openConnection(dbConnector);
        boolean resultsFlag = executor.tryExecuteUpdateQuery(dbConnector, foodTypeToSet);
        executor.closeConnection();
        if(!resultsFlag)throw new UsersPlatformException("Could not update type: {" + foodTypeId + "} name");
    }
    @Override
    public void addFoodType(@NotNull FoodType foodType) throws UsersPlatformException, SQLException {
        executor.openConnection(dbConnector);
        boolean resultsFlag = executor.tryExecuteInsertQuery(dbConnector, foodType);
        executor.closeConnection();
        if (!resultsFlag) throw new UsersPlatformException("Error Inserting new Type");
    }
    @Override
    public void deleteFoodType(@NotNull int foodTypeId) throws UsersPlatformException, SQLException {
        FoodType t = getFoodTypeId(foodTypeId);
        if(t.getName() == "None" || foodTypeId == 1)throw new UsersPlatformException("Cant delete the none object");
        FoodType emptyType = getFoodTypeId(1);
        if(emptyType == null)emptyType = new FoodType(1,"None");
        for (Record record : transactionDAO.getFoodTypeRecords(t.getTypeId())) {
            transactionDAO.setRecordFoodType(record.getRecordId(), emptyType);
        }
        executor.openConnection(dbConnector);
        boolean resultsFlag = executor.tryExecuteDeleteQuery(dbConnector,t);
        executor.closeConnection();
        if (!resultsFlag) throw new UsersPlatformException("Error deleting type {"+foodTypeId+"}");
    }
}
