package estructuras;

public class DiccionarioAvl {

    private NodoAVL raiz;

    public DiccionarioAvl() {
        this.raiz = null;
    }

    public boolean insertar(Comparable clave, Object dato) {
        /*
         * Recibe un elemento y lo agrega en el arbol de manera ordenada. Si el
         * elemento ya se encuentra en el árbol no se realiza la inserción. Devuelve
         * verdadero si el elemento se agrega a la estructura y falso en caso contrario
         */
        boolean exito = true;
        if (this.raiz == null) {
            this.raiz = new NodoAVL(clave, dato, null, null);
        } else {
            exito = insertarAux(this.raiz, null, clave, dato);
            this.raiz.recalcularAltura();
        }
        return exito;
    }

    private boolean insertarAux(NodoAVL nodo, NodoAVL padre, Comparable clave, Object dato) {
        // Precondicion: nodo no es nulo
        boolean exito = true;
        if (clave.compareTo(nodo.getClave()) == 0) {
            // Elemento repetido
            exito = false;
        } else if (clave.compareTo(nodo.getClave()) < 0) {
            // elemento es menor a nodo.getClave()
            // Si tiene HI baja a la izquierda, sino lo setea
            if (nodo.getIzquierdo() != null) {
                exito = insertarAux(nodo.getIzquierdo(), nodo, clave, dato);
                // balancear(nodo.getIzquierdo(), nodo);
            } else {
                nodo.setIzquierdo(new NodoAVL(clave, dato, null, null));
            }
        } else if (clave.compareTo(nodo.getClave()) > 0) {
            // elemento es mayor a nodo.getClave()
            // Si tiene HD baja a la derecha, sino lo setea
            if (nodo.getDerecho() != null) {
                exito = insertarAux(nodo.getDerecho(), nodo, clave, dato);
                // balancear(nodo.getDerecho(), nodo);
            } else {
                nodo.setDerecho(new NodoAVL(clave, dato, null, null));
            }
        }
        if (exito) {
            nodo.recalcularAltura();
            balancear(nodo, padre);
            nodo.recalcularAltura();
        }
        return exito;
    }

    private void balancear(NodoAVL nodo, NodoAVL padre) {
        int balanceNodo;
        int balanceHijo;
        balanceNodo = balance(nodo);
        if (nodo != null) {
            if (balanceNodo == 2) {
                // Torcido hacia la izquierda
                balanceHijo = balance(nodo.getIzquierdo());
                if (balanceHijo == 1 || balanceHijo == 0) {
                    // Rotacion simple derecha
                    if (padre == null) { // El nodo a balancear es la raiz
                        this.raiz = rotarDerecha(nodo);
                    } else {
                        if (padre.getIzquierdo().getClave().equals(nodo.getClave())) {
                            padre.setIzquierdo(rotarDerecha(nodo));
                        } else {
                            padre.setDerecho(rotarDerecha(nodo));
                        }
                    }
                } else if (balanceHijo == -1) {
                    // Rotacion doble izquierda-derecha
                    nodo.setIzquierdo(rotarIzquierda(nodo.getIzquierdo()));
                    if (padre == null) {
                        this.raiz = rotarDerecha(nodo);
                    } else {
                        if (padre.getIzquierdo().getClave().equals(nodo.getClave())) {
                            padre.setIzquierdo(rotarDerecha(nodo));
                        } else {
                            padre.setDerecho(rotarDerecha(nodo));
                        }
                    }
                }
            } else if (balanceNodo == -2) {
                // Torcido hacia la derecha
                balanceHijo = balance(nodo.getDerecho());
                if (balanceHijo == -1 || balanceHijo == 0) {
                    // Rotacion simple izquierda
                    if (padre == null) { // El nodo a balancear es la raiz
                        this.raiz = rotarIzquierda(nodo);
                    } else {
                        if (padre.getIzquierdo().getClave().equals(nodo.getClave())) {
                            padre.setIzquierdo(rotarIzquierda(nodo));
                        } else {
                            padre.setDerecho(rotarIzquierda(nodo));
                        }
                    }
                } else if (balanceHijo == 1) {
                    // Rotacion doble derecha-izquierda
                    nodo.setDerecho(rotarDerecha(nodo.getDerecho()));
                    if (padre == null) {
                        this.raiz = rotarIzquierda(nodo);
                    } else {
                        if (padre.getIzquierdo().getClave().equals(nodo.getClave())) {
                            padre.setIzquierdo(rotarIzquierda(nodo));
                        } else {
                            padre.setDerecho(rotarIzquierda(nodo));
                        }
                    }
                }
            }
            nodo.recalcularAltura();
        }

    }

    private NodoAVL rotarIzquierda(NodoAVL r) {
        NodoAVL h = r.getDerecho();
        NodoAVL temp = h.getIzquierdo();
        h.setIzquierdo(r);
        r.setDerecho(temp);
        h.recalcularAltura();
        r.recalcularAltura();
        return h;
    }

    private NodoAVL rotarDerecha(NodoAVL r) {
        NodoAVL h = r.getIzquierdo();
        NodoAVL temp = h.getDerecho();
        h.setDerecho(r);
        r.setIzquierdo(temp);
        h.recalcularAltura();
        r.recalcularAltura();
        return h;
    }

    private int balance(NodoAVL nodo) {
        int balanceNodo;
        int alturaHijoIzquierdo = -1;
        int alturaHijoDerecho = -1;
        if (nodo.getIzquierdo() != null) {
            alturaHijoIzquierdo = nodo.getIzquierdo().getAltura();
        }
        if (nodo.getDerecho() != null) {
            alturaHijoDerecho = nodo.getDerecho().getAltura();
        }
        balanceNodo = alturaHijoIzquierdo - alturaHijoDerecho;
        return balanceNodo;
    }

    public boolean eliminar(Comparable elem) {
        boolean exito = false;
        if (this.raiz != null) {
            exito = eliminarAux(this.raiz, null, elem);
        }
        return exito;
    }

    private boolean eliminarAux(NodoAVL n, NodoAVL padre, Comparable elem) {
        boolean exito = false;
        if (n != null) {
            if ((elem.compareTo(n.getClave())) == 0) {

                if (n.getIzquierdo() == null && n.getDerecho() == null) {
                    // si n no tiene hijos
                    noTieneHijos(padre, elem);
                    exito = true;

                } else if ((n.getIzquierdo() != null ^ n.getDerecho() != null)) {
                    // si n tiene UN hijo
                    tieneUnHijo(n, padre, elem);
                    exito = true;

                } else {
                    // si n tiene ambos hijos
                    if (n.getIzquierdo().getDerecho() != null) {
                        // caso candidato
                        tieneAmbosCandidato(n.getIzquierdo(), null, n);
                    } else {
                        // caso HI
                        tieneAmbosHI(n);
                    }
                    exito = true;
                }

            } else {
                if (elem.compareTo(n.getClave()) < 0) {
                    exito = eliminarAux(n.getIzquierdo(), n, elem);
                }
                if (elem.compareTo(n.getClave()) > 0) {
                    exito = eliminarAux(n.getDerecho(), n, elem);
                }
            }

            if (exito) {
                n.recalcularAltura();
                balancear(n, padre);
                n.recalcularAltura();
            }
        }

        return exito;

    }

    // CASOS DE ELIMINAR

    private void noTieneHijos(NodoAVL padre, Comparable elem) {

        // si no tiene hijos
        if (padre == null) {
            // caso especial si el padre es nulo (raiz)
            this.raiz = null;
        } else {
            if (padre.getIzquierdo() != null) {
                if (padre.getIzquierdo().getClave().compareTo(elem) == 0) {
                    // si n es el HI de padre
                    padre.setIzquierdo(null);
                } else {
                    // si n es HD de padre
                    padre.setDerecho(null);
                }
            } else {
                // si HI es nulo entonces es el HD
                padre.setDerecho(null);
            }
        }

    }

    private void tieneUnHijo(NodoAVL n, NodoAVL padre, Comparable elem) {

        if (n.getIzquierdo() != null) {
            // si n tiene HI
            if (padre == null) {
                // caso especial si el padre es nulo
                this.raiz = n.getIzquierdo();
            } else {
                if (padre.getIzquierdo().getClave().compareTo(elem) == 0) {
                    // si n es el HI de padre
                    padre.setIzquierdo(n.getIzquierdo());
                } else {
                    // si n es el HD de padre
                    padre.setDerecho(n.getIzquierdo());
                }
            }
        }
        if (n.getDerecho() != null) {
            // si n tiene HD
            if (padre == null) {
                // caso especial si el padre es nulo
                this.raiz.setDerecho(n.getDerecho());
            } else {
                if (padre.getIzquierdo().getClave().compareTo(elem) == 0) {
                    // si n es el HI de padre
                    padre.setIzquierdo(n.getDerecho());
                } else {
                    // si n es el HD de padre
                    padre.setDerecho(n.getDerecho());
                }
            }
        }

    }

    private void tieneAmbosCandidato(NodoAVL n, NodoAVL anterior, NodoAVL raiz) {

        if (n.getDerecho() != null) {

            tieneAmbosCandidato(n.getDerecho(), n, raiz);

        } else {

            // encontro el candidato, setea los elementos del nodo a modificar(raiz)
            raiz.setClave(n.getClave());
            raiz.setDato(n.getDato());
            anterior.setDerecho(n.getIzquierdo());
        }

        n.recalcularAltura();
        balancear(n, anterior);
        n.recalcularAltura();

    }

    private void tieneAmbosHI(NodoAVL n) {
        n.setClave(n.getIzquierdo().getClave());
        n.setDato(n.getIzquierdo().getDato());
        n.setIzquierdo(n.getIzquierdo().getIzquierdo());
    }


    public Object obtenerDato(Comparable clave) {
        /*
         * Si en la estructura se encuentra almacenado un elemento con la clave
         * recibida por parametro, devuelve la informacion asociada a ella.
         * Precondicion: si no existe un elemento con esa clave no se puede asegurar
         * el funcionamiento de la operacion
         */
        Object resultado = null;
        if (this.raiz != null) {
            resultado = buscarDato(this.raiz, clave);
            if (resultado == null) {
                resultado = "La clave no existe";
            }
        }
        return resultado;
    }

    private Object buscarDato(NodoAVL nodo, Comparable clave) {
        Object retorno = null;
        if (nodo != null) {
            if (clave.compareTo(nodo.getClave()) == 0) {
                retorno = nodo.getDato();
            } else if (clave.compareTo(nodo.getClave()) < 0) {
                retorno = buscarDato(nodo.getIzquierdo(), clave);
            } else if (clave.compareTo(nodo.getClave()) > 0) {
                retorno = buscarDato(nodo.getDerecho(), clave);
            }
        }
        return retorno;
    }

    public boolean existeClave(Comparable clave) {
        /*
         * Devuelve verdadero si en la estructura se encuentra almacenado un elemento
         * con la clave recibida por parametro, caso contrario devuelve falso
         */
        boolean resultado = false;
        if (this.raiz != null) {
            resultado = existeClaveAux(this.raiz, clave);
        }
        return resultado;
    }

    private boolean existeClaveAux(NodoAVL nodo, Comparable clave) {
        boolean exito = false;
        if (nodo != null) {
            if (clave.compareTo(nodo.getClave()) == 0) {
                exito = true;
            } else if (clave.compareTo(nodo.getClave()) < 0) {
                exito = existeClaveAux(nodo.getIzquierdo(), clave);
            } else if (clave.compareTo(nodo.getClave()) > 0) {
                exito = existeClaveAux(nodo.getDerecho(), clave);
            }
        }
        return exito;
    }

    public Lista listarClaves() {
        /*
         * Recorre la estructura completa y devuelve una lista ordenada con las
         * claves de los elementos que se encuentran almacenados en ella
         */
        Lista lista = new Lista();
        listarClavesAux(this.raiz, lista);
        return lista;
    }

    private void listarClavesAux(NodoAVL nodo, Lista lista) {
        // Recorrido inorden
        if (nodo != null) {
            listarClavesAux(nodo.getIzquierdo(), lista);
            lista.insertar(nodo.getClave(), lista.longitud() + 1);
            listarClavesAux(nodo.getDerecho(), lista);
        }
    }

    public Lista listarDatos() {
        /*
         * Recorre la estructura completa y devuelve una lista ordenada con la
         * informacion asociada de los elementos que se encuentran almacenados en ella
         */
        Lista lista = new Lista();
        listarDatosAux(this.raiz, lista);
        return lista;
    }

    private void listarDatosAux(NodoAVL nodo, Lista lista) {
        // Recorrido inorden
        if (nodo != null) {
            listarDatosAux(nodo.getIzquierdo(), lista);
            lista.insertar(nodo.getDato(), lista.longitud() + 1);
            listarDatosAux(nodo.getDerecho(), lista);
        }
    }

    public Lista listarRango(Comparable elemMinimo, Comparable elemMaximo) {
        /*
         * Recorre parte del arbol (sólo lo necesario) y devuelve una lista ordenada
         * con los elementos que se encuentran en el intervalo [elemMinimo, elemMaximo]
         */
        Lista lista = new Lista();
        listarRangoAux(this.raiz, lista, elemMinimo, elemMaximo);
        return lista;
    }

    private void listarRangoAux(NodoAVL nodo, Lista lista, Comparable minimo, Comparable maximo) {
        if (nodo != null) {
            if (minimo.compareTo(nodo.getClave()) <= 0) {
                listarRangoAux(nodo.getIzquierdo(), lista, minimo, maximo);
            }
            if (minimo.compareTo(nodo.getClave()) <= 0 && maximo.compareTo(nodo.getClave()) >= 0) {
                lista.insertar(nodo.getDato(), lista.longitud() + 1);
            }
            if (maximo.compareTo(nodo.getClave()) >= 0) {
                listarRangoAux(nodo.getDerecho(), lista, minimo, maximo);
            }
        }
    }

    public void vaciar() {
        this.raiz = null;
    }

    @Override
    public String toString() {
        if (this.raiz == null) {
            return "El arbol es vacio";
        } else {
            return stringAux(this.raiz);
        }
    }

    private String stringAux(NodoAVL nodo) {
        String cadena = "";
        if (nodo != null) {
            cadena = "\n" + nodo.getClave() + " | " + nodo.getDato().toString() + " |" + cadena;
            if (nodo.getIzquierdo() != null) {
                cadena = cadena + " HI: (menor): " + nodo.getIzquierdo().getClave() + " |";
            } else {
                cadena = cadena + " HI: (menor): - |";
            }
            if (nodo.getDerecho() != null) {
                cadena = cadena + " HD: (mayor): " + nodo.getDerecho().getClave() + " |";
            } else {
                cadena = cadena + " HD: (mayor): - |";
            }
            cadena = cadena + " Altura: " + nodo.getAltura();
            if (nodo.getIzquierdo() != null) {
                cadena = cadena + stringAux(nodo.getIzquierdo());
            }
            if (nodo.getDerecho() != null) {
                cadena = cadena + stringAux(nodo.getDerecho());
            }
        }
        return cadena;
    }
}
