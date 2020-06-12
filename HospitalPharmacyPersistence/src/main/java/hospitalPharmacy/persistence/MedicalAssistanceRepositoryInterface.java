package hospitalPharmacy.persistence;

import hospitalPharmacy.model.MedicalAssistance;

public interface MedicalAssistanceRepositoryInterface extends CrudRepository<Long, MedicalAssistance>{
    MedicalAssistance findOneByNamePasswordSection(String firstName, String lastName, String password,String section);
    MedicalAssistance findOneByNamePassword(String firstName, String lastName, String password);
    MedicalAssistance findOneByName(String firstName, String lastName);
}
