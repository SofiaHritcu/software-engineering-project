package hospitalPharmacy.persistence;

import hospitalPharmacy.model.Order;

public interface OrderRepositoryInterface {
    Iterable<Order> findOrdersBySection(String section);
    Iterable<Order> findUnhonoredOrders();
    Order findOne(String medicine,Float quantity,String comments);
    Order findOne(String medicine,Float quantity);
}
