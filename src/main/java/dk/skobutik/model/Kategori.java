package dk.skobutik.model;

public enum Kategori {
    HERRE("Herre"),
    DAME("Dame"),
    BOERN("Børn"),
    SPORT("Sport");

    private final String visningsnavn;

    Kategori(String visningsnavn) {
        this.visningsnavn = visningsnavn;
    }

    public String getVisningsnavn() {
        return visningsnavn;
    }
}
