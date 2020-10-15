package gestion.gestionConcours.joueur;

import gestion.exceptions.ConcoursDejaLanceException;
import gestion.exceptions.EquipeCompleteException;
import gestion.exceptions.JoueurParticipeDejaException;
import gestion.gestionConcours.concours.Concours;
import gestion.gestionConcours.concours.Equipe;

public interface JoueurInterface {

    void rejoindreConcours(Concours concours) throws JoueurParticipeDejaException, ConcoursDejaLanceException;

    void quitterConcours(Concours concours);

    void creerEquipe(Concours concours, String name);

    boolean aUneEquipe();

    void rejoindreEquipe(Equipe equipe) throws EquipeCompleteException;

    void quitterEquipe();

    String getNom();

    Equipe getEquipe();

    void setEquipe(Equipe equipe);


}
