package dk.skobutik.model;

public class IkkeNokPaaLagerException extends RuntimeException {

    public IkkeNokPaaLagerException(String vare) {
        super("Ikke nok på lager: " + vare);
    }
}
