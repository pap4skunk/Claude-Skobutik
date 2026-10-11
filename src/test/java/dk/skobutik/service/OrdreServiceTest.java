package dk.skobutik.service;

import dk.skobutik.model.*;
import dk.skobutik.repository.SkoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrdreServiceTest {

    @Autowired
    private OrdreService ordreService;

    @Autowired
    private SkoRepository skoRepository;

    private SkoVariant str42;
    private SkoVariant str43;

    @BeforeEach
    void opret() {
        Sko sko = new Sko("Testsko", "Test", Kategori.HERRE, new BigDecimal("500.00"), "", "#000000");
        str42 = sko.tilfoejVariant(42, 3);
        str43 = sko.tilfoejVariant(43, 1);
        skoRepository.saveAndFlush(sko);
    }

    @Test
    void ordreTraekkerFraLagerOgBeregnerTotal() {
        Ordre ordre = ordreService.opretOrdre(kunde(), Map.of(str42.getId(), 2, str43.getId(), 1));

        assertThat(ordre.getId()).isNotNull();
        assertThat(ordre.getTotal()).isEqualByComparingTo("1500.00");
        assertThat(str42.getLager()).isEqualTo(1);
        assertThat(str43.getLager()).isZero();
    }

    @Test
    void forStortAntalAfvises() {
        assertThatThrownBy(() -> ordreService.opretOrdre(kunde(), Map.of(str43.getId(), 2)))
                .isInstanceOf(IkkeNokPaaLagerException.class);
    }

    @Test
    void annulleringLaeggerVarerneTilbagePaaLager() {
        Ordre ordre = ordreService.opretOrdre(kunde(), Map.of(str42.getId(), 2));

        ordreService.skiftStatus(ordre.getId(), OrdreStatus.ANNULLERET);

        assertThat(str42.getLager()).isEqualTo(3);
        assertThat(ordre.getStatus()).isEqualTo(OrdreStatus.ANNULLERET);
    }

    private static KasseForm kunde() {
        return new KasseForm("Mads", "mads@example.com", "Skovej 1, 2100 København Ø");
    }
}
