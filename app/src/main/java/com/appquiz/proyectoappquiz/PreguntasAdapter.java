package com.appquiz.proyectoappquiz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Clase para almacenar el adaptador con los datos
 * de los acontecimientos que va a mostrar
 * el RecyclerView
 *
 * Hay que añadir al proyecto la siguiente
 * dependencia en el archivo /app/build.gradle
 * 'com.android.support:recyclerview-v7:+
 */
public class PreguntasAdapter extends RecyclerView.Adapter<PreguntasAdapter.PreguntasViewHolder>
        implements View.OnClickListener{

    private ArrayList<Pregunta> items;
    private View.OnClickListener listener;

    // Clase interna:
    // Se implementa el ViewHolder que se encargará
    // de almacenar la vista del elemento y sus datos
    public static class PreguntasViewHolder extends RecyclerView.ViewHolder {

        private TextView enunciado;
        private TextView categoria;

        public PreguntasViewHolder(View itemView) {
            super(itemView);
            enunciado = (TextView) itemView.findViewById(R.id.TextView_enunciado);
            categoria = (TextView) itemView.findViewById(R.id.TextView_categoria);
        }

        public void PreguntasBind(Pregunta item) {
            enunciado.setText(item.getEnunciado());
            categoria.setText(item.getCategoria());
        }
    }

    // Contruye el objeto adaptador recibiendo la lista de datos
    public PreguntasAdapter(@NonNull ArrayList<Pregunta> items) {
        this.items = items;
    }

    // Se encarga de crear los nuevos objetos ViewHolder necesarios
    // para los elementos de la colección.
    // Infla la vista del layout, crea y devuelve el objeto ViewHolder
    @Override
    public PreguntasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);
        row.setOnClickListener(this);

        PreguntasViewHolder pvh = new PreguntasViewHolder(row);
        return pvh;
    }

    // Se encarga de actualizar los datos de un ViewHolder ya existente.
    @Override
    public void onBindViewHolder(PreguntasViewHolder viewHolder, int position) {
        Pregunta item = items.get(position);
        viewHolder.PreguntasBind(item);
    }

    // Indica el número de elementos de la colección de datos.
    @Override
    public int getItemCount() {
        return items.size();
    }

    // Asigna un listener al elemento
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    /**
     * Limpia el recyclerview
     */
    public void borrarDatos() {
        items.clear();
    }
}
