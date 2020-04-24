package com.example.trackem.Auxiliares;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trackem.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPaciente extends RecyclerView.Adapter<AdapterPaciente.ViewHolder> {
    private final List<Usuario> mDataset;
    private ClickListener clickListener;

    public AdapterPaciente(List<Usuario> myDataset) {
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
                R.layout.item_usuario, null);
        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        ));
        return new ViewHolder(itemLayoutView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario usuario =  mDataset.get(position);
        holder.nombre.setText(usuario.getNombre());
        Glide.with(holder.nombre.getContext()).load(usuario.getImagen())
                .fallback(R.drawable.ic_fallback_user)
                .skipMemoryCache(true)
                .into(holder.cv);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView nombre;
        final CircleImageView cv;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            nombre = itemLayoutView.findViewById(R.id.nom);
            cv = itemLayoutView.findViewById(R.id.imagenusuario);
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
