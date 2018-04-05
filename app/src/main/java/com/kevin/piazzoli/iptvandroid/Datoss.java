package com.kevin.piazzoli.iptvandroid;

/**
 * Created by usuario on 11/11/2016.
 */

public class Datoss {

    //atributos
    protected String foto;
    protected String titulo;
    protected String info;
    protected  long id;

    public Datoss(String titulo) {

        this.titulo = titulo;

        //this.id = id;

    }

    //Obtener datos con metodo get

    public String getTitulo() {
        return titulo;
    }
    //asignar datos
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public long getId() {
        return id;
    }
    //asignar datos
    public void setId(long id){
        this.id = id;
    }


}