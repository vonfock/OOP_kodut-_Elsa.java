import java.util.ArrayList;

/**
 * Hoiab kõiki kirjeid ja arvutab nende põhjal summasid, filtreid ja kokkuvõtteid.
 */
public class Kirjepidaja {
    private ArrayList<Kirje> kirjed;

    public Kirjepidaja() {
        kirjed = new ArrayList<>();
    }

    public void lisa(Kirje kirje) {
        kirjed.add(kirje);
    }

    public int kirjeteArv() {
        return kirjed.size();
    }

    public Kirje getKirje(int indeks) {
        return kirjed.get(indeks);
    }

    public ArrayList<Kirje> getKirjed() {
        return kirjed;
    }

    /** Kõigi selle tüübi ("Tulu" või "Kulu") kirjete summa. */
    public double summa(String tüüp) {
        return summa(kirjed, tüüp);
    }

    /** Sama, mis eelmine, aga suvalise (nt filtreeritud) listi jaoks. */
    public static double summa(ArrayList<Kirje> list, String tüüp) {
        double kokku = 0;
        for (Kirje k : list) {
            if (k.getTüüp().equals(tüüp)) {
                kokku += k.getSumma();
            }
        }
        return kokku;
    }

    /** Tulud miinus kulud. */
    public double saldo() {
        return summa("Tulu") - summa("Kulu");
    }

    /**
     * Tagastab uue listi kirjetega, mis vastavad filtrile.
     * tüüp "" tähendab kõiki tüüpe; alates ja kuni (AAAA-KK-PP) on kaasa arvatud.
     */
    public ArrayList<Kirje> filtreeri(String tüüp, String alates, String kuni) {
        ArrayList<Kirje> tulemus = new ArrayList<>();
        for (Kirje k : kirjed) {
            boolean tüüpSobib = tüüp.isEmpty() || k.getTüüp().equals(tüüp);
            boolean kuupäevSobib = k.getKuupäev().compareTo(alates) >= 0
                    && k.getKuupäev().compareTo(kuni) <= 0;
            if (tüüpSobib && kuupäevSobib) {
                tulemus.add(k);
            }
        }
        return tulemus;
    }

    /** Ühe kategooria summa antud listis. Tühi kategooria tähendab "Määramata". */
    public static double kategooriaSumma(ArrayList<Kirje> list, String tüüp, String kategooria) {
        double kokku = 0;
        for (Kirje k : list) {
            if (k.getTüüp().equals(tüüp) && k.getKategooria().equals(kategooria)) {
                kokku += k.getSumma();
            }
        }
        return kokku;
    }

    /**
     * Prindib kokkuvõtte kategooriate kaupa: summa, osakaal protsentides
     * ja tärnitulp (üks tärn iga 5% kohta).
     */
    public void prindiKokkuvõte(ArrayList<Kirje> list, String tüüp) {
        ArrayList<String> nimed;
        if (tüüp.equals("Tulu")) {
            nimed = Kategoriseerija.TULU_KATEGOORIAD;
        } else {
            nimed = Kategoriseerija.KULU_KATEGOORIAD;
        }

        double kokku = summa(list, tüüp);
        if (kokku == 0) {
            System.out.println("Selle tüübi kirjeid pole.");
            return;
        }

        // Tavalised kategooriad ja lõpus "Määramata" (tühi kategooria)
        for (int i = 0; i <= nimed.size(); i++) {
            String kategooria = (i < nimed.size()) ? nimed.get(i) : "";
            double s = kategooriaSumma(list, tüüp, kategooria);
            if (s == 0) {
                continue;
            }
            double osakaal = s / kokku * 100;
            String tärnid = "";
            int tärnideArv = (int) Math.round(osakaal / 5);
            for (int j = 0; j < tärnideArv; j++) {
                tärnid += "*";
            }
            String nimi = kategooria.isEmpty() ? "Määramata" : kategooria;
            System.out.printf("%-15s %10.2f %6.1f%%  %s%n", nimi, s, osakaal, tärnid);
        }
        System.out.printf("%-15s %10.2f%n", "Kokku", kokku);
    }

    /** Lisab juhuslikud testkirjed (kasutab Math.random). */
    public void genereeriJuhuslikud(int arv, Kategoriseerija kategoriseerija) {
        ArrayList<String> kaupmehed = kategoriseerija.getKaupmehed();
        for (int i = 0; i < arv; i++) {
            String kuupäev = "2026-" + kahekohaline(juhuarv(1, 12)) + "-"
                    + kahekohaline(juhuarv(1, 28));

            if (Math.random() < 0.25) {
                // Tulu
                double summa = juhusumma(500, 2000);
                String kategooria = Kategoriseerija.TULU_KATEGOORIAD.get(
                        juhuarv(0, Kategoriseerija.TULU_KATEGOORIAD.size() - 1));
                lisa(new Kirje(kuupäev, summa, "Tulu", kategooria));
            } else {
                // Kulu: enamasti kaupmehega (kategooria määratakse automaatselt),
                double summa = juhusumma(1, 200);
                String kaupmees;
                String kategooria;
                if (Math.random() < 0.8) {
                    kaupmees = kaupmehed.get(juhuarv(0, kaupmehed.size() - 1));
                    kategooria = kategoriseerija.kategoriseeri(kaupmees, "");
                } else {
                    kaupmees = "";
                    kategooria = Kategoriseerija.KULU_KATEGOORIAD.get(
                            juhuarv(0, Kategoriseerija.KULU_KATEGOORIAD.size() - 1));
                }
                lisa(new Kirje(kuupäev, summa, "Kulu", kategooria, kaupmees, "juhuslik testkirje"));
            }
        }
    }

    /** Juhuslik täisarv vahemikus alumine kuni ülemine (mõlemad kaasa arvatud). */
    private static int juhuarv(int alumine, int ülemine) {
        return (int) (Math.random() * (ülemine - alumine + 1)) + alumine;
    }

    /** Juhuslik summa vahemikus min kuni max, ümardatud kahe komakohani. */
    private static double juhusumma(int min, int max) {
        double summa = Math.random() * (max - min) + min;
        return Math.round(summa * 100) / 100.0;
    }

    /** 5 -> "05", 12 -> "12". */
    private static String kahekohaline(int arv) {
        if (arv < 10) {
            return "0" + arv;
        }
        return "" + arv;
    }
}
