package controleur;

import controleur.exception.InscriptionFermeeException;
import gestion.exceptions.*;
import gestion.gestionConcours.concours.Concours;
import gestion.gestionConcours.concours.Equipe;
import gestion.gestionConcours.joueur.Joueur;
import gestion.gestionConcours.joueur.JoueurInterface;
import gestion.gestionJeu.jeu.LaCarte;
import gestion.gestionJeu.jeu.Manche;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import rmiService.MonService;
import vues.*;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Controleur implements AdminInterface, PlayerInterface {
    final Stage laStageUnique;
    final MonService facade;
    Vue FenetrePrincipale;

    Concours concoursSelectionne;
    String tours;
    String nomDuJoueur;
    boolean isAdmin = false;
    int id;
    int DerniereModif = 0;
    int toursActuel = 0;

    public Controleur(Stage laStageUnique, MonService service) {
        this.laStageUnique = laStageUnique;
        facade = service;
        goToInscription();
        Thread threadMaJ = new Thread(getRunnableMaJ());
        threadMaJ.start();
    }

    /*
        DEBUT LES GETTERS PRIMITFS

     */


    private int getIndiceDeModificationDuModele() {
        try {
            return facade.getIndiceDeModificationDuModele();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public List<Concours> getLesConcours() {
        try {
            return facade.getLesConcours();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JoueurInterface getJoueurByName(String pseudo) {
        try {
            return facade.getJoueurByName(pseudo);
        } catch (RemoteException | AucunJoueurNePossedeCeNomException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Concours getConcoursByName(String name) {
        try {
            return facade.getConcoursByName(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<JoueurInterface> getLesInscrit() {
        try {
            return facade.getLesInscrits();
        } catch (RemoteException | AucunJoueursNEstInscritException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> getLesJoueursDuConcours(String name) {
        List<String> res = new ArrayList<>();
        Objects.requireNonNull(getConcoursByName(name)).getLesJoueursDuConcours().forEach(joueurInterface -> res.add(joueurInterface.getNom()));
        return res;
    }


    private Manche getManche(String nameConcours, String tours, int id) {
        try {
            return facade.getManche(nameConcours, tours, id);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<JoueurInterface, LaCarte> getCartePosee() {
        try {
            return facade.getCartePosee(concoursSelectionne.getNom(), getTours(), getId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
        FIN LES GETTERS PRIMITFS

     */



    /*
        DEBUT MES GETTERS
     */


    public Manche getMancheSelectionnee() {
        return getManche(concoursSelectionne.getNom(), tours, id);
    }

    private void setMancheSelectionnee(String tours, int id) {
        this.tours = tours;
        this.id = id;
    }

    public JoueurInterface getJoueurEnCoursDeTours() {
        try {
            return facade.getJoueurEnCoursDeTours(concoursSelectionne.getNom(), tours, id);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getToursActuel() {
        return toursActuel;
    }

    private String getTours() {
        return tours;
    }

    private int getId() {
        return id;
    }

    public JoueurInterface monJoueur() {
        return getJoueurByName(this.nomDuJoueur);

    }

    public List<Concours> getMesConcours() {
        return ((Joueur) monJoueur()).getLesConcoursDuJoueur();
    }

    public String getNomDuJoueur() {
        return nomDuJoueur;
    }

    public Concours getConcoursSelectionne() {
        return getConcoursByName(concoursSelectionne.getNom());
    }

    public List<Equipe> lesEquipesDuConcoursClassee() {
        List<Equipe> lesEquipes = getConcoursSelectionne().getLesEquipesDuConcours();
        lesEquipes.sort(Comparator.comparingInt(Equipe::getScore));
        return lesEquipes;
    }

    public List<Equipe> lesEquipesDuConcoursClassee(String name) {
        Concours concours = getConcoursByName(name);
        assert concours != null;
        List<Equipe> lesEquipes = concours.getLesEquipesDuConcours();
        lesEquipes.sort(Comparator.comparingInt(Equipe::getScore));
        return lesEquipes;
    }

    public boolean atoutNonPris() {
        try {
            return !facade.atoutPris(getConcoursSelectionne().getNom(), tours, id);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean isAdmin() {
        return isAdmin;
    }


    /*
        FIN MES GETTERS

     */

    /*
        DEBUT LES ACTIONS

     */

    @Override
    public void inscrireUnJoueur(String pseudo) {
        try {
            facade.ajouterJoueurInscrit(pseudo);
            System.out.println("Bienvenu " + pseudo + ", vous vous êtes connecté");
            nomDuJoueur = pseudo;
            goToMenu();
        } catch (NomDeJoueurDejaExistantException e) {
            afficherMessageAlerte("Le pseudo " + pseudo + " est deja pris");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (CaracteresSpeciauxException e) {
            afficherMessageAlerte(e);
        }
    }

    @Override
    public void seDeconnecter() {
        try {
            facade.retirerJoueurInscrit(nomDuJoueur);
            System.out.println("Au revoir " + nomDuJoueur);
            resetAll();
            goToInscription();
        } catch (AucunJoueurNePossedeCeNomException | RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void accepterAtout() {
        try {
            facade.accepterAtout(concoursSelectionne.getNom(), getTours(), getId(), getNomDuJoueur());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refuserAtout() {
        try {
            facade.refuserAtout(concoursSelectionne.getNom(), getTours(), getId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void jouerCarte(String laCarte) {
        try {
            try {
                facade.jouerCarte(concoursSelectionne.getNom(), getTours(), getId(), getNomDuJoueur(), laCarte);
            } catch (CarteNonJouableException e) {
                afficherMessageAlerte("Vous êtes obliger de jouer un " + getMancheSelectionnee().getPli().getAtoutParty().getSigne());
            } catch (NullPointerException e) {
                afficherMessageAlerte("Cette carte ne vous appartient pas voyons !!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void participerConcours(String nomDuConcours) throws InscriptionFermeeException, ConcoursDejaLanceException {
        try {
            this.concoursSelectionne = getConcoursByName(nomDuConcours);
            if (concoursSelectionne != null) {
                int deltaHeures = getConcoursSelectionne().getHeure() - LocalDateTime.now().getHour();
                int deltaMin = getConcoursSelectionne().getMinutes() - LocalDateTime.now().getMinute();
                if (deltaMin < 0) {
                    deltaMin = 60 + deltaMin;
                    deltaHeures--;
                }
                if (deltaHeures < 0) {
                    deltaHeures = 24 + deltaHeures;
                }

                if (deltaHeures == 0 && deltaMin <= 30) {
                    facade.rejoindreConcours(nomDuConcours, nomDuJoueur);
                    System.out.println(nomDuJoueur + ", vous participez à " + nomDuConcours);
                } else if (!getConcoursSelectionne().isEstLance()) {
                    throw new InscriptionFermeeException(deltaHeures, deltaMin);

                }


            } else
                afficherMessageAlerte("Selectionnez un concours");
        } catch (RemoteException e) {
            afficherMessageAlerte("Le concours n'est pas encore lancé, veuillez patientez");
        } catch (AucunJoueurNePossedeCeNomException e) {
            afficherMessageAlerte("Aucun joueur ne possede ce nom");
        } catch (JoueurParticipeDejaException e) {
            afficherMessageAlerte("Vous participe deja a " + nomDuConcours);
        }

    }

    @Override
    public void creerEquipe(String name) {
        try {
            try {
                if (name != null && !name.equals("")) {
                    if (getLesJoueursDuConcours(concoursSelectionne.getNom()).contains(nomDuJoueur)) {
                        facade.creerEquipe(nomDuJoueur, concoursSelectionne.getNom(), name);
                        System.out.println(monJoueur().getNom() + " créé l'équipe " + name + " dans " + concoursSelectionne.getNom());
                    } else
                        afficherMessageAlerte("Vous devez d'abord participer au concours");

                } else
                    afficherMessageAlerte("Vous devez rentrer au moins 1 caractère");
            } catch (DejaDansUneEquipeException | CaracteresSpeciauxException e) {
                afficherMessageAlerte(e.getMessage());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } catch (AucunJoueurNePossedeCeNomException e) {
            afficherMessageAlerte(e.getMessage());
        }

    }

    @Override
    public void rejoindreUneEquipe(String equipe) {
        try {
            try {
                if (equipe != null) {
                    if (getLesJoueursDuConcours(concoursSelectionne.getNom()).contains(nomDuJoueur))
                        facade.rejoindreEquipe(nomDuJoueur, equipe);
                    else
                        afficherMessageAlerte("Vous devez d'abord participer au concours");

                } else
                    afficherMessageAlerte("Selectionnez une equipe");
            } catch (DejaDansUneEquipeException e) {
                afficherMessageAlerte(e.getMessage());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } catch (AucunJoueurNePossedeCeNomException | EquipeCompleteException e) {
            afficherMessageAlerte(e.getMessage());
        }
    }

    @Override
    public void quitterEquipe() {
        try {
            facade.quitterEquipe(getNomDuJoueur());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void connecterAdmin(String pseudo, String motDePasse) {
        try {

            facade.connecterAdmin(pseudo, motDePasse);
            this.isAdmin = true;
            this.nomDuJoueur = "admin";
            goToPageAdmin();

        } catch (DonneesIncorrectesException e) {
            afficherMessageAlerte("Données incorrectes");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ajouterConcours(String name, int heures, int minutes) {
        try {
            facade.ajouterConcours(name, heures, minutes);
        } catch (NomDeConcoursDejaExistantException | RemoteException | CaracteresSpeciauxException e) {
            afficherMessageAlerte(e);
        }

    }

    @Override
    public void retirerConcours(String name) {
        try {
            this.concoursSelectionne = getConcoursByName(name);
            if (concoursSelectionne != null)
                facade.retirerConcours(name);
            else
                afficherMessageAlerte("selectionnez un concours");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /*
        FIN LES ACTIONS

     */

    /*
        DEBUT DES GOTO / Affichages

     */

    private void afficherMessageAlerte(Exception e) {
        Alert a = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
        a.show();
    }

    private void afficherMessageAlerte(String message) {
        Alert a = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        a.show();
    }

    public void afficherLeConcours(String name) {
        this.concoursSelectionne = getConcoursByName(name);
        if (concoursSelectionne != null) {
            goToLeConcours();
        } else
            afficherMessageAlerte("Selectionnez un concours");


    }

    private void goToLeConcours() {
        FenetrePrincipale = LeConcours.creerEtAfficher(this, laStageUnique);
        Thread thread = new Thread(() -> {
            while (concoursSelectionne.getMinutes() - LocalDateTime.now().getMinute() != 0) {
                try {
                    Thread.sleep(1000);

                    Platform.runLater(() -> {
                        int minute = getConcoursSelectionne().getMinutes() - 1 - LocalDateTime.now().getMinute();
                        if (minute < 0) minute += 60;
                        int seconde = 60 - LocalDateTime.now().getSecond();
                        ((LeConcours) FenetrePrincipale).setHoraire(minute, seconde);
                    });
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }

        });
        thread.start();

    }

    public void goToLesConcours() {
        resetConcours();
        FenetrePrincipale = LesConcours.creerEtAfficher(this, laStageUnique);
    }

    public void goToMonHistorique() {
        FenetrePrincipale = Historique.creerEtAfficher(this, laStageUnique);
    }

    public void goToMenu() {
        resetConcours();
        toursActuel = 0;
        FenetrePrincipale = Menu.creerEtAfficher(this, laStageUnique);
    }

    private void goToTable() {
        FenetrePrincipale = Table.creerEtAfficher(this, laStageUnique);
    }

    public void goToInscription() {
        resetAll();
        FenetrePrincipale = Inscription.creerEtAfficher(this, laStageUnique);
    }

    public void goToPageAdmin() {
        FenetrePrincipale = PageAdmin.creerEtAfficher(this, laStageUnique);
    }

    public void goToClassement(String nameConcours) {
        resetConcours();
        this.concoursSelectionne = getConcoursByName(nameConcours);
        if (concoursSelectionne != null) {
            FenetrePrincipale = Classement.creerEtAfficher(this, laStageUnique);
        } else
            afficherMessageAlerte("Selectionnez un concours");

    }

    public void goToToursSuivant() {
        List<Manche> lesManches1 = getConcoursSelectionne().getLesManchesTours1();
        List<Manche> lesManches2 = getConcoursSelectionne().getLesManchesTours2();
        List<Manche> lesManches3 = getConcoursSelectionne().getLesManchesTours3();
        switch (toursActuel) {
            case 0:
                goToMaManche(lesManches1, 1);
                break;
            case 1:
                if (lesManches1.stream().allMatch(Manche::isEstTerminee))
                    goToMaManche(lesManches2, 2);
                else afficherMessageAlerte("Toutes les manches ne sont pas terminées");
                break;
            case 2:
                if (lesManches2.stream().allMatch(Manche::isEstTerminee))
                    goToMaManche(lesManches3, 3);
                else afficherMessageAlerte("Toutes les manches ne sont pas terminées");
                break;
        }
    }

    private void goToMaManche(List<Manche> lesManches, int tours) {
        for (int i = 0; i < lesManches.size(); i++) {
            Manche manche = lesManches.get(i);
            if (manche.getLesEquipes().getValue().getNomEquipe().equals(monJoueur().getEquipe().getNomEquipe()) || manche.getLesEquipes().getKey().getNomEquipe().equals(monJoueur().getEquipe().getNomEquipe())) {
                switch (tours) {
                    case 1:
                        setMancheSelectionnee("Tours1", i);
                        break;
                    case 2:
                        setMancheSelectionnee("Tours2", i);
                        break;
                    case 3:
                        setMancheSelectionnee("Tours3", i);
                        break;
                }
            }

        }
        if (toursActuel < 3) toursActuel++;
        goToTable();
    }



    /*
        FIN DES GOTO

     */


    private void resetConcours() {
        this.concoursSelectionne = null;
    }

    private void resetJoueur() {
        this.nomDuJoueur = null;
    }

    private void resetAll() {
        resetJoueur();
        resetConcours();
        this.isAdmin = false;
    }

    /*
        DEBUT GESTION DES MaJ DE PAGES

     */

    private int getDerniereModif() {
        return DerniereModif;
    }

    private void setDerniereModif(int derniereModif) {
        DerniereModif = derniereModif;
    }


    private Runnable getRunnableMaJ() {
        return () -> {
            while (!Thread.currentThread().isInterrupted()) {
                if (getIndiceDeModificationDuModele() != getDerniereModif()) {
                    setDerniereModif(getIndiceDeModificationDuModele());
                    Platform.runLater(FenetrePrincipale.actualiser());
                }
            }
        };
    }



    /*
        FIN GESTION DES MaJ DE PAGES

     */

}