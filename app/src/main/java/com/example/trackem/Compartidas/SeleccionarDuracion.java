package com.example.trackem.Compartidas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackem.R;
import com.yinglan.circleviewlibrary.CircleAlarmTimerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class SeleccionarDuracion extends AppCompatActivity {
    private String i = "00:00";
    private String f = "00:00";
    private TextView init;
    private TextView fin;
    private TextView dur;
    private long diffMinutes;
    private long diffHours;
    private SimpleDateFormat s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).hide();
        setContentView(R.layout.activity_seleccionar_duracion);
        CircleAlarmTimerView c = findViewById(R.id.circletimerview);
        init = findViewById(R.id.init);
        fin = findViewById(R.id.fin);
        dur = findViewById(R.id.dur);
        s = new SimpleDateFormat("HH:mm");
        c.setOnTimeChangedListener(new CircleAlarmTimerView.OnTimeChangedListener() {
            @Override
            public void start(String starting) {
                i=starting;
                fijarDuración();
                init.setText(String.format(getResources().getString(R.string.inicio_s), starting));
            }

            @Override
            public void end(String ending) {
                f = ending;
                fijarDuración();
                fin.setText(String.format(getResources().getString(R.string.fin_s), ending));
            }
        });
    }

    public void guardar(View view) {
        if(diffMinutes < 0 || diffHours<0){
            Toast.makeText(this, "Seleccione un valor positivo", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent in = getIntent();
        in.putExtra("duracion", diffHours*60+diffMinutes);
        in.putExtra("inicio", i);
        setResult(Activity.RESULT_OK,in);
        finish();

    }

    private void fijarDuración(){
        Date d1 = null, d2 = null;
        try {
            d1 = s.parse(i);
            d2 = s.parse(f);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = Objects.requireNonNull(d2).getTime() - Objects.requireNonNull(d1).getTime();
        diffMinutes = (diff / (60 * 1000))- (diff / (60 * 60 * 1000)*60);
        diffHours = diff / (60 * 60 * 1000);
        dur.setText(String.format(getResources().getString(R.string.dur), diffHours, diffMinutes));
    }
}
