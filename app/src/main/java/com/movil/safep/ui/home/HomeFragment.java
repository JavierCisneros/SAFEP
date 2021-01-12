package com.movil.safep.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.movil.safep.Principal;
import com.movil.safep.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView fecha, tvMonto,DiaT,tg,ti,tb;
    private int day, month, year,mes,dia;
    private Button btnGasto, btnRef;
    private Spinner Spinner_Categoria, Spinner_Tipo;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ListView listView;
    float total=0;
    String[] LC ;
    String [] clave;
    String cat;
    float TotalDia;
    float TI,TG,TB,TImes,TGmes,TBmes;
    private boolean bandera,bandera2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    Calendar actual = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton fab = root.findViewById(R.id.fa2);
        final TextView fecha = root.findViewById(R.id.dateC);
        //Objetos utilizados
        setDate();
        fecha.setText(day+"/"+month+"/"+year);
        final Button btnGasto = root.findViewById(R.id.add_button);
        final Button btnRef = root.findViewById(R.id.btnRefresh);
        final TextView tvMonto = root.findViewById(R.id.Monto);
        final TextView Dia = root.findViewById(R.id.TotalT);
        final TextView tg = root.findViewById(R.id.totalGastos);
        final TextView ti = root.findViewById(R.id.totalIngreso);
        final TextView tb = root.findViewById(R.id.totalBal);
        //Spinner Uno
        Spinner_Categoria = (Spinner) root.findViewById(R.id.spinner);
        final ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(getContext(), R.array.Categorias, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_Categoria.setAdapter(spinner_adapter);
        ///spinner dos
        Spinner_Tipo = (Spinner) root.findViewById(R.id.Spinner_Tipo);
        final ArrayAdapter spinner_adapterdos = ArrayAdapter.createFromResource(getContext(), R.array.Tipo, android.R.layout.simple_spinner_item);
        spinner_adapterdos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_Tipo.setAdapter(spinner_adapterdos);
        //Spinner Tres
        final ArrayAdapter spinner_adaptertres = ArrayAdapter.createFromResource(getContext(), R.array.Ingresos, android.R.layout.simple_spinner_item);
        spinner_adaptertres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Base de datos
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        //ListView
        final ArrayList<String> arrayList = new ArrayList<>();
        final ArrayList<String> clave = new ArrayList<>();
        final ArrayList<String> catcl = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter;
        listView = (ListView) root.findViewById(R.id.LvTotal);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (arrayAdapter.getItem(i).charAt(0) == '$') {
                    //Toast.makeText(getContext(), "Numero:" + arrayAdapter.getItem(i) + " Clave:" + clave.get(i), Toast.LENGTH_SHORT).show();
                    final String[] items = {"Si", "No"};
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.DatePickerDialogTheme);
                    builder.setTitle("¿Quieres eliminar esta cantidad? \t" + arrayAdapter.getItem(i));
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals(items[0])) {
                                Toast.makeText(getContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                String cat = Spinner_Tipo.getSelectedItem().toString();
                                final String id = mAuth.getCurrentUser().getUid();
                                mDatabase.child("Users").child("ID:" + id).child("" + cat).child("Year:" + year).child("Month:" + month).child("Day:" + day).child("Categoria:" + catcl.get(i)).child("" + clave.get(i)).removeValue();
                                arrayAdapter.notifyDataSetChanged();
                                TGmes=0;
                                TImes=0;
                                TBmes=0;
                                TotalDia=0;
                                TG=0;
                                TI=0;
                                TB=0;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        btnRef.performClick();
                                    }
                                },50);
                            }

                        }
                    });
                    builder.setCancelable(true);
                    builder.show();
                }

            }
        });
        final String id = mAuth.getCurrentUser().getUid();

        //BotonRefresh
        btnRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(""+day);
               cat = Spinner_Tipo.getSelectedItem().toString();
                final String id = mAuth.getCurrentUser().getUid();
                mDatabase = FirebaseDatabase.getInstance().getReference();
                int c = 0;
                Resources res = getResources();
               String w= Spinner_Tipo.getSelectedItem().toString();
                if(w.equals("Gastos")){
                     LC = res.getStringArray(R.array.Categorias);
                }
                else {
                    LC = res.getStringArray(R.array.Ingresos);
                }
                arrayAdapter.clear();
                arrayList.clear();
                clave.clear();
                catcl.clear();
                bandera=true;
                bandera2=false;
                TotalDia=0;
                arrayAdapter.notifyDataSetChanged();

                for (int j = 1; j < LC.length; j++) {
                    Query lista = mDatabase.child("Users").child("ID:" + id).child(""+cat).child("Year:" + year).child("Month:" + month).child("Day:" + day).child("Categoria:" + LC[j]);
                    final int finalJ = j;
                    final String t=LC[j];
                    lista.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            total=0;
                            bandera=true;
                            bandera2=false;
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Gastos gasto = postSnapshot.getValue(Gastos.class);
                                if(!gasto.equals("") && bandera)
                                {

                                    arrayList.add(""+t);
                                    clave.add("-");
                                    catcl.add("-");
                                    bandera2=true;

                                }
                                if (!gasto.equals("")) {
                                    String Cantidad = gasto.getCantidad();
                                    String c = postSnapshot.getKey();
                                    arrayList.add("$ " + Cantidad);
                                    clave.add(""+c);
                                    catcl.add(""+t);
                                    arrayAdapter.notifyDataSetChanged();
                                    total+=Float.parseFloat(Cantidad);
                                    TotalDia=TotalDia+Float.parseFloat(Cantidad);
                                    bandera=false;
                                }
                            }
                            System.out.println("TotalDia: "+TotalDia);
                            if(bandera2) {
                                arrayList.add("Total: $      " + total);
                                clave.add("-");
                                catcl.add("-");
                                bandera2=false;
                            }
                            if (finalJ <= LC.length ) {
                                Dia.setText("$ " + TotalDia);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            // ...
                        }

                    });


                }
                TGmes=0;
                TImes=0;
                TBmes=0;
                TotalDia=0;
                TG=0;
                TI=0;
                TB=0;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cajas();
                    }
                },50);
            }


        });



        Spinner_Tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    Spinner_Categoria.setAdapter(spinner_adapter);
                }
                else{
                    Spinner_Categoria.setAdapter(spinner_adaptertres);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Tipo=Spinner_Tipo.getSelectedItem().toString();
                if (Tipo.equals("Gastos")) {
                    Gasto();
                } else if (Tipo.equals("Ingresos")) {
                    Ingreso();

                }
                TGmes=0;
                TImes=0;
                TBmes=0;
                TotalDia=0;
                TG=0;
                TI=0;
                TB=0;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        btnRef.performClick();
                    }
                },50);
            }
        });
        TGmes=0;
        TImes=0;
        TBmes=0;
        TotalDia=0;
        TG=0;
        TI=0;
        TB=0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                btnRef.performClick();
            }
        },50);
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
                        fecha.setText("" + day + "/" + month + "/" + year);
                        fecha.setEnabled(false);
                        btnRef.performClick();
                        System.out.println("////////"+day+month+year);

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
        day = Principal.Uno;
        month = Principal.Dos;
        year = Principal.Tres;
        //Lineas para modificar el fragment con nuevos datos
        View fragment = getView();
        TextView fecha = fragment.findViewById(R.id.dateC);
        setDate();
        fecha.setText("" + day + "/" + month + "/" + year);
        fecha.setEnabled(false);
        TGmes=0;
        TImes=0;
        TBmes=0;
        TotalDia=0;
        TG=0;
        TI=0;
        TB=0;

    }
    public void setDate() {
            Calendar fecha = Calendar.getInstance();
            day = fecha.get(Calendar.DAY_OF_MONTH);
            month = fecha.get(Calendar.MONTH)+1;
            year = fecha.get(Calendar.YEAR);


    }

    public void Gasto() {
        View fragment = getView();
        final TextView tvMonto = fragment.findViewById(R.id.Monto);
        String MontoG = tvMonto.getText().toString();
        String CategoriaG = Spinner_Categoria.getSelectedItem().toString();
        if (day==0) {
            Toast.makeText(getContext(), "Ingresa la fecha", Toast.LENGTH_SHORT).show();
        } else if (MontoG.isEmpty()) {
            Toast.makeText(getContext(), "Ingresa la cantidad gastada", Toast.LENGTH_SHORT).show();

        } else if (CategoriaG.equals("Categoria")) {
            Toast.makeText(getContext(), "Ingresa una categoria", Toast.LENGTH_SHORT).show();
        } else {
            final String id = mAuth.getCurrentUser().getUid();
            Toast.makeText(getContext(), day + "/" + month + "/" + year + " $" + MontoG + " C: " + CategoriaG, Toast.LENGTH_SHORT).show();
            FirebaseUser user = mAuth.getCurrentUser();
            Map<String, Object> map = new HashMap<>();
            map.put("Cantidad", MontoG);
            mDatabase.child("Users").child("ID:" + id).child("Gastos").child("Year:" + year).child("Month:" + month).child("Day:" + day).child("Categoria:" + CategoriaG).push().setValue(map);

        }

    }

    public void Ingreso() {
        View fragment = getView();
        final TextView tvMonto = fragment.findViewById(R.id.Monto);
        String MontoG = tvMonto.getText().toString();
        String CategoriaG = Spinner_Categoria.getSelectedItem().toString();
        if (day == 0) {
            Toast.makeText(getContext(), "Ingresa la fecha", Toast.LENGTH_SHORT).show();
        } else if (MontoG.isEmpty()) {
            Toast.makeText(getContext(), "Ingresa la cantidad ganada", Toast.LENGTH_SHORT).show();

        } else if (CategoriaG.equals("Categoria")) {
            Toast.makeText(getContext(), "Ingresa una categoria", Toast.LENGTH_SHORT).show();
        } else {
            final String id = mAuth.getCurrentUser().getUid();
            Toast.makeText(getContext(), day + "/" + month + "/" + year + " $" + MontoG + " C: " + CategoriaG, Toast.LENGTH_SHORT).show();
            FirebaseUser user = mAuth.getCurrentUser();
            Map<String, Object> map = new HashMap<>();
            map.put("Cantidad", MontoG);
            mDatabase.child("Users").child("ID:" + id).child("Ingresos").child("Year:" + year).child("Month:" + month).child("Day:" + day).child("Categoria:" + CategoriaG).push().setValue(map);

        }
    }
    public void cajas() {
        View fragment = getView();
        final TextView tg = fragment.findViewById(R.id.totalGastos);
        final TextView ti = fragment.findViewById(R.id.totalIngreso);
        final TextView tb = fragment.findViewById(R.id.totalBal);
        final String id = mAuth.getCurrentUser().getUid();
        Resources res = getResources();
        for (int d = 0; d <= 1; d++) {
            String tipo= Spinner_Tipo.getItemAtPosition(d).toString();
            if(tipo.equals("Gastos")){
                LC = res.getStringArray(R.array.Categorias);
            }
            else {
                LC = res.getStringArray(R.array.Ingresos);
            }
        for (int diames = 1; diames <= 31; diames++){
                for (int j = 1; j < LC.length; j++) {

                    Query lista = mDatabase.child("Users").child("ID:" + id).child("" + tipo).child("Year:" + year).child("Month:" + month).child("Day:" + diames).child("Categoria:" + LC[j]);
                    final int finalJ = j;
                    final int finalDiames = diames;
                    final String finalTipo = tipo;
                    final int finalD = d;
                    lista.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            total = 0;
                            TotalDia=0;
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                System.out.println(""+finalTipo+" "+finalDiames+" "+LC.length);
                                Gastos gasto = postSnapshot.getValue(Gastos.class);
                                if (!gasto.equals("")) {
                                    String Cantidad = gasto.getCantidad();
                                    total += Float.parseFloat(Cantidad);
                                    TotalDia = TotalDia + Float.parseFloat(Cantidad);
                                    System.out.println("$"+total);
                                }
                                if (finalTipo.equals("Gastos") ) {
                                    TG = TotalDia;
                                    //tg.setText("$" + TG);
                                    TGmes=TotalDia+TGmes;
                                    TotalDia=0;
                                } else if (!finalTipo.equals("Gastos") ) {
                                    TI = TotalDia;
                                    //ti.setText("$" + TI);
                                    TImes=TotalDia+TImes;
                                    TotalDia=0;
                                    TB = TI - TG;
                                    //tb.setText("$" + TB);
                                    TBmes = TB+TBmes;
                                }
                             }

                            if(finalDiames ==31 && finalD ==1){
                                TBmes=TImes-TGmes;
                                tg.setText("$" + TGmes);
                                ti.setText("$" + TImes);
                                tb.setText("$" + TBmes);
                                TotalDia=0;
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            // ...
                        }

                    });

                }
            }
    }
    }
}