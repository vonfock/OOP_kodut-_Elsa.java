import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

/**
 * ELSA 1. versioon: kulude ja sissetulekute jälgija (tekstiliides, andmed ainult mälus).
 * Peaklass: näitab menüüd ja suhtleb kasutajaga.
 */
public class Elsa {

    public static void main(String[] args) {
        Sisend sisend = new Sisend(new Scanner(System.in));
        Kategoriseerija kategoriseerija = new Kategoriseerija();
        Kirjepidaja pidaja = new Kirjepidaja();

        prindiTutvustus();

        int valik;
        do {
            prindiMenüü();
            valik = sisend.küsiTäisarv("Vali tegevuse number: ");
            System.out.println();

            switch (valik) {
                case 1:
                    lisaTulu(sisend, pidaja);
                    break;
                case 2:
                    lisaKulu(sisend, pidaja, kategoriseerija);
                    break;
                case 3:
                    näitaKõiki(pidaja);
                    break;
                case 4:
                    näitaKokkuvõtet(pidaja.getKirjed(), pidaja);
                    break;
                case 5:
                    filtreeriJaNäita(sisend, pidaja);
                    break;
                case 6:
                    genereeriTestkirjed(sisend, pidaja, kategoriseerija);
                    break;
                case 7:
                    näitaSaldot(pidaja);
                    break;
                case 0:
                    System.out.println("Head aega!");
                    break;
                default:
                    System.out.println("Sellist valikut pole.");
            }
        } while (valik != 0);
    }

    // Teated ja menüü

    private static void prindiTutvustus() {
        System.out.println("ELSA: kulude ja sissetulekute jälgija");
        System.out.println("Siia saad sisestada oma tulud ja kulud, kuvada neid kategooriate kaupa");
        System.out.println("ja arvutada kulude/tulude saldot. Andmed hoitakse ainult programmi töö ajal.");
        System.out.println("Lisaks saad võrrelda perioodide kaupa enda tulusid ja kulusid");
        System.out.println("Kuupäev sisesta kujul AAAA-KK-PP (nt 2026-11-08), summa double tüüp arvuna nt 13.02 või 13,02.");
        System.out.println("Kuupäev saab olla vahemikus 1900 kuni 2100, päevad vahemikus 1-31 ja kuu vahemikus 1-12");
        System.out.println("Testimiseks saad genereerida juhuslikke kirjeid (valik 6).");
        System.out.println();
    }

    private static void prindiMenüü() {
        System.out.println();
        System.out.println("1  Lisa sissetulek");
        System.out.println("2  Lisa väljaminek");
        System.out.println("3  Näita kõiki olemasolevaid kirjeid");
        System.out.println("4  Kokkuvõte kategooriate kaupa");
        System.out.println("5  Filtreeri tüübi ja kuupäevavahemiku järgi");
        System.out.println("6  Genereeri juhuslikud testkirjed");
        System.out.println("7  Näita saldot");
        System.out.println("0  Lõpeta");
    }


    // ---------- Menüüvalikud ----------

    private static void lisaTulu(Sisend sisend, Kirjepidaja pidaja) {
        String kuupäev = sisend.küsiKuupäev();
        double summa = sisend.küsiSumma();
        String kategooria = sisend.valiNimekirjast("Sissetuleku allikas:",
                Kategoriseerija.TULU_KATEGOORIAD, false, true);

        pidaja.lisa(new Kirje(kuupäev, summa, "Tulu", kategooria));
        System.out.println("Sissetulek lisatud.");
    }

    private static void lisaKulu(Sisend sisend, Kirjepidaja pidaja, Kategoriseerija kategoriseerija) {
        String kuupäev = sisend.küsiKuupäev();
        double summa = sisend.küsiSumma();
        String kaupmees = valiKaupmees(sisend, kategoriseerija);
        String kategooria = sisend.valiNimekirjast("Kulu kategooria:",
                Kategoriseerija.KULU_KATEGOORIAD, true, true);

        if (kaupmees.isEmpty() && kategooria.isEmpty()) {
            System.out.println("Sisesta kaupmees või kategooria. Kirjet ei lisatud.");
            return;
        }

        String kirjeldus = sisend.küsiTekst("Lühikirjeldus (võib tühjaks jätta): ");

        // Kategooria määratakse enne kirje loomist
        String lõplik = kategoriseerija.kategoriseeri(kaupmees, kategooria);

        pidaja.lisa(new Kirje(kuupäev, summa, "Kulu", lõplik, kaupmees, kirjeldus));
        System.out.println("Kulu lisatud (kategooria: "
                + (lõplik.isEmpty() ? "Määramata" : lõplik) + ").");
    }

    /** Valib kaupmehe nimekirjast või lisab uue (koos kategooriaga). 0 tähendab "jäta tühjaks". */
    private static String valiKaupmees(Sisend sisend, Kategoriseerija kategoriseerija) {
        ArrayList<String> kaupmehed = kategoriseerija.getKaupmehed();
        System.out.println("Kaupmees:");
        System.out.println("  0 - jäta tühjaks");
        for (int i = 0; i < kaupmehed.size(); i++) {
            System.out.println("  " + (i + 1) + " - " + kaupmehed.get(i));
        }
        System.out.println("  " + (kaupmehed.size() + 1) + " - lisa uus");

        int valik = sisend.küsiVahemikus("Valik: ", 0, kaupmehed.size() + 1);
        if (valik == 0) {
            return "";
        }
        if (valik == kaupmehed.size() + 1) {
            String nimi = sisend.küsiUusNimi(kaupmehed);
            String kategooria = sisend.valiNimekirjast("Selle kaupmehe kategooria:",
                    Kategoriseerija.KULU_KATEGOORIAD, false, true);
            kategoriseerija.lisaKaupmees(nimi, kategooria);
            return nimi;
        }
        return kaupmehed.get(valik - 1);
    }

    // Näitab kõiki olemasolevaid ridu
    private static void näitaKõiki(Kirjepidaja pidaja) {
        if (pidaja.kirjeteArv() == 0) {
            System.out.println("Kirjeid pole veel lisatud.");
            return;
        }
        prindiRead(pidaja.getKirjed());
    }
    // Prindib kõik read välja, mis sisestatud on
    private static void prindiRead(ArrayList<Kirje> list) {
        System.out.printf("%-10s %-6s %10s  %-15s %-14s %s%n", "Kuupäev", "Tüüp", "Summa",
                "Kategooria", "Kaupmees", "Kirjeldus");
        for (Kirje k : list) {
            k.prindiRida();
        }
        System.out.println("Kirjeid: " + list.size());
    }

    /** Küsib, mitu juhuslikku testkirjet luua, ja lisab need kirjepidajasse. */
    private static void genereeriTestkirjed(Sisend sisend, Kirjepidaja pidaja,
                                            Kategoriseerija kategoriseerija) {
        int arv = sisend.küsiTäisarv("Mitu juhuslikku kirjet soovid genereerida? ");
        pidaja.genereeriJuhuslikud(arv, kategoriseerija);
        System.out.println("Lisatud " + arv + " juhuslikku kirjet.");
    }

    // näitab tulu ja kulu kokkuvõtet
    private static void näitaKokkuvõtet(ArrayList<Kirje> list, Kirjepidaja pidaja) {
        System.out.println("--- Kulud kategooriate kaupa ---");
        pidaja.prindiKokkuvõte(list, "Kulu");
        System.out.println();
        System.out.println("--- Tulud kategooriate kaupa ---");
        pidaja.prindiKokkuvõte(list, "Tulu");
    }

    /** Filtreerib kirjed tüübi ja kuupäevavahemiku järgi ning näitab need. */
    private static void filtreeriJaNäita(Sisend sisend, Kirjepidaja pidaja) {
        ArrayList<String> tüübid = new ArrayList<>(Arrays.asList("Kulu", "Tulu"));
        String tüüp = sisend.valiNimekirjast("Tüüp:", tüübid, true, false);
        System.out.println("Alates:");
        String alates = sisend.küsiKuupäev();
        System.out.println("Kuni:");
        String kuni = sisend.küsiKuupäev();

        ArrayList<Kirje> tulemus = pidaja.filtreeri(tüüp, alates, kuni);
        if (tulemus.isEmpty()) {
            System.out.println("Filtrile ei vasta ükski kirje.");
            return;
        }
        prindiRead(tulemus);
        System.out.println();
        näitaKokkuvõtet(tulemus, pidaja);
    }

    /** Prindib saldo (tulud miinus kulud). */
    private static void näitaSaldot(Kirjepidaja pidaja) {
        System.out.printf("Saldo (tulud miinus kulud): %.2f%n", pidaja.saldo());
    }
}
