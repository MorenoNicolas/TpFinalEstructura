package estructuras;

 class Nodo {
    private Object element;
    private Nodo enlace;

    public Nodo(){
        this.element =null;
        this.enlace = null;
    }
    public Nodo(Object elem, Nodo enlace){
        this.element = elem;
        this.enlace = enlace;
    }
    public void setClave(Object elem){
        this.element = elem;
    }
    public void setEnlace(Nodo en){
        this.enlace = en;
    }
    public Object getClave(){
        return element;
    }
    public Nodo getEnlace(){
        return enlace;
    }
}
