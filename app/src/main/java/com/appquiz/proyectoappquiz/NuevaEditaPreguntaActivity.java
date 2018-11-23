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

        // Si el bundle NO es null, rellena los campos de EditText de la pregunta a editar
        if(bundle != null){
            getSupportActionBar().setTitle("Editar Pregunta");
            Pregunta preguntaAEditar = Repositorio.getRepositorio().consultaListarPreguntaEditar(myContext, bundle.getInt("ID"));

            enunciado.setText(preguntaAEditar.getEnunciado());
            correcto.setText(preguntaAEditar.getCorrecto());
            falso1.setText(preguntaAEditar.getIncorrecto_1());
            falso2.setText(preguntaAEditar.getIncorrecto_2());
            falso3.setText(preguntaAEditar.getIncorrecto_3());
        }

        //Botón para guardar una nueva pregunta
        Button botonGuardar = (Button) findViewById(R.id.buttonGuardar);
        botonGuardar.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                //Oculta el teclado al pulsar el botón Guardar
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(enunciado.getWindowToken(), 0);

                //Permisos de escritura
                int WriteExternalStoragePermission = ContextCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                Log.d("NuevaEditaPreguntaActivity", "WRITE_EXTERNAL_STORAGE Permission: " + WriteExternalStoragePermission);

                //Si alguno de los editText está vacío o no se ha seleccionado ninguna categoría, salta el snackbar
                if(enunciado.getText().toString().isEmpty() || correcto.getText().toString().isEmpty() || falso1.getText().toString().isEmpty() ||
                        falso2.getText().toString().isEmpty() || falso3.getText().toString().isEmpty() ||
                        spinner.getSelectedItem().toString().isEmpty()){
                    Snackbar.make(view, "Rellena todos los campos para Guardar.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else{
                    if (WriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED){
                        // Permiso denegado
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            ActivityCompat.requestPermissions(NuevaEditaPreguntaActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                            // Una vez que se pide aceptar o rechazar el permiso se ejecuta el método "onRequestPermissionsResult" para manejar la respuesta
                            // Si el usuario marca "No preguntar más" no se volverá a mostrar este diálogo
                        } else {
                            Snackbar.make(view, "Permisos de escritura denegados.", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }
                    }else {
                        //GUARDA LA PREGUNTA
                        Pregunta p = new Pregunta(enunciado.getText().toString(), spinner.getSelectedItem().toString(), correcto.getText().toString(),
                                                    falso1.getText().toString(), falso2.getText().toString(), falso3.getText().toString());

                        // Si el bundle NO en null, Actualiza la pregunta, sino, crea una nueva
                        if(bundle != null){
                            boolean correcto = Repositorio.getRepositorio().consultaActualizarPregunta(myContext, p, bundle.getInt("ID"));

                            if(correcto == true){
                                Snackbar.make(view, "Pregunta actualizada con éxito.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();

                                esperarYCerrar();
                            }else{
                                Snackbar.make(view, "Error al actualizar la pregunta.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }
                        }else{
                            boolean correcto = Repositorio.getRepositorio().consultaAñadirPregunta(p, myContext);

                            if(correcto == true){
                                Snackbar.make(view, "Pregunta guardada con éxito.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();

                                esperarYCerrar();
                            }else{
                                Snackbar.make(view, "Error al guardar la pregunta.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }
                        }
                    }
                }
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
                        .setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        boolean correcto = true;
                                        // Si coindice la nueva categoria con una ya existente, no la guarda en el spinner
                                        for (int i = 0; i < spinner.getCount(); i++) {
                                            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(dialogInput.getText().toString())) {
                                                Snackbar.make(view, "Error al guardar la categoría.", Snackbar.LENGTH_SHORT)
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
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                })
                        .create()
                        .show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CODE_WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso aceptado
                    Snackbar.make(constraintLayoutMainActivity, "Permisos de escritura aceptados.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } else {
                    // Permiso rechazado
                    Snackbar.make(constraintLayoutMainActivity, "Permisos de escritura denegados.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
