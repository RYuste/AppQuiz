package com.appquiz.proyectoappquiz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    // ArrayList de Preguntas
    private ArrayList<Pregunta> listaPreguntas = new ArrayList<Pregunta>();
    // ArrayList de categorías
    private ArrayList<String> listaCategorias = new ArrayList<String>();

    /*---------------------------------------------------------*/

    /**
     * Comprueba si la BD está vacía
     *
     * @param myContext
     * @return true o false
     */
    public boolean checkEmpty(Context myContext){
        int count = 0;
        BaseDeDatos bd = new BaseDeDatos(myContext, "BDPregunta", null, 1);
        SQLiteDatabase db = bd.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT count(*) FROM Pregunta", null);

        try {
            if(cursor != null)
                if(cursor.getCount() > 0){
                    cursor.moveToFirst();
                    count = cursor.getInt(0);
                }
        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        if(count>0)
            return false;
        else
            return true;
    }

    /**
     * Añade todas las preguntas creadas en la Base de Datos en un ArrayList y lo devuelve
     *
     * @param myContext
     * @return listaPreguntas
     */
    public ArrayList<Pregunta> consultaListarPreguntas(Context myContext){
        MyLog.d(TAG, "Entrando en ListarPreguntas...");

        BaseDeDatos bd = new BaseDeDatos(myContext, "BDPregunta", null, 1);
        SQLiteDatabase db = bd.getWritableDatabase();

        Cursor c = db.rawQuery(" SELECT * FROM Pregunta", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            listaPreguntas.removeAll(listaPreguntas);

            //Recorremos el cursor hasta que no haya más registros
            do {
                int id_pregunta= c.getInt(c.getColumnIndex("id_pregunta"));
                String enunciado= c.getString(c.getColumnIndex("enunciado"));
                String categoria = c.getString(c.getColumnIndex("categoria"));
                String correcto = c.getString(c.getColumnIndex("correcto"));
                String incorrecto_1 = c.getString(c.getColumnIndex("incorrecto_1"));
                String incorrecto_2 = c.getString(c.getColumnIndex("incorrecto_2"));
                String incorrecto_3 = c.getString(c.getColumnIndex("incorrecto_3"));
                String foto = c.getString(c.getColumnIndex("foto"));

                Pregunta p = new Pregunta(id_pregunta, enunciado, categoria, correcto, incorrecto_1, incorrecto_2, incorrecto_3, foto);
                listaPreguntas.add(p);
            } while(c.moveToNext());
        }
        db.close();
        MyLog.d(TAG, "Saliendo del método ListarPreguntas...");

        return listaPreguntas;
    }

    /**
     * Devuelve una pregunta seleccionada por id
     *
     * @param myContext
     * @return p
     */
    public Pregunta consultaListarPreguntaEditar(Context myContext, int id){
        MyLog.d(TAG, "Entrando en ListarPreguntaEditar...");

        BaseDeDatos bd = new BaseDeDatos(myContext, "BDPregunta", null, 1);
        SQLiteDatabase db = bd.getWritableDatabase();

        Pregunta p = null;
        Cursor c = db.rawQuery(" SELECT * FROM Pregunta WHERE id_pregunta = '"+id+"'", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            String enunciado= c.getString(c.getColumnIndex("enunciado"));
            String categoria = c.getString(c.getColumnIndex("categoria"));
            String correcto = c.getString(c.getColumnIndex("correcto"));
            String incorrecto_1 = c.getString(c.getColumnIndex("incorrecto_1"));
            String incorrecto_2 = c.getString(c.getColumnIndex("incorrecto_2"));
            String incorrecto_3 = c.getString(c.getColumnIndex("incorrecto_3"));
            String foto = c.getString(c.getColumnIndex("foto"));

            p = new Pregunta(enunciado, categoria, correcto, incorrecto_1, incorrecto_2, incorrecto_3, foto);
        }
        db.close();
        MyLog.d(TAG, "Saliendo del método ListarPreguntaEditar...");

        return p;
    }

    /**
     * Añade una pregunta a la BD
     *
     * @param p
     * @param myContext
     * @return true o false
     */
    public boolean consultaAñadirPregunta(Pregunta p, Context myContext){
        MyLog.d(TAG, "Entrando en AñadirPregunta...");

        boolean correcto;
        BaseDeDatos bd = new BaseDeDatos(myContext, "BDPregunta", null, 1);
        SQLiteDatabase db = bd.getWritableDatabase();

        if(db != null){
            db.execSQL("INSERT INTO Pregunta(enunciado, categoria, correcto, incorrecto_1, incorrecto_2, incorrecto_3, foto) " +
                    "VALUES('"+p.getEnunciado()+"', '"+p.getCategoria()+"', '"+p.getCorrecto()+"', " +
                    "'"+p.getIncorrecto_1()+"', '"+p.getIncorrecto_2()+"', '"+p.getIncorrecto_3()+"', '"+p.getFoto()+"')");

            correcto = true;

            MyLog.d(TAG, "Saliendo de AñadirPregunta...");
        }else {
            correcto = false;

            MyLog.d(TAG, "Error null en AñadirPregunta...");
        }
        db.close();

        MyLog.d(TAG, "Saliendo del método AñadirPregunta...");
        return correcto;
    }


    /**
     * Actualiza una pregunta
     *
     * @param myContext
     * @param p
     * @param id
     * @return true o false
     */
    public boolean consultaActualizarPregunta(Context myContext, Pregunta p, int id){
        MyLog.d(TAG, "Entrando en ActualizarPregunta...");

        boolean correcto;
        BaseDeDatos bd = new BaseDeDatos(myContext, "BDPregunta", null, 1);
        SQLiteDatabase db = bd.getWritableDatabase();

        if(db != null){
            db.execSQL("UPDATE Pregunta SET enunciado = '"+p.getEnunciado()+"', categoria = '"+p.getCategoria()+"', " +
                    "correcto = '"+p.getCorrecto()+"', incorrecto_1 = '"+p.getIncorrecto_1()+"', incorrecto_2 = '"+p.getIncorrecto_2()+"'" +
                    ", incorrecto_3 = '"+p.getIncorrecto_3()+"', foto = '"+p.getFoto()+"' WHERE id_pregunta = '"+id+"'");

            correcto = true;

            MyLog.d(TAG, "Saliendo de ActualizarPregunta...");
        }else {
            correcto = false;

            MyLog.d(TAG, "Error null en ActualizarPregunta...");
        }
        db.close();

        MyLog.d(TAG, "Saliendo del método ActualizarPregunta...");
        return correcto;
    }

    /**
     * Lista las categorías de todas las preguntas
     *
     * @param myContext
     * @return listaCategorias
     */
    public ArrayList<String> consultaListarCategorias(Context myContext){
        MyLog.d(TAG, "Entrando en ListarCategorias...");

        BaseDeDatos bd = new BaseDeDatos(myContext, "BDPregunta", null, 1);
        SQLiteDatabase db = bd.getWritableDatabase();

        Cursor c = db.rawQuery(" SELECT DISTINCT categoria FROM Pregunta ORDER BY categoria ", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            listaCategorias.removeAll(listaCategorias);

            //Recorremos el cursor hasta que no haya más registros
            do {
                String categoria = c.getString(c.getColumnIndex("categoria"));
                listaCategorias.add(categoria);
            } while(c.moveToNext());
        }
        db.close();
        MyLog.d(TAG, "Saliendo del método ListarCategorias...");

        return listaCategorias;
    }

    /**
     * Elimina una pregunta por id
     *
     * @param myContext
     * @param id
     * @return true o false
     */
    public boolean consultaEliminarPregunta(Context myContext, int id){
        MyLog.d(TAG, "Entrando en EliminarPregunta...");

        boolean correcto;
        BaseDeDatos bd = new BaseDeDatos(myContext, "BDPregunta", null, 1);
        SQLiteDatabase db = bd.getWritableDatabase();

        if(db != null){
            db.execSQL("DELETE FROM Pregunta WHERE id_pregunta = '"+id+"'");

            correcto = true;

            MyLog.d(TAG, "Saliendo de EliminarPregunta...");
        }else {
            correcto = false;

            MyLog.d(TAG, "Error null en EliminarPregunta...");
        }
        db.close();

        MyLog.d(TAG, "Saliendo del método EliminarPregunta...");
        return correcto;
    }

    /**
     * Borra todas las preguntas de la BD
     *
     * @param myContext
     */
    public void consultaBorrarListadoPreguntas(Context myContext){
        MyLog.d(TAG, "Entrando en BorrarListadoPreguntas...");

        BaseDeDatos bd = new BaseDeDatos(myContext, "BDPregunta", null, 1);
        SQLiteDatabase db = bd.getWritableDatabase();

        db.execSQL("DELETE FROM Pregunta");
        db.close();

        MyLog.d(TAG, "Saliendo del método BorrarListadoPreguntas...");
    }

    /**
     * Devuelve el número de preguntas que hay creadas en la BD
     *
     * @param myContext
     * @return
     */
    public int consultaContarPreguntas(Context myContext){
        MyLog.d(TAG, "Entrando en consultaContarPreguntas...");
        int contador = 0;

        BaseDeDatos bd = new BaseDeDatos(myContext, "BDPregunta", null, 1);
        SQLiteDatabase db = bd.getWritableDatabase();

        Cursor c = db.rawQuery(" SELECT * FROM Pregunta", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                contador++;
            } while(c.moveToNext());
        }
        db.close();

        MyLog.d(TAG, "Entrando en consultaContarPreguntas...");
        return contador;
    }

    /**
     * Devuelve el número de categorías que hay creadas en la BD
     *
     * @param myContext
     * @return
     */
    public int consultaContarCategorias(Context myContext){
        MyLog.d(TAG, "Entrando en consultaContarCategorias...");
        int contador = 0;

        BaseDeDatos bd = new BaseDeDatos(myContext, "BDPregunta", null, 1);
        SQLiteDatabase db = bd.getWritableDatabase();

        Cursor c = db.rawQuery(" SELECT DISTINCT categoria FROM Pregunta ORDER BY categoria ", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                contador++;
            } while(c.moveToNext());
        }
        db.close();

        MyLog.d(TAG, "Entrando en consultaContarCategorias...");
        return contador;
    }
}
