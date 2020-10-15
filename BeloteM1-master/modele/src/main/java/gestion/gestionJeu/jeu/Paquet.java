package gestion.gestionJeu.jeu;

import gestion.gestionJeu.jeu.Carte.Signe;
import gestion.gestionJeu.jeu.Carte.Symbole;

import java.util.ArrayList;
import java.util.Collections;

public class Paquet extends ArrayList<LaCarte> {

    private Paquet() {
        for (Signe signe : Signe.values()) {
            for (Symbole symbole : Symbole.values()) {
                add(LaCarte.creerLaCarte(symbole, signe));
            }
        }
    }

    public static Paquet creerPaquet() {
        return new Paquet();
    }

    public void melanger() {
        Collections.shuffle(this);
    }


    Main creerMain() {
        Main maMain = new Main();
        donnerNbCarteMain(maMain, 5);
        return maMain;
    }

    public void donnerNbCarteMain(Main laMain, int nbCartes) {
        laMain.addAll(subList(0, nbCartes));
        removeRange(0, nbCartes);
    }


    LaCarte regarder() {
        return get(0);
    }


}
