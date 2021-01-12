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

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ForoA extends AppCompatActivity {
    private ImageView atrasA;
    private TextView tvPreA;
    private TextView Pregunta,calificacion;
    private Button btPreA,rBtA;
    private PreguntaAdapterA pAdapterA;
    private RecyclerView pRecyclerViewA;
    private ArrayList<Preguntas> pListA = new ArrayList<>();
    private ArrayList<Calificaciones> cListA = new ArrayList<>();
    public static ArrayList<String> idListA = new ArrayList<>();
    public static ArrayList<String> idUListA = new ArrayList<>();
    public static ArrayList<String> sCalA = new ArrayList<>();
    private ArrayList<Usuario> nameListA = new ArrayList<>();
    public static String idPreguntaA,preguntaA,calA;
    private String nombre;
    int uno=0;
    int cero=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foro);
        atrasA = findViewById(R.id.btAtrasA);
        tvPreA=findViewById(R.id.tvDoPreguntaA);
        btPreA=findViewById(R.id.btDoPreguntaA);
        pRecyclerViewA=findViewById(R.id.rcPreguntasA);
        pRecyclerViewA.setLayoutManager(new LinearLayoutManager(this));
        pAdapterA = new PreguntaAdapterA(pListA,cListA,nameListA,R.layout.pregunta_view);
        pRecyclerViewA.setAdapter(pAdapterA);

        pAdapterA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int a = pRecyclerViewA.getChildAdapterPosition(view);
                final String p = pListA.get(a).toString();
                final String id = idListA.get(a).toString();
                final String idU = idUListA.get(a).toString();
                final String c= cListA.get(a).toString();
                final String[] items = {"Mostrar respuestas","Calificar","Reportar"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(ForoA.this);

                builder.setTitle("Tema: \t"+pListA.get(a).toString());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(ForoA.this, ""+items[item], Toast.LENGTH_SHORT).show();
                        idPreguntaA = id;
                        preguntaA = p;
                        calA = c;
                        if(items[item].equals(items[0])){
                            final Intent a = new Intent(ForoA.this, RespuestaA.class);
                            startActivity(a);

                        }
                        if (items[item].equals(items[1])) {
                            final String[] items = {"Buena","Mala"};
                            final AlertDialog.Builder builder = new AlertDialog.Builder(ForoA.this);

                            builder.setTitle("Calificacion");
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    Toast.makeText(ForoA.this, ""+items[item], Toast.LENGTH_SHORT).show();

                                    if(items[item].equals(items[0])){
                                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                        final String id = mAuth.getCurrentUser().getUid();
                                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                        mDatabase.child("Users").child("foro"+date).child("ahorro").child(""+idPreguntaA).child("pregunta").child("cal").child(""+id).child("c").setValue(1);
                                    }
                                    if(items[item].equals(items[1])){
                                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                        final String id = mAuth.getCurrentUser().getUid();
                                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                        mDatabase.child("Users").child("foro"+date).child("ahorro").child(""+idPreguntaA).child("pregunta").child("cal").child(""+id).child("c").setValue(0);

                                    }

                                }
                            });
                            builder.setCancelable(true);
                            builder.show();
                        }
                        if (items[item].equals(items[2])) {
                            Toast.makeText(ForoA.this, "Reporte", Toast.LENGTH_SHORT).show();
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            final String id = mAuth.getCurrentUser().getUid();
                            mDatabase.child("Users").child("ID:"+idU).child("Info").child("Reportes").child("idR"+id).setValue(1);
                            System.out.println("Uusario:::"+idU);
                        }
                    }
                });
                builder.setCancelable(true);
                builder.show();

            }
        });
        atrasA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btPreA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pregunta= tvPreA.getText().toString();
                if(pregunta.length()>240)
                {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(ForoA.this);
                    dialogo1.setTitle("Reduce el numero de caracteres");
                    dialogo1.setMessage("El numero de caracteres es mayor a 240, para que tu pregunta u opinion sea escuchada asegurate que sea corta y clara.");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    dialogo1.show();
                }

                else if (pregunta.length()==0){
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(ForoA.this);
                    dialogo1.setTitle("Tu pregunta no puede estar vacia");
                    dialogo1.setMessage("");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    dialogo1.show();
                }
                else if (pregunta.matches(".*[!&%*/°|¬´´~{}].*")){
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(ForoA.this);
                    dialogo1.setTitle("Evita el uso de caracteres especiales como: !&%*/°|¬´´~{}");
                    dialogo1.setMessage("");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    dialogo1.show();
                }

               else {
                    agregar(pregunta);
                    tvPreA.setText("");
                }

            }
        });
        sCalA.clear();
        getPreguntasfb();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final String id = mAuth.getCurrentUser().getUid();

        mDatabase.child("Users").child("ID:"+id).child("Info").addValueEventListener(new ValueEventListener() {
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
    private void getPreguntasfb(){
         final String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
         DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
         sCalA.clear();
        mDatabase.child("Users").child("foro"+date).child("ahorro").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    pListA.clear();
                    nameListA.clear();
                    idListA.clear();
                    idUListA.clear();
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String pre= ds.child("pregunta").child("pregunta").getValue().toString();
                        String cal = ds.child("pregunta").child("calificacion").getValue().toString();
                        String nam = ds.child("pregunta").child("usuario").getValue().toString();
                        String idu = ds.child("pregunta").child("idU").getValue().toString();
                        String id = ds.getKey().toString();
                        pListA.add(new Preguntas(pre));
                        cListA.add(new Calificaciones(cal));
                        nameListA.add(new Usuario(nam));
                        idListA.add(""+id);
                        idUListA.add(""+idu);
                        System.out.println(""+id);

                    }

                    //pRecyclerViewA.setAdapter(pAdapterA);
                    getCal();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void agregar(String Pregunta){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final String id = mAuth.getCurrentUser().getUid();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Toast.makeText(this, "Pregunta agregada", Toast.LENGTH_SHORT).show();
        FirebaseUser user = mAuth.getCurrentUser();
        Map<String, Object> map = new HashMap<>();
        map.put("pregunta", Pregunta);
        map.put("calificacion",0.0);
        map.put("usuario",nombre);
        map.put("idU",id);
        DatabaseReference push = mDatabase.push();
        String n=push.getKey();
        mDatabase.child("Users").child("foro"+date).child("ahorro").child(""+n).child("pregunta").setValue(map);
        Map<String, Object> mapp = new HashMap<>();
        mapp.put("id", ""+n);
        System.out.println("ps"+n);
        mDatabase.child("Users").child("ID:"+ id).child("foromio").child("preguntas").push().setValue(mapp);
    }
    public void getCal() {
        uno=0;
        cero=0;
        for (int i = 0; i < idListA.size(); i++){
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference();
        mDatabase1.child("Users").child("foro" + date).child("ahorro").child("" + idListA.get(i)).child("pregunta").child("cal").addValueEventListener(new ValueEventListener() {
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
                    sCalA.add(" Buena: "+uno+"Mala: "+cero);
                    uno = 0;
                    cero = 0;
                    pRecyclerViewA.setAdapter(pAdapterA);
                }
                else{
                    sCalA.add(" Buena: "+uno+"Mala: "+cero);
                    uno=0;
                    cero=0;
                    pRecyclerViewA.setAdapter(pAdapterA);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    }
}
