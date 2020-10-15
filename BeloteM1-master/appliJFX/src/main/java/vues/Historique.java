package vues;

import controleur.Controleur;
import gestion.gestionConcours.concours.Concours;
import gestion.gestionConcours.concours.Equipe;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.List;

public class Historique extends Vue {

    private static final String FICHIER_FXML = "/vues/Historique.fxml";
    private static final String TITRE = "Mon historique";
    public VBox maVbox;


    public static Historique creerEtAfficher(Controleur c, Stage laStageUnique) {
        Historique vue = (Historique) Vue.creerEtAfficher(c, laStageUnique, FICHIER_FXML, TITRE);
        vue.afficherMesConcours();
        return vue;
    }


    public void actionRetour() {
        if (monControleur.isAdmin()) goToPageAdmin();
        else goToMenu();
    }

    private void afficherMesConcours() {
        maVbox.getChildren().clear();
        List<Concours> mesConcours = monControleur.getMesConcours();
        double mesVictoires = 0;
        double mesDefaites = 0;
        String res = "";
        for (Concours concours : mesConcours) {
            List<Equipe> lesEquipesClassees = monControleur.lesEquipesDuConcoursClassee(concours.getNom());
            Collections.reverse(lesEquipesClassees);
            Equipe vainqueur = lesEquipesClassees.get(0);
            if (vainqueur.getJoueur1().getNom().equals(monControleur.getNomDuJoueur()) || vainqueur.getJoueur2().getNom().equals(monControleur.getNomDuJoueur()))
                res = "Victoire !";
            else res = "Défaite";
            maVbox.getChildren().add(new Label(concours.getNom() + " " + res + " Vainqueur :aze " + vainqueur.getNomEquipe()));
            if (res.equals("Victoire !"))
                mesVictoires++;
            else mesDefaites++;


        }
        double pourcentageVictoire;
        if (mesDefaites == 0)
            pourcentageVictoire = 100;
        else pourcentageVictoire = (mesVictoires / mesDefaites) * 100;


        maVbox.getChildren().add(new Text(pourcentageVictoire + "% de victoires"));
    }

    public Runnable actualiser() {
        return this::afficherMesConcours;
    }

}
