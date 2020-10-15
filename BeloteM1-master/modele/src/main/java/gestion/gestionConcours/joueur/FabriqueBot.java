package gestion.gestionConcours.joueur;

public class FabriqueBot {
    private static final String NOM = "Bot-";
    private int id;

    public FabriqueBot() {
        this.id = 0;
    }

    public JoueurInterface creerBot() {
        String nom = NOM + id;
        id++;
        return new Joueur(nom);
    }
}
