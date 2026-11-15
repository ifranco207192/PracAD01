package practica1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

// TASCA 3: converteix alumnes-[data-hora].csv a alumnes.json o alumnes.xml
public class Conversio_XML_JSON {

    public static void main(String[] args) {
        Scanner teclat = new Scanner(System.in);

        // 1. Demanem la ruta fins que es pugui llegir el CSV
        ArrayList<Alumne> alumnes = null;
        while (alumnes == null) {
            System.out.print("Ruta del fitxer CSV: ");
            String ruta = teclat.nextLine().trim();
            try {
                alumnes = Tasca2GestorCsv.llegirCsv(ruta);
            } catch (Exception e) {
                System.out.println("No s'ha pogut llegir el fitxer: " + e.getMessage());
            }
        }
        System.out.println("S'han llegit " + alumnes.size() + " alumnes.");

        // 2. Demanem el tipus de conversió
        String tipus = "";
        while (!tipus.equals("1") && !tipus.equals("2")) {
            System.out.print("Tipus de conversió (1 = JSON, 2 = XML): ");
            tipus = teclat.nextLine().trim();
        }

        // 3. Fem la conversió
        try {
            if (tipus.equals("1")) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File("alumnes.json"), alumnes);
                System.out.println("Fitxer alumnes.json creat.");
            } else {
                JAXBContext context = JAXBContext.newInstance(Alumnes.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(new Alumnes(alumnes), new File("alumnes.xml"));
                System.out.println("Fitxer alumnes.xml creat.");
            }
        } catch (Exception e) {
            System.out.println("Error en la conversió: " + e.getMessage());
        }
    }
}
