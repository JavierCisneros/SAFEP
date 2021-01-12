package com.movil.safep;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText TextEmail;
    private EditText TextPassword;
    private EditText TextNombre;
    private ProgressDialog progressDialog1;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        TextEmail = (EditText) findViewById(R.id.txtEmailR);
        TextPassword = (EditText) findViewById(R.id.txtPasswordR);
        TextNombre = (EditText) findViewById(R.id.txtNombreR);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog1 = new ProgressDialog(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    private void updateUI(FirebaseUser currentUser) {
    }


    public void Accion(View view){
        //Obtenemos el email y la contraseña desde las cajas de texto

        final String name = TextNombre.getText().toString().trim();
        final String password = TextPassword.getText().toString().trim();
        final String email = TextEmail.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Falta ingresar nombre", Toast.LENGTH_LONG).show();
            return;

        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Falta ingresar el password", Toast.LENGTH_LONG).show();
            return;

        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Falta ingresar el email", Toast.LENGTH_LONG).show();
            return;

        }
        progressDialog1.setMessage("Realizando registro en linea...");
        progressDialog1.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(getApplicationContext(),"Se ha registrado el usuario con el email: "+ TextEmail.getText(),Toast.LENGTH_LONG).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("email", email );
                    final String id=mAuth.getCurrentUser().getUid();
                    mDatabase.child("Users").child("ID:"+id).child("Info").setValue(map);
                    final Intent a = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(a);
                }
                else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(getApplicationContext(),"No se pudo registrar el usuario, ya existe o el correo es invalido ",Toast.LENGTH_LONG).show();
                    updateUI(null);
                }
                progressDialog1.dismiss();
                // ...
            }
        });



    }


}
