package dk.skobutik.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Indkøbskurven for den aktuelle bruger. Gemmer variant-id og antal i sessionen. */
@Component
@SessionScope
public class Kurv implements Serializable {

    private final Map<Long, Integer> varer = new LinkedHashMap<>();

    public void saetAntal(Long variantId, int antal) {
        if (antal <= 0) {
            varer.remove(variantId);
        } else {
            varer.put(variantId, antal);
        }
    }

    public int antalAf(Long variantId) {
        return varer.getOrDefault(variantId, 0);
    }

    public void fjern(Long variantId) {
        varer.remove(variantId);
    }

    public void toem() {
        varer.clear();
    }

    public boolean erTom() {
        return varer.isEmpty();
    }

    public int getAntalVarer() {
        return varer.values().stream().mapToInt(Integer::intValue).sum();
    }

    public Map<Long, Integer> getVarer() {
        return Collections.unmodifiableMap(varer);
    }
}
