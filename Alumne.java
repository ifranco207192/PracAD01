package practica1;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

// Classe de dades. JAXB (XML) i Jackson (JSON) fan servir els getters i setters.
@XmlType(propOrder = {"nom", "cognoms", "edat", "telefon", "email", "adreca", "cp", "provincia", "pais"})
public class Alumne {

    private int id;
    private String nom;
    private String cognoms;
    private int edat;
    private String telefon;
    private String email;
    private String adreca;
    private String cp;
    private String provincia;
    private String pais;

    // Constructor buit obligatori per al binding
    public Alumne() {
    }

    public Alumne(int id, String nom, String cognoms, int edat, String telefon,
                  String email, String adreca, String cp, String provincia, String pais) {
        this.id = id;
        this.nom = nom;
        this.cognoms = cognoms;
        this.edat = edat;
        this.telefon = telefon;
        this.email = email;
        this.adreca = adreca;
        this.cp = cp;
        this.provincia = provincia;
        this.pais = pais;
    }

    @XmlAttribute // <alumne id="1">
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @XmlElement
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    @XmlElement
    public String getCognoms() { return cognoms; }
    public void setCognoms(String cognoms) { this.cognoms = cognoms; }

    @XmlElement
    public int getEdat() { return edat; }
    public void setEdat(int edat) { this.edat = edat; }

    @XmlElement
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    @XmlElement
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @XmlElement
    public String getAdreca() { return adreca; }
    public void setAdreca(String adreca) { this.adreca = adreca; }

    @XmlElement
    public String getCp() { return cp; }
    public void setCp(String cp) { this.cp = cp; }

    @XmlElement
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    @XmlElement
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    // Línia del CSV: id;nom;cognoms;edat;telefon;email;adreca;cp;provincia;pais
    public String toCsv() {
        return id + ";" + nom + ";" + cognoms + ";" + edat + ";" + telefon + ";"
                + email + ";" + adreca + ";" + cp + ";" + provincia + ";" + pais;
    }

    @Override
    public String toString() {
        return id + " - " + nom + " " + cognoms + ", " + edat + " anys, " + telefon + ", "
                + email + ", " + adreca + ", " + cp + " " + provincia + " (" + pais + ")";
    }
}
