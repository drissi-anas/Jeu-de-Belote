package rmiService.lesServices;

import gestion.exceptions.*;

import java.rmi.RemoteException;

public interface JoueurService {

    void ajouterJoueurInscrit(String name) throws RemoteException, NomDeJoueurDejaExistantException, CaracteresSpeciauxException;

    void retirerJoueurInscrit(String name) throws RemoteException, AucunJoueurNePossedeCeNomException;

    void rejoindreConcours(String nomDuConcours, String nomDuJoueur) throws RemoteException, AucunJoueurNePossedeCeNomException, JoueurParticipeDejaException, ConcoursDejaLanceException;

    void creerEquipe(String joueur, String concours, String name) throws RemoteException, AucunJoueurNePossedeCeNomException, DejaDansUneEquipeException, CaracteresSpeciauxException;

    void rejoindreEquipe(String joueur, String equipe) throws RemoteException, AucunJoueurNePossedeCeNomException, EquipeCompleteException, DejaDansUneEquipeException;

    void accepterAtout(String nameConcours, String tours, int id, String nomJoueur) throws RemoteException;

    void jouerCarte(String nameConcours, String tours, int id, String nomJoueur, String laCarte) throws RemoteException, CarteNonJouableException;

    void refuserAtout(String nameConcours, String tours, int id) throws RemoteException;

    void quitterEquipe(String nomDuJoueur) throws RemoteException;
}
