package rmiService;

import gestion.exceptions.*;
import gestion.gestionConcours.concours.Concours;
import gestion.gestionConcours.concours.Equipe;
import gestion.gestionConcours.joueur.JoueurInterface;
import gestion.gestionJeu.jeu.LaCarte;
import gestion.gestionJeu.jeu.Manche;
import rmiService.lesServices.AdminService;
import rmiService.lesServices.JoueurService;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface MonService extends Remote, JoueurService, AdminService {
    String serviceName = "BelotteService";

    List<JoueurInterface> getLesInscrits() throws RemoteException, AucunJoueursNEstInscritException;

    JoueurInterface getJoueurByName(String name) throws RemoteException, AucunJoueurNePossedeCeNomException;

    List<Equipe> getLesEquipesIncompletes(Concours concours) throws RemoteException, AucuneEquipeEstIncompleteException;

    List<Concours> getLesConcours() throws RemoteException;

    Concours getConcoursByName(String name) throws RemoteException;

    void lancerConcours(String name) throws RemoteException, LeConcoursNeContientAucuneEquipeException, LeConcoursADejaEteLanceException, DejaDansUneEquipeException;

    boolean atoutPris(String nameConcours, String tours, int id) throws RemoteException;

    Manche getManche(String nameConcours, String tours, int id) throws RemoteException;

    Map<JoueurInterface, LaCarte> getCartePosee(String nameConcours, String tours, int id) throws RemoteException;

    JoueurInterface getJoueurEnCoursDeTours(String nom, String tours, int id) throws RemoteException;

    int getIndiceDeModificationDuModele() throws RemoteException;
}
