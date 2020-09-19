package foodTrackerServer.lib.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.File;

public class HibernateDbConnector extends AbstractDbConnector {
    static SessionFactory sessionFactory;

    public HibernateDbConnector(File hibernateConfigFlePath){
        sessionFactory = new Configuration().configure(hibernateConfigFlePath).buildSessionFactory();
    }
    @Override
    Session openConn() {
        if(sessionFactory == null)return null;
        Session session = sessionFactory.openSession();
        return session;
    }
}
