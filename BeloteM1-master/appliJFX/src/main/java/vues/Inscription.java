package vues;

import controleur.Controleur;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Inscription extends Vue {

    private static final String FICHIER_FXML = "/vues/Inscription.fxml";
    private static final String TITRE = "Inscription";
    public PasswordField motDePasse;
    public TextField pseudoAdmin;
    public TextField pseudo;

    public static Inscription creerEtAfficher(Controleur c, Stage laStageUnique) {
        return (Inscription) Vue.creerEtAfficher(c, laStageUnique, FICHIER_FXML, TITRE);
    }


    public void seConnecter() {
        if (pseudo.getText().split("-")[0].equals("Bot") || pseudo.getText().length() < 4)
            afficheMessageErreur("Les pseudos de - de 4 lettres et commençant par Bot- ne sont pas acceptés");
        else monControleur.inscrireUnJoueur(pseudo.getText());
    }


    public void connecterAdmin() {
        monControleur.connecterAdmin(pseudoAdmin.getText(), motDePasse.getText());
    }

    @Override
    public Runnable actualiser() {
        return () -> {
        };
    }
}
