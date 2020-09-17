package foodTrackerServer.lib.QueryUtils;

import org.hibernate.Session;

public abstract class AbstractDbConnector {
    abstract Session openConn();
}
