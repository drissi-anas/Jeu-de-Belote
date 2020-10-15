package application;

import controleur.Controleur;
import javafx.application.Application;
import javafx.stage.Stage;
import rmiService.MonService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client extends Application {

    private static final String HOST = "127.0.0.1";//Mettre l'adresse du serveur.


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Registry registry = LocateRegistry.getRegistry(HOST, 9345);
            String[] names = registry.list();
            for (String name1 : names) {
                System.out.println("~~~~" + name1 + "~~~~");
            }
            MonService serv = (MonService) registry.lookup(MonService.serviceName);
            new Controleur(primaryStage, serv);
        } catch (Exception e) {
            System.err.println("Aucun serveur n'a été trouvé");

        }

    }
}
