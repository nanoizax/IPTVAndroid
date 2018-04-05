package com.kevin.piazzoli.iptvandroid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String IP = "http://izaxwolfapp.com.ve/";
    String GETCategorias = IP + "apis/iptv/query_all_categorias.php";
    String GETSUBCategorias = IP + "apis/iptv/query_all_subcategorias.php";
    String GETPeliculas = IP + "apis/iptv/query_all_peliculas.php";
    String GETPeliculaCategoriaID = IP + "api/alpha/query_id_pelicula.php?categoria=";
    String GETCategoriaID = IP + "api/alpha/query_id_categoria.php?categoria=";
    String GETSeries = IP + "api/alpha/query_all_series.php";
    String GETSeriesID = IP + "api/alpha/query_id_series.php?categoria=";
    String SERIE;
    String temporada = "&temporada=";
    String GETSeriesTemporadasID = IP + "api/alpha/query_id_series_temporadas.php?categoria=";
    String GETCategoriaSeriesID = IP + "api/alpha/query_id_categoria_serie.php?categoria=";
    String SUBCATEGORIA = "&subcategoria=";

    String img2 = "https://www.spreadshirt.es/image-server/v1/mp/designs/16057840,width=178,height=178/cargando-circuito-de-carga-en-negro.png";

    ArrayList<Datoss> arraydatosCategorias = new ArrayList<Datoss>();
    ArrayList<Datoss> arraydatosSubCategorias = new ArrayList<Datoss>();
    ArrayList<Datoss> arraydatosTemporadas = new ArrayList<Datoss>();

    ArrayList<Datos> arraydatosPeliculas = new ArrayList<Datos>();

    Datoss datosCategorias;
    Datoss datosSubCategorias;
    Datoss datosTemporadas;

    Datos datosPeliculas;

    AdapterDatos adapterCategorias;
    AdapterDatos adapterSubCategorias;
    AdapterDatos adapterTemporadas;

    AdapterDatoss adapterPeliculas;

    ObtenerWebServicePeliculas hiloconexionPeliculas;
    ObtenerWebServiceSeries hiloconexionSeries;
    ObtenerWebServiceSeriesBusqueda hiloconexionSeriesBusqueda;
    ObtenerWebServicePeliculasCat hiloconexionPeliculasCat;
    ObtenerWebServicePeliculasCategoria hiloconexionPeliculasCategoria;
    ObtenerWebServiceSubCategorias hiloconexionSubCategorias;
    ObtenerWebServiceCategorias hiloconexionCategorias;
    ObtenerWebServiceTemporadas hiloconexionTemporadas;
    ObtenerWebServicePeliculasCatSeries hiloconexionPeliculasCatSeries;

    Integer contador = 0;
    Integer tiempo;
    Integer tiempoInicial = 1000;
    Integer tiempoFinal = 10000000;
    Integer totalSubCategorias;

    ListView listaPeliculas, listaCategorias, listaSubCategorias, listaTemporadas;
    Button peliculas, infantiles, live, series, adulto;

    VideoView reproductor;
    FloatingActionButton fab;
    MediaController controles;

    String url = "http://vodjs.mine.nu/vods/comedia/scarymovie4.mp4";

    String id;
    String titulo;
    String descripcion;
    String ruta;
    String mensaje = "";
    String CATEGORIA = "";
    String TEMPORADA = "";
    String buscar;
    String CodAdulto = "";

    final ArrayList<String> series_id = new ArrayList<String>();
    final ArrayList<String> arrayCategorias = new ArrayList<String>();
    final ArrayList<String> arraySeries = new ArrayList<String>();
    final ArrayList<String> ruta_video = new ArrayList<String>();
    final ArrayList<String> arrayCat = new ArrayList<String>();
    final ArrayList<String> arraySeriesImg = new ArrayList<String>();
    final ArrayList<String> arrayTemporadas = new ArrayList<String>();

    Intent siguiente;

    private LinearLayout layoutCategorias, layaut_temporadas, layoutPeliculas;

    EditText search;

    TextView tituloCategoria;

    ImageView img, fondo;

    String fondo_peliculas = "https://www.adslzone.net/app/uploads/2016/02/peliculas.jpg";

    String fondo_series = "https://kingstraining.com/wp-content/uploads/2016/07/1jwwo6.jpg";

    String clave;
    String sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Objetos

        clave = getIntent().getStringExtra("clave");

        //mensaje(clave);

        layoutCategorias = (LinearLayout)findViewById(R.id.layaut_categorias);
        layoutPeliculas = (LinearLayout)findViewById(R.id.layaut_peliculas);
        layaut_temporadas = (LinearLayout)findViewById(R.id.layaut_temporadas);

        live = (Button)findViewById(R.id.btn_live_tv);
        peliculas = (Button)findViewById(R.id.btn_peliculas);
        infantiles = (Button)findViewById(R.id.btn_infantiles);
        series = (Button)findViewById(R.id.btn_series);
        adulto = (Button)findViewById(R.id.btn_adultos);

        fab = (FloatingActionButton)findViewById(R.id.fab);

        search = (EditText)findViewById(R.id.txt_search);

        tituloCategoria = (TextView) findViewById(R.id.txt_categoria);

        img = (ImageView)findViewById(R.id.img_serie);
        fondo = (ImageView)findViewById(R.id.img_fondo);

        listaPeliculas = (ListView)findViewById(R.id.Peliculas);
        listaSubCategorias = (ListView)findViewById(R.id.categorias);
        listaCategorias = (ListView)findViewById(R.id.Principales);
        listaTemporadas = (ListView)findViewById(R.id.temporadas);

        //Listas

        listaCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CATEGORIA = arrayCat.get(position).toString();
                ListaCategorias(CATEGORIA);

            }
        });

        listaSubCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListaSubCategorias(position);
            }
        });

        listaPeliculas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListaPeliculas(position);
            }
        });

        listaTemporadas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListaTemporadas(position);
            }
        });

        //Botones

        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_live();
            }
        });

        peliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_peliculas();
            }
        });

        infantiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_infantiles();
            }
        });

        series.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_series();
            }
        });

        adulto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_adulto();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_buscar();
            }
        });

        //Funciones

        primeraCarga();

    }

    //Funciones de la primera carga

    private void primeraCarga(){

        if (isOnlineNet()){

            tiempo = tiempoInicial;
            temporizadorCategorias();
            //temporizador();

        }else{
            mensaje("no tienes conexion a internet");
        }

    }

    //Funciones de las listas

    private void ListaCategorias(String cat){

        if (cat.equals("Peliculas")||(cat.equals("Series"))){

            mensaje(cat);

            CATEGORIA = cat;

            tiempo = tiempoInicial;
            contador = 0;

            if (cat.equals("Peliculas")){

                Glide.with(this).load(fondo_peliculas).into(fondo);

            }else{

                Glide.with(this).load(fondo_series).into(fondo);

            }

            temporizador();
            mostrar(listaCategorias);
            ocultarPeliculas(listaCategorias);
            ocultarTemporadas(listaCategorias);

        }else{

            siguiente = new Intent(MainActivity.this, Video.class);
            startActivity(siguiente);

        }

    }

    private void ListaSubCategorias(Integer position){

        tiempo = tiempoInicial;
        contador = 0;
        TEMPORADA = "";
        ocultarTemporadas(listaSubCategorias);

        sub = arrayCategorias.get(position).toString();

        tituloCategoria.setText(CATEGORIA + " - " + arrayCategorias.get(position).toString());
        temporizadorTodas();
        mostrarPeliculas(listaSubCategorias);
        ocultar(listaCategorias);

    }

    private void ListaPeliculas(Integer position){

        tiempo = tiempoInicial;
        contador = 0;

        if (CATEGORIA.equals("Series")){

            if (TEMPORADA.equals("temporada")){

                siguiente = new Intent(MainActivity.this, Video.class);

                Log.d("la ruta es", ruta_video.get(position).toString());

                siguiente.putExtra("ruta", ruta_video.get(position).toString() );

                startActivity(siguiente);

            }else{

                buscar = "";
                buscar = series_id.get(position).toString();
                //mensaje(buscar);

                //GETSeriesID = GETSeriesID + series_id.get(position).toString();

                String imgRuta = arraySeriesImg.get(position).toString();

                Glide.with(this)
                        .load(imgRuta)
                        .into(img);

                temporizadorTemporadas(buscar);
                mostrarTemporadas(listaPeliculas);
            }

            //ocultarTemporadas(listaTemporadas);

        }else{

            siguiente = new Intent(MainActivity.this, Video.class);

            Log.d("la ruta es", ruta_video.get(position).toString());

            siguiente.putExtra("ruta", ruta_video.get(position).toString() );

            startActivity(siguiente);

        }

    }

    private void ListaTemporadas(Integer position){

        tiempo = tiempoInicial;
        contador = 0;

        TEMPORADA = "temporada";

        String ID = GETSeriesID + buscar + temporada + arrayTemporadas.get(position).toString();
        mensaje(arrayTemporadas.get(position).toString());
        mensaje(ID);
        temporizadorSeriesBusqueda(ID);

    }

    //Funciones de los botones

    private void btn_live(){

        Intent siguiente = new Intent(MainActivity.this, FullscreenActivity.class);
        startActivity(siguiente);

    }

    private void btn_peliculas(){

        totalSubCategorias = arraydatosSubCategorias.size();

        if (totalSubCategorias > 0){

            //mensaje("la lista ya esta cargada");
            mostrar(peliculas);
            ocultarPeliculas(peliculas);

        }else{

            //mensaje("esta es la primera carga de la lista");

            tiempo = tiempoInicial;
            contador = 0;

            temporizador();
            mostrar(peliculas);
            ocultarPeliculas(peliculas);

        }

    }

    private void btn_infantiles(){

    }

    private void btn_series(){

        /*
                tiempo = tiempoInicial;

                contador = 0;

                temporizador();

                mostrarTemporadas(series);
                /*
                mostrar(peliculas);
                ocultarPeliculas(peliculas);


                tiempo = tiempoInicial;
                contador = 0;

                CATEGORIA = "Series";
                temporizadorSeries();
                mostrarPeliculas(lista2);
                ocultar(lista2);
                */

    }

    private void btn_adulto(){

        CATEGORIA = "Adulto";
        //Perfiladulto = 1020;
        mensaje("coloque el codigo en el buscador");
        //Categorias("Adulto");

    }

    private void btn_buscar(){

        buscar = search.getText().toString();


        if (sub.equals("Adulto")){

            if(buscar.equals(clave)){

                //Categorias("Adulto");
                //CATEGORIA = "";

                search.setText("");

                mensaje("Acceso a session de adulto");

                tiempo = tiempoInicial;
                contador = 0;

                temporizadorCategorias(sub);
                mostrarPeliculas(listaSubCategorias);

            }else{

                mensaje("Codigo invalido, empice de nuevo el proceso");
                //CATEGORIA = "";
            }

        }else{

            tiempo = tiempoInicial;
            contador = 0;

            temporizadorPeliculas(buscar);

        }

    }

    //Funciones mixtas

    private void Categorias(String category){

        if (isOnlineNet()){

            tiempo = tiempoInicial;
            contador = 0;

            temporizadorCategorias(category);
            mostrarPeliculas(listaSubCategorias);
            ocultar(listaSubCategorias);

        }else{
            mensaje("no tienes conexion a internet");
        }


    }

    private void mensaje(String mensaje){

        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        mensaje, Toast.LENGTH_SHORT);

        toast1.show();

    }

    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void mostrar(View button)
    {
        if (layoutCategorias.getVisibility() == View.GONE)
        {
            animar(true);
            layoutCategorias.setVisibility(View.VISIBLE);
        }
    }

    public void ocultar(View button)
    {
        if (layoutCategorias.getVisibility() == View.VISIBLE)
        {
            animar(false);
            layoutCategorias.setVisibility(View.GONE);
        }

    }

    private void animar(boolean mostrar)
    {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar)
        {
            //desde la esquina inferior derecha a la superior izquierda
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }
        else
        {    //desde la esquina superior izquierda a la esquina inferior derecha
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        layoutCategorias.setLayoutAnimation(controller);
        layoutCategorias.startAnimation(animation);
    }

    public void mostrarPeliculas(View button)
    {
        if (layoutPeliculas.getVisibility() == View.GONE)
        {
            animarPeliculas(true);
            layoutPeliculas.setVisibility(View.VISIBLE);
        }
    }

    public void ocultarPeliculas(View button)
    {
        if (layoutPeliculas.getVisibility() == View.VISIBLE)
        {
            animarPeliculas(false);
            layoutPeliculas.setVisibility(View.GONE);
        }

    }

    private void animarPeliculas(boolean mostrarPeliculas)
    {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrarPeliculas)
        {
            //desde la esquina inferior derecha a la superior izquierda
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }
        else
        {    //desde la esquina superior izquierda a la esquina inferior derecha
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        layoutPeliculas.setLayoutAnimation(controller);
        layoutPeliculas.startAnimation(animation);
    }

    public void mostrarTemporadas(View button)
    {
        if (layaut_temporadas.getVisibility() == View.GONE)
        {
            animarTemporadas(true);
            layaut_temporadas.setVisibility(View.VISIBLE);
        }
    }

    public void ocultarTemporadas(View button)
    {
        if (layaut_temporadas.getVisibility() == View.VISIBLE)
        {
            animarTemporadas(false);
            layaut_temporadas.setVisibility(View.GONE);
        }

    }

    private void animarTemporadas(boolean mostrarTemporadas)
    {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrarTemporadas)
        {
            //desde la esquina inferior derecha a la superior izquierda
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }
        else
        {    //desde la esquina superior izquierda a la esquina inferior derecha
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        layaut_temporadas.setLayoutAnimation(controller);
        layaut_temporadas.startAnimation(animation);
    }

    //-----------------------------------------------//
    //Cargar Categorias - Funciona
    //-----------------------------------------------//

    //Paso 1 temporizador

    private void temporizadorCategorias(){

        if (isOnlineNet()){

            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(tiempo);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    updateListCategorias();

                                }
                            });
                        }
                    } catch (InterruptedException e) {

                        mensaje(e.toString());

                    }
                }
            };

            t.start();

        }else{
            mensaje("no tienes conexion a internet");
        }


    }

    //Paso 2 update

    private void updateListCategorias() {

        if (contador == 0){

            hilosCategorias();
            listaCategorias.setAdapter(adapterCategorias);

        }else if (contador == 1){

            listaCategorias.setAdapter(adapterCategorias);

        }else if (contador == 2){

            listaCategorias.setAdapter(adapterCategorias);
            tiempo = tiempoFinal;

        }

        contador++;

    }

    //Paso 3 hilo

    public void hilosCategorias(){

        hiloconexionCategorias = new ObtenerWebServiceCategorias();
        hiloconexionCategorias.execute(GETCategorias,"1");

    }

    //Paso 4 service

    public class ObtenerWebServiceCategorias extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];

            URL url = null; // url de donde queremos obtener la informacion

            String devuelve = "";

            if(params[1]=="1"){  //consulta por id

                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //abrir la coneccion
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            "(Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());  //Preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  //Le introdusco en un BufferReader

                        String line;
                        while ((line = reader.readLine()) != null){
                            result.append(line); //pasa toda la entrada al StringBuilder
                        }

                        //creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto
                        JSONObject respuestaJSON = new JSONObject(result.toString()); //Creo un JSONObject apartir de un JSONObject

                        String resultJSON = respuestaJSON.getString("estado");//Estado es el nombre del campo en el JSON

                        Log.d(resultJSON,"esto es lo q trajo el estado");

                        if (resultJSON.equals("1")){ //hay alumnos a mostrar

                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("oficinas"); //estado es el nombre del campo en el JSON

                            Log.d(alumnosJSON.getJSONObject(0).getString("nombre"),"esto es lo q trajo el estado f4era");

                            //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                            arraydatosSubCategorias.clear();
                            arrayCat.clear();

                            for (int i = 0; i < alumnosJSON.length(); i++) {

                                devuelve = "Cargando Categorias";

                                //Log.d(mensaje,"esto es lo q trajo el estado de categorias");

                                Log.d(alumnosJSON.getJSONObject(i).getString("nombre"),"esto es lo q trajo el estado de categorias");

                                //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                                titulo = alumnosJSON.getJSONObject(i).getString("nombre");
                                arrayCat.add(alumnosJSON.getJSONObject(i).getString("nombre"));
                                //ruta = img + alumnosJSON.getJSONObject(i).getString("foto");
                                //ruta = img2;

                                datosCategorias = new Datoss(titulo);
                                arraydatosCategorias.add(datosCategorias);

                                //creo el adater personalizado
                                adapterCategorias = new AdapterDatos(MainActivity.this, arraydatosCategorias);
                            }

                        }
                        else if (resultJSON.equals("2")){

                            devuelve = "Consulta fallida";
                        }

                    }

                }catch (MalformedURLException e){
                    devuelve = e.toString();
                }catch (IOException e) {
                    devuelve = e.toString();
                } catch (JSONException e) {
                    devuelve = e.toString();
                }

                return devuelve;

            }

            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {

            //mensaje = s;

            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            s, Toast.LENGTH_SHORT);

            toast1.show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    //-----------------------------------------------//
    //Cargar Sub Categorias - Funciona
    //-----------------------------------------------//

    //Paso 1 temporizador

    private void temporizador(){

        if (isOnlineNet()){

            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(tiempo);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    updateList();

                                }
                            });
                        }
                    } catch (InterruptedException e) {

                        mensaje(e.toString());

                    }
                }
            };

            t.start();

        }else{
            mensaje("no tienes conexion a internet");
        }


    }

    //Paso 2 update

    private void updateList() {

        if (contador == 0){

            hilos();
            listaSubCategorias.setAdapter(adapterSubCategorias);

        }else if (contador == 1){

            listaSubCategorias.setAdapter(adapterSubCategorias);

        }else if (contador == 2){

            listaSubCategorias.setAdapter(adapterSubCategorias);
            tiempo = tiempoFinal;

        }

        contador++;

    }

    //Paso 3 hilo

    public void hilos(){

        hiloconexionSubCategorias = new ObtenerWebServiceSubCategorias();
        hiloconexionSubCategorias.execute(GETSUBCategorias,"1");

    }

    //Paso 4 service

    public class ObtenerWebServiceSubCategorias extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];

            URL url = null; // url de donde queremos obtener la informacion

            String devuelve = "";

            if(params[1]=="1"){  //consulta por id

                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //abrir la coneccion
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            "(Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());  //Preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  //Le introdusco en un BufferReader

                        String line;
                        while ((line = reader.readLine()) != null){
                            result.append(line); //pasa toda la entrada al StringBuilder
                        }

                        //creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto
                        JSONObject respuestaJSON = new JSONObject(result.toString()); //Creo un JSONObject apartir de un JSONObject

                        String resultJSON = respuestaJSON.getString("estado");//Estado es el nombre del campo en el JSON

                        Log.d(resultJSON,"esto es lo q trajo el estado");

                        if (resultJSON.equals("1")){ //hay alumnos a mostrar

                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("oficinas"); //estado es el nombre del campo en el JSON

                            Log.d(alumnosJSON.getJSONObject(0).getString("sub_categoria"),"esto es lo q trajo el estado fuera sub_categoria");

                            //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                            arraydatosSubCategorias.clear();
                            arrayCategorias.clear();

                            for (int i = 0; i < alumnosJSON.length(); i++) {

                                devuelve = "Cargando Sub-Categorias";

                                Log.d(mensaje,"esto es lo q trajo el estado");

                                Log.d(alumnosJSON.getJSONObject(0).getString("sub_categoria"),"esto es lo q trajo el estado");

                                //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                                titulo = alumnosJSON.getJSONObject(i).getString("sub_categoria");
                                arrayCategorias.add(alumnosJSON.getJSONObject(i).getString("sub_categoria"));
                                //ruta = img + alumnosJSON.getJSONObject(i).getString("foto");
                                //ruta = img2;

                                datosSubCategorias = new Datoss(titulo);
                                arraydatosSubCategorias.add(datosSubCategorias);

                                //creo el adater personalizado
                                adapterSubCategorias = new AdapterDatos(MainActivity.this, arraydatosSubCategorias);
                            }

                        }
                        else if (resultJSON.equals("2")){

                            devuelve = "Consulta fallida";
                        }

                    }

                }catch (MalformedURLException e){
                    devuelve = e.toString();
                }catch (IOException e) {
                    devuelve = e.toString();
                } catch (JSONException e) {
                    devuelve = e.toString();
                }

                return devuelve;

            }

            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {

            // mensaje = s;

            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            s, Toast.LENGTH_SHORT);

            toast1.show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    //---------------------------------------------//
    //Cargar Busqueda por Sub Categoria - Funciona
    //----------------------------------------------//

    //Paso 1 temporizador

    private void temporizadorCategorias(final String categoria){

        if (isOnlineNet()){

            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(tiempo);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    updateListCategorias(categoria);
                                    Log.d(buscar,"Paso numero 2 para la series");

                                }
                            });
                        }
                    } catch (InterruptedException e) {

                        mensaje(e.toString());

                    }
                }
            };

            t.start();

        }else{
            mensaje("no tienes conexion a internet");
        }



    }

    //Paso 2 update

    private void updateListCategorias(String categoria) {

        if (contador == 0){

            hiloPeliculaCategoria(categoria);
            listaPeliculas.setAdapter(adapterPeliculas);
            Log.d(buscar,"Paso numero 3 para la series");

            //mensaje("Cargando");

        }else if (contador == 1){

            listaPeliculas.setAdapter(adapterPeliculas);

            //mensaje("Cargando");

        }else if (contador == 2){

            tiempo = tiempoFinal;

        }

        contador++;

    }

    //Paso 3 hilo

    public void hiloPeliculaCategoria(String categoria){

        String cadena = GETCategoriaID + categoria;
        hiloconexionPeliculasCat = new ObtenerWebServicePeliculasCat();
        hiloconexionPeliculasCat.execute(cadena,"1");


        Log.d(buscar,"Paso numero 4 para la series");
        Log.d(buscar,"link de busqueda series");

    }

    //Paso 4 service

    public class ObtenerWebServicePeliculasCat extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];

            URL url = null; // url de donde queremos obtener la informacion

            String devuelve = "";

            if(params[1]=="1"){  //consulta por id

                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //abrir la coneccion
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            "(Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());  //Preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  //Le introdusco en un BufferReader

                        String line;
                        while ((line = reader.readLine()) != null){
                            result.append(line); //pasa toda la entrada al StringBuilder
                        }

                        //creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto
                        JSONObject respuestaJSON = new JSONObject(result.toString()); //Creo un JSONObject apartir de un JSONObject

                        String resultJSON = respuestaJSON.getString("estado");//Estado es el nombre del campo en el JSON

                        Log.d(resultJSON,"esto es lo q trajo el estado de series");

                        if (resultJSON.equals("1")){ //hay alumnos a mostrar

                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("alumno"); //estado es el nombre del campo en el JSON

                            Log.d(alumnosJSON.getJSONObject(0).getString("sub_categoria"),"Esto esta antes de bucle de busqueda");

                            //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                            arraydatosPeliculas.clear();
                            ruta_video.clear();

                            for (int i = 0; i < alumnosJSON.length(); i++) {

                                devuelve = "Cargando";

                                Log.d(mensaje,"esto es lo q trajo el estado");

                                Log.d(alumnosJSON.getJSONObject(i).getString("nombre"),"esto es lo q trajo la seleccion de la serie");

                                //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                                titulo = alumnosJSON.getJSONObject(i).getString("nombre");
                                descripcion = alumnosJSON.getJSONObject(i).getString("descripcion");
                                ruta = alumnosJSON.getJSONObject(i).getString("img");

                                ruta_video.add(alumnosJSON.getJSONObject(i).getString("ruta"));

                                datosPeliculas = new Datos(ruta, titulo, descripcion);
                                arraydatosPeliculas.add(datosPeliculas);

                                //creo el adater personalizado
                                adapterPeliculas = new AdapterDatoss(MainActivity.this, arraydatosPeliculas);
                            }

                        }
                        else if (resultJSON.equals("2")){

                            devuelve = "Pelicula no encontrada";
                        }

                    }

                }catch (MalformedURLException e){
                    devuelve = e.toString();
                }catch (IOException e) {
                    devuelve = e.toString();
                } catch (JSONException e) {
                    devuelve = e.toString();
                }

                return devuelve;

            }

            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {

            mensaje = s;

            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            s, Toast.LENGTH_SHORT);

            toast1.show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    //---------------------------------------------//
    //Cargar Busqueda por Sub Categoria Series - En Desarrollo
    //----------------------------------------------//

    //Paso 1 temporizador

    private void temporizadorCategoriasSeries(final String categoria){

        if (isOnlineNet()){

            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(tiempo);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    updateListCategoriasSeries(categoria);
                                    Log.d(buscar,"Paso numero 2 para la series");

                                }
                            });
                        }
                    } catch (InterruptedException e) {

                        mensaje(e.toString());

                    }
                }
            };

            t.start();

        }else{
            mensaje("no tienes conexion a internet");
        }



    }

    //Paso 2 update

    private void updateListCategoriasSeries(String categoria) {

        if (contador == 0){

            hiloPeliculaCategoriaSeries(categoria);
            listaPeliculas.setAdapter(adapterPeliculas);
            Log.d(buscar,"Paso numero 3 para la series");

            //mensaje("Cargando");

        }else if (contador == 1){

            listaPeliculas.setAdapter(adapterPeliculas);

            //mensaje("Cargando");

        }else if (contador == 2){

            tiempo = tiempoFinal;

        }

        contador++;

    }

    //Paso 3 hilo

    public void hiloPeliculaCategoriaSeries(String categoria){

        String cadena = GETCategoriaSeriesID + categoria;
        //mensaje(cadena);
        hiloconexionPeliculasCatSeries = new ObtenerWebServicePeliculasCatSeries();
        hiloconexionPeliculasCatSeries.execute(cadena,"1");


        Log.d(buscar,"Paso numero 4 para la series");
        Log.d(buscar,"link de busqueda series");

    }

    //Paso 4 service

    public class ObtenerWebServicePeliculasCatSeries extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];

            URL url = null; // url de donde queremos obtener la informacion

            String devuelve = "";

            if(params[1]=="1"){  //consulta por id

                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //abrir la coneccion
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            "(Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());  //Preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  //Le introdusco en un BufferReader

                        String line;
                        while ((line = reader.readLine()) != null){
                            result.append(line); //pasa toda la entrada al StringBuilder
                        }

                        //creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto
                        JSONObject respuestaJSON = new JSONObject(result.toString()); //Creo un JSONObject apartir de un JSONObject

                        String resultJSON = respuestaJSON.getString("estado");//Estado es el nombre del campo en el JSON

                        Log.d(resultJSON,"esto es lo q trajo el estado de series");

                        if (resultJSON.equals("1")){ //hay alumnos a mostrar

                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("alumno"); //estado es el nombre del campo en el JSON

                            Log.d(alumnosJSON.getJSONObject(0).getString("sub_categoria"),"Esto esta antes de bucle de busqueda");

                            //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                            arraydatosPeliculas.clear();
                            ruta_video.clear();
                            arraySeries.clear();

                            for (int i = 0; i < alumnosJSON.length(); i++) {

                                devuelve = "Cargando";

                                //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                                titulo = alumnosJSON.getJSONObject(i).getString("serie");
                                descripcion = alumnosJSON.getJSONObject(i).getString("descripcion");
                                ruta = alumnosJSON.getJSONObject(i).getString("img");

                                arraySeries.add(alumnosJSON.getJSONObject(i).getString("serie"));
                                arraySeriesImg.add(alumnosJSON.getJSONObject(i).getString("img"));
                                series_id.add(alumnosJSON.getJSONObject(i).getString("id"));

                                datosPeliculas = new Datos(ruta, titulo, descripcion);
                                arraydatosPeliculas.add(datosPeliculas);

                                //creo el adater personalizado
                                adapterPeliculas = new AdapterDatoss(MainActivity.this, arraydatosPeliculas);
                            }

                        }
                        else if (resultJSON.equals("2")){

                            devuelve = "Series no encontradas";
                        }

                    }

                }catch (MalformedURLException e){
                    devuelve = e.toString();
                }catch (IOException e) {
                    devuelve = e.toString();
                } catch (JSONException e) {
                    devuelve = e.toString();
                }

                return devuelve;

            }

            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {

            mensaje = s;

            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            s, Toast.LENGTH_SHORT);

            toast1.show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    //---------------------------------------------//
    //Cargar Todas las Peliculas - Funciona
    //----------------------------------------------//

    //Paso 1 temporizador

    private void temporizadorTodas(){

        if (isOnlineNet()){

            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(tiempo);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    updateListTodas();

                                }
                            });
                        }
                    } catch (InterruptedException e) {

                        mensaje(e.toString());

                    }
                }
            };

            t.start();

        }else{
            mensaje("no tienes conexion a internet");
        }



    }

    //Paso 2 update

    private void updateListTodas() {

        if (contador == 0){

            hiloTodas();
            listaPeliculas.setAdapter(adapterPeliculas);

            mensaje("Cargando");

        }else if (contador == 1){

            listaPeliculas.setAdapter(adapterPeliculas);

            //mensaje("Cargando");

        }else if (contador == 2){

            tiempo = tiempoFinal;

        }

        contador++;

    }

    //Paso 3 hilo

    public void hiloTodas(){

        hiloconexionPeliculas = new ObtenerWebServicePeliculas();
        hiloconexionPeliculas.execute(GETPeliculas,"1");

    }

    //Paso 4 service

    public class ObtenerWebServicePeliculas extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];

            URL url = null; // url de donde queremos obtener la informacion

            String devuelve = "";

            if(params[1]=="1"){  //consulta por id

                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //abrir la coneccion
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            "(Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());  //Preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  //Le introdusco en un BufferReader

                        String line;
                        while ((line = reader.readLine()) != null){
                            result.append(line); //pasa toda la entrada al StringBuilder
                        }

                        //creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto
                        JSONObject respuestaJSON = new JSONObject(result.toString()); //Creo un JSONObject apartir de un JSONObject

                        String resultJSON = respuestaJSON.getString("estado");//Estado es el nombre del campo en el JSON

                        Log.d(resultJSON,"esto es lo q trajo el estado");

                        if (resultJSON.equals("1")){ //hay alumnos a mostrar

                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("oficinas"); //estado es el nombre del campo en el JSON

                            Log.d(alumnosJSON.getJSONObject(0).getString("nombre"),"esta es la pelicula q trajo");

                            //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                            arraydatosPeliculas.clear();
                            ruta_video.clear();

                            for (int i = 0; i < alumnosJSON.length(); i++) {

                                devuelve = "Cargando Peliculas";

                                Log.d(mensaje,"esto es lo q trajo el estado");

                                Log.d(alumnosJSON.getJSONObject(i).getString("nombre"),"esta es la pelicula q trajo");

                                //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                                titulo = alumnosJSON.getJSONObject(i).getString("nombre");
                                descripcion = alumnosJSON.getJSONObject(i).getString("descripcion");
                                ruta = alumnosJSON.getJSONObject(i).getString("img");

                                ruta_video.add(alumnosJSON.getJSONObject(i).getString("ruta"));

                                datosPeliculas = new Datos(ruta, titulo, descripcion);
                                arraydatosPeliculas.add(datosPeliculas);

                                //creo el adater personalizado
                                adapterPeliculas = new AdapterDatoss(MainActivity.this, arraydatosPeliculas);
                            }

                        }
                        else if (resultJSON.equals("2")){

                            devuelve = "Pelicula no encontrada";
                        }

                    }

                }catch (MalformedURLException e){
                    devuelve = e.toString();
                }catch (IOException e) {
                    devuelve = e.toString();
                } catch (JSONException e) {
                    devuelve = e.toString();
                }

                return devuelve;

            }

            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {

            mensaje = s;

            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            s, Toast.LENGTH_SHORT);

            toast1.show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    //---------------------------------------------//
    //Cargar Buscar Peliculas - Funciona
    //----------------------------------------------//

    //Paso 1 temporizador

    private void temporizadorPeliculas(final String nombre){

        if (isOnlineNet()){

            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(tiempo);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    updateListPeliculas(nombre);

                                }
                            });
                        }
                    } catch (InterruptedException e) {

                        mensaje(e.toString());

                    }
                }
            };

            t.start();

        }else{
            mensaje("no tienes conexion a internet");
        }



    }

    //Paso 2 update

    private void updateListPeliculas(String nombre) {

        if (contador == 0){

            hiloPeliculaBusqueda(nombre);
            listaPeliculas.setAdapter(adapterPeliculas);

            //mensaje("Cargando");

        }else if (contador == 1){

            listaPeliculas.setAdapter(adapterPeliculas);

            //mensaje("Cargando");

        }else if (contador == 2){

            tiempo = tiempoFinal;

        }

        contador++;

    }

    //Paso 3 hilo

    public void hiloPeliculaBusqueda(String nombre){

        hiloconexionPeliculasCategoria = new ObtenerWebServicePeliculasCategoria();
        hiloconexionPeliculasCategoria.execute(GETPeliculaCategoriaID + nombre,"1");

    }

    //Paso 4 service

    public class ObtenerWebServicePeliculasCategoria extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];

            URL url = null; // url de donde queremos obtener la informacion

            String devuelve = "";

            if(params[1]=="1"){  //consulta por id

                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //abrir la coneccion
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            "(Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());  //Preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  //Le introdusco en un BufferReader

                        String line;
                        while ((line = reader.readLine()) != null){
                            result.append(line); //pasa toda la entrada al StringBuilder
                        }

                        //creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto
                        JSONObject respuestaJSON = new JSONObject(result.toString()); //Creo un JSONObject apartir de un JSONObject

                        String resultJSON = respuestaJSON.getString("estado");//Estado es el nombre del campo en el JSON

                        Log.d(resultJSON,"esto es lo q trajo el estado");

                        if (resultJSON.equals("1")){ //hay alumnos a mostrar

                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("alumno"); //estado es el nombre del campo en el JSON

                            Log.d(alumnosJSON.getJSONObject(0).getString("nombre"),"esta es la pelicula q trajo");

                            //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                            arraydatosPeliculas.clear();
                            ruta_video.clear();

                            for (int i = 0; i < alumnosJSON.length(); i++) {

                                devuelve = "Cargando";

                                Log.d(mensaje,"esto es lo q trajo el estado");

                                Log.d(alumnosJSON.getJSONObject(i).getString("nombre"),"esta es la pelicula q trajo");

                                //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                                titulo = alumnosJSON.getJSONObject(i).getString("nombre");
                                descripcion = alumnosJSON.getJSONObject(i).getString("descripcion");
                                ruta = alumnosJSON.getJSONObject(i).getString("img");

                                ruta_video.add(alumnosJSON.getJSONObject(i).getString("ruta"));

                                datosPeliculas = new Datos(ruta, titulo, descripcion);
                                arraydatosPeliculas.add(datosPeliculas);

                                //creo el adater personalizado
                                adapterPeliculas = new AdapterDatoss(MainActivity.this, arraydatosPeliculas);
                            }

                        }
                        else if (resultJSON.equals("2")){

                            devuelve = "No se encontro la pelicula";
                        }

                    }

                }catch (MalformedURLException e){
                    devuelve = e.toString();
                }catch (IOException e) {
                    devuelve = e.toString();
                } catch (JSONException e) {
                    devuelve = e.toString();
                }

                return devuelve;

            }

            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {

            mensaje = s;

            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            s, Toast.LENGTH_SHORT);

            toast1.show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    //---------------------------------------------//
    //Cargar Series - Funciona
    //----------------------------------------------//

    //Paso 1 temporizador

    private void temporizadorSeries(){

        if (isOnlineNet()){

            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(tiempo);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    updateListSeries();

                                }
                            });
                        }
                    } catch (InterruptedException e) {

                        mensaje(e.toString());

                    }
                }
            };

            t.start();

        }else{
            mensaje("no tienes conexion a internet");
        }


    }

    //Paso 2 update

    private void updateListSeries() {

        if (contador == 0){

            hilosSeries();
            listaPeliculas.setAdapter(adapterPeliculas);

            //mensaje("Cargando");

        }else if (contador == 1){

            listaPeliculas.setAdapter(adapterPeliculas);

            //mensaje("Cargando");

        }else if (contador == 2){

            listaPeliculas.setAdapter(adapterPeliculas);
            tiempo = tiempoFinal;

        }

        contador++;

    }

    //Paso 3 hilo

    public void hilosSeries(){

        hiloconexionSeries = new ObtenerWebServiceSeries();
        hiloconexionSeries.execute(GETSeries,"1");

    }

    //Paso 4 service

    public class ObtenerWebServiceSeries extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];

            URL url = null; // url de donde queremos obtener la informacion

            String devuelve = "";

            if(params[1]=="1"){  //consulta por id

                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //abrir la coneccion
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            "(Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());  //Preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  //Le introdusco en un BufferReader

                        String line;
                        while ((line = reader.readLine()) != null){
                            result.append(line); //pasa toda la entrada al StringBuilder
                        }

                        //creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto
                        JSONObject respuestaJSON = new JSONObject(result.toString()); //Creo un JSONObject apartir de un JSONObject

                        String resultJSON = respuestaJSON.getString("estado");//Estado es el nombre del campo en el JSON

                        Log.d(resultJSON,"esto es lo q trajo el estado");

                        if (resultJSON.equals("1")){ //hay alumnos a mostrar

                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("oficinas"); //estado es el nombre del campo en el JSON

                            Log.d(alumnosJSON.getJSONObject(0).getString("serie"),"esta es la pelicula q trajo");

                            //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                            arraydatosPeliculas.clear();
                            arraySeriesImg.clear();
                            arraySeries.clear();

                            for (int i = 0; i < alumnosJSON.length(); i++) {

                                devuelve = "Cargando Series";

                                Log.d(mensaje,"esto es lo q trajo el estado");

                                Log.d(alumnosJSON.getJSONObject(i).getString("serie"),"esta es la pelicula q trajo");

                                //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                                titulo = alumnosJSON.getJSONObject(i).getString("serie");
                                descripcion = alumnosJSON.getJSONObject(i).getString("descripcion");
                                ruta = alumnosJSON.getJSONObject(i).getString("img");

                                arraySeries.add(alumnosJSON.getJSONObject(i).getString("serie"));
                                arraySeriesImg.add(alumnosJSON.getJSONObject(i).getString("img"));
                                series_id.add(alumnosJSON.getJSONObject(i).getString("id"));
                                //ruta_video.add(alumnosJSON.getJSONObject(i).getString("ruta"));

                                datosPeliculas = new Datos(ruta, titulo, descripcion);
                                arraydatosPeliculas.add(datosPeliculas);

                                //creo el adater personalizado
                                adapterPeliculas = new AdapterDatoss(MainActivity.this, arraydatosPeliculas);
                            }

                        }
                        else if (resultJSON.equals("2")){

                            devuelve = "Pelicula no encontrada";
                        }

                    }

                }catch (MalformedURLException e){
                    devuelve = e.toString();
                }catch (IOException e) {
                    devuelve = e.toString();
                } catch (JSONException e) {
                    devuelve = e.toString();
                }

                return devuelve;

            }

            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {

            mensaje = s;

            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            s, Toast.LENGTH_SHORT);

            toast1.show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    //---------------------------------------------//
    //Cargar Busqueda de Series -  Funciona
    //----------------------------------------------//

    //Paso 1 temporizador

    private void temporizadorSeriesBusqueda(final String id){

        if (isOnlineNet()){

            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(tiempo);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    updateListSeriesBusqueda(id);

                                }
                            });
                        }
                    } catch (InterruptedException e) {

                        mensaje(e.toString());

                    }
                }
            };

            t.start();

        }else{
            mensaje("no tienes conexion a internet");
        }


    }

    //Paso 2 update

    private void updateListSeriesBusqueda(String id) {

        if (contador == 0){

            hilosSeriesBusqueda(id);
            listaPeliculas.setAdapter(adapterPeliculas);

            mensaje("Update");

        }else if (contador == 1){

            listaPeliculas.setAdapter(adapterPeliculas);

            //mensaje("Cargando");

        }else if (contador == 2){

            tiempo = tiempoFinal;

        }

        contador++;

    }

    //Paso 3 hilo

    public void hilosSeriesBusqueda(String id){

        hiloconexionSeriesBusqueda = new ObtenerWebServiceSeriesBusqueda();
        hiloconexionSeriesBusqueda.execute(id,"1");

    }

    //Paso 4 service

    public class ObtenerWebServiceSeriesBusqueda extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];

            URL url = null; // url de donde queremos obtener la informacion

            String devuelve = "";

            if(params[1]=="1"){  //consulta por id

                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //abrir la coneccion
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            "(Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());  //Preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  //Le introdusco en un BufferReader

                        String line;
                        while ((line = reader.readLine()) != null){
                            result.append(line); //pasa toda la entrada al StringBuilder
                        }

                        //creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto
                        JSONObject respuestaJSON = new JSONObject(result.toString()); //Creo un JSONObject apartir de un JSONObject

                        String resultJSON = respuestaJSON.getString("estado");//Estado es el nombre del campo en el JSON

                        Log.d(resultJSON,"esto es lo q trajo el estado");

                        if (resultJSON.equals("1")){ //hay alumnos a mostrar

                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("alumno"); //estado es el nombre del campo en el JSON

                            Log.d(alumnosJSON.getJSONObject(0).getString("nombre"),"esta es la pelicula q trajo");

                            //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                            arraydatosPeliculas.clear();
                            arraySeries.clear();
                            series_id.clear();

                            for (int i = 0; i < alumnosJSON.length(); i++) {

                                devuelve = "Cargando serie seleccionada";

                                Log.d(mensaje,"esto es lo q trajo el estado");

                                Log.d(alumnosJSON.getJSONObject(i).getString("nombre"),"esta es la pelicula q trajo");

                                //mensaje(alumnosJSON.getJSONObject(0).getString("grupo"));

                                titulo = alumnosJSON.getJSONObject(i).getString("nombre");
                                descripcion = alumnosJSON.getJSONObject(i).getString("descripcion");
                                ruta = alumnosJSON.getJSONObject(i).getString("img");

                                //arraySeries.add(alumnosJSON.getJSONObject(i).getString("nombre"));
                                ruta_video.add(alumnosJSON.getJSONObject(i).getString("ruta"));

                                datosPeliculas = new Datos(ruta, titulo, descripcion);
                                arraydatosPeliculas.add(datosPeliculas);

                                //creo el adater personalizado
                                adapterPeliculas = new AdapterDatoss(MainActivity.this, arraydatosPeliculas);
                            }

                        }
                        else if (resultJSON.equals("2")){

                            devuelve = "Pelicula no encontrada";
                        }

                    }

                }catch (MalformedURLException e){
                    devuelve = e.toString();
                }catch (IOException e) {
                    devuelve = e.toString();
                } catch (JSONException e) {
                    devuelve = e.toString();
                }

                return devuelve;

            }

            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {

            mensaje = s;

            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            s, Toast.LENGTH_SHORT);

            toast1.show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    //---------------------------------------------//
    //Cargar Temporadas - Funciona
    //----------------------------------------------//

    //Paso 1 temporizador

    private void temporizadorTemporadas(final String id){

        if (isOnlineNet()){

            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(tiempo);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    updateListTemporadas(id);

                                }
                            });
                        }
                    } catch (InterruptedException e) {

                        mensaje(e.toString());

                    }
                }
            };

            t.start();

        }else{
            mensaje("no tienes conexion a internet");
        }


    }

    //Paso 2 update

    private void updateListTemporadas(String id) {

        if (contador == 0){

            hilosTemporadas(id);
            listaTemporadas.setAdapter(adapterTemporadas);

        }else if (contador == 1){

            listaTemporadas.setAdapter(adapterTemporadas);

        }else if (contador == 2){

            listaTemporadas.setAdapter(adapterTemporadas);
            tiempo = tiempoFinal;

        }

        contador++;

    }

    //Paso 3 hilo

    public void hilosTemporadas(String id){

        hiloconexionTemporadas = new ObtenerWebServiceTemporadas();
        hiloconexionTemporadas.execute(GETSeriesTemporadasID + id,"1");

    }

    //Paso 4 service

    public class ObtenerWebServiceTemporadas extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];

            URL url = null; // url de donde queremos obtener la informacion

            String devuelve = "";

            if(params[1]=="1"){  //consulta por id

                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //abrir la coneccion
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            "(Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());  //Preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  //Le introdusco en un BufferReader

                        String line;
                        while ((line = reader.readLine()) != null){
                            result.append(line); //pasa toda la entrada al StringBuilder
                        }

                        //creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto
                        JSONObject respuestaJSON = new JSONObject(result.toString()); //Creo un JSONObject apartir de un JSONObject

                        String resultJSON = respuestaJSON.getString("estado");//Estado es el nombre del campo en el JSON

                        Log.d(resultJSON,"esto es lo q trajo el estado de series");

                        if (resultJSON.equals("1")){ //hay alumnos a mostrar

                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("alumno"); //estado es el nombre del campo en el JSON

                            //arraydatosPeliculas.clear();
                            arraydatosTemporadas.clear();
                            ruta_video.clear();

                            for (int i = 0; i < alumnosJSON.length(); i++) {

                                devuelve = "Cargando Temporadas";

                                titulo = "Temporada " + alumnosJSON.getJSONObject(i).getString("temporada");
                                arrayTemporadas.add(alumnosJSON.getJSONObject(i).getString("temporada"));

                                datosTemporadas = new Datoss(titulo);
                                arraydatosTemporadas.add(datosTemporadas);

                                //creo el adater personalizado
                                adapterTemporadas = new AdapterDatos(MainActivity.this, arraydatosTemporadas);

                            }

                        }
                        else if (resultJSON.equals("2")){

                            devuelve = "Pelicula no encontrada";
                        }

                    }

                }catch (MalformedURLException e){
                    devuelve = e.toString();
                }catch (IOException e) {
                    devuelve = e.toString();
                } catch (JSONException e) {
                    devuelve = e.toString();
                }

                return devuelve;

            }

            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {

            mensaje = s;

            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            s, Toast.LENGTH_SHORT);

            toast1.show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }


}