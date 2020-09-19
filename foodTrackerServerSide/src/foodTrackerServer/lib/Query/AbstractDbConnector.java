package foodTrackerServer.lib.Query;

import org.hibernate.Session;

public abstract class AbstractDbConnector {
    abstract Session openConn();
}
