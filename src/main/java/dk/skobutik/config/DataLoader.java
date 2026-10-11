package dk.skobutik.config;

import dk.skobutik.model.Kategori;
import dk.skobutik.model.Sko;
import dk.skobutik.repository.SkoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/** Fylder databasen med eksempelsko ved opstart. */
@Component
public class DataLoader implements CommandLineRunner {

    private final SkoRepository skoRepository;

    public DataLoader(SkoRepository skoRepository) {
        this.skoRepository = skoRepository;
    }

    @Override
    public void run(String... args) {
        if (skoRepository.count() > 0) {
            return;
        }
        skoRepository.saveAll(List.of(
                sko("Air Runner 2", "Nordstep", Kategori.SPORT, "899.95", "Let løbesko med god dæmpning til lange ture.", "#2563eb", 39, 46),
                sko("Trail Pro", "Nordstep", Kategori.SPORT, "1199.00", "Robust trailsko med kraftigt greb i alt terræn.", "#16a34a", 40, 46),
                sko("Klassisk Derby", "Holm & Søn", Kategori.HERRE, "1499.00", "Håndsyet derby i blødt kalveskind.", "#78350f", 40, 46),
                sko("Chelsea Boot", "Holm & Søn", Kategori.HERRE, "1299.00", "Tidløs støvle med elastik i siderne.", "#1f2937", 40, 45),
                sko("Ballerina Soft", "Lumi", Kategori.DAME, "549.00", "Fleksibel ballerina med polstret sål.", "#db2777", 36, 41),
                sko("Ankelstøvle Nora", "Lumi", Kategori.DAME, "999.00", "Varm ankelstøvle med lille hæl.", "#7c2d12", 36, 42),
                sko("Sneaker Hvid", "Basic Co.", Kategori.DAME, "699.00", "Minimalistisk hvid sneaker i læder.", "#d1d5db", 36, 42),
                sko("Gummistøvle Frø", "Pjok", Kategori.BOERN, "249.00", "Vandtæt gummistøvle med sjovt frømotiv.", "#65a30d", 24, 33),
                sko("Lyssko Blink", "Pjok", Kategori.BOERN, "399.00", "Sneaker med lys i sålen når man går.", "#9333ea", 25, 34)
        ));
    }

    private static Sko sko(String navn, String maerke, Kategori kategori, String pris, String beskrivelse,
                           String farve, int fraStr, int tilStr) {
        Sko sko = new Sko(navn, maerke, kategori, new BigDecimal(pris), beskrivelse, farve);
        for (int str = fraStr; str <= tilStr; str++) {
            // Varierende lager, så nogle størrelser er udsolgt
            sko.tilfoejVariant(str, (str * 7 + navn.length()) % 6);
        }
        return sko;
    }
}
