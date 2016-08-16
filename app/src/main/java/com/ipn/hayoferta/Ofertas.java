package com.ipn.hayoferta;

/**
 * Created by ErikFernando on 11/04/2015.
 */
public class Ofertas {
    private String descripcion;
    private String marca;
    private String imagen;
    private String lat;
    private String lad;

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca){
        this.marca = marca;
    }
    public String getImagen() {
        return imagen;
    }
    public void setImagen(String imagen){
        this.imagen = imagen;
    }
    public String getLat() {
        return lat;
    }
    public void setLat(String lat){
        this.lat = lat;
    }
    public String getLad(){
        return lad;
    }

    public void setLad(String lad) {
        this.lad = lad;
    }
}
