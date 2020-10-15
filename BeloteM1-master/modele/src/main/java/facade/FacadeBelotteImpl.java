package facade;

import gestion.exceptions.*;
import gestion.gestionConcours.concours.Concours;
import gestion.gestionConcours.concours.Equipe;
import gestion.gestionConcours.joueur.FabriqueBot;
import gestion.gestionConcours.joueur.Joueur;
import gestion.gestionConcours.joueur.JoueurInterface;
import gestion.gestionJeu.jeu.LaCarte;
import gestion.gestionJeu.jeu.Manche;
import gestion.gestionJeu.jeu.Pli;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class FacadeBelotteImpl extends Observable implements FacadeBelotte {

    final List<JoueurInterface> lesJoueurs;
    final List<Concours> lesConcours;
    final FabriqueBot laFabriqueDeBots;


    public FacadeBelotteImpl() {
        this.lesJoueurs = new ArrayList<>();
        this.lesConcours = new ArrayList<>();
        JoueurInterface admin = new Joueur("admin");
        new Equipe(admin, "er");
        lesJoueurs.add(admin);
        laFabriqueDeBots = new FabriqueBot();

    }

    private boolean joueurExistant(String name) {
        if (!lesJoueurs.isEmpty()) {
            for (JoueurInterface joueur : lesJoueurs) {
                if (joueur.getNom().equals(name)) return true;
            }
        }
        return false;
    }

    @Override
    public List<Equipe> getLesEquipesIncompletes(Concours concours) throws AucuneEquipeEstIncompleteException {
        return concours.getLesEquipesIncompletes();
    }

    @Override
    public void rejoindreConcours(String nomDuConcours, String nomDuJoueur) throws AucunJoueurNePossedeCeNomException, JoueurParticipeDejaException, ConcoursDejaLanceException {
        getJoueurByName(nomDuJoueur).rejoindreConcours(getConcoursByName(nomDuConcours));
        setChanged();
        notifyObservers();
    }

    @Override
    public List<JoueurInterface> getLesInscrits() throws AucunJoueursNEstInscritException {
        if (lesJoueurs.isEmpty()) throw new AucunJoueursNEstInscritException();
        return lesJoueurs;
    }

    @Override
    public void ajouterConcours(String name, int heure, int minutes) throws NomDeConcoursDejaExistantException, CaracteresSpeciauxException {
        for (char c : lesCharacteresInterdits())
            if (name.indexOf(c) != -1) throw new CaracteresSpeciauxException();

        if (concoursExistant(name)) throw new NomDeConcoursDejaExistantException();
        Concours concours = new Concours(name, heure, minutes);
        concours.addObserver(this);
        lesConcours.add(concours);
        setChanged();
        notifyObservers();

    }

    @Override
    public void retirerConcours(String name) {
        lesConcours.removeIf(concours -> concours.getNom().equals(name));
        setChanged();
        notifyObservers();


    }

    private boolean concoursExistant(String name) {
        if (!lesConcours.isEmpty()) {
            for (Concours concours : lesConcours) {
                if (concours.getNom().equals(name)) return true;
            }
        }
        return false;
    }


    @Override
    public void retirerJoueurInscrit(String name) throws AucunJoueurNePossedeCeNomException {
        if (!joueurExistant(name)) throw new AucunJoueurNePossedeCeNomException();
        lesJoueurs.removeIf(y -> y.getNom().equals(name));
        setChanged();
        notifyObservers();

    }

    @Override
    public void ajouterJoueurInscrit(String name) throws NomDeJoueurDejaExistantException, CaracteresSpeciauxException {
        for (char c : lesCharacteresInterdits())
            if (name.indexOf(c) != -1) throw new CaracteresSpeciauxException();
        if (joueurExistant(name)) throw new NomDeJoueurDejaExistantException();
        lesJoueurs.add(new Joueur(name));
        setChanged();
        notifyObservers();

    }

    @Override
    public JoueurInterface getJoueurByName(String name) throws AucunJoueurNePossedeCeNomException {
        JoueurInterface resJoueur = null;
        if (!joueurExistant(name)) throw new AucunJoueurNePossedeCeNomException();
        for (JoueurInterface joueur : lesJoueurs) {
            if (joueur.getNom().equals(name)) resJoueur = joueur;
        }
        return resJoueur;

    }

    @Override
    public Concours getConcoursByName(String name) {
        Concours concours = null;
        for (Concours conc : lesConcours) {
            if (conc.getNom().equals(name)) concours = conc;
        }
        return concours;

    }


    @Override
    public void creerEquipe(String joueur, String concours, String name) throws AucunJoueurNePossedeCeNomException, DejaDansUneEquipeException, CaracteresSpeciauxException {
        for (char c : lesCharacteresInterdits())
            if (name.indexOf(c) != -1) throw new CaracteresSpeciauxException();
        if (getJoueurByName(joueur).aUneEquipe()) throw new DejaDansUneEquipeException();
        getJoueurByName(joueur).creerEquipe(getConcoursByName(concours), name);
        setChanged();
        notifyObservers();

    }

    @Override
    public void rejoindreEquipe(String joueur, String equipe) throws AucunJoueurNePossedeCeNomException, EquipeCompleteException, DejaDansUneEquipeException {
        if (getJoueurByName(joueur).aUneEquipe()) throw new DejaDansUneEquipeException();
        getJoueurByName(joueur).rejoindreEquipe(getEquipeByName(equipe));
        setChanged();
        notifyObservers();

    }

    @Override
    public void quitterEquipe(String nomDuJoueur) {
        try {
            getJoueurByName(nomDuJoueur).quitterEquipe();
        } catch (AucunJoueurNePossedeCeNomException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public JoueurInterface getJoueurEnCoursDeTours(String nom, String tours, int id) {
        return getManche(nom, tours, id).joueurEnCours();
    }

    private List<Equipe> getToutesLesEquipes() {
        List<Equipe> toutesLesEquipes = new ArrayList<>();
        for (JoueurInterface joueur : lesJoueurs) {
            if (!toutesLesEquipes.contains(joueur.getEquipe()))
                toutesLesEquipes.add(joueur.getEquipe());
        }
        return toutesLesEquipes;
    }

    private Equipe getEquipeByName(String name) {
        Equipe equipe = null;
        for (Equipe eq : getToutesLesEquipes()) {
            if (name.equals(eq.getNomEquipe())) equipe = eq;
        }
        return equipe;
    }

    @Override
    public List<Manche> getLesManchesTours1(String nameConcours) {
        return getConcoursByName(nameConcours).getLesManchesTours1();
    }

    @Override
    public List<Manche> getLesManchesTours2(String nameConcours) {
        return getConcoursByName(nameConcours).getLesManchesTours2();
    }

    @Override
    public List<Manche> getLesManchesTours3(String nameConcours) {
        return getConcoursByName(nameConcours).getLesManchesTours3();
    }

    @Override
    public void lancerConcours(String nom) throws LeConcoursADejaEteLanceException, LeConcoursNeContientAucuneEquipeException, DejaDansUneEquipeException {

        try {
            for (JoueurInterface j : getConcoursByName(nom).getLesJoueursSansEquipe()) {
                JoueurInterface bot = laFabriqueDeBots.creerBot();
                creerEquipe(j.getNom(), nom, j.getNom() + " et " + bot.getNom());
                ajouterJoueurInscrit(bot.getNom());
                rejoindreEquipe(bot.getNom(), j.getNom() + " et " + bot.getNom());
            }
        } catch (NomDeJoueurDejaExistantException | AucunJoueurNePossedeCeNomException | EquipeCompleteException e) {
            e.printStackTrace();
        } catch (AucunJoueursNAPasDEquipeException | CaracteresSpeciauxException ignored) {
        }
        try {
            for (Equipe equipe : getConcoursByName(nom).getLesEquipesIncompletes()) {
                JoueurInterface bot = laFabriqueDeBots.creerBot();
                ajouterJoueurInscrit(bot.getNom());
                rejoindreEquipe(bot.getNom(), equipe.getNomEquipe());
            }
        } catch (NomDeJoueurDejaExistantException | AucunJoueurNePossedeCeNomException | EquipeCompleteException e) {
            e.printStackTrace();
        } catch (AucuneEquipeEstIncompleteException | CaracteresSpeciauxException ignored) {

        }
        try {
            getConcoursByName(nom).lancerConcours();
        } catch (LeConcoursContientUnNombreImpairDEquipe e) {
            ajouterEquipeBot(nom);
            try {
                getConcoursByName(nom).lancerConcours();
            } catch (LeConcoursContientUnNombreImpairDEquipe ignored) {
            }

        }

    }

    private void ajouterEquipeBot(String nom) {
        JoueurInterface bot1 = laFabriqueDeBots.creerBot();
        JoueurInterface bot2 = laFabriqueDeBots.creerBot();
        try {
            ajouterJoueurInscrit(bot1.getNom());
            ajouterJoueurInscrit(bot2.getNom());
            creerEquipe(bot1.getNom(), nom, bot1.getNom() + " et " + bot2.getNom());
            rejoindreEquipe(bot2.getNom(), bot1.getNom() + " et " + bot2.getNom());
        } catch (NomDeJoueurDejaExistantException | DejaDansUneEquipeException | AucunJoueurNePossedeCeNomException | EquipeCompleteException | CaracteresSpeciauxException ignored) {
        }
    }

    @Override
    public void connecterAdmin(String pseudo, String motDePasse) throws DonneesIncorrectesException {
        String pseudo1 = "admin";
        String mdp = "admin";
        if (!pseudo1.equals(pseudo) || !mdp.equals(motDePasse)) throw new DonneesIncorrectesException();
        setChanged();
        notifyObservers();


    }

    @Override
    public Manche getManche(String nameConcours, String tours, int id) {
        return getConcoursByName(nameConcours).getManche(tours, id);
    }

    private Pli getParty(String nameConcours, String tours, int id) {
        return getManche(nameConcours, tours, id).getPli();
    }

    @Override
    public Map<JoueurInterface, LaCarte> getCartePosee(String nameConcours, String tours, int id) {
        return getParty(nameConcours, tours, id).getLesCartesPoseesParJoueur();
    }

    @Override
    public boolean atoutPris(String nameConcours, String tours, int id) {
        return getManche(nameConcours, tours, id).isAtoutPris();
    }

    @Override
    public void refuserAtout(String nameConcours, String tours, int id) {
        getManche(nameConcours, tours, id).refus();

    }

    @Override
    public void accepterAtout(String nameConcours, String tours, int id, String nomJoueur) {
        getManche(nameConcours, tours, id).accepterAtout(nomJoueur);


    }

    @Override
    public void jouerCarte(String nameConcours, String tours, int id, String nomJoueur, String laCarte) throws CarteNonJouableException {
        Manche manche = getManche(nameConcours, tours, id);
        LaCarte carte = null;
        for (LaCarte y : manche.get(nomJoueur)) {
            if (y.toString().equals(laCarte)) {
                carte = y;
            }
        }

        try {
            getManche(nameConcours, tours, id).jouerCarte(carte, getJoueurByName(nomJoueur));
        } catch (AucunJoueurNePossedeCeNomException e) {
            e.printStackTrace();
        }

    }

    private List<Character> lesCharacteresInterdits() {
        return Arrays.asList('&', 'é', 'è', 'ç', 'à', '"', '\'', '(', ')', '=', '^', '$', 'ù', '*', '!', ':', ';', ',', '?', '.', '/', '§', '%', 'µ', '£', '¨', '+', '°', '²', '€', '¤', '}', ']', '@', '^', '\\', '`', '|', '[', '{', '#');

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
