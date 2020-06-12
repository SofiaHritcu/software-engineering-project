package hospitalPharmacy.services;

public class HospitalPharmacyException extends Exception {

    public HospitalPharmacyException() {
    }

    public HospitalPharmacyException(String message) {
        super(message);
    }

    public HospitalPharmacyException(String message, Throwable cause) {
        super(message, cause);
    }
}
