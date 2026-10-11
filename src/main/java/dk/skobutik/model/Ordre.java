package dk.skobutik.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ordre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kundeNavn;
    private String email;
    private String adresse;
    private LocalDateTime oprettet = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private OrdreStatus status = OrdreStatus.MODTAGET;

    @OneToMany(mappedBy = "ordre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdreLinje> linjer = new ArrayList<>();

    protected Ordre() {
    }

    public Ordre(String kundeNavn, String email, String adresse) {
        this.kundeNavn = kundeNavn;
        this.email = email;
        this.adresse = adresse;
    }

    public void tilfoejLinje(SkoVariant variant, int antal) {
        linjer.add(new OrdreLinje(this, variant, antal, variant.getSko().getPris()));
    }

    public BigDecimal getTotal() {
        return linjer.stream().map(OrdreLinje::getLinjeTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getAntalVarer() {
        return linjer.stream().mapToInt(OrdreLinje::getAntal).sum();
    }

    public Long getId() { return id; }
    public String getKundeNavn() { return kundeNavn; }
    public String getEmail() { return email; }
    public String getAdresse() { return adresse; }
    public LocalDateTime getOprettet() { return oprettet; }
    public OrdreStatus getStatus() { return status; }
    public void setStatus(OrdreStatus status) { this.status = status; }
    public List<OrdreLinje> getLinjer() { return linjer; }
}
