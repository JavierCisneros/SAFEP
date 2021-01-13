package com.movil.safep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.movil.safep.Foro.MisRespuestas;
import com.movil.safep.Foro.Mispreguntas;

public class MisAI extends AppCompatActivity {

    private ImageView atras;
    private ImageView MisI,MisA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_ai);
        MisA=findViewById(R.id.btnEspacioAhorroM);
        MisI=findViewById(R.id.btnEspacioInversionM);
        atras=findViewById(R.id.btAtrasMI);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        MisA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MisAI.this, MisPA.class);
                startActivity(a);
            }
        });
        MisI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MisAI.this, Mispreguntas.class);
                startActivity(a);
            }
        });
    }
}
