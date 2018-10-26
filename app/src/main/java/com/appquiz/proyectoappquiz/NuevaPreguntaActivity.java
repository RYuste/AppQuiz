package com.appquiz.proyectoappquiz;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NuevaPreguntaActivity extends AppCompatActivity {

    private String TAG = "NuevaPreguntaActivity";
    private EditText enunciado, correcto, falso1, falso2, falso3;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLog.d(TAG, "Iniciando onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_pregunta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        enunciado = (EditText) findViewById(R.id.editTextEnunciado);
        correcto = (EditText) findViewById(R.id.editTextRCorrecta);
        falso1 = (EditText) findViewById(R.id.editTextRIncorrecta1);
        falso2 = (EditText) findViewById(R.id.editTextRIncorrecta2);
        falso3 = (EditText) findViewById(R.id.editTextRIncorrecta3);

        spinner = (Spinner) findViewById(R.id.spinnerCategoria);
        String[] categorias = {"Selecciona una categoría", "Java", "Python", "R", "Go"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias));

        //botón para guardar una nueva pregunta
        Button botonGuardar = (Button) findViewById(R.id.buttonGuardar);
        botonGuardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Oculta el teclado al pulsar el botón Guardar
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(enunciado.getWindowToken(), 0);

                //Si alguno de los editText está vacío o no se ha seleccionado ninguna categoría, salta el snackbar
                if(enunciado.getText().toString().isEmpty() || correcto.getText().toString().isEmpty() || falso1.getText().toString().isEmpty() ||
                        falso2.getText().toString().isEmpty() || falso3.getText().toString().isEmpty() ||
                        spinner.getSelectedItem().toString().equals("Selecciona una categoría")){
                    Snackbar.make(view, "Rellena todos los campos para Guardar.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else{
                    //GUARDA LA PREGUNTA
                }
            }
        });

        MyLog.d(TAG, "Cerrando onCreate...");
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
