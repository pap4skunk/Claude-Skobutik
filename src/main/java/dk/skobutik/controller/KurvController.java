package dk.skobutik.controller;

import dk.skobutik.model.IkkeNokPaaLagerException;
import dk.skobutik.model.Ordre;
import dk.skobutik.service.KasseForm;
import dk.skobutik.service.Kurv;
import dk.skobutik.service.KurvLinje;
import dk.skobutik.service.KurvService;
import dk.skobutik.service.OrdreService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class KurvController {

    private final Kurv kurv;
    private final KurvService kurvService;
    private final OrdreService ordreService;

    public KurvController(Kurv kurv, KurvService kurvService, OrdreService ordreService) {
        this.kurv = kurv;
        this.kurvService = kurvService;
        this.ordreService = ordreService;
    }

    @GetMapping("/kurv")
    public String visKurv(Model model) {
        tilfoejKurvTilModel(model);
        return "kurv";
    }

    @PostMapping("/kurv/tilfoej")
    public String tilfoej(@RequestParam Long skoId,
                          @RequestParam(required = false) Long variantId,
                          @RequestParam(defaultValue = "1") int antal,
                          RedirectAttributes redirect) {
        if (variantId == null) {
            redirect.addFlashAttribute("fejl", "Vælg en størrelse");
            return "redirect:/sko/" + skoId;
        }
        try {
            kurvService.tilfoej(variantId, antal);
            redirect.addFlashAttribute("besked", "Lagt i kurven");
            return "redirect:/kurv";
        } catch (IkkeNokPaaLagerException e) {
            redirect.addFlashAttribute("fejl", e.getMessage());
            return "redirect:/sko/" + skoId;
        }
    }

    @PostMapping("/kurv/fjern/{variantId}")
    public String fjern(@PathVariable Long variantId) {
        kurvService.fjern(variantId);
        return "redirect:/kurv";
    }

    @GetMapping("/kasse")
    public String kasse(Model model) {
        if (kurv.erTom()) {
            return "redirect:/kurv";
        }
        model.addAttribute("kasseForm", new KasseForm());
        tilfoejKurvTilModel(model);
        return "kasse";
    }

    @PostMapping("/kasse")
    public String bestil(@Valid @ModelAttribute KasseForm kasseForm,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirect) {
        if (kurv.erTom()) {
            return "redirect:/kurv";
        }
        if (bindingResult.hasErrors()) {
            tilfoejKurvTilModel(model);
            return "kasse";
        }
        try {
            Ordre ordre = ordreService.opretOrdre(kasseForm, kurv.getVarer());
            kurv.toem();
            return "redirect:/ordre/" + ordre.getId();
        } catch (IkkeNokPaaLagerException e) {
            redirect.addFlashAttribute("fejl", e.getMessage());
            return "redirect:/kurv";
        }
    }

    @GetMapping("/ordre/{id}")
    public String kvittering(@PathVariable Long id, Model model) {
        model.addAttribute("ordre", ordreService.hent(id));
        return "kvittering";
    }

    private void tilfoejKurvTilModel(Model model) {
        List<KurvLinje> linjer = kurvService.linjer();
        model.addAttribute("linjer", linjer);
        model.addAttribute("total", kurvService.total(linjer));
    }
}
