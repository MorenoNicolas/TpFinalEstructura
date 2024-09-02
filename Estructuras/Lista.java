package estructuras;

import TrabajoPractico.Solicitud;

public class Lista {
    private Nodo cabecera;

    public Lista() {
        cabecera = null;
    }

    public boolean insertar(Object elemento, int pos) {
        boolean exito = true;
        int largo = longitud();
        if (pos < 1 || pos > largo + 1) {
            exito = false;
        } else {
            if (pos == 1) {
                cabecera = new Nodo(elemento, cabecera);
            } else {
                Nodo aux = cabecera;
                int i = 1;
                while (i < pos - 1) {
                    aux = aux.getEnlace();
                    i++;
                }
                Nodo nuevo = new Nodo(elemento, aux.getEnlace());
                aux.setEnlace(nuevo);
            }
        }
        return exito;
    }

    public boolean eliminar(int pos) {
        Nodo aux = cabecera;
        boolean exito = true;
        int i = 1;
        if (pos < 1 || pos > longitud() + 1 || this.cabecera == null) {
            exito = false;
        } else {
            if (pos == 1) {
                this.cabecera = cabecera.getEnlace();
            } else {
                while (i < pos - 1) {
                    aux = aux.getEnlace();
                    i++;
                }
                aux.setEnlace(aux.getEnlace().getEnlace());
            }
        }
        return exito;
    }

    public Object recuperar(int pos) {
        Object elemento;
        if (pos < 1 || pos > longitud()) {
            elemento = null;
        } else {
            int i = 1;
            Nodo aux = cabecera;
            while (i < pos) {
                aux = aux.getEnlace();
                i++;
            }
            elemento = aux.getClave();
        }
        return elemento;
    }

    public int localizar(Object elem) {
        int pos = -1, i = 1;
        boolean exito = false;

        Nodo aux = this.cabecera;
        while (i <= longitud() && aux != null && exito == false) {
            if (aux.getClave().equals(elem)) {
                exito = true;
                pos = i;
            } else {
                aux = aux.getEnlace();
                i++;
            }
        }
        return pos;
    }

    public void vaciar() {
        cabecera = null;
    }

    public boolean esVacia() {
        return cabecera == null;
    }

    public Lista clone() {
        Lista clon = new Lista();
        Nodo aux = cabecera.getEnlace();
        clon.cabecera = new Nodo(cabecera.getClave(), null);
        Nodo aux2 = clon.cabecera;
        while (aux != null) {
            aux2.setEnlace(new Nodo(aux.getClave(), null));
            aux = aux.getEnlace();
            aux2 = aux2.getEnlace();
        }
        return clon;
    }

    public int longitud() {
        int i = 0;
        if (cabecera != null) {
            i = 1;
            Nodo enlace = this.cabecera.getEnlace();
            while (enlace != null) {
                enlace = enlace.getEnlace();
                i++;
            }
        }
        return i;
    }

    public String toString() {
        String resultado = "";
        Nodo aux = this.cabecera;
        if (esVacia()) {
            resultado = "La lista esta vacia";
        } else {
            while (aux != null) {
                resultado = resultado + aux.getClave().toString();
                aux = aux.getEnlace();
                if (aux != null) {
                    resultado = resultado + ", ";
                }
            }
        }
        return resultado;
    }

    public Lista obtenerMultiplos(int num) {
        Lista lis = new Lista();
        Nodo auxNueva = null;
        Nodo aux = cabecera;
        int i = 1;
        while (aux != null) {
            if (i % num == 0) {
                // System.out.println(aux.getClave());
                if (auxNueva == null) {
                    lis.cabecera = new Nodo(aux.getClave(), null);
                    auxNueva = lis.cabecera;
                } else {
                    auxNueva.setEnlace(new Nodo(aux.getClave(), null));
                    auxNueva = auxNueva.getEnlace();
                }
            }
            aux = aux.getEnlace();
            i++;
        }
        return lis;
    }

    public void eliminarOcurrencias(Object x) {
        Nodo aux = cabecera;
        while (cabecera.getClave().equals(x)) {
            cabecera = cabecera.getEnlace();
        }
        while (aux.getEnlace() != null) {
            if (aux.getEnlace().getClave().equals(x)) {
                aux.setEnlace(aux.getEnlace().getEnlace());
            } else {
                aux = aux.getEnlace();
            }
        }
    }

    public void cambiarPosicion(int pos1, int pos2) {
        int i = 1;
        if (pos1 > 0 && pos2 > 0 && pos1 != pos2) {
            Nodo aux = cabecera;
            Nodo Npos1 = null;
            Nodo Npos2 = null;
            Nodo anterior = null;
            Nodo anterior2 = null;
            while (aux != null) {// Npos1 == null && Npos2 ==null
                if (i + 1 == pos1) {
                    Npos1 = aux.getEnlace();
                    anterior = aux;
                }
                if (i + 1 == pos2) {
                    anterior2 = aux;
                    Npos2 = aux.getEnlace();
                }
                aux = aux.getEnlace();
                i++;
            }
            if (pos1 == 1) {
                cabecera.setEnlace(Npos2.getEnlace());
                Npos2.setEnlace(cabecera);
                cabecera = cabecera.getEnlace();
            } else if (pos2 == 1) {
                Npos1.setEnlace(Npos2);
                cabecera.setEnlace(Npos1);
                anterior.setEnlace(Npos1.getEnlace());
            } else if (pos1 < pos2) {
                anterior.setEnlace(Npos1.getEnlace());
                Npos1.setEnlace(Npos2.getEnlace());
                Npos2.setEnlace(Npos1);
            } else if (pos1 > pos2) {
                anterior.setEnlace(Npos1.getEnlace());
                Npos1.setEnlace(Npos2);
                anterior2.setEnlace(Npos1);
            }
        }
    }

    public void intercalar(Lista l2) {
        Nodo aux1 = cabecera;
        Nodo aux2 = l2.cabecera;
        Nodo aux3 = cabecera;
        if (aux1 != null && aux2 != null) {
            while (aux1.getEnlace() != null && aux2.getEnlace() != null) {
                aux1 = aux1.getEnlace();
                aux3.setEnlace(aux2);
                aux3 = aux3.getEnlace();
                aux2 = aux2.getEnlace();
                aux3.setEnlace(aux1);
                aux3 = aux3.getEnlace();
            }
            if (aux1.getEnlace() == null) {
                aux3.setEnlace(aux2);
            } else {
                aux2.setEnlace(aux1.getEnlace());
                aux3.setEnlace(aux2);
            }
        }
    }

    public void agregarElemento(Object nuevo, int x) {
        int i = 0;
        Object viejo = cabecera.getClave();
        Nodo aux = cabecera;
        if (x > 0) {
            cabecera.setClave(nuevo);
            cabecera.setEnlace(new Nodo(viejo, cabecera.getEnlace()));
            while (aux != null) {
                if (i == x) {
                    aux.setEnlace(new Nodo(nuevo, aux.getEnlace()));
                    i = -1;
                }
                i++;
                aux = aux.getEnlace();
            }
        } else {
            cabecera.setClave(nuevo);
            cabecera.setEnlace(new Nodo(viejo, cabecera.getEnlace()));
        }
    }
        public Object buscarSolicitud(Object solicitud) {
        Object resultado = null;
        Solicitud nodo;
        Nodo aux = this.cabecera;
        while(aux != null && resultado == null) {
            nodo = (Solicitud) aux.getClave();
            if(nodo.equals((Solicitud)solicitud)) {
                resultado = aux.getClave();
            } else {
                aux = aux.getEnlace();
            }
        }
        return resultado;
    }

    public boolean eliminarSolicitud(Object elem) {
        boolean exito = false;
        Solicitud elemAux = (Solicitud) elem;
        if(this.cabecera != null) {
            Solicitud solicitud = (Solicitud) this.cabecera.getClave();
            if(solicitud.equals(elemAux)) {
                this.cabecera = this.cabecera.getEnlace();
                exito = true;
            } else {
                Nodo aux = this.cabecera;
                while (aux.getEnlace() != null && !exito) {
                    solicitud = (Solicitud) aux.getEnlace().getClave();
                    if(solicitud.equals(elemAux)) {
                        aux.setEnlace(aux.getEnlace().getEnlace());
                        exito = true;
                    } else {
                        aux = aux.getEnlace();
                    }
                }
            }
        }
        return exito;
    }
    public Lista concat(Lista otraLista) {
        Lista nuevaLista = new Lista();
        
        // Insertar todos los elementos de la primera lista en la nueva lista
        for (int i = 1; i <= this.longitud(); i++) {
            nuevaLista.insertar(this.recuperar(i), nuevaLista.longitud() + 1);
        }

        // Insertar todos los elementos de la segunda lista en la nueva lista
        for (int j = 1; j <= otraLista.longitud(); j++) {
            nuevaLista.insertar(otraLista.recuperar(j), nuevaLista.longitud() + 1);
        }

        return nuevaLista;
    }
}
