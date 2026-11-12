import java.util.ArrayList;
import java.util.Arrays;

/**
 * Määrab kulu kategooria kaupmehe järgi.
 * Kaupmehed ja kategooriad on kaks paralleelset listi:
 * kaupmehed.get(i) kuulub kategooriasse kategooriad.get(i).
 */
public class Kategoriseerija {
    // Kategooriate nimekirjad (kõigile klassidele ühised, saab täiendada)
    public static final ArrayList<String> TULU_KATEGOORIAD =
            new ArrayList<>(Arrays.asList("Palk", "Investeerinud", "Lisatöö"));
    public static final ArrayList<String> KULU_KATEGOORIAD =
            new ArrayList<>(Arrays.asList("Söök ja jook", "Meelelahutus",
                    "Kommunaalid", "Laenud", "Auto/transport", "Riided", "Muu", "Tervis"));

    private ArrayList<String> kaupmehed;
    private ArrayList<String> kategooriad;

    public Kategoriseerija() {
        kaupmehed = new ArrayList<>(Arrays.asList("Rimi", "Selver", "Maxima", "COOP", "Apollo",
                "Elektrum", "Tartu Veevärk", "LHV"));
        kategooriad = new ArrayList<>(Arrays.asList("Söök ja jook", "Söök ja jook",
                "Söök ja jook", "Söök ja jook", "Meelelahutus", "Kommunaalid",
                "Kommunaalid", "Laenud"));
    }

    /** Lisab uue kaupmehe ja talle vastava kategooria (mõlemasse listi (kategooriad ja kaupmehed) korraga). */
    public void lisaKaupmees(String nimi, String kategooria) {
        kaupmehed.add(nimi);
        kategooriad.add(kategooria);
    }

    /** Tagastab kaupmehele vastava kategooria või "", kui kaupmeest ei leita/pole. */
    public String leiaKategooria(String kaupmees) {
        for (int i = 0; i < kaupmehed.size(); i++) {
            if (kaupmehed.get(i).equals(kaupmees)) {
                return kategooriad.get(i);
            }
        }
        return "";
    }

    /**
     * Tagastab kulu lõpliku kategooria:
     * - kui kasutaja valis kategooria, jääb see samaks;
     * - kui kategooria on tühi ja kaupmees on olemas, kasutatakse kaupmehe kategooriat;
     * - muidu tagastatakse tühi sõne.
     */
    public String kategoriseeri(String kaupmees, String olemasolev) {
        String olemas = olemasolev.trim();
        if (!olemas.isEmpty()) {
            return olemas;
        }
        String kaup = kaupmees.trim();
        if (kaup.isEmpty()) {
            return olemas;
        }
        return leiaKategooria(kaup);
    }

    public ArrayList<String> getKaupmehed() {
        return kaupmehed;
    }
}