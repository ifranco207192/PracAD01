package practica1;

// Excepció pròpia: es llança quan no existeix cap alumne amb l'id cercat
public class AlumneNoTrobatException extends Exception {

    public AlumneNoTrobatException(String missatge) {
        super(missatge);
    }
}
