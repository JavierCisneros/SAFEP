package com.movil.safep.ui.send;

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

import com.movil.safep.Ahorro;
import com.movil.safep.FinanzasPersonales;
import com.movil.safep.Inversion;
import com.movil.safep.Microgastos;
import com.movil.safep.R;
import com.movil.safep.Sanas;

import java.util.ArrayList;

public class SendFragment extends Fragment {
    private TableRow rowM,rowI,rowA,rowF,rowS,rowC;
    private SendViewModel sendViewModel;
    private TextView tvF,tvM,tvA,tvI,tvS,tvC;
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
        rowC=root.findViewById(R.id.rowC);
        tvF=root.findViewById(R.id.tvF);
        tvA=root.findViewById(R.id.tvA);
        tvM=root.findViewById(R.id.tvM);
        tvI=root.findViewById(R.id.tvI);
        tvS=root.findViewById(R.id.tvS);
        tvC=root.findViewById(R.id.tvC);

        rowM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getContext(), Microgastos.class);
                startActivity(a);
            }
        });
        rowI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getContext(), Inversion.class);
                startActivity(a);
            }
        });
        rowA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getContext(), Ahorro.class);
                startActivity(a);
            }
        });
        rowF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getContext(), FinanzasPersonales.class);
                startActivity(a);
            }
        });
        rowS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getContext(), Sanas.class);
                startActivity(a);
            }
        });
        tvF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getContext(), FinanzasPersonales.class);
                startActivity(a);
            }
        });
        tvM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getContext(), Microgastos.class);
                startActivity(a);
            }
        });
        tvA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getContext(), Ahorro.class);
                startActivity(a);
            }
        });
        tvI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getContext(), Inversion.class);
                startActivity(a);
            }
        });
        tvS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getContext(), Sanas.class);
                startActivity(a);
            }
        });


        return root;
    }

}