package gestion.gestionJeu.jeu;

import gestion.gestionJeu.jeu.Carte.Signe;
import gestion.gestionJeu.jeu.Carte.Symbole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class LaCarte implements Comparable, Serializable {

    final Symbole symbole;
    final Signe signe;


    public static LaCarte creerLaCarte(Symbole symbole, Signe signe) {
        return new LaCarte(symbole, signe);
    }

    @Override
    public String toString() {
        return symbole +
                "-" + signe;
    }

    int getScore(LaCarte atoutManche) {
        if (this.signe.equals(atoutManche.signe)) {
            switch (symbole) {
                case VALET:
                    return 20;
                case NEUF:
                    return 14;
                case AS:
                    return 11;
                case DIX:
                    return 10;
                case ROI:
                    return 4;
                case DAME:
                    return 3;
                case HUIT:
                    return 0;
                case SEPT:
                    return 0;

            }
        }
        switch (symbole) {
            case AS:
                return 11;
            case DIX:
                return 10;
            case ROI:
                return 4;
            case DAME:
                return 3;
            case VALET:
                return 2;
            case NEUF:
                return 0;
            case HUIT:
                return 0;
            case SEPT:
                return 0;
        }
        return 0;
    }

    private int getValeurSimple() {// pour compareTo
        switch (symbole) {
            case AS:
                return 7;
            case DIX:
                return 6;
            case ROI:
                return 5;
            case DAME:
                return 4;
            case VALET:
                return 3;
            case NEUF:
                return 2;
            case HUIT:
                return 1;
        }
        return 0;
    }

    private int getValeurAtout() {
        switch (symbole) {
            case VALET:
                return 7;
            case NEUF:
                return 6;
            case AS:
                return 5;
            case DIX:
                return 4;
            case ROI:
                return 3;
            case DAME:
                return 2;
            case HUIT:
                return 1;
        }
        return 0;
    }

    int compareToConnaissantLesAtouts(LaCarte carte, Signe atoutPartie, Signe atoutPli) {
        boolean monSigneEstAtoutPartie = this.signe.equals(atoutPartie);
        boolean monSigneEstAtoutPli = this.signe.equals(atoutPli);
        boolean signeAdverseEstAtoutPartie = carte.signe.equals(atoutPartie);
        boolean signeAdverseEstAtoutPli = carte.signe.equals(atoutPli);
        boolean monSigneVauxRien = !monSigneEstAtoutPartie && !monSigneEstAtoutPli;
        boolean signeAdverseVauxRien = !signeAdverseEstAtoutPartie && !signeAdverseEstAtoutPli;
        if (monSigneEstAtoutPartie && signeAdverseEstAtoutPartie) return this.compareToAtout(carte);
        else if (monSigneEstAtoutPartie) return 1;
        else if (monSigneVauxRien && signeAdverseVauxRien) return this.compareToSimple(carte);
        else if (monSigneEstAtoutPli && signeAdverseVauxRien) return 1;
        else if (monSigneEstAtoutPli && signeAdverseEstAtoutPli) return this.compareToSimple(carte);
        else return -1;


    }

    public int compareTo(Object o) {
        return this.getValeurSimple() - ((LaCarte) o).getValeurSimple();
    }

    private int compareToSimple(Object o) {
        return this.getValeurSimple() - ((LaCarte) o).getValeurSimple();
    }

    private int compareToAtout(Object o) {
        return this.getValeurAtout() - ((LaCarte) o).getValeurAtout();
    }


}
