package rmiService.lesServices;

import gestion.exceptions.CaracteresSpeciauxException;
import gestion.exceptions.DonneesIncorrectesException;
import gestion.exceptions.NomDeConcoursDejaExistantException;

import java.rmi.RemoteException;

public interface AdminService {

    void ajouterConcours(String name, int heure, int minutes) throws RemoteException, NomDeConcoursDejaExistantException, CaracteresSpeciauxException;

    void retirerConcours(String name) throws RemoteException;

    void connecterAdmin(String pseudo, String motDePasse) throws RemoteException, DonneesIncorrectesException;


}
