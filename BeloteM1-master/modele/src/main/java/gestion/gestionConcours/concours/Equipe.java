package gestion.gestionConcours.concours;

import gestion.exceptions.EquipeCompleteException;
import gestion.gestionConcours.joueur.JoueurInterface;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Equipe implements Serializable {
    static int id = 0;
    final int identifiant;
    String nomEquipe;
    JoueurInterface joueur1;
    JoueurInterface joueur2;
    int score = 0;


    public Equipe(int identifiant) {
        this.identifiant = identifiant;
    }

    public Equipe(JoueurInterface joueur1, String name) {
        this.nomEquipe = name;
        this.joueur1 = joueur1;
        this.identifiant = id;
        id = id + 1;
        joueur1.setEquipe(this);
    }

    private static Equipe pasEquipe() {
        return new Equipe(-1);
    }

    public void ajouterJoueur(JoueurInterface joueur) throws EquipeCompleteException {
        if (joueur1 != null && joueur2 != null) throw new EquipeCompleteException();
        if (this.joueur1 == null)
            this.joueur1 = joueur;
        else
            this.joueur2 = joueur;
    }

    public boolean estIncomplete() {
        return (joueur1 == null || joueur2 == null) && this.identifiant != -1; // l'identifiant -1 caractérise une équipe complète car personne ne peut rejoindre cette équipe
    }

    public void retirerJoueur(String name) {
        if (name.equals(this.joueur1.getNom())) {
            this.joueur1.setEquipe(Equipe.pasEquipe());
            this.joueur1 = null;
        }
        if (name.equals(this.joueur2.getNom())) {
            this.joueur2.setEquipe(Equipe.pasEquipe());
            this.joueur2 = null;

        }

    }

    public void ajouterPoints(int points) {
        this.score += points;
    }
}
