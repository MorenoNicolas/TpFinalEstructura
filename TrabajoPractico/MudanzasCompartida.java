package trabajoPractico;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import estructuras.DiccionarioAvl;
import estructuras.Grafo;
import estructuras.MapeoAMuchos;

public class MudanzasCompartida {
    private static FileWriter logWriter;
    private static final DiccionarioAvl ciudades = new DiccionarioAvl();
    private static final MapeoAMuchos solicitudes = new MapeoAMuchos();
    private static final Grafo mapaRutas = new Grafo();
    private static final HashMap<String, Cliente> clientes = new HashMap<>();

    public static void main(String[] args) {
        boolean exito = cargarDatos();
        if (exito) {
            System.out.println("---------------------------------------------------------------------");
            System.out.println("Bienvenido");
            System.out.println("Se ha cargado la informacion de 30 Ciudades, 20 Clientes, 40 Rutas entre esas Ciudades, y 20 Pedidos");

            //menuuuu
        }
    }

        public static boolean cargarDatos() {
            // Corregir la ruta del archivo
            String nombreArchivo = "C:/Users/Walter/Documents/VsCode/TpFinalEstructura/cargarDatos.txt";
            boolean exito = true;
            System.out.println("Iniciando la carga de datos...");
            String[] datos;
            inicializarLog();
            try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
                String linea;
                // Leer el archivo línea por línea
                while ((linea = br.readLine()) != null) {
                    datos = linea.split(";");
                    switch (datos[0]) {
                        case "C":
                            cargarCiudad(Integer.parseInt(datos[1]), datos[2], datos[3]);
                            ;
                            break;
                        case "P":
                            cargarCliente(datos[1], Integer.parseInt(datos[2]), datos[3], datos[4], datos[5], datos[6]);
                            ;
                            break;
                        case "R":
                            cargarMapa(Integer.parseInt(datos[1]), Integer.parseInt(datos[2]), Double.parseDouble(datos[3]));
                            ;
                            break;
                        case "S":
                            cargarSolicitud(Integer.parseInt(datos[1]), Integer.parseInt(datos[2]), datos[3], datos[4], Integer.parseInt(datos[5]), Integer.parseInt(datos[6]), Integer.parseInt(datos[7]), datos[8], datos[9], datos[10]);
                    }
                }
            } catch (FileNotFoundException ex) {
                exito = false;
                System.err.println(ex.getMessage() + " El archivo al que intenta acceder no existe o la ruta es incorrecta.");
            } catch (IOException ex) {
                exito = false;
                System.err.println(ex.getMessage() + " Error leyendo el archivo.");
            }
    
            return exito;
        }

        public static void inicializarLog() {
            String rutaLog = "C:/Users/Walter/Documents/VsCode/TpFinalEstructura/log.txt";
            try {
                logWriter = new FileWriter(rutaLog, true); // true para permitir agregar registros al archivo existente
                logWriter.write("Inicio del registro: \n");
                logWriter.flush();
            } catch (IOException ex) {
                System.err.println(ex.getMessage() + "Error al inicializar el archivo log.");
            }
        }
        
        public static void escribirEnLog(String mensaje) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String fechaHora = formatoFecha.format(new Date());
            logWriter.write(fechaHora + " - " + mensaje + "\n");
            logWriter.flush();
        } catch (IOException ex) {
            System.err.println(ex.getMessage() + "Error al escribir en el archivo log.");
        }
    }
        public static void cargarCiudad(int numero, String nombre, String provincia){
            Ciudad ciudad = new Ciudad(numero, nombre, provincia);
            boolean exito = ciudades.insertar(numero, ciudad) && mapaRutas.insertarVertice(numero);
            if (exito) {
                escribirEnLog("Se cargo la " + ciudad.toString());
            } else {
                escribirEnLog("No se pudo cargar la " + ciudad.toString());
            }
        }

        public static void cargarCliente(String tipoDocumento, int numeroDocumento, String apellido, String nombre, String telefono, String email){
            Cliente cliente = new Cliente(tipoDocumento, numeroDocumento, apellido, nombre, telefono, email);
        String clave = tipoDocumento + numeroDocumento;
        if (clientes.containsKey(clave)) {
            escribirEnLog("No se pudo cargar el " + cliente.toString());
        } else {
            clientes.put(clave, cliente);
            escribirEnLog("Se cargo el " + cliente.toString());
        }
    }
    public static void cargarMapa(int codigoOrigen, int codigoDestino, double etiqueta) {
        boolean exito = mapaRutas.existeVertice(codigoOrigen) && mapaRutas.existeVertice(codigoDestino);
        if (exito) {
            boolean yaExiste = mapaRutas.existeArco(codigoOrigen, codigoDestino);
            if (yaExiste) {
                escribirEnLog("No se pudo cargar la Ruta entre " + codigoOrigen + " y " + codigoDestino + ", ya que ya existe una ruta entre ambas");
            } else {
                mapaRutas.insertarArco(codigoOrigen, codigoDestino, etiqueta);
                escribirEnLog("Se cargo la Ruta de " + codigoOrigen + " a " + codigoDestino + " con una distancia de " + etiqueta + " kilometros");
            }
        } else {
            escribirEnLog("No se pudo cargar la Ruta entre " + codigoOrigen + " y " + codigoDestino + ", ya que una de las ciudades no esta en el sistema");
        }
    }
    public static void cargarSolicitud(int origen, int destino, String fechaSolicitud, String tipoDocumento, int numeroDocumento, int cantMetrosCubicos,
            int cantBultos, String domicilioRetiro, String domicilioEntrega, String estaPago) {
        // METODO QUE CARGA UN PEDIDO AL AVL MAPEO A MUCHOS ENTRE 2 CIUDADES EXISTENTES
        Solicitud solicitud = new Solicitud(origen, destino, fechaSolicitud, tipoDocumento, numeroDocumento, cantMetrosCubicos, cantBultos, domicilioRetiro, domicilioEntrega, estaPago);
        boolean exito = mapaRutas.existeVertice(origen) && mapaRutas.existeVertice(destino);
        if (exito) {
            solicitudes.asociar(origen + "" + destino, solicitud);
            escribirEnLog("Se cargo el " + solicitud.toString());
        } else {
            escribirEnLog("Una de las ciudades no se encuentra en el sistema. Error al solicitar un pedido");
        }
    }
}
