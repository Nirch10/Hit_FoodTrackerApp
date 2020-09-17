package foodTrackerServer.lib.QueryUtils;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Collection;
import java.util.List;

public class HibernateDbExecutor<T> implements IDbExecutor<T> {
    private Session session;

    public HibernateDbExecutor(){}

    @Override
    public void openConnection(AbstractDbConnector connector){
        session = setSession(connector);
    }
    @Override
    public void closeConnection() {
        session.close();
    }
    @Override
    public boolean tryExecuteWildCardQuery(String query) {
        try{
            Query queryToExec = session.createQuery(query);
            queryToExec.executeUpdate();
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            return false;
        }
    }
    @Override
    public Collection<T> tryExecuteGetQuery(AbstractDbConnector connector, String getQuery, Class type) {
        try {
            List<T> res = (List<T>) session.createSQLQuery(getQuery).addEntity(type).list();
            return res;
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public Boolean TryExecuteInsertQuery(AbstractDbConnector connector, T insertObj) {
        try{
            session.save(insertObj);
            session.getTransaction().commit();
            return true;
        }
        catch (HibernateException e){
            return false;
        }
    }
    @Override
    public Boolean TryExecuteUpdateQuery(AbstractDbConnector connector, T updateObj) {
        try {
            session.merge(updateObj);
            session.getTransaction().commit();
            return true;
        }
        catch (HibernateException e){
            return false;
        }
    }
    @Override
    public Boolean TryExecuteDeleteQuery(AbstractDbConnector connector, T deleteObj) {
        try{
        session.delete(deleteObj);
        session.flush();
        session.getTransaction().commit();
        return true;}
        catch (HibernateException e){
            return false;
        }
    }

    private Session setSession(AbstractDbConnector connector){
        Session session = connector.openConn();
        session.beginTransaction();
        return session;
    }
}
