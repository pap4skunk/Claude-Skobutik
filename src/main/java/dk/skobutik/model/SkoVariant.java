package dk.skobutik.model;

import jakarta.persistence.*;

/** En bestemt størrelse af en sko med eget lagerantal. */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"sko_id", "stoerrelse"}))
public class SkoVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Sko sko;

    private int stoerrelse;

    private int lager;

    protected SkoVariant() {
    }

    public SkoVariant(Sko sko, int stoerrelse, int lager) {
        this.sko = sko;
        this.stoerrelse = stoerrelse;
        this.lager = lager;
    }

    public boolean erPaaLager() {
        return lager > 0;
    }

    public void traekFraLager(int antal) {
        if (antal > lager) {
            throw new IkkeNokPaaLagerException(sko.getNavn() + " str. " + stoerrelse + " (kun " + lager + " tilbage)");
        }
        lager -= antal;
    }

    public Long getId() { return id; }
    public Sko getSko() { return sko; }
    public int getStoerrelse() { return stoerrelse; }
    public int getLager() { return lager; }
    public void setLager(int lager) { this.lager = lager; }
}
