package facade;

import gestion.exceptions.AucunJoueurNePossedeCeNomException;
import gestion.exceptions.AucunJoueursNEstInscritException;
import gestion.exceptions.AucuneEquipeEstIncompleteException;
import gestion.gestionConcours.GestionConcoursInterface;
import gestion.gestionConcours.concours.Concours;
import gestion.gestionConcours.concours.Equipe;
import gestion.gestionConcours.joueur.JoueurInterface;
import gestion.gestionJeu.GestionJeuInterface;
import gestion.gestionJeu.jeu.LaCarte;
import gestion.gestionJeu.jeu.Manche;

import java.util.List;
import java.util.Map;
import java.util.Observer;

public interface FacadeBelotte extends GestionJeuInterface, GestionConcoursInterface, Observer {

    List<JoueurInterface> getLesInscrits() throws AucunJoueursNEstInscritException;

    JoueurInterface getJoueurByName(String name) throws AucunJoueurNePossedeCeNomException;

    List<Equipe> getLesEquipesIncompletes(Concours concours) throws AucuneEquipeEstIncompleteException;

    List<Concours> getLesConcours();

    Concours getConcoursByName(String name);

    List<Manche> getLesManchesTours1(String nameConcours);

    List<Manche> getLesManchesTours2(String nameConcours);

    List<Manche> getLesManchesTours3(String nameConcours);

    Manche getManche(String nameConcours, String tours, int id);

    Map<JoueurInterface, LaCarte> getCartePosee(String nameConcours, String tours, int id);

    boolean atoutPris(String nameConcours, String tours, int id);

    JoueurInterface getJoueurEnCoursDeTours(String nom, String tours, int id);


}
