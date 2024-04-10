public class NodoAVLMapeoM {
    
    private Comparable dominio;
    private Lista rango;
    private int altura;
    private NodoAVLMapeoM izquierdo;
    private NodoAVLMapeoM derecho;
    
    public NodoAVLMapeoM (Comparable dominio, Object rango, NodoAVLMapeoM izquierdo, NodoAVLMapeoM derecho) {
        this.dominio = dominio;
        this.rango = new Lista();
        this.rango.insertar(rango, 1);
        this.izquierdo = izquierdo;
        this.derecho = derecho;
    }
    
    public Comparable getDominio() {
        return dominio;
    }
    
    public void setDominio(Comparable dominio) {
        this.dominio = dominio;
    }
    
    public Lista getRango() {
        return rango;
    }
    
    public void setRango(Lista rango) {
        this.rango = rango;
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
    
    public NodoAVLMapeoM getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoAVLMapeoM izquierdo) {
        this.izquierdo = izquierdo;
        recalcularAltura();
    }

    public NodoAVLMapeoM getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoAVLMapeoM derecho) {
        this.derecho = derecho;
        recalcularAltura();
    }
    
}
