package practica1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

// TASCA 2: menú per gestionar el fitxer alumnes.csv
public class Tasca2GestorCsv {

    static final String FITXER = "alumnes.csv";
    static ArrayList<Alumne> alumnes = new ArrayList<>();
    static Scanner teclat = new Scanner(System.in);

    public static void main(String[] args) {
        carregar(); // Requisit 2: es llegeix el fitxer en iniciar

        int opcio;
        do {
            mostrarMenu();
            opcio = llegirEnter("Opció: ");
            switch (opcio) {
                case 1:
                    carregar();
                    break;
                case 2:
                    afegir();
                    break;
                case 3:
                    cercar();
                    break;
                case 4:
                    eliminar();
                    break;
                case 5:
                    modificar();
                    break;
                case 6:
                    System.out.println("Hi ha " + alumnes.size() + " alumnes.");
                    break;
                case 7:
                    String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
                    guardar("alumnes-" + data + ".csv");
                    break;
                case 0:
                    System.out.println("Adeu!");
                    break;
                default:
                    System.out.println("Opció incorrecta.");
            }
        } while (opcio != 0);
    }

    static void mostrarMenu() {
        System.out.println("\n--- GESTIÓ D'ALUMNES ---");
        System.out.println("1. Carregar les dades del CSV");
        System.out.println("2. Afegir alumne");
        System.out.println("3. Cercar alumne a partir del seu id");
        System.out.println("4. Eliminar alumne a partir del seu id");
        System.out.println("5. Modificar un alumne a partir del seu id");
        System.out.println("6. Comptar alumnes");
        System.out.println("7. Generar un nou fitxer alumnes-[data-hora].csv");
        System.out.println("0. Sortir");
    }

    // ---------------- OPCIONS DEL MENÚ ----------------

    static void carregar() {
        try {
            alumnes = llegirCsv(FITXER);
            System.out.println("S'han carregat " + alumnes.size() + " alumnes.");
        } catch (IOException e) {
            System.out.println("No s'ha pogut llegir " + FITXER + ": " + e.getMessage());
        }
    }

    static void afegir() {
        int id = llegirEnter("Id: ");
        if (id <= 0) {
            System.out.println("L'id ha de ser més gran que 0.");
            return;
        }
        if (existeix(id)) { // Requisit 4: ids no repetits
            System.out.println("Ja existeix un alumne amb l'id " + id + ".");
            return;
        }
        alumnes.add(demanarDades(id));
        guardar(FITXER); // Requisit 3: els canvis es guarden
        System.out.println("Alumne afegit.");
    }

    static void cercar() {
        try {
            System.out.println(cercarPerId(alumnes, llegirEnter("Id: ")));
        } catch (AlumneNoTrobatException e) {
            System.out.println(e.getMessage());
        }
    }

    static void eliminar() {
        try {
            Alumne a = cercarPerId(alumnes, llegirEnter("Id: "));
            alumnes.remove(a);
            guardar(FITXER);
            System.out.println("Alumne eliminat.");
        } catch (AlumneNoTrobatException e) {
            System.out.println(e.getMessage());
        }
    }

    static void modificar() {
        try {
            Alumne a = cercarPerId(alumnes, llegirEnter("Id: "));
            System.out.println("Dades actuals: " + a);
            System.out.println("Introdueix les noves dades:");
            alumnes.set(alumnes.indexOf(a), demanarDades(a.getId()));
            guardar(FITXER);
            System.out.println("Alumne modificat.");
        } catch (AlumneNoTrobatException e) {
            System.out.println(e.getMessage());
        }
    }

    // ---------------- FITXER CSV ----------------

    // Llegeix el CSV línia a línia. Les línies incorrectes es descarten.
    public static ArrayList<Alumne> llegirCsv(String ruta) throws IOException {
        if (!Files.exists(Path.of(ruta))) {
            throw new IOException("el fitxer no existeix");
        }
        ArrayList<Alumne> llista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta, StandardCharsets.UTF_8))) {
            String linia;
            int numLinia = 0;
            while ((linia = br.readLine()) != null) {
                numLinia++;
                if (linia.isBlank() || linia.startsWith("id;")) {
                    continue; // línia buida o capçalera
                }
                String[] camps = linia.split(";");
                try {
                    if (camps.length != 10) {
                        throw new NumberFormatException("no té 10 camps");
                    }
                    int id = Integer.parseInt(camps[0].trim());
                    int edat = Integer.parseInt(camps[3].trim());
                    boolean repetit = false;
                    for (Alumne a : llista) {
                        if (a.getId() == id) {
                            repetit = true;
                        }
                    }
                    if (repetit) {
                        System.out.println("Línia " + numLinia + " descartada: id " + id + " repetit.");
                    } else {
                        llista.add(new Alumne(id, camps[1], camps[2], edat, camps[4], camps[5],
                                camps[6], camps[7], camps[8], camps[9]));
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Línia " + numLinia + " descartada: format incorrecte.");
                }
            }
        }
        return llista;
    }

    static void guardar(String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, StandardCharsets.UTF_8))) {
            bw.write("id;nom;cognoms;edat;telefon;email;adreca;cp;provincia;pais");
            bw.newLine();
            for (Alumne a : alumnes) {
                bw.write(a.toCsv());
                bw.newLine();
            }
            System.out.println("Dades guardades a " + ruta);
        } catch (IOException e) {
            System.out.println("Error en guardar el fitxer: " + e.getMessage());
        }
    }

    // ---------------- MÈTODES AUXILIARS (també els usa la Tasca 4) ----------------

    public static Alumne cercarPerId(ArrayList<Alumne> llista, int id) throws AlumneNoTrobatException {
        for (Alumne a : llista) {
            if (a.getId() == id) {
                return a;
            }
        }
        throw new AlumneNoTrobatException("No existeix cap alumne amb l'id " + id + ".");
    }

    static boolean existeix(int id) {
        try {
            cercarPerId(alumnes, id);
            return true;
        } catch (AlumneNoTrobatException e) {
            return false;
        }
    }

    // Demana i valida totes les dades d'un alumne
    public static Alumne demanarDades(int id) {
        String nom = llegirText("Nom: ");
        String cognoms = llegirText("Cognoms: ");
        int edat = llegirEnter("Edat (16-99): ");
        while (edat < 16 || edat > 99) {
            System.out.println("L'edat ha d'estar entre 16 i 99.");
            edat = llegirEnter("Edat (16-99): ");
        }
        String telefon = llegirAmbFormat("Telèfon (9 xifres): ", "\\d{9}");
        String email = llegirAmbFormat("Email: ", "[^@ ]+@[^@ ]+\\.[a-zA-Z]+");
        String adreca = llegirText("Adreça: ");
        String cp = llegirAmbFormat("Codi postal (5 xifres): ", "\\d{5}");
        String provincia = llegirText("Província: ");
        String pais = llegirText("País: ");
        return new Alumne(id, nom, cognoms, edat, telefon, email, adreca, cp, provincia, pais);
    }

    // Llegeix un enter. Si no ho és, el torna a demanar.
    public static int llegirEnter(String missatge) {
        while (true) {
            System.out.print(missatge);
            try {
                return Integer.parseInt(teclat.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Has d'escriure un número enter.");
            }
        }
    }

    // Llegeix un text no buit i sense ';' (trencaria el format del CSV)
    static String llegirText(String missatge) {
        while (true) {
            System.out.print(missatge);
            String text = teclat.nextLine().trim();
            if (text.isEmpty()) {
                System.out.println("El camp no pot estar buit.");
            } else if (text.contains(";")) {
                System.out.println("El camp no pot contenir ';'.");
            } else {
                return text;
            }
        }
    }

    // Llegeix un text que ha de complir un patró (expressió regular)
    static String llegirAmbFormat(String missatge, String patro) {
        while (true) {
            String text = llegirText(missatge);
            if (text.matches(patro)) {
                return text;
            }
            System.out.println("Format incorrecte.");
        }
    }
}
