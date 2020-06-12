package hospitalPharmacy.persistence.repository;

import hospitalPharmacy.model.Pharmacist;
import hospitalPharmacy.persistence.PharmacistRepositoryInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PharmacistJdbcRepository implements PharmacistRepositoryInterface {
    private JdbcUtils dbUtils;
    private static SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory){this.sessionFactory = sessionFactory;}


    private static final Logger logger= LogManager.getLogger();


    private Pharmacist getPharmacist(ResultSet resultSet) throws SQLException {
        String firstName=resultSet.getString("FirstName");
        String lastName=resultSet.getString("LastName");
        String password=resultSet.getString("Password");
        Pharmacist pharmacist=new Pharmacist(firstName,lastName,password);
        pharmacist.setId(resultSet.getLong("Id"));
        return pharmacist;
    }



    public static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }


    public PharmacistJdbcRepository(Properties props) {
        dbUtils=new JdbcUtils(props);
    }

    public int size() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count (*) as [SIZE] from Pharmacists")) {
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    logger.traceExit(result.getInt("SIZE"));
                    return result.getInt("SIZE");
                }
            }
        }catch(SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        return 0;
    }

    public void save(Pharmacist entity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
            } catch (RuntimeException ex) {
                logger.error(ex);
                System.out.println("Error DB "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        logger.traceExit();

    }

    public void delete(Long longId) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Pharmacist crit = session.createQuery("from Pharmacist where id=?",Pharmacist.class)
                        .setLong(0,longId)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Stergem pharmacist-ul " + crit.getId());
                session.delete(crit);
                tx.commit();
            } catch (RuntimeException ex) {
                logger.error(ex);
                System.out.println("Error DB "+ex);
                if (tx != null)
                    tx.rollback();
            }
            logger.traceExit();

        }
    }

    public void update(Long longId, Pharmacist entity) {
        logger.traceEntry("updating pharmacist with {}",longId);
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                Pharmacist toBeUpdated = session.createQuery("from Pharmacist where id=?",Pharmacist.class)
                        .setLong(0,longId)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Updatam pharmacist-ul " + toBeUpdated.getId());

                toBeUpdated.setId(longId);
                toBeUpdated.setFirstName(entity.getFirstName());
                toBeUpdated.setLastName(entity.getLastName());
                toBeUpdated.setPassword(entity.getPassword());
                tx.commit();

            } catch(RuntimeException ex){
                logger.error(ex);
                System.out.println("Error DB "+ex);
                if (tx!=null)
                    tx.rollback();
            }
        }
        logger.traceExit();

    }

    public Pharmacist findOne(Long longId) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Pharmacist crit = session.createQuery("from Pharmacist where id=?",Pharmacist.class)
                        .setLong(0,longId)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("S-a gasit pharmacist-ul " + crit.getId());
                tx.commit();
                logger.traceExit(crit);
                return crit;
            } catch (RuntimeException ex) {
                logger.error(ex);
                System.out.println("Error DB "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        logger.traceExit();
        return null;
    }

    public Pharmacist findOneByNamePassword(String firstName, String lastName, String password) {
        logger.traceEntry("finding pharmacist with {}{}{}", firstName,lastName);
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Pharmacist crit = session.createQuery("from Pharmacist where firstName=? and lastName=? and password=?",Pharmacist.class)
                        .setString(0,firstName)
                        .setString(1,lastName)
                        .setString(2,password)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("S-a gasit pharmacist-ul " + crit.getId());
                tx.commit();
                logger.traceExit(crit);
                return crit;
            } catch (NullPointerException ex){
                return null;
            } catch (RuntimeException ex) {
                logger.error(ex);
                System.out.println("Error DB "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        logger.traceExit();
        return null;
    }

    public Pharmacist findOneByName(String firstName, String lastName) {
        logger.traceEntry("finding pharmacist with {}{}{}", firstName,lastName);
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                System.out.println("Aici");
                Pharmacist crit = session.createQuery("from Pharmacist where firstName=? and lastName=?",Pharmacist.class)
                        .setString(0,firstName)
                        .setString(1,lastName)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("S-a gasit pharmacist-ul " + crit.getId());
                tx.commit();
                logger.traceExit(crit);
                return crit;
            } catch (NullPointerException ex){
                return null;
            } catch (RuntimeException ex) {
                logger.error(ex);
                System.out.println("Error DB "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        logger.traceExit();
        return null;
    }


    public Iterable<Pharmacist> findAll() {
        List<Pharmacist> pharmacists = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                pharmacists = session.createQuery("from Pharmacist ", Pharmacist.class).
                        list();
                System.out.println(pharmacists.size() + " pharmacist(s) found:");
                tx.commit();
                return pharmacists;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return pharmacists;
    }
}
