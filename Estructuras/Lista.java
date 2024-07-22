package estructuras;

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
            elemento = aux.getElem();
        }
        return elemento;
    }

    public int localizar(Object obj) {
        int retorna = -1;
        int i = 0;
        Nodo temp = cabecera;
        while (retorna == -1 && i < longitud()) {
            if (temp.getElem().equals(obj))
                retorna = i;

            i++;
            temp = temp.getEnlace();
        }
        return retorna + 1;
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
        clon.cabecera = new Nodo(cabecera.getElem(), null);
        Nodo aux2 = clon.cabecera;
        while (aux != null) {
            aux2.setEnlace(new Nodo(aux.getElem(), null));
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
                resultado = resultado + aux.getElem().toString();
                aux = aux.getEnlace();
                if (aux != null) {
                    resultado = resultado + ", \n";
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
                // System.out.println(aux.getElem());
                if (auxNueva == null) {
                    lis.cabecera = new Nodo(aux.getElem(), null);
                    auxNueva = lis.cabecera;
                } else {
                    auxNueva.setEnlace(new Nodo(aux.getElem(), null));
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
        while (cabecera.getElem().equals(x)) {
            cabecera = cabecera.getEnlace();
        }
        while (aux.getEnlace() != null) {
            if (aux.getEnlace().getElem().equals(x)) {
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
        Object viejo = cabecera.getElem();
        Nodo aux = cabecera;
        if (x > 0) {
            cabecera.setElem(nuevo);
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
            cabecera.setElem(nuevo);
            cabecera.setEnlace(new Nodo(viejo, cabecera.getEnlace()));
        }
    }
}