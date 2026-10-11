package dk.skobutik.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
public class Sko {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Navn skal udfyldes")
    private String navn;

    @NotBlank(message = "Mærke skal udfyldes")
    private String maerke;

    @NotNull(message = "Vælg en kategori")
    @Enumerated(EnumType.STRING)
    private Kategori kategori;

    @NotNull(message = "Pris skal udfyldes")
    @DecimalMin(value = "0.0", inclusive = false, message = "Pris skal være større end 0")
    private BigDecimal pris;

    @Size(max = 1000)
    @Column(length = 1000)
    private String beskrivelse;

    private String farve;

    @OneToMany(mappedBy = "sko", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SkoVariant> varianter = new ArrayList<>();

    public Sko() {
    }

    public Sko(String navn, String maerke, Kategori kategori, BigDecimal pris, String beskrivelse, String farve) {
        this.navn = navn;
        this.maerke = maerke;
        this.kategori = kategori;
        this.pris = pris;
        this.beskrivelse = beskrivelse;
        this.farve = farve;
    }

    public SkoVariant tilfoejVariant(int stoerrelse, int lager) {
        SkoVariant variant = new SkoVariant(this, stoerrelse, lager);
        varianter.add(variant);
        return variant;
    }

    public List<SkoVariant> getVarianterSorteret() {
        return varianter.stream().sorted(Comparator.comparingInt(SkoVariant::getStoerrelse)).toList();
    }

    public int getSamletLager() {
        return varianter.stream().mapToInt(SkoVariant::getLager).sum();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNavn() { return navn; }
    public void setNavn(String navn) { this.navn = navn; }
    public String getMaerke() { return maerke; }
    public void setMaerke(String maerke) { this.maerke = maerke; }
    public Kategori getKategori() { return kategori; }
    public void setKategori(Kategori kategori) { this.kategori = kategori; }
    public BigDecimal getPris() { return pris; }
    public void setPris(BigDecimal pris) { this.pris = pris; }
    public String getBeskrivelse() { return beskrivelse; }
    public void setBeskrivelse(String beskrivelse) { this.beskrivelse = beskrivelse; }
    public String getFarve() { return farve; }
    public void setFarve(String farve) { this.farve = farve; }
    public List<SkoVariant> getVarianter() { return varianter; }
}
