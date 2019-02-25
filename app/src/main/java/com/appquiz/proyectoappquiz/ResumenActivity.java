package com.appquiz.proyectoappquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResumenActivity extends AppCompatActivity {

    private String TAG = "ResumenActivity";
    private Context myContext;
    private TextView cantPreguntas;
    private TextView cantCategorias;

    private Animation animation;
    private ImageView imgAnimation;
    private Button btnAnimation, btnAnimationStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLog.d(TAG, "Iniciando onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgAnimation = (ImageView) findViewById(R.id.imageAnimation);
        btnAnimation = (Button) findViewById(R.id.buttonAnimation);
        btnAnimationStop = (Button) findViewById(R.id.buttonStop);

        btnAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trans();
            }
        });

        btnAnimationStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transStop();
            }
        });

        MyLog.d(TAG, "Cerrando onCreate...");
    }

    /**
     * Inicia la animación
     */
    private void trans(){
        animation = AnimationUtils.loadAnimation(this, R.anim.trans);
        animation.setFillAfter(true);
        imgAnimation.startAnimation(animation);
    }

    /**
     * Detiene la animación
     */
    private void transStop(){
        imgAnimation.clearAnimation();
    }

    /*--------------------- AJUSTES ------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resumen, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.i("ActionBar", "Acerca de");;
                startActivity(new Intent(ResumenActivity.this, AcercadeActivity.class));
                return true;
            case R.id.action_questions:
                Log.i("ActionBar", "Listado de Preguntas");;
                startActivity(new Intent(ResumenActivity.this, ListadoPreguntasActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*---------------------------------------------------------------------------*/

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

        cantPreguntas = (TextView) findViewById(R.id.textViewCantPreg);
        cantCategorias = (TextView) findViewById(R.id.textViewCantCat);

        int contPreguntas = Repositorio.getRepositorio().consultaContarPreguntas(myContext);
        int contCategorias = Repositorio.getRepositorio().consultaContarCategorias(myContext);

        String contPrg = String.valueOf(contPreguntas);
        String contCat = String.valueOf(contCategorias);

        cantPreguntas.setText(contPrg);
        cantCategorias.setText(contCat);

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
