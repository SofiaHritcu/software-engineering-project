package hospitalPharmacy.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "OrderHibernate")
public class Order implements Serializable {
    private Long id;
    @Basic
    @Temporal(TemporalType.TIME)
    private Date timeOfPlacing;
    private String status;
    private String comments;
    private Float quantity;
    private String medicine;
    private String section;


    public Order(Long id, Date timeOfPlacing, String status, String comments, Float quantity, String medicine,String section) {
        this.id = id;
        this.timeOfPlacing = timeOfPlacing;
        this.status = status;
        this.comments = comments;
        this.quantity = quantity;
        this.medicine = medicine;
        this.section = section;
    }

    public Order() {
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimeOfPlacing() {
        return timeOfPlacing;
    }

    public void setTimeOfPlacing(Date timeOfPlacing) {
        this.timeOfPlacing = timeOfPlacing;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                Objects.equals(timeOfPlacing, order.timeOfPlacing) &&
                Objects.equals(status, order.status) &&
                Objects.equals(comments, order.comments) &&
                Objects.equals(quantity, order.quantity) &&
                Objects.equals(medicine, order.medicine) &&
                Objects.equals(section, order.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeOfPlacing, status, comments, quantity, medicine, section);
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "Order: " +
                " " + id +
                " " + formatter.format(timeOfPlacing)+
                " " + status  +
                " " + comments +
                " " + quantity +
                " " + medicine +
                " " + section;
    }
}

