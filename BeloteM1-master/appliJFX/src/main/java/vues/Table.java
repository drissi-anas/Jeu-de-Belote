package vues;

import controleur.Controleur;
import gestion.gestionConcours.concours.Equipe;
import gestion.gestionConcours.joueur.JoueurInterface;
import gestion.gestionJeu.jeu.LaCarte;
import gestion.gestionJeu.jeu.Main;
import gestion.gestionJeu.jeu.Manche;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;


public class Table extends Vue {
    private static final String FICHIER_FXML = "/vues/Table.fxml";
    private static final String TITRE = "La table de ";
    public HBox mesCartes;
    public HBox cartesCollegues;
    public HBox cartesDroite;
    public HBox cartesGauche;
    public GridPane laTable;
    public Text coequipier;
    public Text gauche;
    public Text droite;
    public Text moi;
    public Text atoutPli;


    public static Table creerEtAfficher(Controleur c, Stage laStageUnique) {
        Table vue = (Table) Vue.creerEtAfficher(c, laStageUnique, FICHIER_FXML, TITRE + c.getNomDuJoueur());
        vue.afficherLaMain();
        vue.affichagePli();

        return vue;
    }

    private void affichagePli() {
        ImageView carteAtout;
        final ImageView[] maCartePosee = new ImageView[1];
        //on prend la manche du controleur qui prend en parametre le concours, le tour et id de la manche du controleur
        Manche laManche = getManche();
        //si l'atout de la manche n'est pas pris
        if (atoutNonAccepte()) {
            //methode gerant la proposition de l'atout
            carteAtout = proposerAtout(laManche);

        } else {

            String t;
            try {
                t = "Vous devez jouez du " + monControleur.getMancheSelectionnee().getPli().getAtoutParty().getSigne() +
                        "\nL'atout de la manche est " + monControleur.getMancheSelectionnee().getAtoutManche().getSigne();
                try {
                    String tfinal = t;
                    t = tfinal + "\nLe vainqueur précédent est " + monControleur.getMancheSelectionnee().getPli().getVainqueurParty().getNom() +
                            "\n" + monControleur.getMancheSelectionnee().getPli().getVainqueurParty().getEquipe().getNomEquipe()
                            + " a empoché " + monControleur.getMancheSelectionnee().getPli().getRes() + " points !";
                } catch (NullPointerException ignored) {
                }
            } catch (NullPointerException e) {
                t = "En attente de l'atout du pli...";
            }
            atoutPli.setText(t);

            carteAtout = new ImageView("/signes/" + laManche.getAtoutManche().toString().split("-")[1] + ".jpg");
            redim(carteAtout, 20);
            monControleur.getCartePosee().forEach((joueur, carte) -> {
                maCartePosee[0] = new ImageView("/lePaquet/" + carte.toString() + ".png");
                redim(maCartePosee[0], 50);
                afficherCartePoseeJoueur(maCartePosee, joueur);
            });
            laTable.add(new Text("Au tours de :" + getJoueurEnCoursDeTours().getNom()), 2, 0);
            if (getManche().isEstTerminee()) {
                ajoutBoutonToursSuivant();
            }


        }
        laTable.add(carteAtout, 1, 1);


    }

    private void ajoutBoutonQuitter() {
        Button quitter = new Button("quitter");
        quitter.setOnMouseClicked(event -> {
            monControleur.goToClassement(monControleur.getConcoursSelectionne().getNom());
            monControleur.quitterEquipe();
        });
        laTable.add(quitter, 1, 2);
    }

    private void ajoutBoutonToursSuivant() {
        Button toursSuivant = new Button("Tours suivant");
        toursSuivant.setOnMouseClicked(event -> monControleur.goToToursSuivant());
        switch (monControleur.getToursActuel()) {
            case 1:
                if (monControleur.getConcoursSelectionne().getLesManchesTours1().stream().allMatch(Manche::isEstTerminee))
                    laTable.add(toursSuivant, 1, 2);
                else laTable.add(new Text("Attendez les autres manches"), 1, 2);
                break;
            case 2:
                if (monControleur.getConcoursSelectionne().getLesManchesTours2().stream().allMatch(Manche::isEstTerminee))
                    laTable.add(toursSuivant, 1, 2);
                else laTable.add(new Text("Attendez les autres manches"), 1, 2);
                break;
            case 3:
                if (monControleur.getConcoursSelectionne().getLesManchesTours3().stream().allMatch(Manche::isEstTerminee))
                    ajoutBoutonQuitter();
                else laTable.add(new Text("Attendez les autres manches"), 1, 2);
                break;


        }
    }

    private void resetGridPaneTable() {
        laTable.getChildren().clear();
    }

    private void resetMains() {
        mesCartes.getChildren().clear();
        cartesCollegues.getChildren().clear();
        cartesDroite.getChildren().clear();
        cartesGauche.getChildren().clear();
    }


    private ImageView proposerAtout(Manche laManche) {
        ImageView carteAtout;
        carteAtout = new ImageView("/lePaquet/" + laManche.getAtoutManche().toString() + ".png");
        //on identifie l'image précédente par la valeur écrite de la carte
        carteAtout.setId(laManche.getAtoutManche().toString());
        effetCarte(carteAtout);
        //bouton pour accepter ou refuser l'atout
        Button accepter = new Button("Accepter");
        Button refuser = new Button("refuser");
        //on associe le bouton accepter à la méthode accepterAtout()
        if (getJoueurEnCoursDeTours().getNom().equals(getNomDeMonJoueur())) {
            accepter.setOnMouseClicked(event -> accepterAtout());
            refuser.setOnAction(event -> refuserAtout());
        }
        //on ajoute les boutons à la Table.fxml
        laTable.add(accepter, 0, 1);
        laTable.add(refuser, 2, 1);
        laTable.add(new Text("Proposition pour " + getJoueurEnCoursDeTours().getNom()), 1, 0);
        return carteAtout;
    }

    private boolean atoutNonAccepte() {
        return monControleur.atoutNonPris();
    }

    private void afficherLaMain() {
        ArrayList<ImageView> liste = new ArrayList<>();

        Manche laManche = getManche();
        resetMains();
        for (JoueurInterface joueur : laManche.getLesJoueurs()) {
            Main maMain = laManche.get(joueur.getNom());
            for (LaCarte c : maMain) {
                ImageView monImage;
                if (memeNomQueMoi(joueur))
                    monImage = new ImageView("/lePaquet/" + c.toString() + ".png");
                else monImage = new ImageView("/lePaquet/dos-bleu.png");
                monImage.setId(c.toString());
                liste.add(monImage);
            }
            afficherMainJoueur(liste, joueur);
            liste = new ArrayList<>();

        }
    }

    private void afficherCartePoseeJoueur(ImageView[] maCartePosee, JoueurInterface joueur) {
        if (memeNomQueMoi(joueur))
            laTable.add(maCartePosee[0], 1, 2);
        if (memeNomEquipeQueMoi(joueur) && !memeNomQueMoi(joueur))
            laTable.add(maCartePosee[0], 1, 0);
        if (!memeNomEquipeQueMoi(joueur)) {
            if (monControleur.monJoueur().getNom().equals(getMonEquipe().getJoueur1().getNom())) {
                if (estJoueur1DeSonEquipe(joueur))
                    laTable.add(maCartePosee[0], 2, 1);
                else laTable.add(maCartePosee[0], 0, 1);
            } else {
                if (estJoueur1DeSonEquipe(joueur))
                    laTable.add(maCartePosee[0], 0, 1);
                else laTable.add(maCartePosee[0], 2, 1);

            }
        }
    }

    private boolean memeNomEquipeQueMoi(JoueurInterface joueur) {
        return joueur.getEquipe().getNomEquipe().equals(getMonEquipe().getNomEquipe());
    }

    private Manche getManche() {
        return monControleur.getMancheSelectionnee();
    }

    private void accepterAtout() {
        monControleur.accepterAtout();
        resetGridPaneTable();
        resetMains();

    }

    private void refuserAtout() {
        monControleur.refuserAtout();
    }

    private void afficherMainJoueur(ArrayList<ImageView> liste, JoueurInterface joueur) {
        if (memeNomQueMoi(joueur))
            imagerMain(liste, moi, mesCartes, joueur);
        if (memeNomEquipeQueMoi(joueur) && !memeNomQueMoi(joueur))
            imagerMain(liste, coequipier, cartesCollegues, joueur);
        if (!memeNomEquipeQueMoi(joueur)) {
            if (jeSuisJoueur1DeMonEquipe()) {
                if (estJoueur1DeSonEquipe(joueur))
                    imagerMain(liste, droite, cartesDroite, joueur);
                else imagerMain(liste, gauche, cartesGauche, joueur);
            } else {
                if (estJoueur1DeSonEquipe(joueur))
                    imagerMain(liste, gauche, cartesGauche, joueur);
                else imagerMain(liste, droite, cartesDroite, joueur);

            }
        }
    }

    private boolean estJoueur1DeSonEquipe(JoueurInterface j) {
        return j.getEquipe().getJoueur1().getNom().equals(j.getNom());
    }

    private boolean jeSuisJoueur1DeMonEquipe() {
        return getNomDeMonJoueur().equals(getMonEquipe().getJoueur1().getNom());
    }

    private boolean memeNomQueMoi(JoueurInterface j) {
        return j.getNom().equals(getNomDeMonJoueur());
    }

    private Equipe getMonEquipe() {
        return monControleur.monJoueur().getEquipe();
    }

    private String getNomDeMonJoueur() {
        return monControleur.getNomDuJoueur();
    }

    private void effetCarte(ImageView img) {
        redim(img, 50);
        img.setOnMouseEntered(
                event -> redim(img, 80));
        img.setOnMouseExited(
                event -> redim(img, 50));
        img.setOnMouseClicked(event -> {
            if (!atoutNonAccepte() && img.getId() != null) {
                if (getNomDeMonJoueur().equals(getJoueurEnCoursDeTours().getNom())) {
                    monControleur.jouerCarte(img.getId());
                } else {
                    afficheMessageErreur("Ce n'est pas votre tours");
                }
            } else afficheMessageErreur("La carte atout n'a pas encore était acceptée");
        });
    }

    private JoueurInterface getJoueurEnCoursDeTours() {
        return monControleur.getJoueurEnCoursDeTours();
    }

    private void imagerMain(ArrayList<ImageView> liste, Text text, HBox hBox, JoueurInterface joueurInterface) {
        for (ImageView img : liste) {
            effetCarte(img);
            hBox.getChildren().add(img);
        }
        JoueurInterface joueur = monControleur.getJoueurByName(joueurInterface.getNom());
        text.setText(joueur.getNom() + " : " + joueur.getEquipe().getNomEquipe() + " " + joueur.getEquipe().getScore());

    }

    private void redim(ImageView i, int j) {

        i.setFitWidth(j);
        i.setPreserveRatio(true);
        i.setSmooth(true);
    }

    public Runnable actualiser() {
        return () -> {
            resetGridPaneTable();
            afficherLaMain();
            affichagePli();
        };

    }

}