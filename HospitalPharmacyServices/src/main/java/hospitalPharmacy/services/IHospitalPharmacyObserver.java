package hospitalPharmacy.services;

import hospitalPharmacy.model.OrderDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IHospitalPharmacyObserver extends Remote {
    void addedOrder(OrderDTO orderDTO) throws HospitalPharmacyException, RemoteException;
}
