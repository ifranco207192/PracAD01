package practica1;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;

// Element arrel de l'XML: <alumnes> conté tots els <alumne>
@XmlRootElement(name = "alumnes")
public class Alumnes {

    private ArrayList<Alumne> llista = new ArrayList<>();

    public Alumnes() {
    }

    public Alumnes(ArrayList<Alumne> llista) {
        this.llista = llista;
    }

    @XmlElement(name = "alumne")
    public ArrayList<Alumne> getLlista() { return llista; }
    public void setLlista(ArrayList<Alumne> llista) { this.llista = llista; }
}
