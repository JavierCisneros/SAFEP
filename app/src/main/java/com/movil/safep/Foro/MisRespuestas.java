package com.movil.safep.Foro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movil.safep.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MisRespuestas extends AppCompatActivity {

    private ImageView atras;
    private TextView tvPregunta,tvCalificacion,tvId;
    private RecyclerView rRecyclerView;
    private RespuestaAdapterM rAdapter;
    private ArrayList<Respuestas> rList = new ArrayList<>();
    private ArrayList<Calificaciones> rcList = new ArrayList<>();
    public static ArrayList<String> ridList = new ArrayList<>();
    private ArrayList<Usuario> nameList = new ArrayList<>();
    private ArrayList<String> listaC = new ArrayList<>();
    int uno=0;
    int cero=0;
    public static ArrayList<String> sCalRMI = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_respuestas);
        atras= findViewById(R.id.btAtrasMR);
        tvPregunta = findViewById(R.id.tvPreResMR);
        tvId = findViewById(R.id.tvIdRMR);
        tvPregunta.setText(""+Mispreguntas.pregunta);
        tvId.setText(""+Mispreguntas.idPregunta);
        rRecyclerView = findViewById(R.id.RcRespuestasMR);
        rRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rAdapter = new RespuestaAdapterM(rList,rcList,nameList,R.layout.respuesta_view);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String id= tvId.getText().toString();
        System.out.println(""+id);
        rRecyclerView.setAdapter(rAdapter);
        sCalRMI.clear();
        getrespuestasfb(id);
    }
    private void getrespuestasfb(String idP){
        sCalRMI.clear();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child("foro"+date).child("inversion").child(""+idP).child("respuesta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    rList.clear();
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String res= ds.child("respuesta").getValue().toString();
                        String cal = ds.child("calificacion").getValue().toString();
                        String nam = ds.child("usuario").getValue().toString();
                        String id = ds.getKey().toString();
                        rList.add(new Respuestas(res));
                        rcList.add(new Calificaciones(cal));
                        nameList.add(new Usuario(nam));
                        ridList.add(""+id);
                        System.out.println(""+ridList.toString());
                    }

                    getCal();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void getCal()
    {
        uno=0;
        cero=0;
        System.out.println("getCal "+ridList.size());
        System.out.println("id"+ridList.toString());
        String id= tvId.getText().toString();
        for (int i = 0; i < ridList.size(); i++){
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference();
            mDatabase1.child("Users").child("foro" + date).child("inversion").child("" + id).child("respuesta").child(""+ridList.get(i)).child("cal").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String c = ds.child("c").getValue().toString();

                            if (c.equals("0")) {
                                cero++;
                            }
                            if (c.equals("1")) {
                                uno++;
                            }

                            System.out.println("key" + ds.getRef());

                        }
                        sCalRMI.add("Buena: "+uno+"Mala: "+cero);
                        System.out.println("Uno: "+uno+"Mal: "+cero);
                        uno = 0;
                        cero = 0;

                    }
                    else{
                        sCalRMI.add("Buena: "+uno+"Mala: "+cero);
                        System.out.println("Uno: "+uno+"Mal: "+cero);
                        uno=0;
                        cero=0;

                    }
                    rRecyclerView.setAdapter(rAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
