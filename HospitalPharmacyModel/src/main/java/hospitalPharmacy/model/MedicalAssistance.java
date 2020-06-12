package hospitalPharmacy.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table( name = "MedicalAssistanceHibernate" )
public class MedicalAssistance extends User implements Serializable {
    String section;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public MedicalAssistance() {
    }

    public MedicalAssistance(String firstName, String lastName, String password, String section) {
        super(firstName, lastName, password);
        this.section = section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MedicalAssistance that = (MedicalAssistance) o;
        return Objects.equals(section, that.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), section);

    }

    @Override
    public String toString() {
        return "MedicalAssistance: "+this.getFirstName()+" "+this.getLastName()+" "+this.getPassword()+" "+this.section;
    }
}
