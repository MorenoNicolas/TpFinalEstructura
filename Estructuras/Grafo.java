package estructuras;

public class Grafo {
    
    private NodoVert inicio;

    public Grafo() {
        this.inicio = null;
    }

    public boolean insertarVertice(Object elem){
        boolean exito = false;
        NodoVert aux = ubicarVertice(elem);
        if(aux == null){
            inicio = new NodoVert(elem, inicio);
            exito = true;
        }
        return exito;
    }
    private NodoVert ubicarVertice(Object buscado){
        NodoVert aux = this.inicio;
        while (aux != null && !aux.getElem().equals(buscado)) {
            aux = aux.getSigVertice();
        }
        return aux;
    }

    public boolean eliminarVertice(Object elem){
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
        // Dado un elemento, devuelve verdadero si esta en la estructura y falso en caso contrario
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
    public boolean insertarArco(Object origen, Object destino, double etiqueta){
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
    public boolean eliminarArco(Object origen, Object destino){
        boolean exito = false;
        if(inicio != null){
            NodoVert origenAux = ubicarVertice(origen);
            NodoVert destinoAux = ubicarVertice(destino);
            if(origenAux != null && destinoAux != null && (!origen.equals(destino))){
                boolean exitoOrigen = eliminarArcoAux(origenAux , destinoAux);
                boolean exitoDestino = eliminarArcoAux(origenAux, destinoAux);
                exito = exitoOrigen && exitoDestino;
            }
        }
        return exito;
    }
    private boolean eliminarArcoAux(NodoVert origen, Object destino){
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
    public boolean existeArco(Object origen, Object destino){
        boolean exito = false;
        if(inicio != null){
            NodoVert origenAux = ubicarVertice(origen);
            NodoVert destinoAux = ubicarVertice(destino);
            if(origenAux != null && destinoAux != null && (!origen.equals(destino))){
                exito = existeArcoAux(origenAux , destinoAux);
            }
        }
        return exito;
    }
    private boolean existeArcoAux(NodoVert origen, Object destino){
        boolean resultado = false;
        if (origen.getPrimerAdy().getVertice().getElem().equals(destino)) {
            resultado = true;
        } else {
            NodoAdy aux = origen.getPrimerAdy();
            while (aux.getSigAdyacente() != null && !resultado) {
                if (aux.getSigAdyacente().getVertice().getElem().equals(destino)) {
                    resultado = true;
                } else {
                    aux = aux.getSigAdyacente();
                }
            }
        }
        return resultado; 
    }
    
    public boolean existeCamino(Object origen, Object destino) {
        /* Dados dos elementos de TipoVertice (origen y destino), devuelve verdadero
        si existe al menos un camino que permite llegar del vertice origen al
        vertice destino y falso en caso contrario */
        boolean exito = false;
        if (this.inicio != null) {
            NodoVert origenAux = ubicarVertice(origen);
            NodoVert destinoAux = ubicarVertice(destino);
            if (origenAux != null && destinoAux != null) {
                // Si ambos vertices existen, verificamos si existe camino entre ellos
                Lista visitados = new Lista();
                exito = existeCaminoAux(origenAux, destino, visitados);
            }
        }
        return exito;
    }

    private boolean existeCaminoAux(NodoVert nodo, Object destino, Lista visitados) {
        boolean exito = false;
        if (nodo != null) {
            if (nodo.getElem().equals(destino)) {
                // Si vertice nodo es el destino, hay camino
                exito = true;
            } else {
                // Si no es el destino, verifica si hay camino entre nodo y destino
                visitados.insertar(nodo.getElem(), visitados.longitud() + 1);
                NodoAdy ady = nodo.getPrimerAdy();
                while (!exito && ady != null) {
                    if (visitados.localizar(ady.getVertice().getElem()) < 0) {
                        exito = existeCaminoAux(ady.getVertice(), destino, visitados);
                    }
                    ady = ady.getSigAdyacente();
                }
            }
        }
        return exito;
    }
}
