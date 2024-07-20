package TrabajoPractico;

public class Cliente {

    private final String tipoDocumento;
    private final int numeroDocumento;
    private String apellido;
    private String nombre;
    private String telefono;
    private String email;
    
    
    public Cliente(String tipoDocumento, int numeroDocumento) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.apellido = "";
        this.nombre = "";
        this.telefono = "";
        this.email = "";
    }

    public Cliente(String tipoDocumento, int numeroDocumento, String apellido, String nombre, String telefono, String email) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.apellido = apellido;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Cliente con " + this.tipoDocumento + " N°" + this.numeroDocumento + ". "
                + this.apellido + ", " + this.nombre + " con telefono: "
                + this.telefono + " y email: " + this.email;
    }

}
