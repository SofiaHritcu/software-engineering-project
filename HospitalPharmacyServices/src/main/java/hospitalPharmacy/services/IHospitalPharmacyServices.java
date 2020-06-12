package hospitalPharmacy.services;

import hospitalPharmacy.model.*;


public interface IHospitalPharmacyServices {

    void login(User user, IHospitalPharmacyObserver client) throws HospitalPharmacyException;
    void logout(User user, IHospitalPharmacyObserver client) throws HospitalPharmacyException;


    //services from OrderService
    void addOrder(Order order,String section) throws HospitalPharmacyException;
    void deleteOrder(Order order,String section) throws HospitalPharmacyException;
    void updateOrder(Order order,String section) throws HospitalPharmacyException;
    Iterable<Order> findAllOrders() throws HospitalPharmacyException;
    Iterable<Order> findAllUnhonoredOrders() throws HospitalPharmacyException;
    Iterable<Order> findOrdersBySection(String section) throws HospitalPharmacyException;
    String findMedicineByOrder(Long orderId) throws HospitalPharmacyException;
    Order findOrder(Long orderId) throws HospitalPharmacyException;
    Order findOrder(String medicine,Float quantity,String comments) throws HospitalPharmacyException;


    //services from UserService
    User findOneUserNamePassword(User user) throws HospitalPharmacyException;
    User findOneUserName(User user) throws HospitalPharmacyException;

    //services from MedicalAssistanceService
    MedicalAssistance findOneUserName(MedicalAssistance medicalAssistance) throws HospitalPharmacyException;

    //services from PharmacistAssistance
    Pharmacist findOneUserName(Pharmacist pharmacist) throws HospitalPharmacyException;

}
