package dk.skobutik.controller;

import dk.skobutik.model.Kategori;
import dk.skobutik.model.Sko;
import dk.skobutik.service.SkoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

/** JSON-API til fx en separat frontend eller mobilapp. */
@RestController
@RequestMapping("/api/sko")
public class SkoApiController {

    private final SkoService skoService;

    public SkoApiController(SkoService skoService) {
        this.skoService = skoService;
    }

    @GetMapping
    public List<SkoDto> alle(@RequestParam(required = false) Kategori kategori,
                             @RequestParam(required = false) String soeg) {
        return skoService.soeg(kategori, soeg).stream().map(SkoDto::fra).toList();
    }

    @GetMapping("/{id}")
    public SkoDto en(@PathVariable Long id) {
        return SkoDto.fra(skoService.hent(id));
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public FejlDto ikkeFundet(NoSuchElementException e) {
        return new FejlDto(e.getMessage());
    }

    public record SkoDto(Long id, String navn, String maerke, Kategori kategori, BigDecimal pris,
                         String beskrivelse, String farve, List<VariantDto> stoerrelser) {
        static SkoDto fra(Sko s) {
            return new SkoDto(s.getId(), s.getNavn(), s.getMaerke(), s.getKategori(), s.getPris(),
                    s.getBeskrivelse(), s.getFarve(),
                    s.getVarianterSorteret().stream()
                            .map(v -> new VariantDto(v.getId(), v.getStoerrelse(), v.getLager()))
                            .toList());
        }
    }

    public record VariantDto(Long id, int stoerrelse, int lager) {
    }

    public record FejlDto(String fejl) {
    }
}
