package TrabajoPractico;

public class Solicitud {
    private int cantMetrosCubicos;
    private int cantBultos;
    private final String fechaSolicitud;
    private final String tipoDocumento;
    private final int numeroDocumento;
    private String domicilioRetiro;
    private String domicilioEntrega;
    private String estaPago;
    private final int ciudadOrigen;
    private final int ciudadDestino;

    public Solicitud(int ciudadOrigen, int ciudadDestino, String fechaSolicitud,
            String tipoDocumento, int numeroDocumento) {
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.fechaSolicitud = fechaSolicitud;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.cantMetrosCubicos = 0;
        this.cantBultos = 0;
        this.domicilioRetiro = "";
        this.domicilioEntrega = "";
        this.estaPago = "";
    }

    public Solicitud(int ciudadOrigen, int ciudadDestino, String fechaSolicitud,
            String tipoDocumento, int numeroDocumento, int cantMetrosCubicos, int cantBultos,
            String domicilioRetiro, String domicilioEntrega, String estaPago) {
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.fechaSolicitud = fechaSolicitud;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.cantMetrosCubicos = cantMetrosCubicos;
        this.cantBultos = cantBultos;
        this.domicilioRetiro = domicilioRetiro;
        this.domicilioEntrega = domicilioEntrega;
        this.estaPago = estaPago;
    }

    public int getCiudadOrigen() {
        return ciudadOrigen;
    }

    public int getCiudadDestino() {
        return ciudadDestino;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public int getCantMetrosCubicos() {
        return cantMetrosCubicos;
    }

    public void setCantMetrosCubicos(int cantMetrosCubicos) {
        this.cantMetrosCubicos = cantMetrosCubicos;
    }

    public int getCantBultos() {
        return cantBultos;
    }

    public void setCantBultos(int cantBultos) {
        this.cantBultos = cantBultos;
    }

    public String getDomicilioRetiro() {
        return domicilioRetiro;
    }

    public void setDomicilioRetiro(String domicilioRetiro) {
        this.domicilioRetiro = domicilioRetiro;
    }

    public String getDomicilioEntrega() {
        return domicilioEntrega;
    }

    public void setDomicilioEntrega(String domicilioEntrega) {
        this.domicilioEntrega = domicilioEntrega;
    }

    public String getEstaPago() {
        return estaPago;
    }

    public void setEstaPago(String estaPago) {
        this.estaPago = estaPago;
    }

    @Override
    public String toString() {
        return "Pedido hecho por: " + this.tipoDocumento + " N°" + this.numeroDocumento
                + ", de " + this.ciudadOrigen + " a " + this.ciudadDestino + " el "
                + this.fechaSolicitud + ", que ocupa " + this.cantMetrosCubicos
                + "m3 y consta de " + this.cantBultos + " bultos. Se entrega en "
                + this.domicilioEntrega + " y se retira en " + this.domicilioRetiro
                + ". Esta pago: " + this.estaPago;
    }

    public boolean equals(Solicitud solicitud) {
        return this.ciudadOrigen == solicitud.getCiudadOrigen()
                && this.ciudadDestino == solicitud.getCiudadDestino()
                && this.fechaSolicitud.equals(solicitud.getFechaSolicitud())
                && this.tipoDocumento.equals(solicitud.getTipoDocumento())
                && this.numeroDocumento == solicitud.getNumeroDocumento();
    }
}
