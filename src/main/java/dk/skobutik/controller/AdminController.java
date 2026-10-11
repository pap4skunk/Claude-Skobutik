package dk.skobutik.controller;

import dk.skobutik.model.Kategori;
import dk.skobutik.model.OrdreStatus;
import dk.skobutik.model.Sko;
import dk.skobutik.service.OrdreService;
import dk.skobutik.service.SkoService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String LAGER_PREFIX = "lager_";

    private final SkoService skoService;
    private final OrdreService ordreService;

    public AdminController(SkoService skoService, OrdreService ordreService) {
        this.skoService = skoService;
        this.ordreService = ordreService;
    }

    @ModelAttribute("kategorier")
    public Kategori[] kategorier() {
        return Kategori.values();
    }

    @GetMapping
    public String admin() {
        return "redirect:/admin/sko";
    }

    @GetMapping("/sko")
    public String skoListe(Model model) {
        model.addAttribute("skoListe", skoService.soeg(null, null));
        return "admin/sko-liste";
    }

    @GetMapping("/sko/ny")
    public String nySko(Model model) {
        model.addAttribute("sko", new Sko());
        return "admin/sko-form";
    }

    @GetMapping("/sko/{id}/rediger")
    public String redigerSko(@PathVariable Long id, Model model) {
        model.addAttribute("sko", skoService.hent(id));
        return "admin/sko-form";
    }

    @PostMapping("/sko")
    public String gemSko(@Valid @ModelAttribute("sko") Sko sko, BindingResult bindingResult,
                         RedirectAttributes redirect) {
        if (bindingResult.hasErrors()) {
            return "admin/sko-form";
        }
        Sko gemt = skoService.gem(sko);
        redirect.addFlashAttribute("besked", "Skoen er gemt");
        return "redirect:/admin/sko/" + gemt.getId() + "/rediger";
    }

    /** Modtager felter som lager_42=5 samt evt. nyStoerrelse/nyLager. */
    @PostMapping("/sko/{id}/lager")
    public String opdaterLager(@PathVariable Long id,
                              @RequestParam Map<String, String> felter,
                              RedirectAttributes redirect) {
        Map<Integer, Integer> lager = new HashMap<>();
        try {
            felter.forEach((navn, vaerdi) -> {
                if (navn.startsWith(LAGER_PREFIX) && !vaerdi.isBlank()) {
                    lager.put(Integer.parseInt(navn.substring(LAGER_PREFIX.length())), ikkeNegativ(vaerdi));
                }
            });
            String nyStoerrelse = felter.get("nyStoerrelse");
            if (nyStoerrelse != null && !nyStoerrelse.isBlank()) {
                lager.put(Integer.parseInt(nyStoerrelse.trim()), ikkeNegativ(felter.getOrDefault("nyLager", "0")));
            }
        } catch (NumberFormatException e) {
            redirect.addFlashAttribute("fejl", "Lager og størrelse skal være hele tal (0 eller derover)");
            return "redirect:/admin/sko/" + id + "/rediger";
        }
        skoService.opdaterLager(id, lager);
        redirect.addFlashAttribute("besked", "Lageret er opdateret");
        return "redirect:/admin/sko/" + id + "/rediger";
    }

    @PostMapping("/sko/{id}/slet")
    public String sletSko(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            skoService.slet(id);
            redirect.addFlashAttribute("besked", "Skoen er slettet");
        } catch (DataIntegrityViolationException e) {
            redirect.addFlashAttribute("fejl", "Skoen kan ikke slettes, fordi den indgår i ordrer. Sæt lageret til 0 i stedet.");
        }
        return "redirect:/admin/sko";
    }

    @GetMapping("/ordrer")
    public String ordrer(Model model) {
        model.addAttribute("ordrer", ordreService.alle());
        model.addAttribute("statusser", OrdreStatus.values());
        return "admin/ordrer";
    }

    @PostMapping("/ordrer/{id}/status")
    public String skiftStatus(@PathVariable Long id, @RequestParam OrdreStatus status) {
        ordreService.skiftStatus(id, status);
        return "redirect:/admin/ordrer";
    }

    private static int ikkeNegativ(String vaerdi) {
        int tal = Integer.parseInt(vaerdi.trim());
        if (tal < 0) {
            throw new NumberFormatException();
        }
        return tal;
    }
}
