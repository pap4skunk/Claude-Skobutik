package dk.skobutik.service;

import dk.skobutik.model.Ordre;
import dk.skobutik.model.OrdreStatus;
import dk.skobutik.model.SkoVariant;
import dk.skobutik.repository.OrdreRepository;
import dk.skobutik.repository.SkoVariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class OrdreService {

    private final OrdreRepository ordreRepository;
    private final SkoVariantRepository variantRepository;

    public OrdreService(OrdreRepository ordreRepository, SkoVariantRepository variantRepository) {
        this.ordreRepository = ordreRepository;
        this.variantRepository = variantRepository;
    }

    /**
     * Opretter en ordre ud fra kurvens indhold og trækker varerne fra lageret.
     * Er der ikke nok på lager af én vare, rulles hele ordren tilbage.
     */
    @Transactional
    public Ordre opretOrdre(KasseForm kunde, Map<Long, Integer> varer) {
        if (varer.isEmpty()) {
            throw new IllegalStateException("Kurven er tom");
        }
        Ordre ordre = new Ordre(kunde.getNavn(), kunde.getEmail(), kunde.getAdresse());
        varer.forEach((variantId, antal) -> {
            SkoVariant variant = variantRepository.findById(variantId)
                    .orElseThrow(() -> new NoSuchElementException("Ukendt vare: " + variantId));
            variant.traekFraLager(antal);
            ordre.tilfoejLinje(variant, antal);
        });
        return ordreRepository.save(ordre);
    }

    @Transactional(readOnly = true)
    public Ordre hent(Long id) {
        return ordreRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Ordre findes ikke"));
    }

    @Transactional(readOnly = true)
    public List<Ordre> alle() {
        return ordreRepository.findAllByOrderByOprettetDesc();
    }

    @Transactional
    public void skiftStatus(Long id, OrdreStatus status) {
        Ordre ordre = hent(id);
        if (status == OrdreStatus.ANNULLERET && ordre.getStatus() != OrdreStatus.ANNULLERET) {
            ordre.getLinjer().forEach(l -> l.getVariant().setLager(l.getVariant().getLager() + l.getAntal()));
        }
        ordre.setStatus(status);
    }
}
