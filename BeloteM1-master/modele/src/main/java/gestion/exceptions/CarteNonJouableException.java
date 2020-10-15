package gestion.exceptions;

public class CarteNonJouableException extends Exception {
    public CarteNonJouableException(String s) {
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return "Cette carte ne peut pas être jouée";
    }
}
