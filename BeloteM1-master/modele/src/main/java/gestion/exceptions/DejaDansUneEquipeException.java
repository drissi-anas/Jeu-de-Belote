package gestion.exceptions;

public class DejaDansUneEquipeException extends Exception {
    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return "Vous êtes deja dans une équipe";
    }
}
