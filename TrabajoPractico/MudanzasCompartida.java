package TrabajoPractico;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import estructuras.*;

public class MudanzasCompartida {
    private static FileWriter logWriter;
    private static final DiccionarioAvl ciudades = new DiccionarioAvl();
    private static final MapeoAMuchos solicitudes = new MapeoAMuchos();
    private static final GrafoEtiquetado mapaRutas = new GrafoEtiquetado();
    private static final HashMap<String, Cliente> clientes = new HashMap<>();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exito = cargarDatos();
        if (exito) {
            System.out.println("---------------------------------------------------------------------");
            System.out.println("Bienvenido");
            System.out.println(
                    "Se ha cargado la informacion de 30 Ciudades, 20 Clientes, 40 Rutas entre esas Ciudades, y 20 Pedidos");
            menu();
        }
    }

    public static boolean cargarDatos() {
        // Corregir la ruta del archivo
        String nombreArchivo = "C:/Users/Nicolas/.vscode/TpFinalEstructura/cargarDatos.txt";
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
                        cargarMapa(Integer.parseInt(datos[1]), Integer.parseInt(datos[2]),
                                Double.parseDouble(datos[3]));
                        ;
                        break;
                    case "S":
                        cargarSolicitud(Integer.parseInt(datos[1]), Integer.parseInt(datos[2]), datos[3], datos[4],
                                Integer.parseInt(datos[5]), Integer.parseInt(datos[6]), Integer.parseInt(datos[7]),
                                datos[8], datos[9], datos[10]);
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            exito = false;
            System.err
                    .println(ex.getMessage() + " El archivo al que intenta acceder no existe o la ruta es incorrecta.");
        } catch (IOException ex) {
            exito = false;
            System.err.println(ex.getMessage() + " Error leyendo el archivo.");
        }

        return exito;
    }

    public static void inicializarLog() {
        String rutaLog = "C:/Users/Nicolas/.vscode/TpFinalEstructura/log.txt";
        try {
            logWriter = new FileWriter(rutaLog, false); // true para permitir agregar registros al archivo existente
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

    public static void cargarCiudad(int numero, String nombre, String provincia) {
        Ciudad ciudad = new Ciudad(numero, nombre, provincia);
        boolean exito = ciudades.insertar(numero, ciudad) && mapaRutas.insertarVertice(numero);
        if (exito) {
            escribirEnLog("Se cargo la " + ciudad.toString());
        } else {
            escribirEnLog("No se pudo cargar la " + ciudad.toString());
        }
    }

    public static void cargarCliente(String tipoDocumento, int numeroDocumento, String apellido, String nombre,
            String telefono, String email) {
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
                escribirEnLog("No se pudo cargar la Ruta entre " + codigoOrigen + " y " + codigoDestino
                        + ", ya que ya existe una ruta entre ambas");
            } else {
                mapaRutas.insertarArco(codigoOrigen, codigoDestino, etiqueta);
                escribirEnLog("Se cargo la Ruta de " + codigoOrigen + " a " + codigoDestino + " con una distancia de "
                        + etiqueta + " kilometros");
            }
        } else {
            escribirEnLog("No se pudo cargar la Ruta entre " + codigoOrigen + " y " + codigoDestino
                    + ", ya que una de las ciudades no esta en el sistema");
        }
    }

    public static void cargarSolicitud(int origen, int destino, String fechaSolicitud, String tipoDocumento,
            int numeroDocumento, int cantMetrosCubicos,
            int cantBultos, String domicilioRetiro, String domicilioEntrega, String estaPago) {
        // METODO QUE CARGA UN PEDIDO AL AVL MAPEO A MUCHOS ENTRE 2 CIUDADES EXISTENTES
        Solicitud solicitud = new Solicitud(origen, destino, fechaSolicitud, tipoDocumento, numeroDocumento,
                cantMetrosCubicos, cantBultos, domicilioRetiro, domicilioEntrega, estaPago);
        boolean exito = mapaRutas.existeVertice(origen) && mapaRutas.existeVertice(destino);
        if (exito) {
            solicitudes.asociar(origen + "" + destino, solicitud);
            escribirEnLog("Se cargo el " + solicitud.toString());
        } else {
            escribirEnLog("Una de las ciudades no se encuentra en el sistema. Error al solicitar un pedido");
        }
    }

    public static void menu() {
        // MENU PRINCIPAL
        int respuesta;
        do {
            System.out.println("---------------------------------------MENU---------------------------------------");
            System.out.println("<> 1. ABM de ciudades.\n" +
                    "<> 2. ABM de rutas.\n<> 3. ABM de clientes.\n<> 4. ABM de pedidos.\n<> 5. Consultar la informacion de un cliente."
                    +
                    "\n<> 6. Consultar sobre ciudades.\n<> 7. Consultar sobre viajes.\n<> 8. Verificar viajes.\n<> 9. Mostrar sistema\n<> 0. Cerrar el programa.");
            respuesta = sc.nextInt();
            switch (respuesta) {
                case 0:
                    System.out.println(
                            "---------------------------------------<EJECUCION TERMINADA>---------------------------------------");
                    break;
                case 1:
                    ABMCiudades();
                    break;
                case 2:
                    ABMRutas();
                    break;
                // case 3:
                // clearLog();
                // ABMClientes();
                // break;
                // case 4:
                // clearLog();
                // ABMPedidos();
                // break;
                case 5:
                    consultarCliente();
                    break;
                case 6:
                    consultaCiudades();
                    break;
                case 7:
                    consultasViajes();
                    break;
                case 8:
                    verificarViaje();
                    break;
                case 9:
                    mostrarSistema();
                    break;
                default:
                    System.out.println("RESPUESTA INVALIDA");
                    break;
            }
        } while (respuesta != 0);
        sc.close();
    }

    public static void ABMCiudades() {
        int respuesta;
        do {
            System.out.println("------------------------------------ABMCiudades-----------------------------------");
            System.out.println(
                    "<> 1. Agregar una ciudad. \n<> 2. Eliminar una ciudad. \n<> 3. Editar una ciudad.\n<> 4. Volver al menu.");
            respuesta = sc.nextInt();
            switch (respuesta) {
                case 1:
                    agregarCiudad();
                    break;
                case 2:
                    eliminarCiudad();
                    break;
                case 3:
                    editarCiudad();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("RESPUESTA INVALIDA.");
                    break;
            }
        } while (respuesta != 4);
    }

    public static void agregarCiudad() {
        String nombre, provincia;
        int codigoPostal;
        System.out.println("Ingrese los datos de la nueva ciudad a continuacion");
        System.out.println("Nombre de la ciudad:");
        nombre = sc.next();
        System.out.println("Provincia de la ciudad:");
        provincia = sc.next();
        System.out.println("Codigo postal de la ciudad:");
        codigoPostal = sc.nextInt();
        if (!ciudades.existeClave(codigoPostal)) {
            cargarCiudad(codigoPostal, nombre, provincia);
            System.out.println("LA CIUDAD FUE AGREGADA CON EXITO.");
        } else {
            System.out.println("LA CIUDAD YA ESTABA INGRESADA, NO SE AGREGO.");
        }
    }

    public static void eliminarCiudad() {
        // METODO QUE ELIMINA UNA CIUDAD DEL SISTEMA
        int codigo;
        System.out.println("Ingrese el codigo postal de la Ciudad a eliminar");
        codigo = sc.nextInt();
        // PARA ELIMINARLA, LA CIUDAD DEBE EXISTIR
        if (ciudades.existeClave(codigo)) {
            System.out.println("Se elimino la ciudad con codigo: " + codigo + " con exito1");
            ciudades.eliminar(codigo);
            mapaRutas.eliminarVertice(codigo);
            escribirEnLog("La Ciudad con codigo postal " + codigo + " se elimino del sistema");
        } else {
            System.out.println("La Ciudad con codigo postal " + codigo + " no existe. ERROR");
            escribirEnLog("NO se pudo eliminar la Ciudad con codigo postal " + codigo);
        }
    }

    public static void editarCiudad() {
        System.out.println("Ingrese el codigo postal de la ciudad que quiere editar: ");
        String codigoPostal = sc.nextLine();
        codigoPostal = sc.nextLine();
        Ciudad aux = (Ciudad) ciudades.obtenerDato(codigoPostal);
        if (aux != null) {
            System.out.println("<> 1. Editar nombre.\n<> 2. Editar provincia.");
            int respuesta = sc.nextInt();
            switch (respuesta) {
                case 1:
                    System.out.println("Ingrese el nuevo nombre:");
                    String nuevoNomb = sc.nextLine();
                    nuevoNomb = sc.nextLine();
                    aux.setNombre(nuevoNomb);
                    System.out.println("Nombre de la ciudad editado con exito.");
                    escribirEnLog("La Ciudad con codigo postal " + codigoPostal + " ahora se llama " + nuevoNomb);
                    break;
                case 2:
                    System.out.println("Ingrese la nueva provincia:");
                    String nuevaProv = sc.nextLine();
                    nuevaProv = sc.nextLine();
                    aux.setProvincia(nuevaProv);
                    System.out.println("Provincia de la ciudad editada con exito");
                    escribirEnLog("La Ciudad con codigo postal " + codigoPostal + " ahora pertenece a: " + nuevaProv);
                    break;
                default:
                    System.out.println("RESPUESTA INVALIDA, volviendo al ABM.");
                    break;
            }
        } else {
            System.out.println("La Ciudad con codigo postal " + codigoPostal + " no existe. ERROR");
        }
    }

    public static void ABMRutas() {
        int respuesta;
        do {
            System.out.println("------------------------------------ABMRutas-----------------------------------");
            System.out.println(
                    "<> 1. Agregar una ruta. \n<> 2. Eliminar una ruta. \n<> 3. Editar los kilometros de una ruta.\n<> 4. Volver al menu.");
            respuesta = sc.nextInt();
            switch (respuesta) {
                case 1:
                    agregarRuta();
                    break;
                case 2:
                    eliminarRuta();
                    break;
                case 3:
                    editarRuta();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("RESPUESTA INVALIDA.");
                    break;
            }
        } while (respuesta != 4);
    }

    public static void agregarRuta() {
        System.out.println("Ingrese el codigo postal de la ciudad origen de la nueva ruta: ");
        int codPostal1 = sc.nextInt();
        System.out.println("Ingrese el codigo postal de la ciudad destino de la nueva ruta: ");
        int codPostal2 = sc.nextInt();
        System.out.println("Ingrese la distancia en kilometros entre las 2 ciudades: ");
        double kilometros = sc.nextDouble();
        if (!mapaRutas.existeArco(codPostal1, codPostal1)) {
            cargarMapa(codPostal1, codPostal2, kilometros);
            System.out.println("RUTA INSERTADA CON EXITO.");
            escribirEnLog("Se creo la ruta desde: " + codPostal1 + " hasta " + codPostal2);
        } else {
            System.out.println("LA RUTA NO PUDO SER INSERTADA.");
        }
    }

    public static void eliminarRuta() {
        System.out.println("Ingrese el codigo postal de la ciudad origen de la ruta a eliminar: ");
        int codPostal1 = sc.nextInt();
        System.out.println("Ingrese el codigo postal de la ciudad destino de la ruta a eliminar: ");
        int codPostal2 = sc.nextInt();
        if (mapaRutas.existeArco(codPostal1, codPostal2)) {
            mapaRutas.eliminarArco(codPostal1, codPostal2);
            System.out.println("RUTA ELIMINADA CON EXITO.");
            escribirEnLog("Se elimino la ruta desde: " + codPostal1 + " hasta " + codPostal2);
        } else {
            System.out.println("LA RUTA NO PUDO SER ELIMINADA.");
        }
        ;
    }

    public static void editarRuta() {
        System.out.println("Ingrese el codigo postal de la ciudad origen de la ruta a editar: ");
        int codPostal1 = sc.nextInt();
        System.out.println("Ingrese el codigo postal de la ciudad destino de la ruta a editar: ");
        int codPostal2 = sc.nextInt();
        System.out.println("Ingrese la nueva distancia en kilometros entre las 2 ciudades: ");
        double kilometros = sc.nextDouble();
        if (mapaRutas.existeArco(codPostal1, codPostal2)) {
            mapaRutas.eliminarArco(codPostal1, codPostal2);
            cargarMapa(codPostal1, codPostal2, kilometros);
            escribirEnLog("Se actualizaron los kilometros a: " + kilometros + " de la ruta desde " + codPostal1+ " hasta " + codPostal2);
            System.out.println("RUTA EDITADA CON EXITO.");
        } else {
            System.out.println("LA RUTA NO PUDO SER EDITADA.");
        }
    }

    public static void consultarCliente() {
        System.out.println("Ingrese TIPO de Dni del cliente a consultar");
        String tipo = sc.next();
        System.out.println("Ingrese Dni del cliente a consultar");
        String dni = sc.next();

        if (clientes.containsKey(tipo + dni)) {
            System.out.println("INFORMACION DE: " + clientes.get(tipo + dni).getNombre());
            System.out.println(clientes.get(tipo + dni).toString());
        } else {
            System.out.println("CLIENTE NO ENCONTRADO");
        }
    }

    public static void consultaCiudades() {
        int respuesta;
        do {
            System.out.println("-------------------------CONSULTA DE CIUDADES--------------------------");
            System.out.println(
                    "<> 1. Mostrar toda la informacion de una ciudad.\n<> 2. Mostrar ciudades con un prefijo particular.\n<> 3. Volver al menu.");
            respuesta = sc.nextInt();
            switch (respuesta) {
                case 1:
                    informacionDeUnaCiudad();
                    break;
                case 2:
                    ciudadesConPrefijo();
                    break;
                case 3:
                    break; // VUELVE AL MENU
                default:
                    System.out.println("RESPUESTA INVALIDA.");
                    break;
            }
        } while (respuesta != 3);
    }

    public static void informacionDeUnaCiudad() {
        System.out.println("Ingrese el codigo de la ciudad");
        int codigo = sc.nextInt();
        Ciudad encontrada = (Ciudad) ciudades.obtenerDato(codigo);
        if (encontrada != null) {
            System.out.println("LA CIUDAD ES: ");
            System.out.println(encontrada.toString());
        } else {
            System.out.println("CIUDAD NO ENCONTRADA");
        }
    }

    public static void ciudadesConPrefijo() {
        Lista listaCiudad;
        System.out.println("Ingrese el prefijo del codigo de la ciudades: ");
        int prefijo = sc.nextInt();

        if (prefijo < 10) {
            listaCiudad = ciudades.listarRango(prefijo * 1000, (prefijo * 1000) + 999);
        } else if (prefijo < 100) {
            listaCiudad = ciudades.listarRango(prefijo * 100, (prefijo * 100) + 99);
        } else if (prefijo < 1000) {
            listaCiudad = ciudades.listarRango(prefijo * 10, (prefijo * 10) + 9);
        } else {
            listaCiudad = ciudades.listarRango(prefijo, prefijo);
        }
        if (listaCiudad.esVacia()) {
            System.out.println("No existen Ciudades que el prefijo de su codigo postal sea: " + prefijo);
        } else {
            System.out.println("Ciudades con prefijo " + prefijo + " en su codigo postal: \n" + listaCiudad.toString());
        }
    }

    public static void consultasViajes() {
        int respuesta;
        do {
            System.out.println("-------------------------CONSULTA DE VIAJES--------------------------");
            System.out.println(
                    "<> 1. Obtener el camino que llegue de A a B que pase por menos ciudades.\n<> 2. Obtener el camino que llegue de A a B de menor distancia en kilómetros.\n<> 3. Obtener todos los caminos posibles de A a B que pasen por una"
                            +
                            " ciudad C sin pasar dos veces por la misma ciudad.\n<> 4. Verificar si es posible llegar de A a B recorriendo como máximo una cantidad X de kilómetros \n<> 5. Volver al Menu");
            respuesta = sc.nextInt();
            switch (respuesta) {
                case 1:
                    caminoMenosCiudades();
                    break;
                case 2:
                    caminoMenosKilometros();
                    break;
                case 3:// camino de A a B que pase por C sin pasar 2 veces por la misma ciudad
                    break;
                case 4:// llegar de A a B con X
                    break;
                case 5:// VUELVE AL MENU
                    break;
                default:
                    System.out.println("RESPUESTA INVALIDA.");
                    break;
            }
        } while (respuesta != 5);
    }

    public static void caminoMenosCiudades() {
        System.out.println("Ingrese el codigo de la ciudad A inicial");
        int codigoA = sc.nextInt();
        System.out.println("Ingrese el codigo de la ciudad B final");
        int codigoB = sc.nextInt();

        Lista camino = mapaRutas.caminoMasCorto(codigoA, codigoB);
        if (camino != null) {
            System.out.println("EL CAMINO QUE PASA POR MENOS CIUDADES ES: \n" + camino.toString());
        } else {
            System.out.println("NO EXISTE UN CAMINO ENTRE AMBAS CIUDADES");
        }
    }

    public static void caminoMenosKilometros() {
        System.out.println("Ingrese el codigo de la ciudad A inicial");
        int codigoA = sc.nextInt();
        System.out.println("Ingrese el codigo de la ciudad B final");
        int codigoB = sc.nextInt();

        Lista camino = mapaRutas.caminoMasRapido(codigoA, codigoB);
        if (camino != null) {
            System.out.println("EL CAMINO QUE TIENE MENOS KM ES: \n" + camino.toString());
        } else {
            System.out.println("NO EXISTE UN CAMINO ENTRE AMBAS CIUDADES");
        }
    }

    public static void verificarViaje() {
        int respuesta;
        do {
            System.out.println("-------------------VERIFICAR VIAJES-------------------");
            System.out.println(
                    "<> 1. Dada una ciudad A y una ciudad B mostrar todos los pedidos y calcular cuanto espacio total hace falta en el camion"
                            +
                            "\n<> 2.Verificar si sobra espacio para solicitudes intermedias.\n<> 3.Verificar un camino perfecto usando una lista. \n<> 4.Volver al Menu");
            respuesta = sc.nextInt();
            switch (respuesta) {
                case 1:
                    pedidosYCalcularEspacio();
                    break;
                case 2: // espacioSobrante();
                    break;
                case 3:
                    caminoPerfecto();
                    break;
                case 4:// volver al menu
                    break;
                default:
                    System.out.println("RESPUESTA INVALIDA.");
                    break;
            }
        } while (respuesta != 4);
    }

    public static void pedidosYCalcularEspacio() {
        System.out.println("Ingrese el codigo de la ciudad A inicial");
        int codigoA = sc.nextInt();
        System.out.println("Ingrese el codigo de la ciudad B final");
        int codigoB = sc.nextInt();
        Lista listaSolicitudes = solicitudes.obtenerValores(codigoA + "" + codigoB);
        if (mapaRutas.existeVertice(codigoA) && mapaRutas.existeVertice(codigoB)) {
            if (!listaSolicitudes.esVacia()) {
                int espacio = 0;
                System.out.println("Las solicitudes son:\n " + listaSolicitudes.toString());
                for (int i = 1; i <= listaSolicitudes.longitud(); i++) {
                    Solicitud solicitud = (Solicitud) listaSolicitudes.recuperar(i);
                    espacio = espacio + solicitud.getCantMetrosCubicos();
                }
                System.out.println("El espacio necesario de: " + codigoA + " a " + codigoB + " en el camion es de: "
                        + espacio + " m3");
            } else {
                System.out.println("NO HAY PEDIDOS ENTRE LAS CIUDADES");
            }
        } else {
            System.out.println("LOS CODIGOS DE CIUDADES SON INCORRECTOS");
        }
    }

    public static void caminoPerfecto() {
        int capacidad, codigo, longitud, i = 1;
        boolean exito = true;
        Lista listaCodigo = new Lista();
        System.out.println(
                "Dada una lista de Ciudades y una cantidad de metros cubicos que corresponden a capacidad del camion, verificar si es un “camino perfecto”");
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Ingrese la cantidad de metros cubicos de capacidad del camion");
        capacidad = sc.nextInt();
        System.out.println("Ingrese una cantidad de Ciudades");
        longitud = sc.nextInt();
        if (longitud > 1) {
            while (i <= longitud && exito) {
                System.out.println("Ingrese el codigo postal de la Ciudad a insertar en la Lista");
                codigo = sc.nextInt();
                if (mapaRutas.existeVertice(codigo)) {
                    listaCodigo.insertar(codigo, i);
                } else {
                    exito = false;
                }
                i++;
            }
            if (exito) {
                verificarCaminoPerfecto(listaCodigo, capacidad);
            } else {
                System.out.println("Una de las Ciudades ingresadas no existe en el sistema. ERROR");
            }
        } else {
            System.out.println("Ingrese un numero valido de Ciudades");
        }
    }

    public static void verificarCaminoPerfecto(Lista lista, int capacidad) {
        int i = 1, ocupacion = 0;
        boolean exito = true;
        while (i < lista.longitud() && exito) {
            int origen = (int) lista.recuperar(i), destino = (int) lista.recuperar(i + 1);
            if (mapaRutas.existeArco(origen, destino)) {
                Lista listaPedidos = solicitudes.obtenerValores(origen + "" + destino);
                if (listaPedidos.esVacia()) {
                    System.out.println(
                            "No es camino perfecto ya que no existen Pedidos entre " + origen + " y " + destino);
                    exito = false;
                } else {
                    Solicitud solicitud = (Solicitud) listaPedidos.recuperar(1);
                    ocupacion = ocupacion + solicitud.getCantMetrosCubicos();
                }
            } else {
                exito = false;
                System.out.println(
                        "No es camino perfecto ya que no existe una ruta entre la Ciudad " + origen + " y " + destino);
            }
            i++;
        }
        if (exito) {
            if (capacidad >= ocupacion) {
                System.out.println("Es camino perfecto");
            } else {
                System.out.println("No es camino perfecto ya que la capacidad del camion no soporta todos los pedidos");
            }
        }
    }

    public static void mostrarSistema() {
        int respuesta;
        do {
            System.out.println("<> 1. Mostrar las ciudades.\n<> 2. Mostrar las rutas.\n<> 3. Mostrar los pedidos" +
                    "\n<> 4. Mostrar los clientes.\n<> 5. Volver al menu.");
            respuesta = sc.nextInt();
            switch (respuesta) {
                case 1:
                    System.out.println("------------SISTEMA DE CIUDADES------------");
                    System.out.println(ciudades.toString());
                    break;
                case 2:
                    System.out.println("------------SISTEMA DE RUTAS------------");
                    System.out.println(mapaRutas.toString());
                    break;
                case 3:
                    System.out.println("------------SISTEMA DE PEDIDOS------------");
                    System.out.println(solicitudes.toString());
                    break;
                case 4:
                    System.out.println("------------SISTEMA DE CLIENTES------------");
                    System.out.println(clientes.toString());
                    break;
                case 5:
                    break;
                default:
                    System.out.println("RESPUESTA INVALIDA.");
                    break;
            }
        } while (respuesta != 5);

    }
}
