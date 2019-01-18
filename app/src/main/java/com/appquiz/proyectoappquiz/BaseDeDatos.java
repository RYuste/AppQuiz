package com.appquiz.proyectoappquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class BaseDeDatos extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla Pregunta
    String sqlCreate = "CREATE TABLE Pregunta (id_pregunta INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, enunciado TEXT, categoria TEXT, " +
            "correcto TEXT, incorrecto_1 TEXT, incorrecto_2 TEXT, incorrecto_3 TEXT, foto TEXT)";

    //Constructor
    public BaseDeDatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Para el control de versiones
    }
}
