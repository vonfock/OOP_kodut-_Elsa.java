/**
 * Üks tulu või kulu kirje.
 * Kuupäev hoitakse sõnena kujul AAAA-KK-PP, sest sellist kuju saab
 * võrrelda meetodiga compareTo (sorteerimine ja vahemiku filter toimivad).
 */
public class Kirje {
    private String kuupäev;     // "AAAA-KK-PP"
    private double summa;
    private String tüüp;        // "Tulu" või "Kulu"
    private String kategooria;  // "" tähendab, et kategooria on määramata
    private String kaupmees;    // "" kui puudub (tulul alati "")
    private String kirjeldus;   // "" kui puudub

    // Täielik konstruktor
    public Kirje(String kuupäev, double summa, String tüüp, String kategooria,
                 String kaupmees, String kirjeldus) {
        this.kuupäev = kuupäev;
        this.summa = summa;
        this.tüüp = tüüp;
        this.kategooria = kategooria;
        this.kaupmees = kaupmees;
        this.kirjeldus = kirjeldus;
    }

    // Lühem konstruktor: kaupmees ja kirjeldus jäävad tühjaks (sobib tuludele)
    public Kirje(String kuupäev, double summa, String tüüp, String kategooria) {
        this(kuupäev, summa, tüüp, kategooria, "", "");
    }

    // Vaikekonstruktor
    public Kirje() {
        this("", 0.0, "Kulu", "", "", "");
    }

    public String getKuupäev() {
        return kuupäev;
    }

    public void setKuupäev(String kuupäev) {
        this.kuupäev = kuupäev;
    }

    public double getSumma() {
        return summa;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }

    public String getTüüp() {
        return tüüp;
    }

    public void setTüüp(String tüüp) {
        this.tüüp = tüüp;
    }

    public String getKategooria() {
        return kategooria;
    }

    public void setKategooria(String kategooria) {
        this.kategooria = kategooria;
    }

    public String getKaupmees() {
        return kaupmees;
    }

    public void setKaupmees(String kaupmees) {
        this.kaupmees = kaupmees;
    }

    public String getKirjeldus() {
        return kirjeldus;
    }

    public void setKirjeldus(String kirjeldus) {
        this.kirjeldus = kirjeldus;
    }

    /** Tõene, kui kirje on kulu. Sõnu võrreldakse equals-iga, mitte ==. */
    public boolean onKulu() {
        return tüüp.equals("Kulu");
    }

    /** Tõene, kui kirje on tulu. */
    public boolean onTulu() {
        return tüüp.equals("Tulu");
    }

    /** Kirje ühe reana, nt "2026-11-08 | Kulu | 12.50 | Söök ja jook | Rimi | piim". */
    @Override
    public String toString() {
        return kuupäev + " | " + tüüp + " | " + String.format("%.2f", summa) + " | "
                + (kategooria.isEmpty() ? "Määramata" : kategooria) + " | "
                + kaupmees + " | " + kirjeldus;
    }

    /** Kirje tabeli reana: veerud joondatud. */
    public void prindiRida() {
        System.out.printf("%-10s %-6s %10.2f  %-15s %-14s %s%n", kuupäev, tüüp, summa,
                kategooria.isEmpty() ? "Määramata" : kategooria, kaupmees, kirjeldus);
    }
}
