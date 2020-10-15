package gestion.gestionConcours.concours;

import gestion.exceptions.*;
import gestion.gestionConcours.joueur.JoueurInterface;
import gestion.gestionJeu.jeu.Manche;
import gestion.gestionJeu.jeu.Paquet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Concours extends Observable implements Serializable, Observer {
    static int id = 0;
    final int identifiant;
    final String nom;
    final List<JoueurInterface> lesJoueursDuConcours;
    final List<Equipe> lesEquipesDuConcours;
    final int heure;
    final int minutes;
    List<Manche> lesManchesTours1 = new ArrayList<>();
    List<Manche> lesManchesTours2 = new ArrayList<>();
    List<Manche> lesManchesTours3 = new ArrayList<>();
    boolean estLance;

    public Concours(String nom, int heure, int minutes) {
        this.nom = nom;
        this.heure = heure;
        this.minutes = minutes;
        this.identifiant = id;
        id = id + 1;
        this.lesJoueursDuConcours = new ArrayList<>();
        this.lesEquipesDuConcours = new ArrayList<>();

    }

    public boolean isEstLance() {
        return estLance;
    }

    public void ajouterJoueurConcours(JoueurInterface joueur) throws ConcoursDejaLanceException {
        if (isEstLance()) throw new ConcoursDejaLanceException();
        lesJoueursDuConcours.add(joueur);
    }

    public void retirerJoueurConcours(JoueurInterface joueur) {
        lesJoueursDuConcours.removeIf(y -> y.equals(joueur));
    }

    public void ajouterEquipe(Equipe equipe) {
        lesEquipesDuConcours.add(equipe);
    }

    public List<JoueurInterface> getLesJoueursSansEquipe() throws AucunJoueursNAPasDEquipeException {
        List<JoueurInterface> lesJoueursSansEquipe = lesJoueursDuConcours.stream().filter(joueur -> !joueur.aUneEquipe()).collect(Collectors.toList());
        if (lesJoueursSansEquipe.isEmpty()) throw new AucunJoueursNAPasDEquipeException();
        return lesJoueursSansEquipe;
    }

    public List<Equipe> getLesEquipesIncompletes() throws AucuneEquipeEstIncompleteException {
        List<Equipe> lesEquipesIncompletes = lesEquipesDuConcours.stream().filter(Equipe::estIncomplete).collect(Collectors.toList());
        if (lesEquipesIncompletes.isEmpty()) throw new AucuneEquipeEstIncompleteException();
        return lesEquipesIncompletes;

    }

    private Manche creerManche(Equipe equipe1, Equipe equipe2) {
        Paquet paquet = Paquet.creerPaquet();
        paquet.melanger();
        Manche manche = Manche.creerManche(equipe1, equipe2, paquet);
        manche.addObserver(this);
        return manche;
    }

    private List<Manche> creerToutesLesManches() {
        List<Manche> lesManches = new ArrayList<>();
        Collections.shuffle(lesEquipesDuConcours);
        for (int i = 0; i < lesEquipesDuConcours.size(); i += 2) {
            lesManches.add(creerManche(lesEquipesDuConcours.get(i), lesEquipesDuConcours.get(i + 1)));
        }
        return lesManches;
    }

    public void lancerConcours() throws LeConcoursNeContientAucuneEquipeException, LeConcoursContientUnNombreImpairDEquipe, LeConcoursADejaEteLanceException {//Bouton pour l'admin
        if (lesEquipesDuConcours.size() == 0) throw new LeConcoursNeContientAucuneEquipeException();
        if (lesEquipesDuConcours.size() % 2 == 1) throw new LeConcoursContientUnNombreImpairDEquipe();
        if (this.estLance) throw new LeConcoursADejaEteLanceException();
        lesManchesTours1 = creerToutesLesManches();
        lesManchesTours2 = creerToutesLesManches();
        lesManchesTours3 = creerToutesLesManches();
        estLance = true;
    }

    public Manche getManche(String nomTour, int idManche) {
        switch (nomTour) {
            case "Tours1":
                return lesManchesTours1.get(idManche);
            case "Tours2":
                return lesManchesTours2.get(idManche);
            case "Tours3":
                return lesManchesTours3.get(idManche);
        }
        return null;
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }
}
