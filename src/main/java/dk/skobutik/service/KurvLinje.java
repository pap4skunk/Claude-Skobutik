package dk.skobutik.service;

import dk.skobutik.model.SkoVariant;

import java.math.BigDecimal;

public record KurvLinje(SkoVariant variant, int antal) {

    public BigDecimal linjeTotal() {
        return variant.getSko().getPris().multiply(BigDecimal.valueOf(antal));
    }
}
