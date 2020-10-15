package gestion.gestionConcours;

import gestion.exceptions.*;

public interface GestionConcoursInterface {

    void ajouterConcours(String name, int heure, int minutes) throws NomDeConcoursDejaExistantException, CaracteresSpeciauxException;

    void retirerConcours(String name);

    void ajouterJoueurInscrit(String name) throws NomDeJoueurDejaExistantException, CaracteresSpeciauxException;

    void retirerJoueurInscrit(String name) throws AucunJoueurNePossedeCeNomException;

    void rejoindreConcours(String nomDuConcours, String nomDuJoueur) throws AucunJoueurNePossedeCeNomException, JoueurParticipeDejaException, ConcoursDejaLanceException;

    void creerEquipe(String joueur, String concours, String name) throws AucunJoueurNePossedeCeNomException, DejaDansUneEquipeException, CaracteresSpeciauxException;

    void rejoindreEquipe(String joueur, String equipe) throws AucunJoueurNePossedeCeNomException, EquipeCompleteException, DejaDansUneEquipeException;

    void quitterEquipe(String nomDuJoueur);

    void lancerConcours(String nom) throws LeConcoursNeContientAucuneEquipeException, LeConcoursADejaEteLanceException, DejaDansUneEquipeException;

    void connecterAdmin(String pseudo, String motDePasse) throws DonneesIncorrectesException;

}
