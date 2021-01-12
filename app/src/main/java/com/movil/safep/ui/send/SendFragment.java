package com.movil.safep.ui.send;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.movil.safep.Educacion.Microgastos;
import com.movil.safep.R;

import java.util.ArrayList;

public class SendFragment extends Fragment {
    private TableRow rowM,rowI,rowA,rowF,rowS;
    private SendViewModel sendViewModel;
    private TextView tvF,tvM,tvA,tvI,tvS;
    public static ArrayList<String> idList = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
        ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
        ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        rowM = root.findViewById(R.id.rowM);
        rowA=root.findViewById(R.id.rowA);
        rowI=root.findViewById(R.id.rowI);
        rowF=root.findViewById(R.id.rowF);
        rowS=root.findViewById(R.id.rowS);
        tvF=root.findViewById(R.id.tvF);
        tvA=root.findViewById(R.id.tvA);
        tvM=root.findViewById(R.id.tvM);
        tvI=root.findViewById(R.id.tvI);
        tvS=root.findViewById(R.id.tvS);

        rowM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                micro();
            }
        });
        rowI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inversion();
            }
        });
        rowA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             ahorro();
            }
        });
        rowF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personales();
            }
        });
        rowS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sanas();
            }
        });
        tvF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              personales();
            }
        });
        tvM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              micro();
            }
        });
        tvA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              ahorro();
            }
        });
        tvI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inversion();
            }
        });
        tvS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           sanas();
            }
        });


        return root;
    }
    private void micro(){
        final Intent a = new Intent(getContext(), Microgastos.class);
        final String[] items = {"¿Qué son los microgastos?","¿Por qué son importantes?","Ayuda"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Microgastos");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getContext(), ""+items[item], Toast.LENGTH_SHORT).show();
                if(items[item].equals(items[0])){
                    a.putExtra("Tipo",1);
                    startActivity(a);
                }
                if (items[item].equals(items[1])) {
                    a.putExtra("Tipo",2);
                    startActivity(a);
                }
                if (items[item].equals(items[2])) {
                    a.putExtra("Tipo",3);
                    startActivity(a);
                }
            }
        });
        builder.setCancelable(true);
        builder.show();
    }
    private void personales(){
        final Intent a = new Intent(getContext(), Microgastos.class);
        final String[] items = {"¿Finanzas personales?","¿Por qué son importantes?","Ayuda"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Finanzas personales");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getContext(), ""+items[item], Toast.LENGTH_SHORT).show();
                if(items[item].equals(items[0])){
                    a.putExtra("Tipo",4);
                    startActivity(a);
                }
                if (items[item].equals(items[1])) {
                    a.putExtra("Tipo",5);
                    startActivity(a);
                }
                if (items[item].equals(items[2])) {
                    a.putExtra("Tipo",6);
                    startActivity(a);
                }
            }
        });
        builder.setCancelable(true);
        builder.show();
    }
    private void inversion(){
        final Intent a = new Intent(getContext(), Microgastos.class);
        final String[] items = {"¿Qué es una inversión?","¿Por qué son importantes?","Ayuda"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Inversión");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getContext(), ""+items[item], Toast.LENGTH_SHORT).show();
                if(items[item].equals(items[0])){
                    a.putExtra("Tipo",7);
                    startActivity(a);
                }
                if (items[item].equals(items[1])) {
                    a.putExtra("Tipo",8);
                    startActivity(a);
                }
                if (items[item].equals(items[2])) {
                    a.putExtra("Tipo",9);
                    startActivity(a);
                }
            }
        });
        builder.setCancelable(true);
        builder.show();
    }
    private void sanas(){

        final Intent a = new Intent(getContext(), Microgastos.class);
        final String[] items = {"¿Finanzas sanas?","¿Por qué son importantes?","Ayuda"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Finanzas sanas");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getContext(), ""+items[item], Toast.LENGTH_SHORT).show();
                if(items[item].equals(items[0])){
                    a.putExtra("Tipo",10);
                    startActivity(a);
                }
                if (items[item].equals(items[1])) {
                    a.putExtra("Tipo",11);
                    startActivity(a);
                }
                if (items[item].equals(items[2])) {
                    a.putExtra("Tipo",12);
                    startActivity(a);
                }
            }
        });
        builder.setCancelable(true);
        builder.show();
    }
    private void ahorro(){
        final Intent a = new Intent(getContext(), Microgastos.class);
        final String[] items = {"¿Qué son los ahorros?","¿Porque son importantes?","Ayuda"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Ahorro");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getContext(), ""+items[item], Toast.LENGTH_SHORT).show();
                if(items[item].equals(items[0])){
                    a.putExtra("Tipo",13);
                    startActivity(a);
                }
                if (items[item].equals(items[1])) {
                    a.putExtra("Tipo",14);
                    startActivity(a);
                }
                if (items[item].equals(items[2])) {
                    a.putExtra("Tipo",15);
                    startActivity(a);
                }
            }
        });
        builder.setCancelable(true);
        builder.show();
    }

}