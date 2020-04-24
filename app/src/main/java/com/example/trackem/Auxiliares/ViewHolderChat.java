package com.example.trackem.Auxiliares;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trackem.R;

public class ViewHolderChat extends RecyclerView.ViewHolder {

    public final RelativeLayout layoutChat;
    public final TextView msgPropio;
    public final TextView fechaPropia;
    public final TextView nombreOtro;
    public final TextView msgOtro;
    public final RelativeLayout msgOtroLayout;
    public final TextView fechaOtro;
    public final ImageView imagenPerfil;

    public ViewHolderChat(View itemView) {
        super(itemView);
        layoutChat = itemView.findViewById(R.id.msgPropio);
        msgPropio = itemView.findViewById(R.id.txtpropio);
        fechaPropia = itemView.findViewById(R.id.fecha);
        nombreOtro = itemView.findViewById(R.id.nombreotro);
        msgOtro = itemView.findViewById(R.id.txtotro);
        msgOtroLayout = itemView.findViewById(R.id.msgOtro);
        fechaOtro = itemView.findViewById(R.id.fechaotro);
        imagenPerfil = itemView.findViewById(R.id.imgotro);  
    }
}
