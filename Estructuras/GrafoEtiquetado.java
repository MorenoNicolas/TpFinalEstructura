package estructuras;

import java.util.concurrent.TimeUnit;

public class GrafoEtiquetado {

    private NodoVert inicio;

    public GrafoEtiquetado() {
        this.inicio = null;
    }

    public boolean insertarVertice(Object elem) {
        /*
         * Dado un elemento de TipoVertice se lo agrega a la estructura controlando
         * que no se inserten vertices repetidos. Si puede realizar la insercion
         * devuelve verdadero, en caso contrario devuelve falso
         */
        boolean exito = false;
        NodoVert aux = ubicarVertice(elem);
        if (aux == null) {
            this.inicio = new NodoVert(elem, this.inicio);
            exito = true;
        }
        return exito;
    }

    public NodoVert ubicarVertice(Object buscado) {
        NodoVert aux = this.inicio;
        while (aux != null && !aux.getElem().equals(buscado)) {
            aux = aux.getSigVertice();
        }
        return aux;
    }

    public boolean eliminarVertice(Object elem) {
        /*
         * Dado un elemento de TipoVertice se lo quita de la estructura. Si se
         * encuentra el vertice, tambien deben eliminarse todos los arcos que lo
         * tengan como origen o destino. Si se puede realizar la eliminacion con
         * exito devuelve verdadero, en caso contrario devuelve falso
         */
        boolean exito = false;
        if (this.inicio != null) {
            if (this.inicio.getElem().equals(elem)) {
                eliminarVerticeAux(this.inicio.getPrimerAdy(), elem);
                this.inicio = this.inicio.getSigVertice();
                exito = true;
            } else {
                NodoVert aux = this.inicio;
                while (aux != null && !exito) {
                    if (aux.getSigVertice().getElem().equals(elem)) {
                        eliminarVerticeAux(aux.getSigVertice().getPrimerAdy(), elem);
                        aux.setSigVertice(aux.getSigVertice().getSigVertice());
                        exito = true;
                    } else {
                        aux = aux.getSigVertice();
                    }
                }
            }
        }
        return exito;
    }

    private void eliminarVerticeAux(NodoAdy nodo, Object elem) {
        // Modulo que elimina los arcos que tengan como destino a elem
        while (nodo != null) {
            NodoAdy aux = nodo.getVertice().getPrimerAdy();
            if (aux.getVertice().getElem().equals(elem)) {
                nodo.getVertice().setPrimerAdy(aux.getSigAdyacente());
            } else {
                boolean corte = false;
                while (aux != null && !corte) {
                    if (aux.getSigAdyacente().getVertice().getElem().equals(elem)) {
                        aux.setSigAdyacente(aux.getSigAdyacente().getSigAdyacente());
                        corte = true;
                    } else {
                        aux = aux.getSigAdyacente();
                    }
                }
            }
            nodo = nodo.getSigAdyacente();
        }
    }

    public boolean existeVertice(Object buscado) {
        // Dado un elemento, devuelve verdadero si esta en la estructura y falso en caso
        // contrario
        boolean resultado = false;
        if (this.inicio != null) {
            NodoVert aux = this.inicio;
            while (aux != null && !resultado) {
                if (aux.getElem().equals(buscado)) {
                    resultado = true;
                } else {
                    aux = aux.getSigVertice();
                }
            }
        }
        return resultado;
    }

    public boolean insertarArco(Object origen, Object destino, double etiqueta) {
        /*
         * Dados dos elementos de TipoVertice (origen y destino) agrega el arco
         * en la estructura, solo si ambos vertices ya existen en el grafo. Si puede
         * realizar la insercion devuelve verdadero, en caso contrario devuelve falso
         */
        boolean exito = false;
        if (this.inicio != null) {
            NodoVert origenAux = ubicarVertice(origen);
            NodoVert destinoAux = ubicarVertice(destino);
            if (origenAux != null && destinoAux != null && (!origen.equals(destino))) {
                origenAux.setPrimerAdy(new NodoAdy(destinoAux, origenAux.getPrimerAdy(), etiqueta));
                destinoAux.setPrimerAdy(new NodoAdy(origenAux, destinoAux.getPrimerAdy(), etiqueta));
                exito = true;
            }
        }
        return exito;
    }

    public boolean eliminarArco(Object origen, Object destino) {
        /*
         * Dados dos elementos de TipoVertice (origen y destino) se quita de la
         * estructura el arco que une ambos vertices. Si el arco existe y se puede
         * realizar la eliminacion con éxito devuelve verdadero, en caso contrario
         * devuelve falso
         */
        boolean exito = false;
        if (this.inicio != null) {
            NodoVert origenAux = ubicarVertice(origen);
            NodoVert destinoAux = ubicarVertice(destino);
            if (origenAux != null && destinoAux != null && (!origen.equals(destino))) {
                // Si los vertices existen, verifico si tienen un arco y lo elimino
                boolean exitoOrigen = eliminarArcoAux(origenAux, destino);
                boolean exitoDestino = eliminarArcoAux(destinoAux, origen);
                exito = exitoOrigen && exitoDestino;
            }
        }
        return exito;
    }

    private boolean eliminarArcoAux(NodoVert origen, Object destino) {
        boolean resultado = false;
        if (origen.getPrimerAdy().getVertice().getElem().equals(destino)) {
            origen.setPrimerAdy(origen.getPrimerAdy().getSigAdyacente());
            resultado = true;
        } else {
            NodoAdy aux = origen.getPrimerAdy();
            while (aux.getSigAdyacente() != null && !resultado) {
                if (aux.getSigAdyacente().getVertice().getElem().equals(destino)) {
                    aux.setSigAdyacente(aux.getSigAdyacente().getSigAdyacente());
                    resultado = true;
                } else {
                    aux = aux.getSigAdyacente();
                }
            }
        }
        return resultado;
    }

    public boolean existeArco(Object origen, Object destino) {
        /*
         * Dados dos elementos de TipoVertice (origen y destino), devuelve verdadero
         * si existe un arco en la estructura que los une y falso en caso contrario
         */
        boolean resultado = false;
        if (this.inicio != null) {
            NodoVert origenAux = ubicarVertice(origen);
            if (origenAux != null) {
                NodoAdy aux = origenAux.getPrimerAdy();
                while (aux != null && !resultado) {
                    if (aux.getVertice().getElem().equals(destino)) {
                        resultado = true;
                    } else {
                        aux = aux.getSigAdyacente();
                    }
                }
            }
        }
        return resultado;
    }

    public boolean existeCamino(Object origen, Object destino) {
        boolean exito = false;
        if (!origen.equals(destino) && this.inicio != null) {
            NodoVert nOrigen = ubicarVertice(origen);
            NodoVert nDestino = ubicarVertice(destino);
            if (nOrigen != null && nDestino != null) {
                Lista visitados = new Lista();
                exito = existeCaminoAux(nOrigen, destino, visitados);
            } else {
                System.out.println("No se encontraron los nodos origen o destino.");
            }
        }
        return exito;
    }

    private boolean existeCaminoAux(NodoVert nAux, Object destino, Lista visitados) {
        boolean exito = false;
        if (nAux != null) {
            if (nAux.getElem().equals(destino)) {
                exito = true;
            } else {
                visitados.insertar(nAux.getElem(), visitados.longitud() + 1);
                NodoAdy aux = nAux.getPrimerAdy();
                while (!exito && aux != null) {
                    if (visitados.localizar(aux.getVertice().getElem()) < 0) {
                        exito = existeCaminoAux(aux.getVertice(), destino, visitados);
                    }
                    aux = aux.getSigAdyacente();
                }
            }
        }
        return exito;
    }

    public Lista caminoMasCorto(Object origen, Object destino) {
        /*
         * Dados dos elementos de TipoVertice (origen y destino), devuelve un camino
         * (lista de vertices) que indique el camino que pasa por menos vertices que
         * permite llegar del vertice origen al vertice destino. Si hay mas de un
         * camino con igual cantidad de vertices, devuelve cualquiera de ellos. Si
         * alguno de los vertices no existe o no hay camino posible entre ellos
         * devuelve la lista vacia
         */
        Lista visitados = new Lista();
        Lista res = new Lista();
        if (this.inicio != null) {
            NodoVert origenAux = ubicarVertice(origen);
            NodoVert destinoAux = ubicarVertice(destino);
            if (origenAux != null && destinoAux != null) {
                res = caminoMasCortoAux(origenAux, destino, visitados, res);
            }
        }
        return res;
    }

    private Lista caminoMasCortoAux(NodoVert vert, Object destino, Lista visitados, Lista res) {
        if (vert != null) {
            if ((visitados.longitud() < res.longitud()) || res.esVacia()) {
                visitados.insertar(vert.getElem(), visitados.longitud() + 1);

                if (vert.getElem().equals(destino)) {
                    res = visitados.clone();
                    System.out.println("Llego " + res.toString());
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {

                    NodoAdy ady = vert.getPrimerAdy();
                    while (ady != null) {
                        if (visitados.localizar(ady.getVertice().getElem()) < 0) {
                            res = caminoMasCortoAux(ady.getVertice(), destino, visitados, res);
                        }
                        ady = ady.getSigAdyacente();
                    }
                }
                visitados.eliminar(visitados.longitud());// ya lo visite, lo elimino del camino
            }
        }
        return res;
    }

    public Lista caminoMasRapido(Object origen, Object destino) {
        Lista visitados = new Lista();
        Lista res = new Lista();
        double[] menosKM = new double[1];
        menosKM[0] = 0;
        if (this.inicio != null) {
            NodoVert origenAux = ubicarVertice(origen);
            NodoVert destinoAux = ubicarVertice(destino);
            if (origenAux != null && destinoAux != null) {
                res = caminoMasRapidoAux(origenAux, destino, 0, menosKM, visitados, res);
            }
        }
        return res;
    }

    private Lista caminoMasRapidoAux(NodoVert vert, Object destino, double kmAux, double[] menosKM, Lista visitados,
            Lista res) {
        if (vert != null) {
            double km = menosKM[0];
            if (km == 0 || km > kmAux) {
                visitados.insertar(vert.getElem(), visitados.longitud() + 1);
                if (vert.getElem().equals(destino)) {
                    menosKM[0] = kmAux;
                    res = visitados.clone();

                } else {
                    NodoAdy ady = vert.getPrimerAdy();
                    while (ady != null) {
                        if (visitados.localizar(ady.getVertice().getElem()) < 0) {
                            double aux = kmAux + ady.getEtiqueta();
                            res = caminoMasRapidoAux(ady.getVertice(), destino, aux, menosKM, visitados, res);
                        }
                        ady = ady.getSigAdyacente();
                    }
                }
                visitados.eliminar(visitados.longitud());
            }
        }
        return res;
    }

    // Metodo que busca el camino mas corto pero devuelve su kilometraje
    public double caminoMasRapidoenKM(Object origen, Object destino, int KmMax) {
        Lista visitados = new Lista();
        double res = 0;
        double[] menosKM = new double[1];
        if (this.inicio != null) {
            NodoVert origenAux = ubicarVertice(origen);
            NodoVert destinoAux = ubicarVertice(destino);
            if (origenAux != null && destinoAux != null) {
                res = caminoMasRapidoAuxEnKm(origenAux, destino, 0, menosKM, visitados, res, KmMax);
            }
        }
        return res;
    }

    private double caminoMasRapidoAuxEnKm(NodoVert vert, Object destino, double kmAux, double[] menosKM, Lista visitados, double res, int KmMax) {
        if (vert != null && kmAux <= KmMax) {
            visitados.insertar(vert.getElem(), visitados.longitud() + 1);
            if (vert.getElem().equals(destino)) {
                double km = menosKM[0];
                if (km == 0 || km > kmAux) {
                    menosKM[0] = kmAux;
                }
            } else {
                NodoAdy ady = vert.getPrimerAdy();
                while (ady != null) {
                    if (visitados.localizar(ady.getVertice().getElem()) < 0) {
                        double aux = kmAux + ady.getEtiqueta();
                        res = caminoMasRapidoAuxEnKm(ady.getVertice(), destino, aux, menosKM, visitados, res, KmMax);
                    }
                    ady = ady.getSigAdyacente();
                }
            }
            visitados.eliminar(visitados.longitud());
        }
        return menosKM[0];
    }

    // Método que encuentra todos los caminos de A a B pasando por C
    public Lista caminosConIntermedio(Object origen, Object intermedio, Object destino) {
        Lista visitados = new Lista();
        Lista actual = new Lista();
        Lista res = new Lista();
        Lista union = new Lista();

        if (this.inicio != null) {

            NodoVert origenAux = ubicarVertice(origen);
            NodoVert intermedioAux = ubicarVertice(intermedio);
            NodoVert destinoAux = ubicarVertice(destino);

            Lista listaA_C = new Lista();
            Lista listaC_B = new Lista();

            if (origenAux != null && intermedioAux != null && destinoAux != null) {
                // Encontramos todos los caminos de A a C
                listaA_C = listarCaminoAux(origenAux, intermedioAux.getElem(), visitados, actual, res);
                // Si se encontró un camino de A a C, buscamos un camino de C a B
                if (!listaA_C.esVacia()) {
                    listaC_B = listarCaminoAux(intermedioAux, destinoAux.getElem(), listaA_C, actual, res);
                }
                // Si ambos caminos existen, combinamos los resultados
                if (!listaA_C.esVacia() && !listaC_B.esVacia()) {
                    listaA_C.eliminar(listaA_C.longitud());
                    union = listaA_C.concat(listaC_B);
                }
            }
        }
        return union;
    }

    private Lista listarCaminoAux(NodoVert vert, Object destino, Lista visitados, Lista actual, Lista res) {
        if (vert != null) {
            visitados.insertar(vert.getElem(), visitados.longitud() + 1);
            actual.insertar(vert.getElem(), actual.longitud() + 1);
            if (vert.getElem().equals(destino)) {
                if ((actual.longitud() < res.longitud()) || res.esVacia()) {
                    res = actual.clone();
                }
            } else {
                NodoAdy ady = vert.getPrimerAdy();
                while (ady != null) {
                    if (visitados.localizar(ady.getVertice().getElem()) < 0) {
                        res = caminoMasCortoAux(ady.getVertice(), destino, visitados, actual, res);
                    }
                    ady = ady.getSigAdyacente();
                }
            }
            actual.eliminar(actual.longitud()); // ya lo visite, lo elimino del camino
            visitados.eliminar(visitados.longitud());
        }
        return res;
    }

    public Lista caminoMasLargo(Object origen, Object destino) {
        /*
         * Dados dos elementos de TipoVertice (origen y destino), devuelve un camino
         * (lista de vertices) que indique el camino que pasa por más vertices (sin
         * ciclos)
         * que permite llegar del vertice origen al vertice desti.getEtno. Si hay mas de
         * un
         * camino con igual cantidad de vertices, devuelve cualquiera de ellos. Si
         * alguno de los vertices no existe o no hay camino posible entre ellos
         * devuelve la lista vacía
         */
        Lista visitados = new Lista();
        Lista res = new Lista();
        if (this.inicio != null) {
            NodoVert origenAux = ubicarVertice(origen);
            NodoVert destinoAux = ubicarVertice(destino);
            if (origenAux != null && destinoAux != null) {
                res = caminoMasLargoAux(origenAux, destino, visitados, res);
            }
        }
        return res;
    }

    private Lista caminoMasLargoAux(NodoVert vert, Object destino, Lista visitados, Lista res) {
        if (vert != null) {
            visitados.insertar(vert.getElem(), visitados.longitud() + 1);
            if (vert.getElem().equals(destino)) {
                if ((visitados.longitud() > res.longitud()) || res.esVacia()) {
                    res = visitados.clone();
                }
            } else {
                NodoAdy ady = vert.getPrimerAdy();
                while (ady != null) {
                    if (visitados.localizar(ady.getVertice().getElem()) < 0) {
                        res = caminoMasLargoAux(ady.getVertice(), destino, visitados, res);
                    }
                    ady = ady.getSigAdyacente();
                }
            }
            visitados.eliminar(visitados.longitud());
        }
        return res;
    }

    public Lista listarEnProfundidad() {
        // Devuelve una lista con los vertices del grafo visitados segun el recorrido en
        // profundidad
        Lista visitados = new Lista();
        // Define un vertice donde empezar a recorrer
        NodoVert aux = this.inicio;
        while (aux != null) {
            if (visitados.localizar(aux.getElem()) < 0) {
                // Si el vertice todavia no se visito, avanza en profundidad
                listarEnProfundidadAux(aux, visitados);
            }
            aux = aux.getSigVertice();
        }
        return visitados;
    }

    private void listarEnProfundidadAux(NodoVert nodo, Lista visitados) {
        if (nodo != null) {
            // Marca al vertice como visitado
            visitados.insertar(nodo.getElem(), visitados.longitud() + 1);
            NodoAdy ady = nodo.getPrimerAdy();
            while (ady != null) {
                // Visita en profundidad los adyacentes de nodo aun no visitados
                if (visitados.localizar(ady.getVertice().getElem()) < 0) {
                    listarEnProfundidadAux(ady.getVertice(), visitados);
                }
                ady = ady.getSigAdyacente();
            }
        }
    }

    public Lista listarEnAnchura() {
        // Devuelve una lista con los vertices del grafo visitados según el recorrido en
        // anchura
        Lista visitados = new Lista();
        NodoVert aux = this.inicio;
        while (aux != null) {
            if (visitados.localizar(aux.getElem()) < 0) {
                listarEnAnchuraAux(aux, visitados);
            }
            aux = aux.getSigVertice();
        }
        return visitados;
    }

    private void listarEnAnchuraAux(NodoVert verticeInicial, Lista visitados) {
        Cola cola = new Cola();
        visitados.insertar(verticeInicial.getElem(), visitados.longitud() + 1);
        cola.poner(verticeInicial);
        while (!cola.esVacia()) {
            NodoVert aux = (NodoVert) cola.obtenerFrente();
            cola.sacar();
            NodoAdy ady = aux.getPrimerAdy();
            while (ady != null) {
                if (visitados.localizar(ady.getVertice().getElem()) < 0) {
                    visitados.insertar(ady.getVertice().getElem(), visitados.longitud() + 1);
                    cola.poner(ady.getVertice());
                }
                ady = ady.getSigAdyacente();
            }
        }
    }

    public boolean esVacio() {
        return this.inicio == null;
    }

    @Override
    public GrafoEtiquetado clone() {
        /*
         * Genera y devuelve un grafo que es equivalente (igual estructura y
         * contenido de los nodos) al original
         */
        GrafoEtiquetado grafoClon = new GrafoEtiquetado();
        if (this.inicio != null) {
            grafoClon.inicio = new NodoVert(this.inicio.getElem(), null);
            clonarVertices(this.inicio, grafoClon.inicio);
            clonarAdyacentes(this.inicio, grafoClon.inicio);
        }
        return grafoClon;
    }

    private void clonarAdyacentes(NodoVert vertOriginal, NodoVert vertClon) {
        if (vertOriginal != null) {
            if (vertOriginal.getPrimerAdy() != null) {
                vertClon.setPrimerAdy(new NodoAdy(vertOriginal.getPrimerAdy().getVertice(), null,
                        vertOriginal.getPrimerAdy().getEtiqueta()));
                NodoAdy ady1 = vertOriginal.getPrimerAdy();
                NodoAdy ady2 = vertClon.getPrimerAdy();
                while (ady1.getSigAdyacente() != null) {
                    ady2.setSigAdyacente(new NodoAdy(ady1.getSigAdyacente().getVertice(), null,
                            ady1.getSigAdyacente().getEtiqueta()));
                    ady1 = ady1.getSigAdyacente();
                    ady2 = ady2.getSigAdyacente();
                }
            }
            clonarAdyacentes(vertOriginal.getSigVertice(), vertClon.getSigVertice());
        }
    }

    private void clonarVertices(NodoVert vertOriginal, NodoVert vertClon) {
        if (vertOriginal != null) {
            if (vertOriginal.getSigVertice() != null) {
                vertClon.setSigVertice(new NodoVert(vertOriginal.getSigVertice().getElem(), null));
            }
            clonarVertices(vertOriginal.getSigVertice(), vertClon.getSigVertice());
        }
    }

    @Override
    public String toString() {
        /*
         * Con fines de debugging, este metodo genera y devuelve una cadena String
         * que muestra los vertices almacenados en el grafo y que adyacentes tiene cada
         * uno de ellos
         */
        String resultado;
        if (this.inicio != null) {
            resultado = toStringAux(this.inicio);
        } else {
            resultado = "El grafo esta vacio ";
        }
        return resultado;
    }

    private String toStringAux(NodoVert vertice) {
        String cadena = "";
        if (vertice != null) {
            cadena = cadena + vertice.getElem().toString() + " | Adyacentes: ";
            NodoAdy ady = vertice.getPrimerAdy();
            while (ady != null) {
                if (ady.getSigAdyacente() != null) {
                    cadena = cadena + ady.getVertice().getElem().toString() + "(" + ady.getEtiqueta() + ")" + ", ";
                } else {
                    cadena = cadena + ady.getVertice().getElem().toString() + "(" + ady.getEtiqueta() + ")";
                }
                ady = ady.getSigAdyacente();
            }
            cadena = cadena + "\n" + toStringAux(vertice.getSigVertice());
        }
        return cadena;
    }
}
