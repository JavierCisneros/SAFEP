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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movil.safep.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Respuesta extends AppCompatActivity {

    private ImageView atras;
    private TextView tvPregunta,tvCalificacion,tvId,tvRespuesta;
    private Button btRespuesta;
    private RecyclerView rRecyclerView;
    private RespuestaAdapter rAdapter;
    private ArrayList<Respuestas> rList = new ArrayList<>();
    private ArrayList<Calificaciones> rcList = new ArrayList<>();
    public static ArrayList<String> ridList = new ArrayList<>();
    public static ArrayList<String> idUListA = new ArrayList<>();
    private ArrayList<Usuario> nameList = new ArrayList<>();
    private String nombre;
    private ArrayList<String> listaC = new ArrayList<>();
    int uno=0;
    int cero=0;
    public static ArrayList<String> sCalRI = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_respuesta);
        atras= findViewById(R.id.btAtras);
        tvPregunta = findViewById(R.id.tvPreRes);
        tvId = findViewById(R.id.tvIdR);
        tvPregunta.setText(""+ForoI.pregunta);
        tvId.setText(""+ForoI.idPregunta);
        tvRespuesta = findViewById(R.id.tvDoRespuesta);
        btRespuesta = findViewById(R.id.btResponder);
        rRecyclerView = findViewById(R.id.RcRespuestas);
        rRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rAdapter = new RespuestaAdapter(rList,rcList,nameList,R.layout.respuesta_view);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int a = rRecyclerView.getChildAdapterPosition(view);
                final String p = rList.get(a).toString();
                final String idR = ridList.get(a).toString();
                final String c= rcList.get(a).toString();
                final String idU = idUListA.get(a).toString();
                final String[] items = {"Calificar","Reportar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(Respuesta.this);
                builder.setTitle("Respuesta: \t"+rList.get(a).toString());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(Respuesta.this, ""+items[item], Toast.LENGTH_SHORT).show();
                        if (items[item].equals(items[0])) {
                            final String[] items = {"Buena","Mala"};
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Respuesta.this);

                            builder.setTitle("Calificacion");
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    Toast.makeText(Respuesta.this, ""+items[item], Toast.LENGTH_SHORT).show();

                                    if(items[item].equals(items[0])){
                                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                        final String id = mAuth.getCurrentUser().getUid();
                                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                        String idP = tvId.getText().toString();
                                        mDatabase.child("Users").child("foro"+date).child("inversion").child(""+idP).child("respuesta").child(""+idR).child("cal").child(""+id).child("c").setValue(1);                                    }
                                    if(items[item].equals(items[1])){
                                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                        final String id = mAuth.getCurrentUser().getUid();
                                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                        String idP = tvId.getText().toString();
                                        mDatabase.child("Users").child("foro"+date).child("inversion").child(""+idP).child("respuesta").child(""+idR).child("cal").child(""+id).child("c").setValue(0);
                                    }
                                }
                            });
                            builder.setCancelable(true);
                            builder.show();
                        }
                        if (items[item].equals(items[1])) {
                            Toast.makeText(Respuesta.this, "Reporte", Toast.LENGTH_SHORT).show();
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            final String id = mAuth.getCurrentUser().getUid();
                            mDatabase.child("Users").child("ID:"+idU).child("Info").child("Reportes").child("idR"+id).setValue(1);
                        }


                    }
                });
                builder.setCancelable(true);
                builder.show();

            }
        });
        btRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String respuesta= tvRespuesta.getText().toString();
                String idPregunta = tvId.getText().toString();
                if(respuesta.length()>240)
                {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Respuesta.this);
                    dialogo1.setTitle("Reduce el numero de caracteres");
                    dialogo1.setMessage("El numero de caracteres es mayor a 240, para que tu pregunta u opinion sea escuchada asegurate que sea corta y clara.");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    dialogo1.show();
                }
                else{
                    agregar(respuesta,idPregunta);
                    tvRespuesta.setText("");
                }
            }
        });

        String id= tvId.getText().toString();
        System.out.println(""+id);
        rRecyclerView.setAdapter(rAdapter);
        sCalRI.clear();
        getrespuestasfb(id);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final String d = mAuth.getCurrentUser().getUid();

        mDatabase.child("Users").child("ID:"+d).child("Info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    nombre = dataSnapshot.child("name").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getrespuestasfb(final String idP){
        final String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        sCalRI.clear();
        mDatabase.child("Users").child("foro"+date).child("inversion").child(""+idP).child("respuesta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    rList.clear();
                    listaC.clear();
                    idUListA.clear();
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String res= ds.child("respuesta").getValue().toString();
                        String cal = ds.child("calificacion").getValue().toString();
                        String nam = ds.child("usuario").getValue().toString();
                        String idu = ds.child("idU").getValue().toString();
                        String id = ds.getKey().toString();
                        rList.add(new Respuestas(res));
                        rcList.add(new Calificaciones(cal));
                        ridList.add(""+id);
                        nameList.add(new Usuario(nam));
                        idUListA.add(""+idu);
                        System.out.println(""+ridList.toString());
                    }

                   // rRecyclerView.setAdapter(rAdapter);
                    getCal();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void agregar(String Respuesta,String idP){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final String id = mAuth.getCurrentUser().getUid();
        Toast.makeText(this, "Respuesta agregada", Toast.LENGTH_SHORT).show();
        FirebaseUser user = mAuth.getCurrentUser();
        Map<String, Object> map = new HashMap<>();
        map.put("respuesta", Respuesta);
        map.put("calificacion",0.0);
        map.put("usuario",nombre);
        map.put("idU",id);
        DatabaseReference push = mDatabase.push();
        String n=push.getKey();
        mDatabase.child("Users").child("foro"+date).child("inversion").child(""+idP).child("respuesta").child(""+n).setValue(map);
        Map<String, Object> mapp = new HashMap<>();
        mapp.put("id", ""+n);
        System.out.println("ps"+n);
        mDatabase.child("Users").child("ID:"+ id).child("foromio").child("respuesta").child(""+idP).push().setValue(mapp);
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
                        sCalRI.add("Buena: "+uno+"Mala: "+cero);
                        System.out.println("Uno: "+uno+"Mal: "+cero);
                        uno = 0;
                        cero = 0;

                    }
                    else{
                        sCalRI.add("Buena: "+uno+"Mala: "+cero);
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
