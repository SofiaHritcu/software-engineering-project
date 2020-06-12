package hospitalPharmacy.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table( name = "PharmacistHibernate" )
public class Pharmacist extends User implements Serializable {
    public Pharmacist(String firstName, String lastName, String password) {
        super(firstName, lastName, password);
    }

    public Pharmacist() {
    }

    @Override
    public String toString() {
        return "Pharmacist: "+this.getFirstName()+" "+this.getLastName()+" "+this.getPassword();
    }
}
