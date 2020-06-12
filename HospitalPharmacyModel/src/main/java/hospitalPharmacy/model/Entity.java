package hospitalPharmacy.model;

import java.io.Serializable;

/***
 * Entity Class has one private unique attribute
 * @param <ID> defines the id of an entity
 */
public class Entity<ID> implements Serializable {
    private ID id ;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
