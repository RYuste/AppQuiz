package com.appquiz.proyectoappquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class Repositorio {

    private String TAG = "Repositorio";

    // Singleton
    private static Repositorio miRepo = null;
    public static Repositorio getRepositorio(){
        if(miRepo == null){
            miRepo = new Repositorio();
        }
        return miRepo;
    }

    /*-------------------------------------------------------*/

    /**
     * Añade una pregunta a la BD
     *
     * @param p
     * @param myContext
     * @return
     */
    public boolean consultaAñadirPregunta(Pregunta p, Context myContext){
        MyLog.d(TAG, "Entrando en AñadirPregunta...");

        boolean correcto;
        BaseDeDatos bd = new BaseDeDatos(myContext, "BDPregunta", null, 1);
        SQLiteDatabase db = bd.getWritableDatabase();

        if(db != null){
            db.execSQL("INSERT INTO Pregunta(enunciado, categoria, correcto, incorrecto_1, incorrecto_2, incorrecto_3) " +
                    "VALUES('"+p.getEnunciado()+"', '"+p.getCategoria()+"', '"+p.getCorrecto()+"', " +
                    "'"+p.getIncorrecto_1()+"', '"+p.getIncorrecto_2()+"', '"+p.getIncorrecto_3()+"')");

            correcto = true;
            db.close();

            MyLog.d(TAG, "Saliendo de AñadirPregunta...");
        }else {
            correcto = false;
            db.close();

            MyLog.d(TAG, "Error null en AñadirPregunta...");
        }
        MyLog.d(TAG, "Saliendo del método AñadirPregunta...");
        return correcto;
    }

    /**
     * Lista todas las preguntas
     */
    /*public void consultaListarPreguntas(){
        BaseDeDatos bd = new BaseDeDatos(myContext, "BDPregunta", null, 1);
        SQLiteDatabase db = bd.getWritableDatabase();

        db.execSQL("SELECT * FROM Pregunta");

        db.close();
    }*/
}
