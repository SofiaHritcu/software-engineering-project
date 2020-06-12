package hospitalPharmacy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDTO extends Order implements Serializable {
    List<Order> orders=new ArrayList<>();

    public OrderDTO() {
    }

    public OrderDTO(Long id,List<Order> orders) {
        super();
        super.setId(id);
        this.orders = orders;
    }

    public OrderDTO(Long id, Date timeOfPlacing, String status, String comments, Float quantity, String medicine, String section, List<Order> orders) {
        super(id, timeOfPlacing, status, comments, quantity, medicine, section);
        this.orders = orders;
    }


    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }


    @Override
    public String toString() {
        return this.getId()+" "+this.getOrders();

    }
}
