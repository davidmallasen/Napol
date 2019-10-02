package asint.ast.tipos;

/**
 * Representa un tipo primitivo de Napol.
 * Puede tomar dos valores, INT o BOOL
 */
public enum Tipo {
    INT("int"),
    BOOL("bool");

    private final String nombre;

    Tipo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
