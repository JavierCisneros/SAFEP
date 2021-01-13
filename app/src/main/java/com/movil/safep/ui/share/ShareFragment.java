package com.movil.safep.ui.share;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.movil.safep.Foro.ForoA;
import com.movil.safep.Foro.ForoI;
import com.movil.safep.Foro.Mispreguntas;
import com.movil.safep.MisAI;
import com.movil.safep.R;


public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;
    private ImageView foroIn,foroAh,Mis;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        foroIn= root.findViewById(R.id.btnEspacioInversion);
        foroAh=root.findViewById(R.id.btnEspacioAhorro);
        Mis = root.findViewById(R.id.btnMis);
        foroIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent a = new Intent(getActivity(), ForoI.class);
                startActivity(a);
            }
        });
        foroAh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent  b = new Intent(getActivity(), ForoA.class);
                startActivity(b);
            }
        });
        Mis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent a = new Intent(getActivity(), MisAI.class);
                startActivity(a);
            }
        });
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext(),R.style.DatePickerDialogTheme);
        dialogo1.setTitle("Uso del foro");
        dialogo1.setMessage("Haz uso adecuado del foro evita el uso de palabras altisonantes y respeta a los demas usuarios, gracias por ayudar a la comunidad de la aplicacion");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
        return root;
    }
}