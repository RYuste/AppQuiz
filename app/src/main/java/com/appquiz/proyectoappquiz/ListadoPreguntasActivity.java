package com.appquiz.proyectoappquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class ListadoPreguntasActivity extends AppCompatActivity {

    private String TAG = "ListadoPreguntasActivity";
    private Context myContext;
    private TextView noCreadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLog.d(TAG, "Iniciando onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_preguntas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListadoPreguntasActivity.this, NuevaEditaPreguntaActivity.class));
            }
        });

        MyLog.d(TAG, "Cerrando onCreate...");
    }

    /**
     * Crea una flecha para volver atrás
     */
    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listadopreguntas, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                Log.i("ActionBar", "Borrar listado de Preguntas");

                // Elimina la lista de preguntas
                AlertDialog.Builder builder = new AlertDialog.Builder(ListadoPreguntasActivity.this);
                builder.setMessage(R.string.eliminarListado);

                builder.setPositiveButton(R.string.eliminar, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Repositorio.getRepositorio().consultaBorrarListadoPreguntas(myContext);
                        finish();
                    }
                }).setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show(); //show alert dialog
                return true;
            case R.id.action_export:
                Intent emailXML = exportarXML(myContext);
                startActivity(Intent.createChooser(emailXML, "Exportar Listado")); //error al intentar R.string.exportarListado
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Crea y envía un archivo xml a un e-mail.
     *
     * @param myContext
     * @return xml
     */
    private static Intent exportarXML(Context myContext){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/exportarListadoPreguntas");
        String fname = "listadoPreguntas.xml";
        File file = new File (myDir, fname);
        try{
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            if (file.exists ())
                file.delete ();
            FileWriter fw=new FileWriter(file);
            fw.write(Repositorio.CreateXMLString(myContext));
            fw.close();
        }catch (Exception ex){
            MyLog.e("Fichero", "Error al escribir el fichero.");
        }

        String cadena = myDir.getAbsolutePath()+"/"+fname;
        Uri path = Uri.parse("file://"+cadena);

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","rafalinyj@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.msgPreguntasExp);
        emailIntent.putExtra(Intent.EXTRA_TEXT, R.string.msgPreguntasExpApp);
        emailIntent .putExtra(Intent.EXTRA_STREAM, path);

        return emailIntent;
    }

    @Override
    protected void onStart() {
        MyLog.d(TAG, "Iniciando onStart...");

        super.onStart();

        MyLog.d(TAG, "Cerrando onStart...");
    }

    @Override
    protected void onRestart() {
        MyLog.d(TAG, "Iniciando onRestart...");

        super.onRestart();

        MyLog.d(TAG, "Cerrando onRestart...");
    }

    @Override
    protected void onResume() {
        MyLog.d(TAG, "Iniciando onResume...");
        super.onResume();

        // Almacenamos el contexto de la actividad para utilizar en las clases internas
        myContext = this;

        final ArrayList<Pregunta> listaPreguntas = Repositorio.getRepositorio().consultaListarPreguntas(myContext);

        // Inicializa el RecyclerView
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_ListaPreguntas);
        // Crea el Adaptador con los datos de la lista anterior
        final PreguntasAdapter adaptador = new PreguntasAdapter(listaPreguntas);
        // Si la BD está vacía devuelve true, sino false
        boolean BDVacia = Repositorio.getRepositorio().checkEmpty(myContext);

        //Si la BD está vacía, oculta el mensaje de "No hay preguntas creadas", sino lo muestra
        noCreadas = (TextView) findViewById(R.id.textView_noPreguntas);
        if(BDVacia == false){
            noCreadas.setVisibility(View.INVISIBLE);

            // Asocia el elemento de la lista con una acción al ser pulsado
            adaptador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acción al pulsar el elemento
                    int position = recyclerView.getChildAdapterPosition(v);

                    Intent intent = new Intent(ListadoPreguntasActivity.this, NuevaEditaPreguntaActivity.class);

                    // Creamos la información a pasar entre actividades
                    Bundle b = new Bundle();
                    b.putInt("ID", listaPreguntas.get(position).getId());

                    // Añadimos la información al intent
                    intent.putExtras(b);
                    // Iniciamos la actividad
                    startActivity(intent);
                }
            });

            // Asocia el Adaptador al RecyclerView
            recyclerView.setAdapter(adaptador);
            // Muestra el RecyclerView en vertical
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            /*
            ------ FUNCIONA PARCIALMENTE---
            // Permite deslizar y mover elementos del RecyclerView
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    final int position = viewHolder.getAdapterPosition(); //get position which is swipe
                    final int posicionID = listaPreguntas.get(position).getId();

                    if (direction == ItemTouchHelper.RIGHT) { //if swipe RIGHT
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListadoPreguntasActivity.this);
                        builder.setMessage("¿Deseas eliminar esta pregunta?"); //set message

                        // Elimina la pregunta
                        builder.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adaptador.notifyItemRemoved(position); //item removed from recylcerview
                                boolean correcto = Repositorio.getRepositorio().consultaEliminarPregunta(myContext, posicionID);

                                if(correcto == true){
                                    Snackbar.make(recyclerView, R.string.eliminarPregunta_exito, Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();
                                }else{
                                    Snackbar.make(recyclerView, R.string.eliminarPregunta_error, Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();
                                }
                            }
                        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //adaptador.notifyItemRemoved(position + 1); //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                                adaptador.notifyItemRangeChanged(position, adaptador.getItemCount()); //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                                return;
                            }
                        }).show(); //show alert dialog
                    }
                }
            }).attachToRecyclerView(recyclerView);*/
        }else{
            noCreadas.setVisibility(View.VISIBLE);
            adaptador.borrarDatos();
            //adaptador.notifyDataSetChanged();
        }
        MyLog.d(TAG, "Cerrando onResume...");
    }

    @Override
    protected void onPause() {
        MyLog.d(TAG, "Iniciando onPause...");

        super.onPause();

        MyLog.d(TAG, "Cerrando onPause...");
    }

    @Override
    protected void onStop() {
        MyLog.d(TAG, "Iniciando onStop...");

        super.onStop();

        MyLog.d(TAG, "Cerrando onStop...");
    }

    @Override
    protected void onDestroy() {
        MyLog.d(TAG, "Iniciando onDestroy...");

        super.onDestroy();

        MyLog.d(TAG, "Cerrando onDestroy...");
    }
}
