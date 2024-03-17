public class NodoAVL {
    private Comparable clave;
    private Object dato;
    private int altura;
    private NodoAVL izquierdo;
    private NodoAVL derecho;

    public NodoAVL(Comparable clave, Object dato, NodoAVL izquierdo, NodoAVL derecho) {
        this.clave = clave;
        this.dato = dato;
        this.izquierdo = izquierdo;
        this.derecho = derecho;
    }

    public Comparable getClave() {
        return clave;
    }

    public void setClave(Comparable clave) {
        this.clave = clave;
    }

    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }

    public int getAltura() {
        return altura;
    }

    public void recalcularAltura() {
        int izq = -1, der = -1;
        if (this.izquierdo != null) {
            izq = this.izquierdo.altura;
        }
        if (this.derecho != null) {
            der = this.derecho.altura;
        }
        this.altura = (Math.max(izq, der)) + 1;
    }

    public NodoAVL getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoAVL izquierdo) {
        this.izquierdo = izquierdo;
        recalcularAltura();
    }

    public NodoAVL getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoAVL derecho) {
        this.derecho = derecho;
        recalcularAltura();
    }
}
