package com.appquiz.proyectoappquiz;

import android.os.Environment;

public class Constants {
    public static final float VIEWPORT_WIDTH = 5.0f;
    public static final String REST_URL = "http://www.ejemplo.com/api";

    public static final String BDNOMBRE = "BDPregunta";
    public static final String BD_CHECKEMPTY = "SELECT count(*) FROM Pregunta";
    public static final String BD_LISTARPREGUNTAS = "SELECT * FROM Pregunta";
    public static final String BD_LISTARCATEGORIAS = "SELECT DISTINCT categoria FROM Pregunta ORDER BY categoria";
    public static final String BD_BORRARLISTADO = "DELETE FROM Pregunta";

    public static final String RUTA_FOTOS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/misfotos/";
    public static final String PATH_FOTOS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/demoAndroidImages/";
    public static final int CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 123;
    public static final int REQUEST_CAPTURE_IMAGE = 200;
    public static final int REQUEST_SELECT_IMAGE = 201;
}
