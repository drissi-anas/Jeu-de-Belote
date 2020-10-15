package vues;

import controleur.Controleur;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Menu extends Vue {

    private static final String FICHIER_FXML = "/vues/menu.fxml";
    private static final String TITRE = "Menu";

    public Text titre;
    public Button histo;

    public static Menu creerEtAfficher(Controleur c, Stage laStageUnique) {
        Menu vue = (Menu) Vue.creerEtAfficher(c, laStageUnique, FICHIER_FXML, TITRE);
        vue.titre.setText("Bienvenue " + c.getNomDuJoueur());
        return vue;
    }

    public void seDeconnecter() {
        monControleur.seDeconnecter();
    }

    public void voirLesConcours() {
        monControleur.goToLesConcours();
    }

    @Override
    public Runnable actualiser() {
        return () -> {
        };
    }

}
