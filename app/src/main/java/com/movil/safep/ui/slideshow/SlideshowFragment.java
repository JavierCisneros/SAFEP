package com.movil.safep.ui.slideshow;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movil.safep.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private int day, month, year,fin,mes,dia;
    private TextView fecha;
    private TextView cantidad,resultado;
    private Button generar,add;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    float total;
    int dias;
    String ult;
    String t,c;
    Calendar actual = Calendar.getInstance();
    String fechaFija;
    Calendar calendar = Calendar.getInstance();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        FloatingActionButton fab = root.findViewById(R.id.fa4);

        setDate();
        generar = root.findViewById(R.id.Generar);
        cantidad=root.findViewById(R.id.Cantidad);
        resultado=root.findViewById(R.id.resultado);
        fecha = root.findViewById(R.id.dateAhorro);
        add=root.findViewById(R.id.addF);
        mAuth = FirebaseAuth.getInstance();
        final String id=mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child("ID:"+id).child("Info").child("Plan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String total = dataSnapshot.child("cantidad").getValue().toString();
                    String dias = dataSnapshot.child("dias").getValue().toString();
                    String ult = dataSnapshot.child("fecha").getValue().toString();
                    String tot = dataSnapshot.child("total").getValue().toString();
                    Date hoy = null;
                    Date menos = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        hoy = sdf.parse(fechaFija);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        menos = sdf.parse(ult);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int resta = (int) ((menos.getTime()-hoy.getTime())/86400000);
                    String quedan;
                    if(resta>=0){
                        quedan="Dias restantes: "+resta;
                    }
                    else {
                        quedan = "Este plan ha caducado";
                    }
                    System.out.println("Resta"+resta);
                    resultado.setText("Plan guardado"+"\n\n"+"Total: $"+tot+"\n\n"+"Ahorro por dia: " + "$" + total + "\n\n" +"Durante: "+dias+" dias"+"\n\n"+ "Hasta la fecha: " + ult+"\n\n"+ quedan);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Inicio = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
                dias = fin-Inicio;
                c =cantidad.getText().toString();
                ult = fecha.getText().toString();
                if(c.isEmpty()){
                    Toast.makeText(getContext(), "Introduce una cantidad valida", Toast.LENGTH_SHORT).show();
                }
                else if(dias==0||dias<0){
                    Toast.makeText(getContext(), "Inserte una fecha valida", Toast.LENGTH_SHORT).show();
                }
                else if( Float.parseFloat(c)==0f){
                    Toast.makeText(getContext(), "Introduce una cantidad valida", Toast.LENGTH_SHORT).show();
                }
                else {
                    float Ahorro = Float.parseFloat(c);
                   total = Ahorro / dias;
                   t = String.format("%.2f",total);
                   resultado.setText("Plan creado"+"\n\n"+"Total: $"+c+"\n\n"+"Ahorro por dia: " + "$" + t + "\n\n" +"Durante: "+dias+" dias"+"\n\n"+ "Hasta la fecha: " + ult);

                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                if (total == 0f) {
                    Toast.makeText(getContext(), "Para agregar un nuevo plan, primero crea uno", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext(),R.style.DatePickerDialogTheme);
                    dialogo1.setTitle("Importante");
                    dialogo1.setMessage("Al agregar un nuevo plan eliminara el existente");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            Toast.makeText(getContext(), "Aceptar", Toast.LENGTH_SHORT).show();
                            agregar(c, dias, ult);
                        }
                    });
                    dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            Toast.makeText(getContext(), "Cancelar", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialogo1.show();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year=actual.get(Calendar.YEAR);
                mes=actual.get(Calendar.MONTH);
                dia=actual.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(Calendar.DAY_OF_MONTH,i2);
                        calendar.set(Calendar.MONTH,i1);
                        calendar.set(Calendar.YEAR,i);
                        day=i2;
                        month=i1+1;
                        year=i;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(i,i1,i2);
                        System.out.println("////////"+day+month+year);
                        fin = calendar.get(Calendar.DAY_OF_YEAR);
                        fecha.setText("" + day + "/" + month + "/" + year);
                        fecha.setEnabled(false);
                    }

                },year,mes,dia);
                datePickerDialog.show();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH)+1;
        year = calendar.get(Calendar.YEAR);
        //Lineas para modificar el fragment con nuevos datos
        View fragment = getView();
        fecha = fragment.findViewById(R.id.dateAhorro);
        setDate();
        fecha.setText("" + day + "/" + month + "/" + year);
        fechaFija = fecha.getText().toString();
        fecha.setEnabled(false);
    }
    public void agregar(String total,int dias,String ult){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> map = new HashMap<>();
        map.put("cantidad", t);
        map.put("dias", dias );
        map.put("fecha", ult);
        map.put("total",total);
        final String id=mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child("ID:"+id).child("Info").child("Plan").setValue(map);
        Toast.makeText(getContext(),"Se ha agregado el nuevo plan de ahorro",Toast.LENGTH_LONG).show();

    }

    public void setDate() {
            Calendar fecha = Calendar.getInstance();
            day = fecha.get(Calendar.DAY_OF_MONTH);
            month = fecha.get(Calendar.MONTH)+1;
            year = fecha.get(Calendar.YEAR);
            fin=fecha.get(Calendar.DAY_OF_YEAR);



    }
}

