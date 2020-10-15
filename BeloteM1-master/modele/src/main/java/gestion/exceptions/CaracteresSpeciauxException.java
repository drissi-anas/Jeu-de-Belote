package gestion.exceptions;

public class CaracteresSpeciauxException extends Exception {
    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return "Les caractères &éèçà\"\'()=^$ù*!:;,?./§%µ£¨+°²€¤}]@^\\`|[{#\"  ne sont pas acceptés";
    }
}
