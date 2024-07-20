package estructuras;

import TrabajoPractico.*;
public class MapeoAMuchos {
    private NodoAVLMapeoM raiz;
    
    public MapeoAMuchos() {
        this.raiz = null;
    }
    
    public boolean asociar(Comparable valorDominio, Object valorRango) {
        /* Recibe un valor que representa a un elemento del dominio y un segundo
        valor que representa a un elemento del rango. Si no existe otro par que 
        contenga a valorDominio, se agrega en el mapeo el par (valorDominio, {valorRango}),
        donde el segundo término es un conjunto de rangos con un solo valor. Si ya
        existe un par con valorDominio, se agrega valorRango al conjunto de rangos existente.
        Si la operación termina con éxito devuelve verdadero y falso en caso contrario
        */
        boolean exito = true;
        if (this.raiz == null) {
            this.raiz = new NodoAVLMapeoM(valorDominio, valorRango, null, null);
        } else {
            exito = asociarAux(this.raiz, null, valorDominio, valorRango);
        }
        return exito;
        
    }
    
    private boolean asociarAux(NodoAVLMapeoM nodo, NodoAVLMapeoM padre, Comparable valorDominio, Object valorRango) {
        // Precondicion: nodo no es nulo
        boolean exito = true;
        if (valorDominio.compareTo(nodo.getDominio()) == 0) {
            // Si ya existe, agregar rango a la lista de rangos
            nodo.getRango().insertar(valorRango, nodo.getRango().longitud() + 1);
        } else if (valorDominio.compareTo(nodo.getDominio()) < 0) {
            // elemento es menor a nodo.getElem()
            // Si tiene HI baja a la izquierda, sino lo setea
            if (nodo.getIzquierdo() != null) {
                exito = asociarAux(nodo.getIzquierdo(), nodo, valorDominio, valorRango);
            } else {
                nodo.setIzquierdo(new NodoAVLMapeoM(valorDominio, valorRango, null, null));
            }
        } else if (valorDominio.compareTo(nodo.getDominio()) > 0) {
            // elemento es mayor a nodo.getElem()
            // Si tiene HD baja a la derecha, sino lo setea
            if (nodo.getDerecho() != null) {
                exito = asociarAux(nodo.getDerecho(), nodo, valorDominio, valorRango);
            } else {
                nodo.setDerecho(new NodoAVLMapeoM(valorDominio, valorRango, null, null));
            }
        }
        if (exito) {
            nodo.recalcularAltura();
            balancear(nodo, padre);
            nodo.recalcularAltura();
        }
        return exito;
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

    private boolean eliminarAux(NodoAVLMapeoM nodo, NodoAVLMapeoM padre, Comparable dominio) {
        boolean exito = false;
        if (nodo != null) {
            if (dominio.compareTo(nodo.getDominio()) == 0) {
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
            } else if (dominio.compareTo(nodo.getDominio()) < 0) {
                exito = eliminarAux(nodo.getIzquierdo(), nodo, dominio);
            } else if (dominio.compareTo(nodo.getDominio()) > 0) {
                exito = eliminarAux(nodo.getDerecho(), nodo, dominio);
            }
        }
        if (exito && nodo != null) {
            nodo.recalcularAltura();
            balancear(nodo, padre);
            nodo.recalcularAltura();
        }
        return exito;
    }
    
    public boolean desasociar(Comparable valorDominio, Object valorRango) {
        /* Elimina valorRango del conjunto de rangos asociado a valorDominio. 
        Caso especial: si al eliminar valorRango del conjunto este quedara vacio, debe
        eliminar el par (valorDominio,{}) del mapeo. Si encuentra el par y la operación
        de eliminación termina con éxito devuelve verdadero y falso en caso contrario */
        boolean resultado = false;
        if (this.raiz != null) {
            resultado = desasociarAux(this.raiz, null, valorDominio, valorRango);
        }
        return resultado;
    }
    
    private boolean desasociarAux(NodoAVLMapeoM nodo, NodoAVLMapeoM padre, Comparable valorDominio, Object valorRango) {
        boolean exito = false;
        if (nodo != null) {
            if (valorDominio.compareTo(nodo.getDominio()) == 0) {
                exito = nodo.getRango().eliminarSolicitud(valorRango);
                if(exito) {
                    if(nodo.getRango().esVacia()) {
                        /*  Si es hoja: eliminar segun el caso 1
                            Si tiene un hijo: eliminar segun el caso 2
                            Si tiene ambos hijos: eliminar segun el caso 3  */
                        if (nodo.getIzquierdo() == null && nodo.getDerecho() == null) {
                            caso1(nodo, padre);
                        } else if ((nodo.getIzquierdo() != null && nodo.getDerecho() == null) || (nodo.getIzquierdo() == null && nodo.getDerecho() != null)) {
                            caso2(nodo, padre);
                        } else if (nodo.getIzquierdo() != null && nodo.getDerecho() != null) {
                            caso3(nodo, padre);
                        }
                    }
                }
            } else if (valorDominio.compareTo(nodo.getDominio()) < 0) {
                exito = desasociarAux(nodo.getIzquierdo(), nodo, valorDominio, valorRango);
            } else if (valorDominio.compareTo(nodo.getDominio()) > 0) {
                exito = desasociarAux(nodo.getDerecho(), nodo, valorDominio, valorRango);
            }
        }
        if (exito && nodo != null) {
            nodo.recalcularAltura();
            balancear(nodo, padre);
            nodo.recalcularAltura();
        }
        return exito;
    }
    
    private void caso1(NodoAVLMapeoM nodo, NodoAVLMapeoM padre) {
        //  El nodo a eliminar es una hoja
        if (padre != null) {
            if (nodo.getDominio().compareTo(padre.getDominio()) < 0) {
                // Caso en el que nodo es HI de su padre
                padre.setIzquierdo(null);
            } else if (nodo.getDominio().compareTo(padre.getDominio()) > 0) {
                // Caso en el que nodo es HD de su padre
                padre.setDerecho(null);
            }
        } else {
            // Caso raiz
            this.raiz = null;
        }
    }

    private void caso2(NodoAVLMapeoM nodo, NodoAVLMapeoM padre) {
        // El nodo a eliminar tiene un solo hijo
        if (padre != null) {
            if (nodo.getDominio().compareTo(padre.getDominio()) < 0) {
                // Caso en el que nodo es HI de su padre
                if (nodo.getIzquierdo() != null) {
                    // Caso en el que el unico hijo de nodo es HI
                    padre.setIzquierdo(nodo.getIzquierdo());
                } else {
                    // Caso en el que el unico hijo de nodo es HD
                    padre.setIzquierdo(nodo.getDerecho());
                }
            } else if (nodo.getDominio().compareTo(padre.getDominio()) > 0) {
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

    private void caso3(NodoAVLMapeoM nodo, NodoAVLMapeoM padre) {
        // El nodo a eliminar tiene ambos hijos
        if (padre != null) {
            // Caso NO raiz
            if (nodo.getIzquierdo().getDerecho() != null) {
                // Caso en el que el candidato es el nieto/bisnieto
                NodoAVLMapeoM nodoAux = nodo.getIzquierdo();
                NodoAVLMapeoM padreAux = null;
                while (nodoAux.getDerecho() != null) {
                    padreAux = nodoAux;
                    nodoAux = nodoAux.getDerecho();
                }
                nodo.setDominio(nodoAux.getDominio());
                nodo.setRango(nodoAux.getRango());
                if (nodoAux.getIzquierdo() != null) {
                    padreAux.setDerecho(nodoAux.getIzquierdo());
                } else {
                    padreAux.setDerecho(null);
                }
            } else {
                // Caso en el que el candidato es el hijo
                nodo.setDominio(nodo.getIzquierdo().getDominio());
                nodo.setRango(nodo.getIzquierdo().getRango());
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
                NodoAVLMapeoM nodoAux = nodo.getIzquierdo();
                NodoAVLMapeoM padreAux = null;
                while (nodoAux.getDerecho() != null) {
                    padreAux = nodoAux;
                    nodoAux = nodoAux.getDerecho();
                }
                this.raiz.setDominio(nodoAux.getDominio());
                this.raiz.setRango(nodoAux.getRango());
                if (nodoAux.getIzquierdo() != null) {
                    padreAux.setDerecho(nodoAux.getIzquierdo());
                } else {
                    padreAux.setDerecho(null);
                }
            } else {
                // Caso en el que el candidato es el hijo
                this.raiz.setDominio(nodo.getIzquierdo().getDominio());
                this.raiz.setRango(nodo.getIzquierdo().getRango());
                if (nodo.getIzquierdo().getIzquierdo() != null) {
                    this.raiz.setIzquierdo(nodo.getIzquierdo().getIzquierdo());
                } else {
                    this.raiz.setIzquierdo(null);
                }
            }
        }
    }
    
    private void balancear(NodoAVLMapeoM nodo, NodoAVLMapeoM padre) {
        int balanceNodo;
        int balanceHijo;
        balanceNodo = balance(nodo);
        if (balanceNodo == 2) {
            // Torcido hacia la izquierda
            balanceHijo = balance(nodo.getIzquierdo());
            if (balanceHijo == 1 || balanceHijo == 0) {
                // Rotacion simple derecha
                if (padre == null) { // El nodo a balancear es la raiz
                    this.raiz = rotarDerecha(nodo);
                } else {
                    padre.setIzquierdo(rotarDerecha(nodo));
                }
            } else if (balanceHijo == -1) {
                // Rotacion doble izquierda-derecha
                nodo.setIzquierdo(rotarIzquierda(nodo.getIzquierdo()));
                if (padre == null) {
                    this.raiz = rotarDerecha(nodo);
                } else {
                    if (padre.getIzquierdo().getDominio().equals(nodo.getDominio())) {
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
                    padre.setDerecho(rotarIzquierda(nodo));
                }
            } else if (balanceHijo == 1) {
                // Rotacion doble derecha-izquierda
                nodo.setDerecho(rotarDerecha(nodo.getDerecho()));
                if (padre == null) {
                    this.raiz = rotarIzquierda(nodo);
                } else {
                    if (padre.getIzquierdo().getDominio().equals(nodo.getDominio())) {
                        padre.setIzquierdo(rotarIzquierda(nodo));
                    } else {
                        padre.setDerecho(rotarIzquierda(nodo));
                    }
                }
            }
        }
        nodo.recalcularAltura();
    }

    private NodoAVLMapeoM rotarIzquierda(NodoAVLMapeoM r) {
        NodoAVLMapeoM h = r.getDerecho();
        NodoAVLMapeoM temp = h.getIzquierdo();
        h.setIzquierdo(r);
        r.setDerecho(temp);
        h.recalcularAltura();
        r.recalcularAltura();
        return h;
    }

    private NodoAVLMapeoM rotarDerecha(NodoAVLMapeoM r) {
        NodoAVLMapeoM h = r.getIzquierdo();
        NodoAVLMapeoM temp = h.getDerecho();
        h.setDerecho(r);
        r.setIzquierdo(temp);
        h.recalcularAltura();
        r.recalcularAltura();
        return h;
    }

    private int balance(NodoAVLMapeoM nodo) {
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
    
    public Object obtenerPedido(Comparable dominio, Solicitud solicitud) {
        Object resultado = null;
        if(this.raiz != null) {
            resultado = buscarPedido(this.raiz, dominio, solicitud);
        }
        return resultado;
    }
    
    private Object buscarPedido(NodoAVLMapeoM nodo, Comparable dominio, Solicitud solicitud) {
        Object retorno = null;
        if (nodo != null) {
            if (dominio.compareTo(nodo.getDominio()) == 0) {
                retorno = nodo.getRango().buscarSolicitud(solicitud);
            } else if (dominio.compareTo(nodo.getDominio()) < 0) {
                retorno = buscarPedido(nodo.getIzquierdo(), dominio, solicitud);
            } else if (dominio.compareTo(nodo.getDominio()) > 0) {
                retorno = buscarPedido(nodo.getDerecho(), dominio, solicitud);
            }
        }
        return retorno;
    }
    
    public Lista obtenerValores(Comparable valorDominio) {
        /* Si en el mapeo se encuentra almacenado algun par cuyo dominio es valorDominio, 
        devuelve el conjunto de valores de rango asociado a el. Precondición: valorDominio
        está en el mapeo (si no existe, no se puede asegurar el funcionamiento de la operación) */
        Lista resultado = new Lista();
        if (this.raiz != null) {
            resultado = buscarValores(this.raiz, valorDominio);
        }
        return resultado;
    }
    
    private Lista buscarValores(NodoAVLMapeoM nodo, Comparable valorDominio) {
        Lista retorno = new Lista();
        if (nodo != null) {
            if (valorDominio.compareTo(nodo.getDominio()) == 0) {
                retorno = nodo.getRango();
            } else if (valorDominio.compareTo(nodo.getDominio()) < 0) {
                retorno = buscarValores(nodo.getIzquierdo(), valorDominio);
            } else if (valorDominio.compareTo(nodo.getDominio()) > 0) {
                retorno = buscarValores(nodo.getDerecho(), valorDominio);
            }
        }
        return retorno;
    }
    
    public Lista obtenerConjuntoDominio() {
        // Devuelve un conjunto con todos los valores de tipo dominio almacenados en el mapeo
        Lista lista = new Lista();
        obtenerConjuntoDominioAux(this.raiz, lista);
        return lista;
    }

    private void obtenerConjuntoDominioAux(NodoAVLMapeoM nodo, Lista lista) {
        // Recorrido inorden
        if (nodo != null) {
            obtenerConjuntoDominioAux(nodo.getIzquierdo(), lista);
            lista.insertar(nodo.getDominio(), lista.longitud() + 1);
            obtenerConjuntoDominioAux(nodo.getDerecho(), lista);
        }
    }
    
    public Lista obtenerConjuntoRango() {
        // Devuelve un conjunto con la unión de todos los valores de tipo rango almacenados en el mapeo
        Lista lista = new Lista();
        obtenerConjuntoRangoAux(this.raiz, lista);
        return lista;
    }

    private void obtenerConjuntoRangoAux(NodoAVLMapeoM nodo, Lista lista) {
        // Recorrido inorden
        if (nodo != null) {
            obtenerConjuntoRangoAux(nodo.getIzquierdo(), lista);
            int i = 1;
            while(i <= nodo.getRango().longitud()) {
                lista.insertar(nodo.getRango().recuperar(i), lista.longitud() + 1);
                i++;
            }
            obtenerConjuntoRangoAux(nodo.getDerecho(), lista);
        }
    }
    
    public boolean esVacio() {
        return this.raiz == null;
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

    private String stringAux(NodoAVLMapeoM nodo) {
        String cadena = "";
        if (nodo != null) {
            cadena = "\n" + nodo.getDominio() + " | " + nodo.getRango().toString() + " |" + cadena;
            if (nodo.getIzquierdo() != null) {
                cadena = cadena + " HI: (menor): " + nodo.getIzquierdo().getDominio() + " |";
            } else {
                cadena = cadena + " HI: (menor): - |";
            }
            if (nodo.getDerecho() != null) {
                cadena = cadena + " HD: (mayor): " + nodo.getDerecho().getDominio() + " |";
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
