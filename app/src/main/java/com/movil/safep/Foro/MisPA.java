package com.movil.safep.Foro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movil.safep.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MisPA extends AppCompatActivity {

    private ImageView atrasl;
    private PreguntaAdapterA pAdapterA;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private PreguntaMisA pAdapter;
    private RecyclerView pRecyclerView;
    private ArrayList<Preguntas> pList = new ArrayList<>();
    private ArrayList<Calificaciones> cList = new ArrayList<>();
    public static ArrayList<String> idList = new ArrayList<>();
    public static ArrayList<String> idListUno = new ArrayList<>();
    private ArrayList<Usuario> nameList = new ArrayList<>();
    public static String idPregunta,pregunta,cal;
    public static ArrayList<String> sCal = new ArrayList<>();
    int uno=0;
    int cero=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_p);
        atrasl= findViewById(R.id.btAtrasM);
        pRecyclerView=findViewById(R.id.rcViewA);
        pRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pAdapter = new PreguntaMisA(pList,cList,nameList,R.layout.pregunta_view);
        pRecyclerView.setAdapter(pAdapter);
        sCal.clear();
        getMisPreguntas();
        atrasl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int a = pRecyclerView.getChildAdapterPosition(view);
                final String p = pList.get(a).toString();
                final String id = idList.get(a).toString();
                final String c= cList.get(a).toString();
                final String[] items = {"Mostrar respuestas"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(MisPA.this);
                builder.setTitle("Tema: \t"+pList.get(a).toString());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(MisPA.this, ""+items[item], Toast.LENGTH_SHORT).show();
                        if(items[item].equals(items[0])){
                            idPregunta = id;
                            pregunta = p;
                            cal = c;
                            System.out.println("id"+id);
                            System.out.println("p"+pregunta);
                            System.out.println("cal"+cal);
                            final Intent a = new Intent(MisPA.this, MisRA.class);
                            startActivity(a);
                        }
                    }
                });
                builder.setCancelable(true);
                builder.show();
            }
        });
    }
    private void getMisPreguntas(){
        sCal.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child("ID:"+id).child("foromio").child("preguntas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    idListUno.clear();
                    idList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String pre = ds.child("id").getValue().toString();
                        idListUno.add("" + pre);
                    }
                }
                System.out.println("Lista" + idListUno.size());

                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String in="inversion";
                for (int i = 0; i < idListUno.size(); i++) {
                    mDatabase.child("Users").child("foro" + date).child("ahorro").child("" + idListUno.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String pre = dataSnapshot.child("pregunta").child("pregunta").getValue().toString();
                                String cal = dataSnapshot.child("pregunta").child("calificacion").getValue().toString();
                                String nam = dataSnapshot.child("pregunta").child("usuario").getValue().toString();
                                String id = dataSnapshot.getKey().toString();
                                pList.add(new Preguntas(pre));
                                cList.add(new Calificaciones(cal));
                                nameList.add(new Usuario(nam));
                                idList.add("" + id);
                                System.out.println("p" + pList);
                                System.out.println("c" + cList);
                                System.out.println("n" + nameList);

                            }
                                getCal();
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
    }
    public void getCal() {
        uno=0;
        cero=0;
        for (int i = 0; i < idList.size(); i++){
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference();
            mDatabase1.child("Users").child("foro" + date).child("ahorro").child("" + idList.get(i)).child("pregunta").child("cal").addValueEventListener(new ValueEventListener() {
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


                        }
                        sCal.add(" Buena: "+uno+"Mala: "+cero);
                        uno = 0;
                        cero = 0;
                        pRecyclerView.setAdapter(pAdapter);
                    }
                    else{
                        sCal.add(" Buena: "+uno+"Mala: "+cero);
                        uno=0;
                        cero=0;
                        pRecyclerView.setAdapter(pAdapter);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
