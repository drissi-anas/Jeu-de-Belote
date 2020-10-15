package gestion.gestionJeu.jeu;

public class Pair<T, T1> {
    private final T key;
    private final T1 value;

    public Pair(T key, T1 value) {
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public T1 getValue() {
        return value;
    }

}
