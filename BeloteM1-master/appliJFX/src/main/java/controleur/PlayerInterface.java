package controleur;

import controleur.exception.InscriptionFermeeException;
import gestion.exceptions.ConcoursDejaLanceException;

interface PlayerInterface {

    void inscrireUnJoueur(String pseudo);

    void seDeconnecter();

    void participerConcours(String nomDuConcours) throws InscriptionFermeeException, ConcoursDejaLanceException;

    void creerEquipe(String name);

    void rejoindreUneEquipe(String equipe);

    void quitterEquipe();

    void accepterAtout();

    void refuserAtout();

    void jouerCarte(String laCarte);
}
