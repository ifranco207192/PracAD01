package practica1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

// TASCA 1: genera el fitxer alumnes.csv amb X alumnes
public class Tasca1GenerarCsv {

    static String[] noms = {"Joan", "Maria", "Antoni", "Laura", "Jordi", "Anna", "Marc", "Elena", "Pau", "Marta"};
    static String[] cognoms = {"Garcia", "Marti", "Vidal", "Serra", "Ferrer", "Puig", "Roca", "Soler", "Vila", "Pons"};
    static String[] carrers = {"Carrer Major", "Carrer del Mar", "Avinguda Diagonal", "Placa Nova", "Carrer de la Pau"};
    static String[] provincies = {"Barcelona", "Girona", "Lleida", "Tarragona", "Illes Balears"};
    static String[] prefixCp = {"08", "17", "25", "43", "07"};

    public static void main(String[] args) {
        Scanner teclat = new Scanner(System.in);
        Random random = new Random();

        // Demanem X fins que sigui un número vàlid
        int x = 0;
        while (x < 1 || x > 1000) {
            System.out.print("Quants alumnes vols generar? (1-1000): ");
            try {
                x = Integer.parseInt(teclat.nextLine().trim());
                if (x < 1 || x > 1000) {
                    System.out.println("El número ha d'estar entre 1 i 1000.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Has d'escriure un número enter.");
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("alumnes.csv", StandardCharsets.UTF_8))) {
            bw.write("id;nom;cognoms;edat;telefon;email;adreca;cp;provincia;pais");
            bw.newLine();

            for (int id = 1; id <= x; id++) {
                String nom = noms[random.nextInt(noms.length)];
                String cognom = cognoms[random.nextInt(cognoms.length)] + " " + cognoms[random.nextInt(cognoms.length)];
                int edat = 16 + random.nextInt(50);
                String telefon = "6" + String.format("%08d", random.nextInt(100000000));
                String email = nom.toLowerCase() + id + "@formacio.cat";
                String adreca = carrers[random.nextInt(carrers.length)] + " " + (1 + random.nextInt(100));
                int p = random.nextInt(provincies.length);
                String cp = prefixCp[p] + String.format("%03d", random.nextInt(1000));

                Alumne a = new Alumne(id, nom, cognom, edat, telefon, email, adreca, cp, provincies[p], "Espanya");
                bw.write(a.toCsv());
                bw.newLine();
            }
            System.out.println("Fitxer alumnes.csv creat amb " + x + " alumnes.");
        } catch (IOException e) {
            System.out.println("Error en escriure el fitxer: " + e.getMessage());
        }
    }
}
