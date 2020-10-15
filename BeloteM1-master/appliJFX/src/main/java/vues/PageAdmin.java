package vues;

import controleur.Controleur;
import gestion.gestionConcours.concours.Concours;
import gestion.gestionConcours.joueur.JoueurInterface;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.List;

public class PageAdmin extends Vue {

    private static final String FICHIER_FXML = "/vues/PageAdmin.fxml";
    private static final String TITRE = "Accueil Administrateur";
    public ComboBox<String> ListConcours;
    public ComboBox<String> ListJoueurs;
    public TextField nomConcours;
    public TextField heure;
    public TextField minute;


    public static PageAdmin creerEtAfficher(Controleur c, Stage laStageUnique) {
        PageAdmin vue = (PageAdmin) Vue.creerEtAfficher(c, laStageUnique, FICHIER_FXML, TITRE + " : " + c.getNomDuJoueur());
        vue.afficherLesListes();
        return vue;
    }

    private void afficherLesListes() {
        Collection<Concours> lesConcours = monControleur.getLesConcours();
        List<JoueurInterface> lesInscrit = monControleur.getLesInscrit();
        ListConcours.getItems().clear();
        ListJoueurs.getItems().clear();
        for (Concours concours : lesConcours) {
            if (concours.isEstLance())
                ListConcours.getItems().add(concours.getNom() + " : Lancé");
            else
                ListConcours.getItems().add(concours.getNom() + " : Début " + concours.getHeure() + "H" + concours.getMinutes() + "min");
        }
        for (JoueurInterface joueur : lesInscrit) {
            ListJoueurs.getItems().add(joueur.getNom());
        }
        ListJoueurs.setPromptText(lesInscrit.size() + " Joueurs inscrits");
        ListConcours.setPromptText((lesConcours.size() + " Concours"));

    }


    public void retirerConcours() {
        String nomDeMonConcours;
        try {
            nomDeMonConcours = ListConcours.getValue().split(" : ")[0];
            monControleur.retirerConcours(nomDeMonConcours);
        } catch (NullPointerException e) {
            afficheMessageErreur("Selectionnez un concours");
        }

    }

    public void ajouterConcours() {
        if (nomConcours.getText().length() > 4)
            try {
                int h = Integer.parseInt(heure.getText());
                int m = Integer.parseInt(minute.getText());
                if (h < 0 || 23 < h || m < 0 || 59 < m)
                    afficheMessageErreur(" Format supporté : 00h00min à 23h59min");
                monControleur.ajouterConcours(nomConcours.getText(), h, m);/*TODO*/
            } catch (NumberFormatException e) {
                afficheMessageErreur("Heure incorrecte");
            }
        else afficheMessageErreur("Votre concours doit contenir au - 4 lettres");
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

    @Override
    public Runnable actualiser() {
        return this::afficherLesListes;
    }
}
