package gestion.gestionJeu.jeu;

import gestion.exceptions.CarteNonJouableException;
import gestion.gestionConcours.concours.Equipe;
import gestion.gestionConcours.joueur.JoueurInterface;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Manche extends Observable implements Serializable {


    final List<JoueurInterface> lesJoueurs = new ArrayList<>();
    final HashMap<String, Main> lesMains;
    boolean atoutPris = false;
    LaCarte atoutManche;
    Paquet paquetRestant;
    Pli pli;
    int nbRefus;
    int numeroTours;


    private Manche(Equipe equipe1, Equipe equipe2, Paquet lePaquet) {
        lesMains = new HashMap<>();
        lePaquet.melanger();
        this.lesJoueurs.add(equipe1.getJoueur1());
        this.lesJoueurs.add(equipe2.getJoueur1());
        this.lesJoueurs.add(equipe1.getJoueur2());
        this.lesJoueurs.add(equipe2.getJoueur2());
        for (JoueurInterface joueur : lesJoueurs) {
            lesMains.put(joueur.getNom(), lePaquet.creerMain());
        }
        atoutManche = lePaquet.regarder();
        paquetRestant = lePaquet;
        nbRefus = 0;
        numeroTours = 1;

        Thread jouer = new Thread(() -> {
            while (!this.isEstTerminee()) {
                try {
                    Thread.sleep(1000);
                    if (joueurEnCours().getNom().split("-")[0].equals("Bot"))
                        actionBot();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (getPli() != null && getPli().size() == 4) {
                    finPli();
                }

            }
        });
        jouer.start();

    }

    public static Manche creerManche(Equipe equipe1, Equipe equipe2, Paquet lePaquet) {
        return new Manche(equipe1, equipe2, lePaquet);

    }

    public Main get(String s) {
        return lesMains.get(s);
    }

    private void actionBot() {

        String nom = joueurEnCours().getNom();
        if (nom.split("-")[0].equals("Bot"))
            if (!isAtoutPris())
                accepterAtout(nom);
            else {
                Main mainDuBot = lesMains.get(nom);
                assert (getPli() != null);
                if (!isEstTerminee() && getPli().size() < 4) {
                    if (getPli().getAtoutParty() == null || !mainDuBot.contientSigneAtout(getPli().getAtoutParty())) {
                        try {
                            jouerCarte(mainDuBot.get(0), joueurEnCours());

                        } catch (CarteNonJouableException e) {
                            e.printStackTrace();
                        }
                    } else {
                        for (LaCarte carte : mainDuBot) {
                            if (carte.getSigne().equals(getPli().getAtoutParty().getSigne())) {
                                try {
                                    jouerCarte(carte, joueurEnCours());
                                } catch (CarteNonJouableException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }

                }
            }
    }

    public Pair<Equipe, Equipe> getLesEquipes() {
        return new Pair<>(lesJoueurs.get(0).getEquipe(), lesJoueurs.get(1).getEquipe());
    }

    public boolean isEstTerminee() {
        return numeroTours == 9;
        /*
        La manches est dite terminée lorsque toute les mains de la table ont 0 pour taille
        donc plus de cartes.
         */

    }

    public JoueurInterface joueurEnCours() {
        return lesJoueurs.get(0);
    }

    private void nouvelleManche() {
        Paquet lePaquet = Paquet.creerPaquet();
        lePaquet.melanger();
        for (JoueurInterface joueur : lesJoueurs) {
            lesMains.put(joueur.getNom(), lePaquet.creerMain());
        }
        atoutManche = lePaquet.regarder();
        paquetRestant = lePaquet;
        nbRefus = 0;

    }

    /*décaler la liste pour faire en sorte que:
     * - l'on puisse tirer au sort le donneur (on décale au hasard et on choisi le premier)
     * - qu'une fois l'atout pris que ça soit le joueur d'après qui joue le premier*/
    private void decalage(List<JoueurInterface> a) {
        for (int j = 0; j < 1; j++) {
            JoueurInterface x = a.get(0);
            a.remove(0);
            a.add(x);
        }
    }

    private void decalage(String nomJoueur) {
        int i = 0;
        for (JoueurInterface joueurInterface : lesJoueurs) {
            if (joueurInterface.getNom().equals(nomJoueur)) {
                i = lesJoueurs.indexOf(joueurInterface);
            }
        }
        decalage(i);

    }

    private void decalage() {
        JoueurInterface x = lesJoueurs.get(0);
        lesJoueurs.remove(0);
        lesJoueurs.add(x);
        //return lesJoueurs;
    }

    private void decalage(int i) {
        for (int j = 0; j < i; j++) {
            decalage();
        }
    }

    public void jouerCarte(LaCarte y, JoueurInterface j) throws CarteNonJouableException {
        int id = 0;
        for (LaCarte c : lesMains.get(j.getNom())) {
            if (c.toString().equals(y.toString())) id = lesMains.get(j.getNom()).indexOf(c);
        }
        if (pli.getAtoutParty() == null) {
            pli.setAtoutParty(y);
        } else {
            LaCarte carteAtoutPli = pli.getAtoutParty();
            if (!carteAtoutPli.getSigne().equals(y.getSigne()) && lesMains.get(j.getNom()).contientSigneAtout(carteAtoutPli)) {
                throw new CarteNonJouableException("Vous devez jouer une Carte " + carteAtoutPli.getSymbole());
            }
        }
        LaCarte laCarte = lesMains.get(j.getNom()).deposerCarte(id);
        pli.ajouterCarte(laCarte, j);
        decalage(lesJoueurs);
        setChanged();
        notifyObservers();


    }

    private void finPli() {
        getPli().finPli();
        getPli().getVainqueurParty().getEquipe().ajouterPoints(getPli().getRes());
        decalage(getPli().getVainqueurParty().getNom());
        numeroTours++;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        reinitialiserPli();
        setChanged();
        notifyObservers();


    }

    private void reinitialiserPli() {
        getPli().reinitialiserPli();
    }

    public void refus() {
        decalage();
        nbRefus++;
        if (nbRefus == 4)
            this.nouvelleManche();
        setChanged();
        notifyObservers();
    }

    public void accepterAtout(String nomJoueur) {
        getPaquetRestant().donnerNbCarteMain(lesMains.get(nomJoueur), 3);
        for (JoueurInterface j : getLesJoueurs()) {
            if (!j.getNom().equals(nomJoueur)) {
                getPaquetRestant().donnerNbCarteMain(lesMains.get(j.getNom()), 3);
            }
        }
        setAtoutPris(true);
        setPli(Pli.creerParty(getAtoutManche()));
        setChanged();
        notifyObservers();
    }
}
