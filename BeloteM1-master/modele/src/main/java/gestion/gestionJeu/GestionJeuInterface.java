package gestion.gestionJeu;

import gestion.exceptions.CarteNonJouableException;

public interface GestionJeuInterface {

    void accepterAtout(String nameConcours, String tours, int id, String nomJoueur);

    void refuserAtout(String nameConcours, String tours, int id);

    void jouerCarte(String nameConcours, String tours, int id, String nomJoueur, String laCarte) throws CarteNonJouableException;

}
