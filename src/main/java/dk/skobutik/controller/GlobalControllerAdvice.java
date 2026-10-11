package dk.skobutik.controller;

import dk.skobutik.model.Kategori;
import dk.skobutik.service.Kurv;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

/** Data som alle sider skal bruge (fx antal i kurven i headeren) samt fejlsider. */
@ControllerAdvice
public class GlobalControllerAdvice {

    private final Kurv kurv;

    public GlobalControllerAdvice(Kurv kurv) {
        this.kurv = kurv;
    }

    @ModelAttribute("kurvAntal")
    public int kurvAntal() {
        return kurv.getAntalVarer();
    }

    @ModelAttribute("alleKategorier")
    public Kategori[] alleKategorier() {
        return Kategori.values();
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String ikkeFundet(NoSuchElementException e, Model model) {
        model.addAttribute("kurvAntal", kurv.getAntalVarer());
        model.addAttribute("besked", e.getMessage());
        return "fejl";
    }
}
