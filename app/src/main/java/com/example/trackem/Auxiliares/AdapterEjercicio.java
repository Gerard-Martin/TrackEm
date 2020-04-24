package com.example.trackem.Auxiliares;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackem.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterEjercicio extends RecyclerView.Adapter<AdapterEjercicio.ViewHolder> {
    private List<Ejercicio> mDataset;
    private ClickListener clickListener;
    private final Context context;

    public AdapterEjercicio(List<Ejercicio> myDataset, Context c) {
        mDataset = myDataset;
        context = c;
        clickListener = new ClickListener() {
            @Override
            public void onItemClick() {

            }
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_ejercicios, null);
        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        ));
        return new ViewHolder(itemLayoutView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ejercicio ejercicio =  mDataset.get(position);
        long a = ejercicio.fecha;
        SimpleDateFormat dateFormat = new SimpleDateFormat(" dd/MM/yyyy \n HH:mm", Locale.ENGLISH);
        Date d = new Date(a);
        holder.fecha.setText(dateFormat.format(d));
        holder.desc.setText(ejercicio.descripcion);
        holder.tiempo.setText(String.format(Locale.getDefault(),"Duración: %d minutos.", ejercicio.tiempo));
        if(ejercicio.promedio < 1){
            holder.emoji.setImageDrawable(context.getDrawable(R.drawable.cara1));
        }else if (ejercicio.promedio > 1 && ejercicio.promedio < 2){
            holder.emoji.setImageDrawable(context.getDrawable(R.drawable.cara2));
        }else if (ejercicio.promedio > 2 && ejercicio.promedio < 3){
            holder.emoji.setImageDrawable(context.getDrawable(R.drawable.cara3));
        }else if (ejercicio.promedio > 3 && ejercicio.promedio < 4){
            holder.emoji.setImageDrawable(context.getDrawable(R.drawable.cara4));
        }else{
            holder.emoji.setImageDrawable(context.getDrawable(R.drawable.cara5));
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView fecha;
        final TextView desc;
        final TextView tiempo;
        final ImageView emoji;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            fecha =  itemLayoutView.findViewById(R.id.fecha);
            desc =  itemLayoutView.findViewById(R.id.sdesc);
            tiempo = itemLayoutView.findViewById(R.id.duracion);
            emoji = itemLayoutView.findViewById(R.id.face);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick();

        }
    }

    interface ClickListener {
        void onItemClick();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void clear() {
        mDataset.clear();
    }

    public void addAll(List<Ejercicio> events) {
        mDataset = events;
    }
}
