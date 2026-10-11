package dk.skobutik.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class OrdreLinje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Ordre ordre;

    @ManyToOne(optional = false)
    private SkoVariant variant;

    private int antal;

    /** Prisen på købstidspunktet, så senere prisændringer ikke påvirker ordren. */
    private BigDecimal enhedspris;

    protected OrdreLinje() {
    }

    OrdreLinje(Ordre ordre, SkoVariant variant, int antal, BigDecimal enhedspris) {
        this.ordre = ordre;
        this.variant = variant;
        this.antal = antal;
        this.enhedspris = enhedspris;
    }

    public BigDecimal getLinjeTotal() {
        return enhedspris.multiply(BigDecimal.valueOf(antal));
    }

    public Long getId() { return id; }
    public Ordre getOrdre() { return ordre; }
    public SkoVariant getVariant() { return variant; }
    public int getAntal() { return antal; }
    public BigDecimal getEnhedspris() { return enhedspris; }
}
