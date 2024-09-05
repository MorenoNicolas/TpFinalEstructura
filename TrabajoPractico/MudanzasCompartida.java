package TrabajoPractico;

import java.io.BufferedReader;
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
            logWriter = new FileWriter(rutaLog, false); // false para permitir agregar registros al archivo existente
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
            boolean yaExiste = mapaRutas.existeArco(codigoOrigen, codigoDestino);
            if (yaExiste) {
                escribirEnLog("No se pudo cargar la Ruta entre " + codigoOrigen + " y " + codigoDestino
                        + ", ya que ya existe una ruta entre ambas");
            } else {
                mapaRutas.insertarArco(codigoOrigen, codigoDestino, etiqueta);
                escribirEnLog("Se cargo la Ruta de " + codigoOrigen + " a " + codigoDestino + " con una distancia de "
                        + etiqueta + " kilometros");
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
                case 3:
                    ABMClientes();
                    break;
                case 4:
                    ABMPedidos();
                    break;
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
            escribirEnLog("Se actualizaron los kilometros a: " + kilometros + " de la ruta desde " + codPostal1
                    + " hasta " + codPostal2);
            System.out.println("RUTA EDITADA CON EXITO.");
        } else {
            System.out.println("LA RUTA NO PUDO SER EDITADA.");
        }
    }

    public static void ABMClientes() {
        int respuesta;
        do {
            System.out.println("------------------------------------ABMClientes-----------------------------------");
            System.out.println(
                    "<> 1. Agregar un cliente. \n<> 2. Eliminar un cliente. \n<> 3. Editar un cliente.\n<> 4. Volver al menu.");
            respuesta = sc.nextInt();
            switch (respuesta) {
                case 1:
                    agregarCliente();
                    break;
                case 2:
                    eliminarCliente();
                    break;
                case 3:
                    editarCliente();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("RESPUESTA INVALIDA.");
                    break;
            }
        } while (respuesta != 4);
    }

    public static void eliminarCliente() {
        System.out.println("Ingrese el tipo DNI del cliente a eliminar: ");
        String tipoDni = sc.next().toUpperCase();
        System.out.println("Ingrese el DNI del cliente a eliminar:");
        String dni = sc.next();
        if (clientes.containsKey(tipoDni + dni)) {
            Cliente aux = clientes.get(tipoDni + dni);
            escribirEnLog("Se elimino el cliente: " + aux.getNombre() + " " + aux.getApellido());
            clientes.remove(tipoDni + dni);
            System.out.println("EL CLIENTE FUE ELIMINADO CON EXITO");
        } else {
            System.out.println("EL CLIENTE NO ESTA INGRESADO, POR ENDE NO FUE ELIMINADO");
        }
    }

    public static void editarCliente() {
        System.out.println("Ingrese el tipo DNI del cliente a editar: ");
        String tipoDni = sc.next().toUpperCase();
        System.out.println("Ingrese el DNI del cliente a editar:");
        String dni = sc.next();
        if (clientes.containsKey(tipoDni + dni)) {
            System.out.println(
                    "<> 1. Editar nombre. \n<> 2. Editar apellido. \n<> 3. Editar numero de telefono.\n<> 4. Editar e-mail.\n<> 5. Volver al ABMClientes");
            int respuesta = sc.nextInt();
            switch (respuesta) {
                case 1:
                    System.out.println("Ingrese el nuevo nombre del cliente (en un solo mensaje): ");
                    String nombre = sc.next();
                    clientes.get(tipoDni + dni).setNombre(nombre);
                    escribirEnLog("Se actualizo el nombre de: " + tipoDni + ": " + dni + " A " + nombre);
                    System.out.println("EL NOMBRE FUE ACTUALIZADO CON EXITO");
                    break;
                case 2:
                    System.out.println("Ingrese el nuevo apellido del cliente (en un solo mensaje): ");
                    String apellido = sc.next();
                    clientes.get(tipoDni + dni).setApellido(apellido);
                    escribirEnLog("Se actualizo el apellido de: " + tipoDni + ": " + dni + " A " + apellido);
                    System.out.println("EL APELLIDO FUE ACTUALIZADO CON EXITO");
                    break;
                case 3:
                    System.out.println("Ingrese el nuevo numero de telefono del cliente: ");
                    String numero = sc.next();
                    clientes.get(tipoDni + dni).setTelefono(numero);
                    escribirEnLog("Se actualizo el telofono de: " + tipoDni + ": " + dni + " A " + numero);
                    System.out.println("EL NUMERO DE TELEFONO FUE ACTUALIZADO CON EXITO");
                    break;
                case 4:
                    System.out.println("Ingrese el nuevo e-mail del cliente: ");
                    String email = sc.next();
                    clientes.get(tipoDni + dni).setEmail(email);
                    escribirEnLog("Se actualizo el e-mail de: " + tipoDni + ": " + dni + " A " + email);
                    System.out.println("EL EMAIL FUE ACTUALIZADO CON EXITO");
                    break;
                case 5:
                    break;
                default:
                    System.out.println("RESPUESTA INVALIDA.");
                    break;
            }
        } else {
            System.out.println("TIPO DNI O NUMERO INCORRECTO");
        }
    }

    public static void agregarCliente() {
        System.out.println("Ingrese el tipo DNI del nuevo cliente: ");
        String tipoDni = sc.next().toUpperCase();
        System.out.println("Ingrese el DNI del nuevo cliente");
        String dni = sc.next();
        if (!clientes.containsKey(tipoDni + dni)) {
            System.out.println("Ingrese el/los apellido/s del nuevo cliente (en un solo mensaje): ");
            String apellidos = sc.next();
            System.out.println("Ingrese el/los nombre/s del nuevo cliente (en un solo mensaje): ");
            String nombres = sc.next();
            System.out.println("Ingrese el numero de telefono del nuevo cliente: ");
            String telefono = sc.next();
            System.out.println("Ingrese el email del nuevo cliente: ");
            String email = sc.next();
            cargarCliente(tipoDni, 0, apellidos, nombres, telefono, email);
            escribirEnLog("Se agrego el cliente: " + tipoDni + dni);
            System.out.println("CLIENTE AGREGADO CON EXITO.");
        } else {
            System.out.println("EL CLIENTE YA ESTABA INGRESADO, POR ENDE NO FUE AGREGADO.");
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

    public static void ABMPedidos() {
        int respuesta;
        do {
            System.out.println("------------------------------------ABMPedidos-----------------------------------");
            System.out.println(
                    "<> 1. Agregar un pedido. \n<> 2. Eliminar los pedidos de un cliente.\n<> 3. Editar un pedido \n<> 4. Volver al menu.");
            respuesta = sc.nextInt();
            switch (respuesta) {
                case 1:
                    agregarPedido();
                    break;
                case 2:
                    eliminarPedido();
                    break;
                case 3:
                    editarPedido();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("RESPUESTA INVALIDA.");
                    break;
            }
        } while (respuesta != 4);
    }

    public static void editarPedido() {
        System.out.println("Ingrese el tipo de documento del cliente: ");
        String tipoDni = sc.next().toUpperCase();
        System.out.println("Ingrese el numero de documento del ciente: ");
        int dni = sc.nextInt();
        System.out.println("Ingrese la ciudad de origen del pedido (codigo postal): ");
        int ciudadOrigen = sc.nextInt();
        System.out.println("Ingrese la ciudad de destino del pedido (codigo postal): ");
        int ciudadDestino = sc.nextInt();
        System.out.println("Ingrese la fecha que solicito el Pedido");
        String fechaSolicitud = sc.next();
        Solicitud solicitud = (Solicitud) solicitudes.obtenerPedido(ciudadOrigen + "" + ciudadDestino,
                new Solicitud(ciudadOrigen, ciudadDestino, fechaSolicitud, tipoDni, dni, 0, 0, null, null, null));
        if (solicitud != null) {
            String cadena;
            int cantidad, respuesta;
            do {
                System.out.println(
                        "<> 1. Editar la cantidad de metros cubicos.\n<> 2. Editar la cantidad de bultos.\n<> 3. Editar la direccion de retiro"
                                + "\n<> 4. Editar la direccion de entrega.\n<> 5. Editar si esta pago.");
                respuesta = sc.nextInt();
                switch (respuesta) {
                    case 1:
                        System.out.println("Ingrese la cantidad de metros cubicos que ahora ocupa el Pedido");
                        cantidad = sc.nextInt();
                        escribirEnLog(
                                "El Pedido " + solicitud.toString() + " ahora ocupa " + cantidad + " metros cubicos");
                        solicitud.setCantMetrosCubicos(cantidad);
                        break;
                    case 2:
                        System.out.println("Ingrese la cantidad de bultos que ahora compone el Pedido");
                        cantidad = sc.nextInt();
                        escribirEnLog("El Pedido " + solicitud.toString() + " ahora consta de " + cantidad + " bultos");
                        solicitud.setCantBultos(cantidad);
                        break;
                    case 3:
                        System.out.println("Ingrese la nueva direccion de retiro");
                        cadena = sc.next();
                        escribirEnLog("El Pedido " + solicitud.toString() + " ahora se retirara en " + cadena);
                        solicitud.setDomicilioRetiro(cadena);
                        break;
                    case 4:
                        System.out.println("Ingrese la nueva direccion de entrega");
                        cadena = sc.next();
                        escribirEnLog("El Pedido " + solicitud.toString() + " ahora se entregara en " + cadena);
                        solicitud.setDomicilioEntrega(cadena);
                        break;
                    case 5:
                        System.out.println("Ingrese la nueva condicion en la que se encuentra el Pedido (T/F)");
                        cadena = sc.next();
                        escribirEnLog("El Pedido " + solicitud.toString() + " ahora se encuentra pagado?: " + cadena);
                        solicitud.setEstaPago(cadena);
                        break;
                    case 6:
                        break; // SE CORTA EL BUCLE
                    default:
                        System.out.println("Opcion invalida. Por favor ingrese una opcion valida");
                }
            } while (respuesta != 6);
        } else {
            System.out.println("NO SE PUDO ENCONTRAR LA SOLICITUD");
        }
    }

    public static void eliminarPedido() {
        System.out.println("Ingrese el tipo de documento del cliente: ");
        String tipoDni = sc.next().toUpperCase();
        System.out.println("Ingrese el numero de documento del ciente: ");
        int dni = sc.nextInt();
        System.out.println("Ingrese la ciudad de origen del pedido (codigo postal): ");
        int ciudadOrigen = sc.nextInt();
        System.out.println("Ingrese la ciudad de destino del pedido (codigo postal): ");
        int ciudadDestino = sc.nextInt();
        System.out.println("Ingrese la fecha que solicito el Pedido");
        String fechaSolicitud = sc.next();
        if (solicitudes.desasociar(ciudadOrigen + "" + ciudadDestino,
                new Solicitud(ciudadOrigen, ciudadDestino, fechaSolicitud, tipoDni, dni, 0, 0, null, null, null))) {
            System.out.println("SE ELIMINO EL PEDIDO CORRECTAMENTE");
            escribirEnLog("Se cancelo el Pedido de " + (tipoDni + dni) + " entre " + ciudadOrigen + " y "
                    + ciudadDestino + " realizado el " + fechaSolicitud);
        } else {
            System.out.println("EL PEDIDO NO PUDO SER ELIMINADO");
        }
    }

    public static void agregarPedido() {
        System.out.println("Ingrese la ciudad origen del pedido (codigo postal): ");
        int ciudadOrigen = sc.nextInt();
        System.out.println("Ingrese la ciudad destino del pedido (codigo postal): ");
        int ciudadDestino = sc.nextInt();
        if (mapaRutas.existeCamino(ciudadOrigen, ciudadDestino)) {
            System.out.println("Ingrese la fecha del pedido: ");
            String fecha = sc.next();
            System.out.println("Ingrese el tipo DNI del cliente: ");
            String tipoDni = sc.next();
            System.out.println("Ingrese el DNI del cliente");
            int dni = sc.nextInt();
            System.out.println("Ingrese la cantidad de metros cubicos a ocupar en el camion: ");
            int metrosCubicos = sc.nextInt();
            System.out.println("Ingrese la cantidad de bultos del pedido: ");
            int cantBultos = sc.nextInt();
            System.out.println("Ingrese la direccion del domicilio de retiro del pedido: ");
            String direccionRetiro = sc.nextLine();
            direccionRetiro = sc.nextLine();
            System.out.println("Ingrese la direccion del domicilio de entrega del pedido: ");
            String direccionEntrega = sc.nextLine();
            System.out.println("Ingrese si el pedido esta pago o no (T para true, F para false): ");
            String estaPago = sc.next();
            cargarSolicitud(ciudadOrigen, ciudadDestino, fecha, tipoDni, dni, metrosCubicos, cantBultos,
                    direccionRetiro, direccionEntrega, estaPago);
            System.out.println("SE AGREGO EL PEDIDO CORRECTAMENTE");
        } else {
            System.out.println("NO EXISTE UNA RUTA ENTRE LAS CIUDADES, NO SE PUEDE AGREGAR EL PEDIDO.");
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
                case 3:
                    caminoPorCiudadIntermedia();
                    break;
                case 4:
                    caminoXkilometrosMax();
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

    public static void caminoXkilometrosMax() {
        System.out.println("Ingrese el codigo de la ciudad A inicial");
        int codigoA = sc.nextInt();
        System.out.println("Ingrese el codigo de la ciudad B final");
        int codigoB = sc.nextInt();
        System.out.println("Ingrese la cantidad maxima de kilometros que desea recorrer:");
        int cantKm = sc.nextInt();

        double kmCamino = mapaRutas.caminoMasRapidoenKM(codigoA, codigoB, cantKm);
        if (kmCamino < cantKm) {
            System.out.println("ES POSIBLE IR DE " + codigoA + " A " + codigoB + " RECORRIENDO: " + kmCamino + " KM");
        } else {
            System.out.println("NO EXISTE UN CAMINO CON MENOS KM QUE EL INGRESADO ");
        }
    }

    public static void caminoPorCiudadIntermedia() {
        // System.out.println("Ingrese el codigo de la ciudad A inicial");
        // int codigoA = sc.nextInt();
        // System.out.println("Ingrese el codigo de la ciudad C intermedia");
        // int codigoC = sc.nextInt();
        // System.out.println("Ingrese el codigo de la ciudad B final");
        // int codigoB = sc.nextInt();



        //Lista camino = mapaRutas.caminosConIntermedio(5253, 9200, 10200);
        Lista camino = mapaRutas.listarTodosLosCaminos(6000, 5253);
        System.out.println(camino.toString());
        // if (!camino.esVacia()) {
        //     for(int i=0; i<camino.longitud();i++){
        //         Lista aux = (Lista)camino.recuperar(i);
        //         System.out.println("POSIBLES CAMINOS: " + aux.toString());
        //     }
        // } else {
        //     System.out.println("NO EXISTEN CAMINOS");
        // }
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
                case 2:
                    espacioSobrante();
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

    // Dada una ciudad A y una ciudad B y una cantidad en metros cúbicos (espacio en
    // un camión), verificar si sobra espacio en el camión y hacer un listado de
    // posibles
    // solicitudes a ciudades intermedias que se podrían aprovechar a cubrir,
    // considerando el camino más corto en kilómetros
    public static void espacioSobrante() {
        System.out.println("Ingrese el codigo de la ciudad A inicial");
        int codigoA = sc.nextInt();
        System.out.println("Ingrese el codigo de la ciudad B final");
        int codigoB = sc.nextInt();
        System.out.println("Ingrese los metros cubicos del camion ");
        int mtsNecesarios = sc.nextInt();

        int espacio = obtenerEspacio(codigoA, codigoB);
        if (espacio < mtsNecesarios) {
            System.out.println("Hay suficiente espacio para otras solicitudes");
            Lista camino = mapaRutas.caminoMasCorto(codigoA, codigoB);
            for(int i=1;i<camino.longitud()-1;i++){
                Lista listaSolicitudes = solicitudes.obtenerValores(codigoA + "" + camino.recuperar(i));
                if(!listaSolicitudes.esVacia()){
                    System.out.println("Posibles pedidos a despachar son:");
                    for (int j = 1; j <= listaSolicitudes.longitud(); j++) {
                        Solicitud solicitud = (Solicitud) listaSolicitudes.recuperar(j);
                        System.out.println(solicitud.toString());
                    }
                }
                
            }
        } else {
            System.out.println("NO HAY SUFICIENTE ESPACIO PARA MAS PEDIDOS");
        }
    }

    private static int obtenerEspacio(int codigoA, int codigoB) {
        Lista listaSolicitudes = solicitudes.obtenerValores(codigoA + "" + codigoB);
        int espacio = 0;
        if (mapaRutas.existeVertice(codigoA) && mapaRutas.existeVertice(codigoB)) {
            if (!listaSolicitudes.esVacia()) {
                for (int i = 1; i <= listaSolicitudes.longitud(); i++) {
                    Solicitud solicitud = (Solicitud) listaSolicitudes.recuperar(i);
                    espacio = espacio + solicitud.getCantMetrosCubicos();
                }
            }
        }
        return espacio;
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
