import java.util.ArrayList;
import java.util.Scanner;

/** Küsib kasutajalt andmeid ja kontrollib, et sisend oleks õiget kujul. */
public class Sisend {
    private Scanner scan;

    public Sisend(Scanner scan) {
        this.scan = scan;
    }

    /** Küsib täisarvu seni, kuni sisend on korrektne. */
    public int küsiTäisarv(String küsimus) {
        while (true) {
            System.out.print(küsimus);
            String tekst = scan.nextLine().trim();
            if (onTäisarv(tekst)) {
                return Integer.parseInt(tekst);
            }
            System.out.println("Sisesta täisarv (ainult numbrid, ilma miinuseta).");
        }
    }

    /** Küsib summat seni, kuni see on korrektne arv ja suurem kui null. */
    public double küsiSumma() {
        while (true) {
            System.out.print("Sisesta summa (double tüüpi) (nt 13.02 või 13,02): ");
            String tekst = scan.nextLine().trim().replace(",", ".");
            if (!onSumma(tekst)) {
                System.out.println("Sisesta summa arvuna, nt 13.02.");
            } else {
                double summa = Double.parseDouble(tekst);
                if (summa > 0) {
                    return summa;
                }
                System.out.println("Summa peab olema suurem kui null.");
            }
        }
    }

    /** Küsib ühe tekstirea ja eemaldab otstest tühikud. */
    public String küsiTekst(String küsimus) {
        System.out.print(küsimus);
        return scan.nextLine().trim();
    }

    /** Küsib täisarvu, mis peab olema vahemikus min kuni max. */
    public int küsiVahemikus(String küsimus, int min, int max) {
        while (true) {
            int arv = küsiTäisarv(küsimus);
            if (arv >= min && arv <= max) {
                return arv;
            }
            System.out.println("Arv peab olema " + min + " kuni " + max + ".");
        }
    }

    /** Küsib kuupäeva seni, kuni see on kujul AAAA-KK-PP ja lubatud vahemikes. */
    public String küsiKuupäev() {
        while (true) {
            System.out.print("Kuupäev (AAAA-KK-PP): ");
            String tekst = scan.nextLine().trim();
            if (onKorrektneKuupäev(tekst)) {
                return tekst;
            }
            System.out.println("Sisesta korrektne kuupäev kujul AAAA-KK-PP.");
            System.out.println("AAAA vahemikus 1900-2100, KK 01-12, PP 01-31. NB! Ära unusta - märke!");
        }
    }

    /**
     * Näitab nummerdatud valikuid ja tagastab valitud sõne.
     * Kui lubaTühi on tõene, siis 0 tähendab "jäta tühjaks" ja tagastatakse "".
     * Kui lubaUus on tõene, siis viimane number lisab nimekirja uue nime.
     */
    public String valiNimekirjast(String pealkiri, ArrayList<String> valikud,
                                  boolean lubaTühi, boolean lubaUus) {
        System.out.println(pealkiri);
        if (lubaTühi) {
            System.out.println("  0 - jäta tühjaks");
        }
        for (int i = 0; i < valikud.size(); i++) {
            System.out.println("  " + (i + 1) + " - " + valikud.get(i));
        }
        if (lubaUus) {
            System.out.println("  " + (valikud.size() + 1) + " - lisa uus");
        }
        int min = lubaTühi ? 0 : 1;
        int max = lubaUus ? valikud.size() + 1 : valikud.size();
        int valik = küsiVahemikus("Valik: ", min, max);
        if (valik == 0) {
            return "";
        }
        if (lubaUus && valik == valikud.size() + 1) {
            String uus = küsiUusNimi(valikud);
            valikud.add(uus);
            return uus;
        }
        return valikud.get(valik - 1);
    }

    /** Küsib uue nime seni, kuni see pole tühi ega ole nimekirjas juba olemas. */
    public String küsiUusNimi(ArrayList<String> nimekiri) {
        while (true) {
            String nimi = küsiTekst("Uus nimi: ");
            boolean olemas = false;
            for (String s : nimekiri) {
                if (s.equalsIgnoreCase(nimi)) {
                    olemas = true;
                }
            }
            if (nimi.isEmpty()) {
                System.out.println("Nimi ei tohi olla tühi.");
            } else if (olemas) {
                System.out.println("Selline nimi on juba olemas.");
            } else {
                return nimi;
            }
        }
    }

    /** Tõene, kui tekst koosneb ainult numbritest (1 kuni 9 märki, et see mahuks int-i). */
    private static boolean onTäisarv(String tekst) {
        if (tekst.isEmpty() || tekst.length() > 9) {
            return false;
        }
        for (int i = 0; i < tekst.length(); i++) {
            char c = tekst.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /** Tõene, kui tekst on kujul 13 või 13.02 (numbrid, kuni üks punkt, vähemalt üks number). */
    private static boolean onSumma(String tekst) {
        if (tekst.isEmpty() || tekst.length() > 12) {
            return false;
        }
        int punkte = 0;
        int numbreid = 0;
        for (int i = 0; i < tekst.length(); i++) {
            char c = tekst.charAt(i);
            if (c == '.') {
                punkte++;
            } else if (c >= '0' && c <= '9') {
                numbreid++;
            } else {
                return false;
            }
        }
        return punkte <= 1 && numbreid >= 1;
    }

    /** Tõene, kui tekst on korrektne kuupäev kujul AAAA-KK-PP. */
    private static boolean onKorrektneKuupäev(String tekst) {
        if (tekst.length() != 10) {
            return false;
        }
        // kohtadel 4 ja 7 on kriips, kõik teised märgid on numbrid
        for (int i = 0; i < 10; i++) {
            char c = tekst.charAt(i);
            if (i == 4 || i == 7) {
                if (c != '-') {
                    return false;
                }
            } else if (c < '0' || c > '9') {
                return false;
            }
        }
        // osad on kindlasti numbrid, parseInt ei saa viga visata
        int aasta = Integer.parseInt(tekst.substring(0, 4));
        int kuu = Integer.parseInt(tekst.substring(5, 7));
        int päev = Integer.parseInt(tekst.substring(8, 10));

        return aasta >= 1900 && aasta <= 2100
                && kuu >= 1 && kuu <= 12
                && päev >= 1 && päev <= 31;
    }
}