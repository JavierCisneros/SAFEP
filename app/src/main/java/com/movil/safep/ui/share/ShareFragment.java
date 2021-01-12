package com.movil.safep.ui.share;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movil.safep.Foro.ForoA;
import com.movil.safep.Foro.ForoI;
import com.movil.safep.Foro.MisAI;
import com.movil.safep.R;


public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;
    private ImageView foroIn,foroAh,Mis;
    int suma;
    Boolean foro=false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        foroIn= root.findViewById(R.id.btnEspacioInversion);
        foroAh=root.findViewById(R.id.btnEspacioAhorro);
        Mis = root.findViewById(R.id.btnMis);
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child("ID:"+ id).child("Info").child("Reportes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                         suma=suma+1;
                        System.out.println("Suma: "+suma);

                    }

                }
                if(suma>2){
                    System.out.println("Denegar acceso");
                    foroIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getContext(), "Acceso denegado", Toast.LENGTH_SHORT).show();
                        }
                    });
                    foroAh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getContext(), "Acceso denegado", Toast.LENGTH_SHORT).show();
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
                    dialogo1.setTitle("Acceso Denegado");
                    dialogo1.setMessage("Tu cuenta ha sido reportado por uso inadecuado, deberas esperar 10 dias para poder usar el foro");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                        }
                    });
                    dialogo1.show();
                }
                else{

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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        return root;
    }
}