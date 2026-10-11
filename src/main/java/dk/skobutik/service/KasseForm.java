package dk.skobutik.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Kundeoplysninger fra kasse-formularen. */
public class KasseForm {

    @NotBlank(message = "Navn skal udfyldes")
    private String navn;

    @NotBlank(message = "Email skal udfyldes")
    @Email(message = "Ugyldig email")
    private String email;

    @NotBlank(message = "Adresse skal udfyldes")
    private String adresse;

    public KasseForm() {
    }

    public KasseForm(String navn, String email, String adresse) {
        this.navn = navn;
        this.email = email;
        this.adresse = adresse;
    }

    public String getNavn() { return navn; }
    public void setNavn(String navn) { this.navn = navn; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
}
