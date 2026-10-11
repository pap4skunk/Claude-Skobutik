package dk.skobutik.controller;

import dk.skobutik.model.Kategori;
import dk.skobutik.service.SkoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ButikController {

    private final SkoService skoService;

    public ButikController(SkoService skoService) {
        this.skoService = skoService;
    }

    @GetMapping("/")
    public String forside(@RequestParam(required = false) Kategori kategori,
                          @RequestParam(required = false) String soeg,
                          Model model) {
        model.addAttribute("sko", skoService.soeg(kategori, soeg));
        model.addAttribute("valgtKategori", kategori);
        model.addAttribute("soeg", soeg);
        return "index";
    }

    @GetMapping("/sko/{id}")
    public String visSko(@PathVariable Long id, Model model) {
        model.addAttribute("sko", skoService.hent(id));
        return "sko";
    }
}
