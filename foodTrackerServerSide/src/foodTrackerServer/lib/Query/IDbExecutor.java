package foodTrackerServer.lib.Query;

import java.sql.SQLException;
import java.util.Collection;

public interface IDbExecutor<T> {
    boolean tryExecuteWildCardQuery(String query);
    void openConnection(AbstractDbConnector connector);
    void closeConnection();

    Collection<T> tryExecuteGetQuery(AbstractDbConnector connector, String getQuery, Class type);
    Boolean tryExecuteInsertQuery(AbstractDbConnector connector, T insertObj) throws SQLException;
    Boolean tryExecuteUpdateQuery(AbstractDbConnector connector, T updateObj) throws SQLException;
    Boolean tryExecuteDeleteQuery(AbstractDbConnector connector, T deleteObj) throws SQLException;
}
