package com.example.trackem.Auxiliares;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackem.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterSucesoExtraordinario extends RecyclerView.Adapter<AdapterSucesoExtraordinario.ViewHolder> {
    private List<Suceso> mDataset;
    private ClickListener clickListener;

    public AdapterSucesoExtraordinario(List<Suceso> myDataset) {
        mDataset = myDataset;
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
                R.layout.item_suceso_extraordinario, null);
        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        ));
        return new ViewHolder(itemLayoutView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Suceso suceso =  mDataset.get(position);
        long a = suceso.fecha;
        SimpleDateFormat dateFormat = new SimpleDateFormat(" dd/MM/yyyy\n HH:mm", Locale.ENGLISH);
        Date d = new Date(a);
        holder.fecha.setText(dateFormat.format(d));
        holder.desc.setText(suceso.descripción);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView fecha;
        final TextView desc;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            fecha =  itemLayoutView.findViewById(R.id.fecha);
            desc =  itemLayoutView.findViewById(R.id.sdesc);
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

    public void addAll(List<Suceso> events) {
        mDataset = events;
    }
}
