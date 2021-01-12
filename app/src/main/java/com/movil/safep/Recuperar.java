package com.movil.safep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Recuperar extends AppCompatActivity {
    private TextView tvEmail;
    private Button btRestablecer;
    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);
        tvEmail=findViewById(R.id.tvEmailR);
        btRestablecer=findViewById(R.id.btRestablecer);
        mAuth= FirebaseAuth.getInstance();
        mDialog=new ProgressDialog(Recuperar.this);
        btRestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = tvEmail.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(Recuperar.this, "Ingresa un correo", Toast.LENGTH_SHORT).show();
                }
                else{
                    mDialog.setMessage("Enviando....");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    mAuth.setLanguageCode("es");
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Recuperar.this, "El correo de restablecimiento ha sido enviado", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(Recuperar.this, "No ha podido enviarse el correo de restablecimieto", Toast.LENGTH_SHORT).show();
                            }
                            mDialog.dismiss();
                            finish();
                        }
                    });
                }

            }
        });
    }
}
