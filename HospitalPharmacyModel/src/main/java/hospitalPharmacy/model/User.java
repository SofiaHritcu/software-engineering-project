package hospitalPharmacy.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

/***
 * The User class extends the Person class and represents the persons who are using effectively the application
 */
@Entity
@Table( name = "UsersHibernate" )
@Inheritance (strategy =  InheritanceType.TABLE_PER_CLASS)
public class User implements Serializable {
    private Long id;
    private String firstName,lastName;
    private String password;

    public User() {
    }

    /***
     * A User is differentiated from a simple Person by his password
     * @param firstName - the firstName of the User
     * @param lastName - the lastName of the User
     * @param password - the password that is used by the User to log into the app
     */
    public User(String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), password);
    }

    @Override
    public String toString() {
        return "User: "+this.firstName+" "+this.lastName+" "+this.password;
    }
}
