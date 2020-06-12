package hospitalPharmacy.persistence;

import hospitalPharmacy.model.Pharmacist;

public interface PharmacistRepositoryInterface extends CrudRepository<Long, Pharmacist> {
    Pharmacist findOneByNamePassword(String firstName, String lastName, String password);
    Pharmacist findOneByName(String firstName, String lastName);
}
