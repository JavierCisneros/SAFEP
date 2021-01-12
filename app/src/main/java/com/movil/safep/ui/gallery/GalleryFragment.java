package com.movil.safep.ui.gallery;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.PieChart;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.movil.safep.R;
import com.movil.safep.ui.home.Gastos;

import java.util.ArrayList;
import java.util.Calendar;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    public static int dayG, monthG, yearG;
    PieChart pieChart;
    ArrayList<PieEntry> Entries;
    ArrayList<String> PieEntryLabels;
    ArrayList<Float> valores=new ArrayList<>();
    ArrayList<Float> valores2=new ArrayList<>();
    ArrayList<String> labels=new ArrayList<>();
    ArrayList<String> labels2=new ArrayList<>();
    PieDataSet pieDataSet;
    PieData pieData;
    private int day, month, year,mes,dia;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    float total=0;
    int i = 1;
    private Spinner Spinner_Tipo;
    String[] LC ;
    String[] LC2 ;
    Button act;
    @Override
    public void onStop(){
        super.onStop();
        try {
            finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    Calendar actual = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();
    public View onCreateView(@NonNull LayoutInflater inflater,
       ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        FloatingActionButton fab = root.findViewById(R.id.fa3);

        ///spinner dos
        Spinner_Tipo = (Spinner) root.findViewById(R.id.spinner_Gra);
        final ArrayAdapter spinner_adapterdos = ArrayAdapter.createFromResource(getContext(), R.array.Tipo, android.R.layout.simple_spinner_item);
        spinner_adapterdos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_Tipo.setAdapter(spinner_adapterdos);
        ///////////
        setDate();
        mAuth = FirebaseAuth.getInstance();
        Resources res = getResources();
        final String id = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

            Entries = new ArrayList<>();
        PieEntryLabels = new ArrayList<String>();
           final Button act = root.findViewById(R.id.refrescar);
           ///////////////////BOTON
        //////////////////
       act.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               pieChart.invalidate();
               String cat = Spinner_Tipo.getSelectedItem().toString();
               Toast.makeText(getContext(), ""+cat, Toast.LENGTH_SHORT).show();
               Entries.clear();
               if(cat.equals("Gastos")) {
                   if(valores.size()==0){
                       Toast.makeText(getContext(), "No hay gastos registrados este mes", Toast.LENGTH_SHORT).show();
                   }
                   for (int i = 0; i < valores.size(); i++) {
                       Entries.add(new PieEntry(0 + valores.get(i), labels.get(i)));
                   }
               }
               else
               {
                   if(valores2.size()==0){
                       Toast.makeText(getContext(), "No hay ingresos registrados este mes", Toast.LENGTH_SHORT).show();
                   }
                   for (int i = 0; i < valores2.size(); i++) {
                       Entries.add(new PieEntry(0 + valores2.get(i), labels2.get(i)));
                                         }
               }
               int color= R.color.cuatro;
               int color2= R.color.uno;
               int color3= R.color.dos;
               int color4= R.color.tres;
               int color5= R.color.cinco;

               pieDataSet = new PieDataSet(Entries,"Categorias" );
               pieDataSet.setColors(new int[] {color,color2,color3,color5,color4},getContext());
               pieData = new PieData(pieDataSet);
               pieChart.setData(pieData);
               pieChart.animateY(2000);

           }
       });
       final TextView fecha = root.findViewById(R.id.dateG);

        pieChart = (PieChart) root.findViewById(R.id.piechart);

        /////fab
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
                        dayG=i2;
                        monthG=i1+1;
                        yearG=i;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(i,i1,i2);
                        System.out.println("////////"+day+month+year);
                        fecha.setText("" + dayG + "/" + monthG + "/" + yearG);
                        Entries.clear();
                        pieDataSet = new PieDataSet(Entries,"Categorias" );
                        descarga();

                    }

                },year,mes,dia);
                datePickerDialog.show();
            }
        });
         return root;
    }

    public void setDate() {
            Calendar fecha = Calendar.getInstance();
            dayG = fecha.get(Calendar.DAY_OF_MONTH);
            monthG = fecha.get(Calendar.MONTH)+1;
            yearG = fecha.get(Calendar.YEAR);


    }

    @Override
    public void onResume() {
        super.onResume();
        View fragment = getView();
        TextView fecha = fragment.findViewById(R.id.dateG);
        setDate();
        fecha.setText("" + dayG + "/" + monthG + "/" + yearG);
        fecha.setEnabled(false);
        setDate();
        descarga();


    }
    public void descarga(){
        valores.clear();
        valores2.clear();
        labels.clear();
        labels2.clear();
        pieChart.invalidate();
        Entries = new ArrayList<>();
        //Lineas para modificar el fragment con nuevos datos
        Resources res = getResources();
        final String id = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        LC = res.getStringArray(R.array.Categorias);
        for (int diames = 1; diames <= 31; diames++) {
            for (int j = 1; j < res.getStringArray(R.array.Categorias).length; j++) {
                Query lista = mDatabase.child("Users").child("ID:" + id).child("Gastos").child("Year:" + yearG).child("Month:" + monthG).child("Day:" + diames).child("Categoria:" + LC[j]);
                final int finalJ = j;
                lista.addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        total = 0;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Gastos gasto = postSnapshot.getValue(Gastos.class);
                            if (!gasto.equals("")) {
                                String Cantidad = gasto.getCantidad();
                                total += Float.parseFloat(Cantidad);
                            }
                        }
                        if (total != 0) {
                            System.out.println("" + total);
                            valores.add(total);
                            labels.add(LC[finalJ]);
                            i++;
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }
        LC2 = res.getStringArray(R.array.Ingresos);
        for (int diames = 1; diames <= 31; diames++) {
            for (int j = 1; j < res.getStringArray(R.array.Ingresos).length; j++) {
                Query lista2 = mDatabase.child("Users").child("ID:" + id).child("Ingresos").child("Year:" + yearG).child("Month:" + monthG).child("Day:" + diames).child("Categoria:" + LC2[j]);
                final int finalJ = j;
                lista2.addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        total = 0;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Gastos gasto = postSnapshot.getValue(Gastos.class);
                            if (!gasto.equals("")) {
                                String Cantidad = gasto.getCantidad();
                                total += Float.parseFloat(Cantidad);
                            }
                        }
                        if (total != 0) {
                            System.out.println("ingreso" + total);
                            valores2.add(total);
                            labels2.add(LC2[finalJ]);
                            i++;
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }
    }

}
