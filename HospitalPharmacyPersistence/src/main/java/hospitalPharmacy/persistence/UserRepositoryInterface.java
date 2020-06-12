package hospitalPharmacy.persistence;

import hospitalPharmacy.model.User;

public interface UserRepositoryInterface extends CrudRepository<Long, User> {
    User findOneByNamePassword(String firstName, String lastName, String password);
    User findOneByName(String firstName, String lastName);
}
