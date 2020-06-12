package hospitalPharmacy.persistence.repository;

import hospitalPharmacy.model.Order;
import hospitalPharmacy.persistence.OrderRepositoryInterface;
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

public class OrderJdbcRepository implements OrderRepositoryInterface{
    private JdbcUtils dbUtils;
    private static SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory){this.sessionFactory = sessionFactory;}


    private static final Logger logger= LogManager.getLogger();

    public static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }


    public OrderJdbcRepository(Properties props) {
        dbUtils=new JdbcUtils(props);
    }

    public int size() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count (*) as [SIZE] from Orders")) {
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

    public void save(Order entity) {
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
                Order crit = session.createQuery("from Order where id=?",Order.class)
                        .setLong(0,longId)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Stergem order-ul " + crit.getId());
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

    public void update(Long longId, Order entity) {
        logger.traceEntry("updating order with {}",longId);
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                Order toBeUpdated = session.createQuery("from Order where id=?",Order.class)
                        .setLong(0,longId)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Updatam order-ul " + toBeUpdated.getId());

                toBeUpdated.setId(longId);
                toBeUpdated.setTimeOfPlacing(entity.getTimeOfPlacing());
                toBeUpdated.setStatus(entity.getStatus());
                toBeUpdated.setComments(entity.getComments());
                toBeUpdated.setQuantity(entity.getQuantity());
                toBeUpdated.setMedicine(entity.getMedicine());
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

    public Order findOne(Long longId) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Order crit = session.createQuery("from Order where id=?",Order.class)
                        .setLong(0,longId)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("S-a gasit order-ul " + crit.getId());
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

    public Iterable<Order> findOrdersBySection(String section) {
        logger.traceEntry("finding orders with {}",section);
        List<Order> orders = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                orders = session.createQuery("from Order where section=?", Order.class).
                        setString(0,section).
                        list();
                System.out.println(orders.size() + " order(s) found:");
                tx.commit();
                return orders;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return orders;
    }

    @Override
    public Iterable<Order> findUnhonoredOrders() {
        logger.traceEntry("finding unhonored orders");
        List<Order> orders = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                orders = session.createQuery("from Order where status=?", Order.class).
                        setString(0,"unhonored").
                        list();
                System.out.println(orders.size() + " order(s) found:");
                tx.commit();
                return orders;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return orders;
    }

    @Override
    public Order findOne(String medicine, Float quantity, String comments) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Order crit = session.createQuery("from Order where medicine=? AND quantity=? AND comments=?",Order.class)
                        .setString(0,medicine)
                        .setFloat(1,quantity)
                        .setString(2,comments)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("S-a gasit order-ul " + crit.getId());
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

    @Override
    public Order findOne(String medicine, Float quantity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Order crit = session.createQuery("from Order where medicine=? AND quantity=?",Order.class)
                        .setString(0,medicine)
                        .setFloat(1,quantity)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("S-a gasit order-ul " + crit.getId());
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


    public Iterable<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                orders = session.createQuery("from Order", Order.class).
                        list();
                System.out.println(orders.size() + " order(s) found:");
                tx.commit();
                return orders;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return orders;
    }
}
