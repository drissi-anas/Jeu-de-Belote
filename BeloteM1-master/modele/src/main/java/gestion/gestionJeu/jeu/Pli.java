package gestion.gestionJeu.jeu;

import gestion.gestionConcours.joueur.JoueurInterface;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
public class Pli extends ArrayList<LaCarte> {
    final LaCarte atoutManche;
    final ComparatorCarte comparateur;
    final Map<JoueurInterface, LaCarte> lesCartesPoseesParJoueur = new HashMap<>();
    LaCarte atoutParty; // atout du pli
    int res;
    JoueurInterface vainqueurParty;

    private Pli(LaCarte atoutManche) {
        this.atoutManche = atoutManche;
        this.comparateur = ComparatorCarte.creerComparateurCarte(atoutManche);
    }

    public static Pli creerParty(LaCarte atoutManche) {
        return new Pli(atoutManche);
    }

    // nécessaire ? parce que pas de modifications sur le pli (excepté les cartes posées par les joueurs)
    void setAtoutParty(LaCarte atoutParty) {
        this.atoutParty = atoutParty;
        comparateur.setAtoutPli(atoutParty);
    }

    void finPli() {
        res = 0;
        for (LaCarte carte : this) {
            res += carte.getScore(this.atoutManche);
        }
        lesCartesPoseesParJoueur.forEach((leJoueur, laCarte) -> {
            if (laCarteGagnante().equals(laCarte)) vainqueurParty = leJoueur;
        });
        atoutParty = null;
    }


    void ajouterCarte(LaCarte carte, JoueurInterface joueur) {
        add(carte);
        lesCartesPoseesParJoueur.forEach((leJoueur, laCarte) -> {
            if (joueur.getNom().equals(leJoueur.getNom())) lesCartesPoseesParJoueur.remove(laCarte);
        });
        lesCartesPoseesParJoueur.put(joueur, carte);
    }

    private LaCarte laCarteGagnante() {
        return Collections.max(this, comparateur);

    }

    void reinitialiserPli() {

        removeAll(this);
        lesCartesPoseesParJoueur.clear();
    }
}
