package hospitalPharmacy.server;

import hospitalPharmacy.model.*;
import hospitalPharmacy.persistence.repository.MedicalAssistanceJdbcRepository;
import hospitalPharmacy.persistence.repository.OrderJdbcRepository;
import hospitalPharmacy.persistence.repository.PharmacistJdbcRepository;
import hospitalPharmacy.persistence.repository.UserJdbcRepository;
import hospitalPharmacy.services.HospitalPharmacyException;
import hospitalPharmacy.services.IHospitalPharmacyObserver;
import hospitalPharmacy.services.IHospitalPharmacyServices;
import org.hibernate.SessionFactory;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HospitalPharmacyServerImpl implements IHospitalPharmacyServices {
    private UserJdbcRepository userJdbcRepository;
    private MedicalAssistanceJdbcRepository medicalAssistanceJdbcRepository;
    private PharmacistJdbcRepository pharmacistJdbcRepository;
    private OrderJdbcRepository orderJdbcRepository;
    private Map<Long, IHospitalPharmacyObserver> loggedClients;

    public HospitalPharmacyServerImpl(UserJdbcRepository userJdbcRepository, MedicalAssistanceJdbcRepository medicalAssistanceJdbcRepository, PharmacistJdbcRepository pharmacistJdbcRepository, OrderJdbcRepository orderJdbcRepository) {
        this.userJdbcRepository=userJdbcRepository;
        SessionFactory sessionFactoryApp = userJdbcRepository.initialize();
        this.medicalAssistanceJdbcRepository=medicalAssistanceJdbcRepository;
        this.medicalAssistanceJdbcRepository.setSessionFactory(sessionFactoryApp);
        this.pharmacistJdbcRepository=pharmacistJdbcRepository;
        this.pharmacistJdbcRepository.setSessionFactory(sessionFactoryApp);
        this.orderJdbcRepository=orderJdbcRepository;
        this.orderJdbcRepository.setSessionFactory(sessionFactoryApp);
        loggedClients=new ConcurrentHashMap<>();
    }


    @Override
    public synchronized void login(User user, IHospitalPharmacyObserver client) throws HospitalPharmacyException {
        User userR=userJdbcRepository.findOneByNamePassword(user.getFirstName(),user.getLastName(),user.getPassword());
        if (userR!=null){
            if(loggedClients.get(userR.getId())!=null)
                throw new HospitalPharmacyException("User already logged in.");
            loggedClients.put(userR.getId(), client);
        }else
            throw new HospitalPharmacyException("Authentication failed.");
    }

    @Override
    public void logout(User user, IHospitalPharmacyObserver client) throws HospitalPharmacyException {
        IHospitalPharmacyObserver localClient=loggedClients.remove(user.getId());
        if (localClient==null)
            throw new HospitalPharmacyException("User "+user.getId()+" is not logged in.");
    }

    @Override
    public synchronized void addOrder(Order order,String section) throws HospitalPharmacyException {
        orderJdbcRepository.save(order);
        OrderDTO added = new OrderDTO(order.getId(),order.getTimeOfPlacing(),order.getStatus(),order.getComments(),order.getQuantity(),order.getMedicine(),order.getSection(),(List<Order>) orderJdbcRepository.findOrdersBySection(section));
        notifyUsers(added);
    }

    @Override
    public void deleteOrder(Order order,String section) throws HospitalPharmacyException {
        orderJdbcRepository.delete(order.getId());
        OrderDTO deleted = new OrderDTO(order.getId(),order.getTimeOfPlacing(),order.getStatus(),order.getComments(),order.getQuantity(),order.getMedicine(),order.getSection(),(List<Order>) orderJdbcRepository.findOrdersBySection(section));
        notifyUsers(deleted);
    }

    @Override
    public void updateOrder(Order order,String section) throws HospitalPharmacyException {
        orderJdbcRepository.update(order.getId(),order);
        OrderDTO updated = new OrderDTO(order.getId(),order.getTimeOfPlacing(),order.getStatus(),order.getComments(),order.getQuantity(),order.getMedicine(),order.getSection(),(List<Order>) orderJdbcRepository.findOrdersBySection(section));
        notifyUsers(updated);
    }

    @Override
    public Iterable<Order> findAllOrders() throws HospitalPharmacyException {
        System.out.println("Orders : ");
        Iterable<Order> result=orderJdbcRepository.findAll();
        int size=0;
        for (Order ord : result){
            size+=1;
        }
        System.out.println("Number of orders: "+size);
        return result;
    }

    @Override
    public Iterable<Order> findAllUnhonoredOrders() throws HospitalPharmacyException {
        System.out.println("Unhonored Orders : ");
        Iterable<Order> result=orderJdbcRepository.findUnhonoredOrders();
        int size=0;
        for (Order ord : result){
            size+=1;
        }
        System.out.println("Number of orders: "+size);
        return result;
    }

    @Override
    public Iterable<Order> findOrdersBySection(String section) throws HospitalPharmacyException {
        System.out.println("Orders for section: "+section);
        Iterable<Order> result=orderJdbcRepository.findOrdersBySection(section);
        int size=0;
        for (Order ord : result){
            size+=1;
        }
        System.out.println("Number of orders: "+size);
        return result;
    }

    @Override
    public String findMedicineByOrder(Long orderId) throws HospitalPharmacyException {
        System.out.println("Order with : "+orderId);
        String result=orderJdbcRepository.findOne(orderId).getMedicine();
        System.out.println("Found medicine: "+result);
        return result;
    }

    @Override
    public Order findOrder(Long orderId) throws HospitalPharmacyException {
        System.out.println("Order with id  :"+orderId);
        Order orderFound=orderJdbcRepository.findOne(orderId);
        return orderFound;
    }

    @Override
    public Order findOrder(String medicine, Float quantity, String comments) throws HospitalPharmacyException {
        System.out.println("Order with medicine  :"+medicine +" and quantity  :"+quantity.toString());
        if(comments != null){
            Order orderFound=orderJdbcRepository.findOne(medicine,quantity,comments);
            return orderFound;
        }else {
            Order orderFound=orderJdbcRepository.findOne(medicine,quantity);
            return orderFound;
        }
    }

    private final int defaultThreadsNo=5;
    public void notifyUsers(OrderDTO orderDTO){
        Iterable<User> users=getUsers();
        System.out.println("Logged "+users);
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(User us :users){
            IHospitalPharmacyObserver hospitalPharmacyClient = loggedClients.get(us.getId());
            if (hospitalPharmacyClient !=null){
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying [" + us.getId()+ "] other user changed a order  ["+orderDTO.getId()+"] .");
                        System.out.println(hospitalPharmacyClient);
                        hospitalPharmacyClient.addedOrder(orderDTO);
                    } catch (HospitalPharmacyException | RemoteException e) {
                        System.err.println("Error notifying user " + e);
                    }
                });
            }
        }
        executor.shutdown();
    }

    private synchronized Iterable<User> getUsers(){
        return userJdbcRepository.findAll();
    }


    @Override
    public User findOneUserNamePassword(User user) throws HospitalPharmacyException {
        System.out.println("User with name and password :"+user.getFirstName()+" "+user.toString()+" "+user.getPassword());
        User userFound=userJdbcRepository.findOneByNamePassword(user.getFirstName(),user.getLastName(),user.getPassword());
        return userFound;
    }

    @Override
    public User findOneUserName(User user) throws HospitalPharmacyException {
        System.out.println("User with name  :"+user.getFirstName()+" "+user.toString()+" ");
        User userFound=userJdbcRepository.findOneByName(user.getFirstName(),user.getLastName());
        return userFound;
    }

    @Override
    public MedicalAssistance findOneUserName(MedicalAssistance medicalAssistance) throws HospitalPharmacyException {
        System.out.println("MedicalAssistance with name  :"+medicalAssistance.getFirstName()+" "+medicalAssistance.toString()+" ");
        MedicalAssistance medicalAssistanceFound=medicalAssistanceJdbcRepository.findOneByName(medicalAssistance.getFirstName(),medicalAssistance.getLastName());
        return medicalAssistanceFound;
    }

    @Override
    public Pharmacist findOneUserName(Pharmacist pharmacist) throws HospitalPharmacyException {
        System.out.println("Pharmacist with name  :"+pharmacist.getFirstName()+" "+pharmacist.toString()+" ");
        Pharmacist pharmacistFound=pharmacistJdbcRepository.findOneByName(pharmacist.getFirstName(),pharmacist.getLastName());
        return pharmacistFound;
    }
}
