package vues;

import controleur.Controleur;
import gestion.gestionConcours.concours.Equipe;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.List;

public class Classement extends Vue {
    private static final String FICHIER_FXML = "/vues/Classement.fxml";
    private static final String TITRE = "Classement";
    public VBox maVbox;


    public static Classement creerEtAfficher(Controleur c, Stage laStageUnique) {
        Classement vue = (Classement) Vue.creerEtAfficher(c, laStageUnique, FICHIER_FXML, TITRE + " : " + c.getConcoursSelectionne().getNom());
        vue.afficherClassement();
        return vue;
    }

    public void actionRetour() {
        if (monControleur.isAdmin()) goToPageAdmin();
        else goToMenu();
    }

    private void afficherClassement() {
        maVbox.getChildren().clear();
        List<Equipe> LesEquipesClassees = monControleur.lesEquipesDuConcoursClassee();
        Collections.reverse(LesEquipesClassees);
        LesEquipesClassees.forEach(y -> maVbox.getChildren().add(new Label(y.getNomEquipe() + " " + y.getScore() + " points")));
    }

    public Runnable actualiser() {
        return this::afficherClassement;
    }

}
