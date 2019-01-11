package com.appquiz.proyectoappquiz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class NuevaEditaPreguntaActivity extends AppCompatActivity {

    private String TAG = "NuevaEditaPreguntaActivity";
    private EditText enunciado, correcto, falso1, falso2, falso3;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;

    final private int CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 123;
    private Context myContext;
    private ConstraintLayout constraintLayoutMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLog.d(TAG, "Iniciando onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_pregunta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();

        // Almacenamos el contexto de la actividad para utilizar en las clases internas
        myContext = this;
        // Recuperamos el Layout donde mostrar el Snackbar con las notificaciones
        constraintLayoutMainActivity = findViewById(R.id.layout);

        //Recuperamos la información pasada en el intent
        final Bundle bundle = this.getIntent().getExtras();

        enunciado = (EditText) findViewById(R.id.editTextEnunciado);
        correcto = (EditText) findViewById(R.id.editTextRCorrecta);
        falso1 = (EditText) findViewById(R.id.editTextRIncorrecta1);
        falso2 = (EditText) findViewById(R.id.editTextRIncorrecta2);
        falso3 = (EditText) findViewById(R.id.editTextRIncorrecta3);

        // Definición de la lista de opciones del spinner almacenados en la BD
        ArrayList<String> items = Repositorio.getRepositorio().consultaListarCategorias(myContext);
        // Definición del Adaptador que contiene la lista de opciones
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        // Definición del Spinner
        spinner = (Spinner) findViewById(R.id.spinnerCategoria);
        spinner.setAdapter(adapter);

        // Botón para guardar una nueva pregunta
        final Button botonGuardar = (Button) findViewById(R.id.buttonGuardar);
        // Botón para eliminar pregunta
        final Button botonEliminar = (Button) findViewById(R.id.buttonEliminar);
        final Button botonEliminar2 = (Button) findViewById(R.id.buttonEliminar2);
        // Botón para abrir cámara
        final Button botonCamara = (Button) findViewById(R.id.buttonCamara);
        // Botón para abrir galería
        final Button botonGaleria = (Button) findViewById(R.id.buttonGaleria);


        // Si el bundle NO es null, rellena los campos de EditText de la pregunta a editar
        if(bundle != null){
            // Cambia título de la actividad
            getSupportActionBar().setTitle(R.string.title_activity_editarPregunta);
            // Habilita el botón Eliminar
            botonEliminar.setEnabled(true);
            botonEliminar2.setVisibility(View.INVISIBLE);

            Pregunta preguntaAEditar = Repositorio.getRepositorio().consultaListarPreguntaEditar(myContext, bundle.getInt("ID"));
            enunciado.setText(preguntaAEditar.getEnunciado());
            correcto.setText(preguntaAEditar.getCorrecto());
            falso1.setText(preguntaAEditar.getIncorrecto_1());
            falso2.setText(preguntaAEditar.getIncorrecto_2());
            falso3.setText(preguntaAEditar.getIncorrecto_3());
        }else{
            // Inhabilita el botón Eliminar
            botonEliminar.setEnabled(false);
            //botonEliminar.setBackgroundColor(getResources().getColor(R.color.colorEnabled);
            botonEliminar.setVisibility(View.INVISIBLE);
        }

        // Acción al pulsar el botón GUARDAR
        botonGuardar.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                //Oculta el teclado al pulsar el botón Guardar
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(enunciado.getWindowToken(), 0);

                //Permisos de escritura
                //int WriteExternalStoragePermission = ContextCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                //Log.d("NuevaEditaPreguntaActivity", "WRITE_EXTERNAL_STORAGE Permission: " + WriteExternalStoragePermission);

                //Si alguno de los editText está vacío o no se ha seleccionado ninguna categoría, salta el snackbar
                if(enunciado.getText().toString().isEmpty() || correcto.getText().toString().isEmpty() || falso1.getText().toString().isEmpty() ||
                        falso2.getText().toString().isEmpty() || falso3.getText().toString().isEmpty() ||
                        spinner.getAdapter().isEmpty()){
                    Snackbar.make(view, R.string.rellenarCamposGuardar, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else{
                        //GUARDA LA PREGUNTA
                        Pregunta p = new Pregunta(enunciado.getText().toString(), spinner.getSelectedItem().toString(), correcto.getText().toString(),
                                                    falso1.getText().toString(), falso2.getText().toString(), falso3.getText().toString());

                        // Si el bundle NO en null, actualiza la pregunta, sino, crea una nueva
                        if(bundle != null){
                            boolean correcto = Repositorio.getRepositorio().consultaActualizarPregunta(myContext, p, bundle.getInt("ID"));

                            if(correcto == true){
                                Snackbar.make(view, R.string.actualizarPregunta_exito, Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();

                                esperarYCerrar();
                            }else{
                                Snackbar.make(view, R.string.actualizarPregunta_error, Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }
                        }else{
                            boolean correcto = Repositorio.getRepositorio().consultaAñadirPregunta(p, myContext);

                            if(correcto == true){
                                Snackbar.make(view, R.string.guardarPregunta_exito, Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();

                                esperarYCerrar();
                            }else{
                                Snackbar.make(view, R.string.guardarPregunta_error, Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }
                        }
                }
            }
        });

        // Acción del botón ELIMINAR
        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Oculta el teclado al pulsar el botón Guardar
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(enunciado.getWindowToken(), 0);

                // Elimina la pregunta
                AlertDialog.Builder builder = new AlertDialog.Builder(NuevaEditaPreguntaActivity.this);
                builder.setMessage("¿Deseas eliminar esta pregunta?"); //set message

                builder.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean correcto = Repositorio.getRepositorio().consultaEliminarPregunta(myContext, bundle.getInt("ID"));

                        if(correcto == true){
                            Snackbar.make(view, R.string.eliminarPregunta_exito, Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();

                            esperarYCerrar();
                        }else{
                            Snackbar.make(view, R.string.eliminarPregunta_error, Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }
                    }
                }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show(); //show alert dialog
            }
        });

        // Definición de la acción del botón para añadir categorías
        Button button = (Button) findViewById(R.id.buttonAddItem);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // Recuperación de la vista del AlertDialog a partir del layout de la Actividad
                LayoutInflater layoutActivity = LayoutInflater.from(myContext);
                View viewAlertDialog = layoutActivity.inflate(R.layout.alert_dialog, null);

                // Definición del AlertDialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(myContext);

                // Asignación del AlertDialog a su vista
                alertDialog.setView(viewAlertDialog);

                // Recuperación del EditText del AlertDialog
                final EditText dialogInput = (EditText) viewAlertDialog.findViewById(R.id.dialogInput);

                // Configuración del AlertDialog
                alertDialog
                        .setCancelable(false)
                        // Botón Añadir
                        .setPositiveButton(R.string.aceptar,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        boolean correcto = true;
                                        // Si se intenta Aceptar con el dialog vacío, salta un error
                                        if(dialogInput.getText().toString().isEmpty()){
                                            Snackbar.make(view, R.string.error_guardarCategoria, Snackbar.LENGTH_SHORT)
                                                    .setAction("Action", null).show();
                                            correcto = false;
                                        }
                                        // Si coindice la nueva categoria con una ya existente, no la guarda en el spinner
                                        for (int i = 0; i < spinner.getCount(); i++) {
                                            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(dialogInput.getText().toString())) {
                                                Snackbar.make(view, R.string.error_guardarCategoria, Snackbar.LENGTH_SHORT)
                                                        .setAction("Action", null).show();
                                                correcto = false;
                                            }
                                        }
                                        if(correcto == true){
                                            adapter.add(dialogInput.getText().toString());
                                            spinner.setSelection(adapter.getPosition(dialogInput.getText().toString()));
                                        }
                                    }
                                })
                        // Botón Cancelar
                        .setNegativeButton(R.string.cancelar,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                })
                        .create()
                        .show();
            }
        });

        // Acción al pulsar el botón CAMARA
        botonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permisosEscritura(v) == true){

                    //Oculta el teclado al pulsar el botón CAMARA
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(enunciado.getWindowToken(), 0);


                }
            }
        });

        // Acción al pulsar el botón GALERIA
        botonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permisosEscritura(v) == true){

                    //Oculta el teclado al pulsar el botón GALERIA
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(enunciado.getWindowToken(), 0);


                }
            }
        });

        MyLog.d(TAG, "Cerrando onCreate...");
    }

    /**
     * Espera y cierra la aplicación tras los milisegundos indicados
     */
    public void esperarYCerrar() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                finish();
            }
        }, 2000);
    }

    /**
     * Método que contiene los permisos de escritura
     *
     * @param view
     * @return true o false
     */
    public boolean permisosEscritura(View view){
        boolean aceptar = true;
        int WriteExternalStoragePermission = ContextCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.d("NuevaEditaPregunta", "WRITE_EXTERNAL_STORAGE Permission: " + WriteExternalStoragePermission);

        if (WriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED){
            // Permiso denegado
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                ActivityCompat.requestPermissions(NuevaEditaPreguntaActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                // Una vez que se pide aceptar o rechazar el permiso se ejecuta el método "onRequestPermissionsResult" para manejar la respuesta
                // Si el usuario marca "No preguntar más" no se volverá a mostrar este diálogo

                aceptar = false;
            } else {
                Snackbar.make(view, R.string.perEscDene, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                aceptar = false;
            }
        }
        return aceptar;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CODE_WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso aceptado
                    Snackbar.make(constraintLayoutMainActivity, R.string.perEscAcep, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } else {
                    // Permiso rechazado
                    Snackbar.make(constraintLayoutMainActivity, R.string.perEscDene, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
