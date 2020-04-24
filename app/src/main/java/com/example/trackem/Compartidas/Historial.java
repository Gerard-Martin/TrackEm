package com.example.trackem.Compartidas;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.trackem.Auxiliares.Diario;
import com.example.trackem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Historial extends AppCompatActivity {
    private String id = "";
    private List<DataEntry> seriesData;
    private Cartesian cartesian;
    private AnyChartView anyChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getIntent().hasExtra("id"))id = getIntent().getStringExtra("id");
        anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        seriesData = new ArrayList<>();

        cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Historial");

        cartesian.yAxis(0).title("Parámetros");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        recuperar();

    }

    private void recuperar() {
        if(id.equals("")) id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore.getInstance().collection("Datos")
                .document(id)
                .collection("Seguimiento")
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> l = Objects.requireNonNull(task.getResult()).getDocuments();
                            for (DocumentSnapshot document : l) {
                                Diario d = document.toObject(Diario.class);
                                seriesData.add(new CustomDataEntry(Objects.requireNonNull(d)));
                            }
                            generarGrafica();
                        }
                    }
                });
    }

    private static class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(Diario d) {
            super(new SimpleDateFormat("dd-MM-yyyy").format(new Date(d.time)), Math.round(d.concentracion * 100.0) / 100.0);
            setValue("value2", Math.round(d.fatiga * 100.0) / 100.0);
            setValue("value3", Math.round(d.memoria * 100.0) / 100.0);
            setValue("value4",Math.round(d.animo * 100.0) / 100.0);
            setValue("value5", Math.round(d.energia * 100.0) / 100.0);
            setValue("value6", Math.round(d.sueño * 100.0) / 100.0);
        }

    }
    
    private void generarGrafica(){
        if(seriesData.isEmpty()){
            findViewById(R.id.any_chart_view).setVisibility(View.GONE);
            findViewById(R.id.nodata).setVisibility(View.VISIBLE);
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            return;
        }
        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");
        Mapping series4Mapping = set.mapAs("{ x: 'x', value: 'value4' }");
        Mapping series5Mapping = set.mapAs("{ x: 'x', value: 'value5' }");
        Mapping series6Mapping = set.mapAs("{ x: 'x', value: 'value6' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Concentración");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name("Memoria");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series3 = cartesian.line(series3Mapping);
        series3.name("Energía");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series4 = cartesian.line(series4Mapping);
        series4.name("Fatiga");
        series4.hovered().markers().enabled(true);
        series4.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series4.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series5 = cartesian.line(series5Mapping);
        series5.name("Ánimo");
        series5.hovered().markers().enabled(true);
        series5.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series5.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series6 = cartesian.line(series6Mapping);
        series6.name("Sueño");
        series6.hovered().markers().enabled(true);
        series6.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series6.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);
        anyChartView.setChart(cartesian);
    }

}