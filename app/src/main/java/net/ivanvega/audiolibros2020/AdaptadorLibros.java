package net.ivanvega.audiolibros2020;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class AdaptadorLibros extends  RecyclerView.Adapter<AdaptadorLibros.ViewHolder> {

    private  Vector<Libro> vectorLibros;
    private  Context contexto;
    private  LayoutInflater inflador;

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public void setOnclickListener(View.OnClickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

    private View.OnLongClickListener onLongClickListener;
    private View.OnClickListener onclickListener;


    public AdaptadorLibros(Context contexto,
                           Vector<Libro> vectorLibros) {
        inflador = (LayoutInflater) contexto.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        this.vectorLibros = vectorLibros;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = inflador.inflate(R.layout.elemento_selector, null);

        v.setOnClickListener(this.onclickListener);
        v.setOnLongClickListener(this.onLongClickListener);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Libro libro = vectorLibros.get(position);

        holder.portada.setImageResource(libro.recursoImagen);
        holder.titulo.setText(libro.titulo);
    }

    @Override
    public int getItemCount() {
        return vectorLibros.size();
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView portada;
        public TextView titulo;

        public ViewHolder(View itemView)
        {
            super(itemView);
            portada = (ImageView) itemView.findViewById(R.id.portada);
            portada.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
        }
    }

}
