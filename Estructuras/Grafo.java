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
    
}
