package gestion.gestionJeu.jeu;

import java.util.ArrayList;

public class Main extends ArrayList<LaCarte> {

    LaCarte deposerCarte(int index) {
        LaCarte carte = get(index);
        remove(index);
        return carte;
    }

    public boolean contientSigneAtout(LaCarte atout) {
        for (LaCarte c : this) {
            if (c.getSigne() == atout.getSigne())
                return true;
        }
        return false;
    }
}
