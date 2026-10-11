package dk.skobutik.service;

import dk.skobutik.model.IkkeNokPaaLagerException;
import dk.skobutik.model.SkoVariant;
import dk.skobutik.repository.SkoVariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class KurvService {

    private final Kurv kurv;
    private final SkoVariantRepository variantRepository;

    public KurvService(Kurv kurv, SkoVariantRepository variantRepository) {
        this.kurv = kurv;
        this.variantRepository = variantRepository;
    }

    public void tilfoej(Long variantId, int antal) {
        SkoVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new NoSuchElementException("Ukendt størrelse"));
        int nytAntal = kurv.antalAf(variantId) + antal;
        if (nytAntal > variant.getLager()) {
            throw new IkkeNokPaaLagerException(variant.getSko().getNavn() + " str. " + variant.getStoerrelse());
        }
        kurv.saetAntal(variantId, nytAntal);
    }

    public void fjern(Long variantId) {
        kurv.fjern(variantId);
    }

    public List<KurvLinje> linjer() {
        List<KurvLinje> linjer = new ArrayList<>();
        kurv.getVarer().forEach((variantId, antal) ->
                variantRepository.findById(variantId).ifPresent(v -> linjer.add(new KurvLinje(v, antal))));
        return linjer;
    }

    public BigDecimal total(List<KurvLinje> linjer) {
        return linjer.stream().map(KurvLinje::linjeTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
