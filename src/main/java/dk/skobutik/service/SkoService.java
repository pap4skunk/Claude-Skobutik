package dk.skobutik.service;

import dk.skobutik.model.Kategori;
import dk.skobutik.model.Sko;
import dk.skobutik.repository.SkoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class SkoService {

    private final SkoRepository skoRepository;

    public SkoService(SkoRepository skoRepository) {
        this.skoRepository = skoRepository;
    }

    public List<Sko> soeg(Kategori kategori, String soeg) {
        String tekst = (soeg == null || soeg.isBlank()) ? null : soeg.trim();
        return skoRepository.soeg(kategori, tekst);
    }

    public Sko hent(Long id) {
        return skoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Sko findes ikke"));
    }

    @Transactional
    public Sko gem(Sko sko) {
        if (sko.getId() == null) {
            return skoRepository.save(sko);
        }
        // Opdater eksisterende sko men behold dens varianter
        Sko eksisterende = hent(sko.getId());
        eksisterende.setNavn(sko.getNavn());
        eksisterende.setMaerke(sko.getMaerke());
        eksisterende.setKategori(sko.getKategori());
        eksisterende.setPris(sko.getPris());
        eksisterende.setBeskrivelse(sko.getBeskrivelse());
        eksisterende.setFarve(sko.getFarve());
        return eksisterende;
    }

    /** Sætter lager pr. størrelse. Nye størrelser oprettes, eksisterende opdateres. */
    @Transactional
    public void opdaterLager(Long skoId, Map<Integer, Integer> lagerPrStoerrelse) {
        Sko sko = hent(skoId);
        lagerPrStoerrelse.forEach((stoerrelse, lager) -> sko.getVarianter().stream()
                .filter(v -> v.getStoerrelse() == stoerrelse)
                .findFirst()
                .ifPresentOrElse(v -> v.setLager(lager), () -> sko.tilfoejVariant(stoerrelse, lager)));
    }

    @Transactional
    public void slet(Long id) {
        skoRepository.deleteById(id);
        // Flush nu, så en evt. fremmednøglefejl (skoen indgår i ordrer) kastes her
        skoRepository.flush();
    }
}
