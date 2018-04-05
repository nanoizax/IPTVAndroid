package com.kevin.piazzoli.iptvandroid;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class AdapterDatos extends BaseAdapter {

    protected Activity activity;
    //dentro del arraylist colocamos la clase datos
    protected ArrayList<Datoss> items;

    public AdapterDatos(Activity activity, ArrayList<Datoss> items){
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemcategorias, null);
        }

        Datoss datos = items.get(position);
        Datoss dir = items.get(position);

        //llenamos la fotografica
        //ImageView foto = (ImageView)v.findViewById(R.id.imageView);
        //foto.setImageDrawable(datos.getFoto());

        //ImageView imagen;
        //imagen = (ImageView) v.findViewById(R.id.imageView);
        //web.loadUrl(datos.getFoto());
        //Glide.with(activity).load(datos.getFoto()).into(imagen);

        // llenamos el titulo
        TextView titulo = (TextView)v.findViewById(R.id.textView_titulo);
        titulo.setText(datos.getTitulo());
        // llenamos la informacion
        //TextView info = (TextView)v.findViewById(R.id.textView_info);
        //info.setText(datos.getInfo());

        return  v;
    }
}