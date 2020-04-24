package com.example.trackem.Auxiliares;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackem.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AdapterEvento extends RecyclerView.Adapter<AdapterEvento.ViewHolder> {
    private final List<EventPersonalizado> mDataset;
    private ClickListener clickListener;

    public AdapterEvento(List<EventPersonalizado> myDataset) {
        mDataset = myDataset;
        clickListener = new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

            }
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_event, null);
        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        ));
        return new ViewHolder(itemLayoutView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventPersonalizado evento =  mDataset.get(position);
        long a = evento.getTimeInMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        Date d = new Date(a);
        String hour = dateFormat.format(d);
        holder.hora.setText(hour);
        holder.desc.setText(Objects.requireNonNull(evento.getData()).toString());
        if(evento.getColor() == Color.parseColor("#FBB44E")){
            holder.tipo.setText(R.string.medi);
            holder.hora.setVisibility(View.GONE);
            holder.cv.getBackground().setTint(Color.parseColor("#FBB44E"));
        }else{
            holder.tipo.setText(R.string.cita);
            holder.cv.getBackground().setTint(Color.parseColor("#4EA5FB"));

        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView hora;
        final TextView tipo;
        final TextView desc;
        final CardView cv;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            hora =  itemLayoutView.findViewById(R.id.hora);
            tipo =  itemLayoutView.findViewById(R.id.tipo);
            desc =  itemLayoutView.findViewById(R.id.desc);
            cv = itemLayoutView.findViewById(R.id.card);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getPosition(), v);

        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void clear() {
        mDataset.clear();
    }

}
