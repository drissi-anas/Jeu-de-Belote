package gestion.exceptions;

public class LeConcoursContientUnNombreImpairDEquipe extends Exception {
    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return "Le Concours a déja été lancé";
    }

}
