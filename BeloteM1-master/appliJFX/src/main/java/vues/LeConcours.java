package vues;

import controleur.Controleur;
import gestion.gestionConcours.concours.Concours;
import gestion.gestionConcours.concours.Equipe;
import gestion.gestionConcours.joueur.JoueurInterface;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collection;

public class LeConcours extends Vue {

    private static final String FICHIER_FXML = "/vues/LeConcours.fxml";
    private static final String TITRE = "Le Concours";
    public ComboBox<String> ListJoueurs;
    public ComboBox<String> ListEquipes;
    public TextField nomEquipe;
    public Text tempRestant;

    public static LeConcours creerEtAfficher(Controleur c, Stage laStageUnique) {
        LeConcours vue = (LeConcours) Vue.creerEtAfficher(c, laStageUnique, FICHIER_FXML, TITRE + " " + c.getConcoursSelectionne().getNom());
        vue.afficherLeConcours();
        return vue;
    }

    private void afficherLeConcours() {
        remplirComboBoxEquipes();
        remplirComboBoxJoueur();


    }

    private void remplirComboBoxJoueur() {
        Concours concours = monControleur.getConcoursSelectionne();
        Collection<JoueurInterface> lesJoueursDuConcours = concours.getLesJoueursDuConcours();
        ListJoueurs.getItems().clear();
        for (JoueurInterface joueur : lesJoueursDuConcours) {
            if (!ListJoueurs.getItems().contains(joueur.getNom()))
                if (joueur.aUneEquipe())
                    ListJoueurs.getItems().add(joueur.getNom() + " : " + joueur.getEquipe().getNomEquipe());
                else ListJoueurs.getItems().add(joueur.getNom() + " : Pas d'équipe");
        }
        setPromptListJoueurs(lesJoueursDuConcours.size() + " joueurs");

    }

    private void remplirComboBoxEquipes() {
        ListEquipes.getItems().clear();
        Collection<Equipe> lesEquipes = monControleur.getConcoursSelectionne().getLesEquipesDuConcours();
        for (Equipe equipe : lesEquipes) {
            if (equipe.estIncomplete())
                ListEquipes.getItems().add(equipe.getNomEquipe() + " : 1 Place");
            else ListEquipes.getItems().add(equipe.getNomEquipe() + " : Complète");
        }
        setPromptListEquipe(lesEquipes.size() + " équipes");

    }

    private void setPromptListEquipe(String message) {
        ListEquipes.setPromptText(message);
    }

    private void setPromptListJoueurs(String message) {
        ListJoueurs.setPromptText(message);
    }


    public void creerEquipe() {
        if (nomEquipe.getText().length() > 4)
            monControleur.creerEquipe(nomEquipe.getText());
        else afficheMessageErreur("Votre équipe doit contenir au - 4 lettres");
    }

    public void rejoindreEquipe() {
        String nomDeMonEquipe;
        try {
            nomDeMonEquipe = ListEquipes.getValue().split(" : ")[0];
            monControleur.rejoindreUneEquipe(nomDeMonEquipe);
        } catch (NullPointerException e) {
            afficheMessageErreur("Selectionnez une equipe");
        }


    }


    private void commencerCompetition() {
        boolean monJoueurEstDansTournoi = false;
        for (JoueurInterface joueur : monControleur.getConcoursSelectionne().getLesJoueursDuConcours()) {
            if (joueur.getNom().equals(monControleur.monJoueur().getNom()))
                monJoueurEstDansTournoi = true;
        }
        if (monJoueurEstDansTournoi)
            monControleur.goToToursSuivant();
        else {
            afficheMessageErreur("Le concours a démarrer sans vous...");
        }

    }

    public void setHoraire(int minute, int seconde) {
        if (minute == 59) {
            tempRestant.setText("GO!");
            try {
                Thread.sleep(300);
                actualiser();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else
            tempRestant.setText(minute + ":" + seconde);

    }


    @Override
    public Runnable actualiser() {
        return () -> {
            try {
                if (monControleur.getConcoursSelectionne().isEstLance())
                    commencerCompetition();
                else afficherLeConcours();

            } catch (NullPointerException e) {
                monControleur.goToMenu();
                afficheMessageErreur("Le concours a été supprimé car aucun participant");
            }
        };
    }

    public void retour() {
        if (monControleur.getConcoursSelectionne().getLesJoueursDuConcours().stream()
                .noneMatch(joueur -> joueur.getNom().equals(monControleur.getNomDuJoueur())))
            monControleur.goToLesConcours();
        else
            afficheMessageErreur("Vous etes deja inscrit vous ne pouvez plus revenir en arrière");
    }


}
