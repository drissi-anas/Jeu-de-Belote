package rmiService;

import facade.FacadeBelotte;
import facade.FacadeBelotteImpl;
import gestion.exceptions.*;
import gestion.gestionConcours.concours.Concours;
import gestion.gestionConcours.concours.Equipe;
import gestion.gestionConcours.joueur.JoueurInterface;
import gestion.gestionJeu.jeu.LaCarte;
import gestion.gestionJeu.jeu.Manche;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

@FieldDefaults(level = AccessLevel.PRIVATE)

public class Serveur implements MonService, Observer {
    final FacadeBelotte monModele;
    int indiceDeModificationDuModele = 0;

    private Serveur() {
        this.monModele = new FacadeBelotteImpl();
        ((FacadeBelotteImpl) monModele).addObserver(this);
    }

    public static void main(String[] args) {
        try {
            Registry rmiRegistry = LocateRegistry.createRegistry(9345);
            MonService rmiService = (MonService) UnicastRemoteObject
                    .exportObject(new Serveur(), 9345);
            rmiRegistry.bind("BelotteService", rmiService);
            System.out.println("++++++++++++++ Remote service bound ++++++++++++++++++++");
            Thread lancementDesConcours = new EtatConcours(rmiService);
            lancementDesConcours.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
    Un thread est lancé sur chaque controleur qui stocke la valeur de l'indice.
    Si il s'apperçoit qui la valeur stocké est diff de celle du serveur on lance l'actualisation de la page.
    Une adaptation du pattern Observateur.
    changement effectué a le role du setChanged(), et le thread s'occupe de la notification sur le Controleur
    permettant de savoir quand le Serveur a connu une modif.
    changementEffectue() est utiliser uniquement dans le update de la classe qui lui meme se lance quand le facade a
    un changement mais en respectant le Pattern Observateur.
     */
    private void changementEffectue() {
        this.indiceDeModificationDuModele++;
    }

    @Override
    public List<JoueurInterface> getLesInscrits() throws AucunJoueursNEstInscritException {
        return monModele.getLesInscrits();
    }

    @Override
    public JoueurInterface getJoueurByName(String name) throws AucunJoueurNePossedeCeNomException {
        return monModele.getJoueurByName(name);
    }

    @Override
    public void ajouterJoueurInscrit(String name) throws NomDeJoueurDejaExistantException, CaracteresSpeciauxException {
        monModele.ajouterJoueurInscrit(name);
        System.out.println(name + " s'est inscrit");
    }

    @Override
    public void retirerJoueurInscrit(String name) throws AucunJoueurNePossedeCeNomException {
        monModele.retirerJoueurInscrit(name);
        System.out.println(name + " s'est désinscrit");

    }

    @Override
    public void ajouterConcours(String name, int heure, int minutes) throws NomDeConcoursDejaExistantException, CaracteresSpeciauxException {
        monModele.ajouterConcours(name, heure, minutes);
        System.out.println("Le concours " + name + " a été ajouté");

    }

    @Override
    public void retirerConcours(String name) {
        monModele.retirerConcours(name);
        System.out.println("Le concours " + name + " a été retiré");
    }

    @Override
    public List<Equipe> getLesEquipesIncompletes(Concours concours) throws AucuneEquipeEstIncompleteException {
        return monModele.getLesEquipesIncompletes(concours);
    }

    @Override
    public List<Concours> getLesConcours() {
        return monModele.getLesConcours();
    }

    @Override
    public void rejoindreConcours(String nomDuConcours, String nomDuJoueur) throws AucunJoueurNePossedeCeNomException, JoueurParticipeDejaException, ConcoursDejaLanceException {
        monModele.rejoindreConcours(nomDuConcours, nomDuJoueur);
        System.out.println(nomDuJoueur + " a rejoint le concours " + nomDuConcours);

    }

    @Override
    public Concours getConcoursByName(String name) {
        return monModele.getConcoursByName(name);
    }

    @Override
    public void creerEquipe(String joueur, String concours, String name) throws AucunJoueurNePossedeCeNomException, DejaDansUneEquipeException, CaracteresSpeciauxException {
        monModele.creerEquipe(joueur, concours, name);
        System.out.println(joueur + " créé et rejoins l'equipe " + name + " dans le concours " + concours);

    }

    @Override
    public void rejoindreEquipe(String joueur, String equipe) throws AucunJoueurNePossedeCeNomException, EquipeCompleteException, DejaDansUneEquipeException {
        monModele.rejoindreEquipe(joueur, equipe);
        System.out.println(joueur + " rejoins l'equipe " + equipe);
    }

    @Override
    public void lancerConcours(String name) throws LeConcoursNeContientAucuneEquipeException, LeConcoursADejaEteLanceException, DejaDansUneEquipeException {
        monModele.lancerConcours(name);
        System.out.println("Le concours " + name + " a été lancé");

    }

    @Override
    public boolean atoutPris(String nameConcours, String tours, int id) {
        return monModele.atoutPris(nameConcours, tours, id);
    }

    @Override
    public void connecterAdmin(String pseudo, String motDePasse) throws DonneesIncorrectesException {
        monModele.connecterAdmin(pseudo, motDePasse);
    }

    @Override
    public Manche getManche(String nameConcours, String tours, int id) {
        return monModele.getManche(nameConcours, tours, id);
    }

    @Override
    public void accepterAtout(String nameConcours, String tours, int id, String nomJoueur) {
        monModele.accepterAtout(nameConcours, tours, id, nomJoueur);

    }

    @Override
    public Map<JoueurInterface, LaCarte> getCartePosee(String nameConcours, String tours, int id) {
        return monModele.getCartePosee(nameConcours, tours, id);
    }

    @Override
    public void jouerCarte(String nameConcours, String tours, int id, String nomJoueur, String laCarte) throws CarteNonJouableException {
        monModele.jouerCarte(nameConcours, tours, id, nomJoueur, laCarte);
    }

    @Override
    public void refuserAtout(String nameConcours, String tours, int id) {
        monModele.refuserAtout(nameConcours, tours, id);
        System.out.println("Atout refusé dans " + nameConcours + " " + tours + " " + id);
    }

    @Override
    public void quitterEquipe(String nomDuJoueur) {
        monModele.quitterEquipe(nomDuJoueur);
        System.out.println(nomDuJoueur + " a quitter son équipe");
    }

    @Override
    public JoueurInterface getJoueurEnCoursDeTours(String nom, String tours, int id) {
        return monModele.getJoueurEnCoursDeTours(nom, tours, id);
    }

    @Override
    public int getIndiceDeModificationDuModele() {
        return indiceDeModificationDuModele;
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
        changementEffectue();
    }

    //compteur entre date du concours et date actuelle
    static class EtatConcours extends Thread {

        EtatConcours(MonService rmiService) {
            super(
                    () -> {

                        while (!Thread.currentThread().isInterrupted()) {
                            try {
                                Thread.sleep(1000);
                                try {
                                    rmiService.getLesConcours().forEach(concours -> {

                                        int deltaHeures = concours.getHeure() - LocalDateTime.now().getHour();
                                        int deltaMin = concours.getMinutes() - LocalDateTime.now().getMinute();
                                        if (deltaMin < 0) {
                                            deltaMin = 60 + deltaMin;
                                            deltaHeures--;
                                        }
                                        if (deltaHeures < 0) {
                                            deltaHeures = 24 + deltaHeures;
                                        }

                                        if (deltaHeures == 0 && deltaMin == 0) {
                                            try {
                                                if (!concours.isEstLance())
                                                    rmiService.lancerConcours(concours.getNom());
                                            } catch (LeConcoursNeContientAucuneEquipeException e) {
                                                try {
                                                    rmiService.retirerConcours(concours.getNom());
                                                } catch (RemoteException e1) {
                                                    e1.printStackTrace();
                                                }
                                            } catch (LeConcoursADejaEteLanceException | DejaDansUneEquipeException | RemoteException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }

            );
        }
    }
}
