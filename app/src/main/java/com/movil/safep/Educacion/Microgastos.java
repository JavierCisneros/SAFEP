package com.movil.safep.Educacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.movil.safep.R;

public class Microgastos extends AppCompatActivity {
    private ImageView atras,tema;
    private TextView titulo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microgastos);
        titulo = findViewById(R.id.tituloA);
        tema = findViewById(R.id.ImagenTipo);
        Intent a = getIntent();
        Bundle b = a.getExtras();
        Resources r = Resources.getSystem();
        final int tipo;
        if(b==null)
        {
            tipo = 1;
        }
        else {
        tipo = (Integer) b.get("Tipo");
        }

        if(tipo==1){
            titulo.setText("Microgastos");
            tema.setImageResource(R.drawable.que_micro);

        }
        if(tipo==2){
            titulo.setText("Microgastos");
            tema.setImageResource(R.drawable.imp_micro);
        }
        if(tipo==3){
            titulo.setText("Microgastos");
            tema.setImageResource(R.drawable.ayu_micro);
        }
        if(tipo==4){
            titulo.setText("Finanzas Personales");
            tema.setImageResource(R.drawable.que_per);
        }
        if(tipo==5){
            titulo.setText("Finanzas Personales");
            tema.setImageResource(R.drawable.imp_per);
        }
        if(tipo==6){
            titulo.setText("Finanzas Personales");
            tema.setImageResource(R.drawable.ayu_per);
        }
        if(tipo==7){
            titulo.setText("Inversiones");
            tema.setImageResource(R.drawable.que_inv);
        }
        if(tipo==8){
            titulo.setText("Inversiones");
            tema.setImageResource(R.drawable.imp_inv);
        }
        if(tipo==9){
            titulo.setText("Inversiones");
            tema.setImageResource(R.drawable.ayu_inv);
        }
        if(tipo==10){
            titulo.setText("Finanzas Sanas");
            tema.setImageResource(R.drawable.que_sanas);
        }
        if(tipo==11){
            titulo.setText("Finanzas Sanas");
            tema.setImageResource(R.drawable.imp_sanas);
        }
        if(tipo==12){
            titulo.setText("Finanzas Sanas");
            tema.setImageResource(R.drawable.ayu_sanas);
        }
        if(tipo==13){
            titulo.setText("Ahorro");
            tema.setImageResource(R.drawable.que_ahorro);
        }
        if(tipo==14){
            titulo.setText("Ahorro");
            tema.setImageResource(R.drawable.imp_ahorro);
        }
        if(tipo==15){
            titulo.setText("Ahorro");
            tema.setImageResource(R.drawable.ayu_ahorro);
        }

        atras= findViewById(R.id.atrasM);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
