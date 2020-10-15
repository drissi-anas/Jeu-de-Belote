package controleur.exception;

public class InscriptionFermeeException extends Exception {
    private final int deltaHeures;
    private final int deltaMin;

    public InscriptionFermeeException(int deltaHeures, int deltaMin) {
        this.deltaHeures = deltaHeures;
        this.deltaMin = deltaMin;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return "le concours n'est pas encore ouvert. Patientez " + deltaHeures + " heure(s) et " + deltaMin + " minute(s) svp";
    }
}
