package com.movil.safep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.WorkManager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Notificaciones extends AppCompatActivity {
    private ImageView atras;
    private Spinner Spinner_Categoria;
    Calendar actual = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();
    private int minuto, hora, dia, mes, year;
    private TextView tvFecha,tvhora;
    private Button btAP,btEP;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    String fecha;
    private ListView listView;
    private ArrayList<String> idListA = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
        atras = findViewById(R.id.btAtrasN);
        tvFecha=findViewById(R.id.dateN);
        tvhora=findViewById(R.id.horaN);
        btAP=findViewById(R.id.btAgregarP);
        FloatingActionButton fab = findViewById(R.id.fa5);
        FloatingActionButton fab2 = findViewById(R.id.fa6);
        Spinner_Categoria = (Spinner) findViewById(R.id.SpinnerN);
        final ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(Notificaciones.this, R.array.tiponotificacion, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_Categoria.setAdapter(spinner_adapter);

        //
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        listView = (ListView) findViewById(R.id.LvNot);
        final ArrayList<String> arrayList = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<String>(Notificaciones.this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year=actual.get(Calendar.YEAR);
                mes=actual.get(Calendar.MONTH);
                dia=actual.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Notificaciones.this, R.style.DatePickerDialogTheme,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        calendar.set(Calendar.DAY_OF_MONTH,d);
                        calendar.set(Calendar.MONTH,m);
                        calendar.set(Calendar.YEAR,y);

                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        fecha = format.format(calendar.getTime());
                        tvFecha.setText(""+fecha);

                    }
                },year,mes,dia);
                datePickerDialog.show();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hora = calendar.get(Calendar.HOUR_OF_DAY);
                minuto=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(Notificaciones.this, R.style.DatePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        calendar.set(Calendar.HOUR_OF_DAY,h);
                        calendar.set(Calendar.MINUTE,m);
                        System.out.println(""+h);
                        tvhora.setText(String.format("%02d:%02d",h,m));
                    }
                },hora,minuto,true);
                timePickerDialog.show();
            }
        });
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference push = mDatabase.push();
                String tag=push.getKey();
                Long alerttime = calendar.getTimeInMillis() - System.currentTimeMillis();
                String fh=String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
                String fm=String.valueOf(calendar.get(Calendar.MINUTE));
                System.out.println("HORA"+fh+"Minuto"+fm);
                String titulo=Spinner_Categoria.getSelectedItem().toString();
                Data data ;
                final String id = mAuth.getCurrentUser().getUid();
                if(titulo.equals(Spinner_Categoria.getItemAtPosition(0).toString())){
                    Toast.makeText(Notificaciones.this, "Selecciona un tipo de notificación", Toast.LENGTH_SHORT).show();
                }
                else if(titulo.equals(Spinner_Categoria.getItemAtPosition(1).toString()))
                {
                    data = guardar(""+titulo, "Balance del mes: ", tag);
                    WorkManagerNoti.guardanot(alerttime,data,tag);
                    Map<String, Object> map = new HashMap<>();
                    map.put("tipo",titulo);
                    map.put("hora",fh+":"+fm);
                    map.put("fecha",fecha);
                    mDatabase.child("Users").child("ID:" + id).child("Info").child("noti").child(""+tag).setValue(map);
                    Toast.makeText(Notificaciones.this, "Notificación Añadidad", Toast.LENGTH_SHORT).show();
                }
                else if (titulo.equals(Spinner_Categoria.getItemAtPosition(2).toString()))
                {
                    data = guardar(""+titulo, "Recuerda visitar los sitios de \ninversion y revisar sus tasas de interes", tag);
                    WorkManagerNoti.guardanot(alerttime,data,tag);
                    Map<String, Object> map = new HashMap<>();
                    map.put("tipo",titulo);
                    map.put("hora",fh+":"+fm);
                    map.put("fecha",fecha);
                    mDatabase.child("Users").child("ID:" + id).child("Info").child("noti").child(""+tag).setValue(map);
                    Toast.makeText(Notificaciones.this, "Notificación Añadidad", Toast.LENGTH_SHORT).show();
                }
                else if (titulo.equals(Spinner_Categoria.getItemAtPosition(3).toString()))
                {
                    data = guardar(""+titulo, "Recuerda cumplir con tu ahorro", tag);
                    WorkManagerNoti.guardanot(alerttime,data,tag);
                    Map<String, Object> map = new HashMap<>();
                    map.put("tipo",titulo);
                    map.put("hora",fh+":"+fm);
                    map.put("fecha",fecha);
                    mDatabase.child("Users").child("ID:" + id).child("Info").child("noti").child(""+tag).setValue(map);
                    Toast.makeText(Notificaciones.this, "Notificación Añadidad", Toast.LENGTH_SHORT).show();
                }
                else if (titulo.equals(Spinner_Categoria.getItemAtPosition(4).toString()))
                {
                    data = guardar(""+titulo, "Registra tus gastos del dia", tag);
                    WorkManagerNoti.guardanot(alerttime,data,tag);
                    Map<String, Object> map = new HashMap<>();
                    map.put("tipo",titulo);
                    map.put("hora",fh+":"+fm);
                    map.put("fecha",fecha);
                    mDatabase.child("Users").child("ID:" + id).child("Info").child("noti").child(""+tag).setValue(map);
                    Toast.makeText(Notificaciones.this, "Notificación Añadidad", Toast.LENGTH_SHORT).show();
                }



            }
        });

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        fecha = format.format(calendar.getTime());
        tvFecha.setText(""+fecha);

            final String id = mAuth.getCurrentUser().getUid();
            mDatabase.child("Users").child("ID:" + id).child("Info").child("noti").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        idListA.clear();
                        arrayAdapter.clear();
                        arrayList.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            String tipo= ds.child("tipo").getValue().toString();
                            String hora = ds.child("hora").getValue().toString();
                            String fecha = ds.child("fecha").getValue().toString();
                            String id = ds.getKey().toString();
                            idListA.add(""+id);
                            arrayList.add(tipo+"\nFecha: "+fecha+"\nHora: "+hora );
                            arrayAdapter.notifyDataSetChanged();
                        }

                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {


                    final String[] items = {"Si", "No"};
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Notificaciones.this,R.style.DatePickerDialogTheme);
                    builder.setTitle("Eliminar " + arrayAdapter.getItem(i));
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals(items[0])) {
                                WorkManager.getInstance(Notificaciones.this).cancelAllWorkByTag(idListA.get(i));
                                Toast.makeText(Notificaciones.this, "Eliminado", Toast.LENGTH_SHORT).show();
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                final String id = mAuth.getCurrentUser().getUid();
                                mDatabase.child("Users").child("ID:" + id).child("Info").child("noti").child("" + idListA.get(i)).removeValue();
                                arrayAdapter.notifyDataSetChanged();
                                arrayList.clear();
                            }

                        }
                    });
                    builder.setCancelable(true);
                    builder.show();


            }
        });


    }/////FinOnCreate


    private Data guardar(String titulo, String detalle,String id_noti){
        return new Data.Builder()
                .putString("titulo",titulo)
                .putString("detalle",detalle)
                .putString("id_noti",id_noti)
                .build();


    }
}
