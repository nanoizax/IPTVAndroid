package com.kevin.piazzoli.iptvandroid;

/**
 * Created by usuario on 11/11/2016.
 */

public class Datos {

    //atributos
    protected String foto;
    protected String titulo;
    protected String info;
    protected  long id;

    public Datos(String foto, String titulo, String info) {

        this.foto = foto;
        this.titulo = titulo;
        this.info = info;
        //this.id = id;

    }

    //Obtener datos con metodo get
    public String getFoto() {
        return foto;
    }
    //asignar datos
    public void setFoto(String foto){
        this.foto = foto;
    }

    public String getTitulo() {
        return titulo;
    }
    //asignar datos
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public String getInfo() {
        return info;
    }
    //asignar datos
    public void setInfo(String cargo){
        this.info = cargo;
    }

    public long getId() {
        return id;
    }
    //asignar datos
    public void setId(long id){
        this.id = id;
    }


}