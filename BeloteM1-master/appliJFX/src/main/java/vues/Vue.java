package vues;

import controleur.Controleur;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public abstract class Vue {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 600;
    Controleur monControleur;


    static Vue creerEtAfficher(Controleur c, Stage laStageUnique, String nameFxml, String titre) {
        URL location = Vue.class.getResource(nameFxml);
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Vue vue = fxmlLoader.getController();
        laStageUnique.setTitle(titre);
        assert root != null;
        laStageUnique.setScene(new Scene(root, WIDTH, HEIGHT));
        laStageUnique.show();
        vue.setMonControleur(c);
        return vue;
    }

    private void setMonControleur(Controleur c) {
        this.monControleur = c;
    }

    void afficheMessageErreur(String message) {
        Alert a = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        a.show();
    }

    public void goToAccueil() {
        if (monControleur.isAdmin()) goToPageAdmin();
        else goToMenu();
        monControleur.quitterEquipe();
    }

    public void goToInscription() {
        monControleur.goToInscription();
    }

    void goToMenu() {
        monControleur.goToMenu();
    }

    public void goToPageAdmin() {
        monControleur.goToPageAdmin();
    }

    public void goToMonHistorique() {
        monControleur.goToMonHistorique();
    }


    public abstract Runnable actualiser();

}
