package gestion.gestionConcours.joueur;

import gestion.exceptions.ConcoursDejaLanceException;
import gestion.exceptions.EquipeCompleteException;
import gestion.exceptions.JoueurParticipeDejaException;
import gestion.gestionConcours.concours.Concours;
import gestion.gestionConcours.concours.Equipe;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Joueur implements JoueurInterface, Serializable {

    final String nom;
    final List<Concours> lesConcoursDuJoueur;
    Equipe equipe = new Equipe(-1); //-1 signifie que le joueur n'a aucune equipe
    boolean aUneEquipe = false;

    public Joueur(String nom) {
        this.nom = nom;
        lesConcoursDuJoueur = new ArrayList<>();
    }

    @Override
    public void rejoindreConcours(Concours concours) throws JoueurParticipeDejaException, ConcoursDejaLanceException {
        if (lesConcoursDuJoueur.contains(concours)) throw new JoueurParticipeDejaException();
        concours.ajouterJoueurConcours(this);
        lesConcoursDuJoueur.add(concours);
    }

    @Override
    public void quitterConcours(Concours concours) {
        concours.retirerJoueurConcours(this);
        lesConcoursDuJoueur.removeIf(conc -> conc.equals(concours));
    }

    @Override
    public void creerEquipe(Concours concours, String name) {
        this.equipe = new Equipe(this, name);
        concours.ajouterEquipe(this.equipe);
        aUneEquipe = true;
    }

    @Override
    public void quitterEquipe() {
        aUneEquipe = false;
    }

    @Override
    public boolean aUneEquipe() {
        return aUneEquipe;
    }

    @Override
    public void rejoindreEquipe(Equipe equipe) throws EquipeCompleteException {
        this.equipe = equipe;
        equipe.ajouterJoueur(this);
        aUneEquipe = true;


    }


}
