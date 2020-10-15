package gestion.gestionJeu.jeu;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Comparator;

@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
class ComparatorCarte implements Comparator<LaCarte>, Serializable {
    final LaCarte atoutManche;
    LaCarte atoutPli;

    private ComparatorCarte(LaCarte atoutManche) {
        this.atoutManche = atoutManche;
    }

    static ComparatorCarte creerComparateurCarte(LaCarte atoutManche) {
        return new ComparatorCarte(atoutManche);
    }

    @Override
    public int compare(LaCarte o1, LaCarte o2) {
        return o1.compareToConnaissantLesAtouts(o2, atoutManche.getSigne(), atoutPli.getSigne());
    }
}
