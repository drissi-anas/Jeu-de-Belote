package controleur;

interface AdminInterface {

    void seDeconnecter();

    void connecterAdmin(String pseudo, String motDePasse);

    void ajouterConcours(String name, int heures, int minutes);

    void retirerConcours(String name);
}
