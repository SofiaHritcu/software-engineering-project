package hospitalPharmacy.persistence.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import hospitalPharmacy.model.User;
import hospitalPharmacy.persistence.UserRepositoryInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserJdbcRepository implements UserRepositoryInterface {
    private JdbcUtils dbUtils;
    private static SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory){this.sessionFactory = sessionFactory;}


    private static final Logger logger= LogManager.getLogger();


    private User getUser(ResultSet resultSet) throws SQLException {
        String firstName=resultSet.getString("FirstName");
        String lastName=resultSet.getString("LastName");
        String password=resultSet.getString("Password");
        User user=new User(firstName,lastName,password);
        user.setId(resultSet.getLong("Id"));
        return user;
    }

    public SessionFactory initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            return sessionFactory;
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy( registry );
            throw new RuntimeException(e);
        }
    }


    public static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }


    public UserJdbcRepository(Properties props) {
        dbUtils=new JdbcUtils(props);
    }

    public int size() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count (*) as [SIZE] from Users")) {
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

    public void save(User entity) {
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
                String queryString = "from User where Id=?";
                User crit = session.createQuery("from User where id=?",User.class)
                        .setLong(0,longId)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Stergem user-ul " + crit.getId());
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

    public void update(Long longId, User entity) {
        logger.traceEntry("updating person with {}",longId);
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                User toBeUpdated = session.createQuery("from User where id=?",User.class)
                        .setLong(0,longId)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Updatam user-ul " + toBeUpdated.getId());

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

    public User findOne(Long longId) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                User crit = session.createQuery("from User where id=?",User.class)
                        .setLong(0,longId)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("S-a gasit user-ul " + crit.getId());
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

    public User findOneByNamePassword(String firstName, String lastName, String password) {
        logger.traceEntry("finding user with {}{}{}", firstName,lastName);
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                User crit = session.createQuery("from User where firstName=? and lastName=? and password=?",User.class)
                        .setString(0,firstName)
                        .setString(1,lastName)
                        .setString(2,password)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("S-a gasit user-ul " + crit.getId());
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

    public User findOneByName(String firstName, String lastName) {
        logger.traceEntry("finding user with {}{}{}", firstName,lastName);
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                System.out.println("Aici");
                User crit = session.createQuery("from User where firstName=? and lastName=?",User.class)
                        .setString(0,firstName)
                        .setString(1,lastName)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("S-a gasit user-ul " + crit.getId());
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


    public Iterable<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                users = session.createQuery("from User", User.class).
                        list();
                System.out.println(users.size() + " user(s) found:");
                tx.commit();
                return users;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return users;
    }
}
