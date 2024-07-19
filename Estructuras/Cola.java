package estructuras;

public class Cola {
    private Nodo frente;
    private Nodo fin;

    public Cola() {
        frente = null;
        fin = null;
    }

    public boolean poner(Object valor) {
        Nodo nuevo = new Nodo(valor, null);
        if (esVacia()) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.setEnlace(nuevo);
            fin = nuevo;
        }
        return true;
    }

    public String toString() {
        String s = "";

        if (esVacia()) {
            s = "Cola vacia";
        } else {
            Nodo aux = frente;
            s = "[";
            while (aux != null) {
                s += aux.getElem().toString();
                aux = aux.getEnlace();
                if (aux != null)
                    s += ",";
            }
            s += "]";
        }
        return s;
    }

    public boolean sacar() {
        boolean exito = true;
        if (esVacia()) {
            exito = false;
        } else {
            this.frente = this.frente.getEnlace();
            if (frente == null)
                fin = null;
        }
        return exito;
    }

    public Object obtenerFrente() {
        return frente.getElem();
    }

    public boolean esVacia() {
        return frente == null;
    }

    public void vaciar(){
        frente = null;
    }

    public Cola clone(){
        Cola culon = new Cola();
        if(!esVacia()){ 
            auxiliar(culon, frente);
        }
        return culon;
    }
    private void auxiliar(Cola culon, Nodo enlace){
        if(enlace!=null){
            auxiliar(culon, enlace.getEnlace());
            culon.frente = new Nodo(enlace.getElem(), frente.getEnlace());
        }
    }

}
