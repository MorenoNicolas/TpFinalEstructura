package estructuras;

public class DiccionarioAvl {

    private NodoAVL raiz;

    public DiccionarioAvl() {
        this.raiz = null;
    }

    public boolean insertar(Comparable clave, Object dato) {
        /* Recibe un elemento y lo agrega en el arbol de manera ordenada. Si el 
        elemento ya se encuentra en el árbol no se realiza la inserción. Devuelve 
        verdadero si el elemento se agrega a la estructura y falso en caso contrario */
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
            // elemento es menor a nodo.getElem()
            // Si tiene HI baja a la izquierda, sino lo setea
            if (nodo.getIzquierdo() != null) {
                exito = insertarAux(nodo.getIzquierdo(), nodo, clave, dato);
                //balancear(nodo.getIzquierdo(), nodo);
            } else {
                nodo.setIzquierdo(new NodoAVL(clave, dato, null, null));
            }
            nodo.recalcularAltura();
        } else if (clave.compareTo(nodo.getClave()) > 0) {
            // elemento es mayor a nodo.getElem()
            // Si tiene HD baja a la derecha, sino lo setea
            if (nodo.getDerecho() != null) {
                exito = insertarAux(nodo.getDerecho(), nodo, clave, dato);
                //balancear(nodo.getDerecho(), nodo);
            } else {
                nodo.setDerecho(new NodoAVL(clave, dato, null, null));
            }
            nodo.recalcularAltura();
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
            System.out.println("Entra a balancear nodo: "+nodo.getClave());
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

    public boolean eliminar(Comparable clave) {
        /* Recibe el elemento que se desea eliminar y se procede a removerlo del
        arbol. Si no se encuentra, no se puede realizar la eliminación. Devuelve
        verdadero si el elemento se elimina de la estructura y falso en caso contrario */
        boolean resultado = false;
        if (this.raiz != null) {
            resultado = eliminarAux(this.raiz, null, clave);
        }
        return resultado;
    }

    private boolean eliminarAux(NodoAVL nodo, NodoAVL padre, Comparable clave) {
        boolean exito = false;
        if (nodo != null) {
            if (clave.compareTo(nodo.getClave()) == 0) {
                /*  Si es hoja: eliminar segun el caso 1
                    Si tiene un hijo: eliminar segun el caso 2
                    Si tiene ambos hijos: eliminar segun el caso 3
                 */
                if (nodo.getIzquierdo() == null && nodo.getDerecho() == null) {
                    caso1(nodo, padre);
                } else if ((nodo.getIzquierdo() != null && nodo.getDerecho() == null) || (nodo.getIzquierdo() == null && nodo.getDerecho() != null)) {
                    caso2(nodo, padre);
                } else if (nodo.getIzquierdo() != null && nodo.getDerecho() != null) {
                    caso3(nodo, padre);
                }
                exito = true;
            } else if (clave.compareTo(nodo.getClave()) < 0) {
                exito = eliminarAux(nodo.getIzquierdo(), nodo, clave);
            } else if (clave.compareTo(nodo.getClave()) > 0) {
                exito = eliminarAux(nodo.getDerecho(), nodo, clave);
            }
        }
        if (exito && nodo != null) {
            nodo.recalcularAltura();
            balancear(nodo, padre);
            nodo.recalcularAltura();
        }
        return exito;
    }

    private void caso1(NodoAVL nodo, NodoAVL padre) {
        //  El nodo a eliminar es una hoja
        if (padre != null) {
            if (nodo.getClave().compareTo(padre.getClave()) < 0) {
                // Caso en el que nodo es HI de su padre
                padre.setIzquierdo(null);
            } else if (nodo.getClave().compareTo(padre.getClave()) > 0) {
                // Caso en el que nodo es HD de su padre
                padre.setDerecho(null);
            }
        } else {
            // Caso raiz
            this.raiz = null;
        }
    }

    private void caso2(NodoAVL nodo, NodoAVL padre) {
        // El nodo a eliminar tiene un solo hijo
        if (padre != null) {
            if (nodo.getClave().compareTo(padre.getClave()) < 0) {
                // Caso en el que nodo es HI de su padre
                if (nodo.getIzquierdo() != null) {
                    // Caso en el que el unico hijo de nodo es HI
                    padre.setIzquierdo(nodo.getIzquierdo());
                } else {
                    // Caso en el que el unico hijo de nodo es HD
                    padre.setIzquierdo(nodo.getDerecho());
                }
            } else if (nodo.getClave().compareTo(padre.getClave()) > 0) {
                // Caso en el que nodo es HD de su padre
                if (nodo.getIzquierdo() != null) {
                    // Caso en el que el unico hijo de nodo es HI
                    padre.setDerecho(nodo.getIzquierdo());
                } else {
                    // Caso en el que el unico hijo de nodo es HD
                    padre.setDerecho(nodo.getDerecho());
                }
            }
        } else {
            // Caso raiz
            if (nodo.getIzquierdo() != null) {
                // Caso en el que el unico hijo de la raiz es HI
                this.raiz = this.raiz.getIzquierdo();
            } else {
                // Caso en el que el unico hijo de nodo es HD
                this.raiz = this.raiz.getDerecho();
            }
        }
    }

    private void caso3(NodoAVL nodo, NodoAVL padre) {
        // El nodo a eliminar tiene ambos hijos
        if (padre != null) {
            // Caso NO raiz
            if (nodo.getIzquierdo().getDerecho() != null) {
                // Caso en el que el candidato es el nieto/bisnieto
                NodoAVL nodoAux = nodo.getIzquierdo();
                NodoAVL padreAux = null;
                while (nodoAux.getDerecho() != null) {
                    padreAux = nodoAux;
                    nodoAux = nodoAux.getDerecho();
                }
                nodo.setClave(nodoAux.getClave());
                nodo.setDato(nodoAux.getDato());
                if (nodoAux.getIzquierdo() != null) {
                    padreAux.setDerecho(nodoAux.getIzquierdo());
                } else {
                    padreAux.setDerecho(null);
                }
            } else {
                // Caso en el que el candidato es el hijo
                nodo.setClave(nodo.getIzquierdo().getClave());
                nodo.setDato(nodo.getIzquierdo().getDato());
                if (nodo.getIzquierdo().getIzquierdo() != null) {
                    nodo.setIzquierdo(nodo.getIzquierdo().getIzquierdo());
                } else {
                    nodo.setIzquierdo(null);
                }
            }
        } else {
            // Caso raiz
            if (nodo.getIzquierdo().getDerecho() != null) {
                // Caso en el que el candidato es el nieto/bisnieto
                NodoAVL nodoAux = nodo.getIzquierdo();
                NodoAVL padreAux = null;
                while (nodoAux.getDerecho() != null) {
                    padreAux = nodoAux;
                    nodoAux = nodoAux.getDerecho();
                }
                this.raiz.setClave(nodoAux.getClave());
                this.raiz.setDato(nodoAux.getDato());
                if (nodoAux.getIzquierdo() != null) {
                    padreAux.setDerecho(nodoAux.getIzquierdo());
                } else {
                    padreAux.setDerecho(null);
                }
            } else {
                // Caso en el que el candidato es el hijo
                this.raiz.setClave(nodo.getIzquierdo().getClave());
                this.raiz.setDato(nodo.getIzquierdo().getDato());
                if (nodo.getIzquierdo().getIzquierdo() != null) {
                    this.raiz.setIzquierdo(nodo.getIzquierdo().getIzquierdo());
                } else {
                    this.raiz.setIzquierdo(null);
                }
            }
        }
    }

    public Object obtenerDato(Comparable clave) {
        /* Si en la estructura se encuentra almacenado un elemento con la clave 
        recibida por parametro, devuelve la informacion asociada a ella.
        Precondicion: si no existe un elemento con esa clave no se puede asegurar
        el funcionamiento de la operacion */
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
        /* Devuelve verdadero si en la estructura se encuentra almacenado un elemento
        con la clave recibida por parametro, caso contrario devuelve falso */
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
        /* Recorre la estructura completa y devuelve una lista ordenada con las 
        claves de los elementos que se encuentran almacenados en ella */
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
        /* Recorre la estructura completa y devuelve una lista ordenada con la 
        informacion asociada de los elementos que se encuentran almacenados en ella */
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
        /* Recorre parte del arbol (sólo lo necesario) y devuelve una lista ordenada
        con los elementos que se encuentran en el intervalo [elemMinimo, elemMaximo] */
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
