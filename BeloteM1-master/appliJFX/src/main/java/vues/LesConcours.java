package vues;

import controleur.Controleur;
import controleur.exception.InscriptionFermeeException;
import gestion.exceptions.ConcoursDejaLanceException;
import gestion.gestionConcours.concours.Concours;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.Collection;

public class LesConcours extends Vue {

    private static final String FICHIER_FXML = "/vues/lesconcours.fxml";
    private static final String TITRE = "Les Concours";
    public ComboBox<String> ListConcours;

    public static LesConcours creerEtAfficher(Controleur c, Stage laStageUnique) {
        LesConcours vue = (LesConcours) Vue.creerEtAfficher(c, laStageUnique, FICHIER_FXML, TITRE + " : " + c.getNomDuJoueur());
        vue.afficherListeConcours();
        return vue;
    }


    private void afficherListeConcours() {
        Collection<Concours> lesConcours = monControleur.getLesConcours();
        if (!ListConcours.getItems().isEmpty()) {
            for (String concours : ListConcours.getItems()) {
                if (lesConcours.stream().noneMatch(concours1 -> concours1.getNom().equals(concours.split(" : ")[0])))
                    ListConcours.getItems().remove(concours);
            }
        }
        if (!lesConcours.isEmpty()) {
            for (Concours concours : lesConcours) {
                if (concours.isEstLance() && !ListConcours.getItems().contains(concours.getNom() + " : Est lancé")) {
                    ListConcours.getItems().remove(concours.getNom() + " : Début " + concours.getHeure() + "H" + concours.getMinutes() + "min");
                    ListConcours.getItems().add(concours.getNom() + " : Est lancé");
                }
                if (!concours.isEstLance() && !ListConcours.getItems().contains(concours.getNom() + " : Début " + concours.getHeure() + "H" + concours.getMinutes() + "min"))
                    ListConcours.getItems().add(concours.getNom() + " : Début " + concours.getHeure() + "H" + concours.getMinutes() + "min");
            }
        } else ListConcours.getItems().clear();
    }

    public void afficherClassement() {
        String nomDeMonConcours;
        try {
            nomDeMonConcours = ListConcours.getValue().split(" : ")[0];
            monControleur.goToClassement(nomDeMonConcours);
        } catch (NullPointerException e) {
            afficheMessageErreur("Selectionnez un concours");
        }
    }

    public void afficherLeConcours() {
        String nomDeMonConcours;
        try {
            nomDeMonConcours = ListConcours.getValue().split(" : ")[0];
            monControleur.afficherLeConcours(nomDeMonConcours);
        } catch (NullPointerException e) {
            afficheMessageErreur("Selectionnez un concours");
        }

    }

    public void participerConcours() {
        String nomDeMonConcours;
        try {
            nomDeMonConcours = ListConcours.getValue().split(" : ")[0];
            try {
                monControleur.participerConcours(nomDeMonConcours);
                monControleur.afficherLeConcours(nomDeMonConcours);
            } catch (InscriptionFermeeException | ConcoursDejaLanceException e) {
                afficheMessageErreur(e.getMessage());
            }
        } catch (NullPointerException e) {
            afficheMessageErreur("Selectionnez un concours");
        }
    }

    public Runnable actualiser() {
        return this::afficherListeConcours;

    }
}
