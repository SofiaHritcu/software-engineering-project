package hospitalPharmacy.persistence.repository;

import hospitalPharmacy.model.MedicalAssistance;
import hospitalPharmacy.persistence.MedicalAssistanceRepositoryInterface;
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

public class MedicalAssistanceJdbcRepository implements MedicalAssistanceRepositoryInterface {
    private JdbcUtils dbUtils;
    private static SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory){this.sessionFactory = sessionFactory;}


    private static final Logger logger= LogManager.getLogger();


    private MedicalAssistance getMedicalAssistance(ResultSet resultSet) throws SQLException {
        String firstName=resultSet.getString("FirstName");
        String lastName=resultSet.getString("LastName");
        String password=resultSet.getString("Password");
        String section=resultSet.getString("Section");
        MedicalAssistance medicalAssistance=new MedicalAssistance(firstName,lastName,password,section);
        medicalAssistance.setId(resultSet.getLong("Id"));
        return medicalAssistance;
    }


    public static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }


    public MedicalAssistanceJdbcRepository(Properties props) {
        dbUtils=new JdbcUtils(props);
    }

    public int size() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count (*) as [SIZE] from MedicalAssistance")) {
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

    public void save(MedicalAssistance entity) {
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
                String queryString = "from MedicalAssistance where Id=?";
                MedicalAssistance crit = session.createQuery("from MedicalAssistance where id=?",MedicalAssistance.class)
                        .setLong(0,longId)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Stergem medicalAssistance-ul " + crit.getId());
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

    public void update(Long longId, MedicalAssistance entity) {
        logger.traceEntry("updating medicalAssistance with {}",longId);
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                MedicalAssistance toBeUpdated = session.createQuery("from MedicalAssistance where id=?",MedicalAssistance.class)
                        .setLong(0,longId)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Updatam medicalAssistance-ul " + toBeUpdated.getId());

                toBeUpdated.setId(longId);
                toBeUpdated.setFirstName(entity.getFirstName());
                toBeUpdated.setLastName(entity.getLastName());
                toBeUpdated.setPassword(entity.getPassword());
                toBeUpdated.setSection(entity.getSection());
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

    public MedicalAssistance findOne(Long longId) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                MedicalAssistance crit = session.createQuery("from MedicalAssistance where id=?",MedicalAssistance.class)
                        .setLong(0,longId)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("S-a gasit medicalAssistance-ul " + crit.getId());
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

    public MedicalAssistance findOneByNamePassword(String firstName, String lastName, String password) {
        logger.traceEntry("finding medicalAssistance with {}{}{}", firstName,lastName);
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                MedicalAssistance crit = session.createQuery("from MedicalAssistance where firstName=? and lastName=? and password=?",MedicalAssistance.class)
                        .setString(0,firstName)
                        .setString(1,lastName)
                        .setString(2,password)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("S-a gasit medicalAssistance-ul " + crit.getId());
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


    public MedicalAssistance findOneByNamePasswordSection(String firstName, String lastName, String password, String section) {
        logger.traceEntry("finding medicalAssistance with {}{}{}", firstName,lastName);
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                MedicalAssistance crit = session.createQuery("from MedicalAssistance where firstName=? and lastName=? and password=? and section=?",MedicalAssistance.class)
                        .setString(0,firstName)
                        .setString(1,lastName)
                        .setString(2,password)
                        .setString(3,section)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("S-a gasit medicalAssistance-ul " + crit.getId());
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


    public MedicalAssistance findOneByName(String firstName, String lastName) {
        logger.traceEntry("finding medicalAssistance with {}{}{}", firstName,lastName);
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                MedicalAssistance crit = session.createQuery("from MedicalAssistance where firstName=? and lastName=?",MedicalAssistance.class)
                        .setString(0,firstName)
                        .setString(1,lastName)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("S-a gasit medicalAssistance-ul " + crit.getId());
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


    public Iterable<MedicalAssistance> findAll() {
        List<MedicalAssistance> medicalAssistances = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                medicalAssistances = session.createQuery("from MedicalAssistance ", MedicalAssistance.class).
                        list();
                System.out.println(medicalAssistances.size() + " medicalAssistance(s) found:");
                tx.commit();
                return medicalAssistances;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return medicalAssistances;
    }
}
