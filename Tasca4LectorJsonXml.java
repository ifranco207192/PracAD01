package practica1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// TASCA 4: menú per llegir i modificar alumnes.json o alumnes.xml
public class Tasca4LectorJsonXml {

    static ArrayList<Alumne> alumnes = new ArrayList<>();
    static String fitxer = null; // fitxer carregat
    static Scanner teclat = Tasca2GestorCsv.teclat; // un sol Scanner per a System.in

    public static void main(String[] args) {
        int opcio;
        do {
            mostrarMenu();
            opcio = Tasca2GestorCsv.llegirEnter("Opció: ");
            if (opcio >= 2 && opcio <= 7 && fitxer == null) {
                System.out.println("Primer has de carregar un fitxer (opció 1).");
                continue;
            }
            switch (opcio) {
                case 1:
                    carregar();
                    break;
                case 2:
                    for (Alumne a : alumnes) {
                        System.out.println(a);
                    }
                    break;
                case 3:
                    System.out.println("Total d'alumnes: " + alumnes.size());
                    break;
                case 4:
                    cercar();
                    break;
                case 5:
                    eliminar();
                    break;
                case 6:
                    modificar();
                    break;
                case 7:
                    mitjanaEdat();
                    break;
                case 8:
                    System.out.println("Adeu!");
                    break;
                default:
                    System.out.println("Opció incorrecta.");
            }
        } while (opcio != 8);
    }

    static void mostrarMenu() {
        System.out.println("\n--- LECTOR JSON / XML ---");
        System.out.println("1. Carregar l'arxiu alumnes.json o alumnes.xml");
        System.out.println("2. Mostrar tots els alumnes");
        System.out.println("3. Mostrar el nombre total d'alumnes");
        System.out.println("4. Cercar un alumne per ID");
        System.out.println("5. Eliminar alumne");
        System.out.println("6. Modificar alumne");
        System.out.println("7. Calcular la mitjana d'edat");
        System.out.println("8. Sortir");
    }

    static void carregar() {
        System.out.print("Fitxer (alumnes.json o alumnes.xml): ");
        String ruta = teclat.nextLine().trim();
        if (!new File(ruta).exists()) {
            System.out.println("El fitxer no existeix.");
            return;
        }
        try {
            if (ruta.endsWith(".json")) {
                ObjectMapper mapper = new ObjectMapper();
                Alumne[] llegits = mapper.readValue(new File(ruta), Alumne[].class);
                alumnes = new ArrayList<>(Arrays.asList(llegits));
            } else if (ruta.endsWith(".xml")) {
                JAXBContext context = JAXBContext.newInstance(Alumnes.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                alumnes = ((Alumnes) unmarshaller.unmarshal(new File(ruta))).getLlista();
            } else {
                System.out.println("El fitxer ha de ser .json o .xml");
                return;
            }
            fitxer = ruta;
            System.out.println("S'han carregat " + alumnes.size() + " alumnes.");
        } catch (Exception e) {
            System.out.println("El fitxer no té un format correcte.");
        }
    }

    static void cercar() {
        try {
            System.out.println(Tasca2GestorCsv.cercarPerId(alumnes, Tasca2GestorCsv.llegirEnter("Id: ")));
        } catch (AlumneNoTrobatException e) {
            System.out.println(e.getMessage());
        }
    }

    static void eliminar() {
        try {
            Alumne a = Tasca2GestorCsv.cercarPerId(alumnes, Tasca2GestorCsv.llegirEnter("Id: "));
            alumnes.remove(a);
            guardar();
            System.out.println("Alumne eliminat.");
        } catch (AlumneNoTrobatException e) {
            System.out.println(e.getMessage());
        }
    }

    static void modificar() {
        try {
            Alumne a = Tasca2GestorCsv.cercarPerId(alumnes, Tasca2GestorCsv.llegirEnter("Id: "));
            System.out.println("Dades actuals: " + a);
            System.out.println("Introdueix les noves dades:");
            alumnes.set(alumnes.indexOf(a), Tasca2GestorCsv.demanarDades(a.getId()));
            guardar();
            System.out.println("Alumne modificat.");
        } catch (AlumneNoTrobatException e) {
            System.out.println(e.getMessage());
        }
    }

    static void mitjanaEdat() {
        if (alumnes.isEmpty()) {
            System.out.println("No hi ha alumnes.");
            return;
        }
        int suma = 0;
        for (Alumne a : alumnes) {
            suma += a.getEdat();
        }
        System.out.printf("Mitjana d'edat: %.2f anys%n", (double) suma / alumnes.size());
    }

    // Guarda els canvis al mateix fitxer i en el mateix format
    static void guardar() {
        try {
            if (fitxer.endsWith(".json")) {
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(new File(fitxer), alumnes);
            } else {
                Marshaller marshaller = JAXBContext.newInstance(Alumnes.class).createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(new Alumnes(alumnes), new File(fitxer));
            }
        } catch (Exception e) {
            System.out.println("Error en guardar el fitxer: " + e.getMessage());
        }
    }
}
