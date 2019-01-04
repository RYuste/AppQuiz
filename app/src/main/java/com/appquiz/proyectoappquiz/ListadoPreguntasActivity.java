package com.appquiz.proyectoappquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.sql.SQLException;
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
                Log.i("ActionBar", "Borrar Listado de Preguntas");

                Repositorio.getRepositorio().consultaBorrarPreguntas(myContext);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        //Si listaPreguntas está lleno, oculta el mensaje de "No hay preguntas creadas", sino, lo muestra
        noCreadas = (TextView) findViewById(R.id.textView_noPreguntas);
        if(!listaPreguntas.isEmpty()){
            noCreadas.setVisibility(View.INVISIBLE);

            // Asocia el elemento de la lista con una acción al ser pulsado
            adaptador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acción al pulsar el elemento
                    int position = recyclerView.getChildAdapterPosition(v);
                    /*Toast.makeText(ListadoPreguntasActivity.this,
                            "Posición: " + listaPreguntas.get(position).getId() + " Enunciado: " + listaPreguntas.get(position).getEnunciado() +
                                    " Categoría: " + listaPreguntas.get(position).getCategoria(), Toast.LENGTH_SHORT).show();*/

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
            }).attachToRecyclerView(recyclerView);
        }else{
            noCreadas.setVisibility(View.VISIBLE);
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
