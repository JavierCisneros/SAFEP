package com.movil.safep;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
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

public class MainActivity extends AppCompatActivity {

    private EditText TextEmail;
    private EditText TextPassword;
    private Button btnRegistrar;
    private Button Recuperar;
    private ProgressDialog progressDialog;
    private Button btnIngresar;
    //Objeto firebase
    private FirebaseAuth mAuth;
    //Agregar Nombre
    private DatabaseReference mDatabase;
    private EditText TextNombre;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        Recuperar=findViewById(R.id.btRecuperar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        btnRegistrar = (Button)findViewById(R.id.int_Registro);
        TextEmail = (EditText) findViewById(R.id.TxtEmail);
        TextPassword=(EditText) findViewById(R.id.TxtPassword);
        btnIngresar=(Button) findViewById(R.id.BotonIngresar);
        progressDialog = new ProgressDialog(this);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin();
            }
        });
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });
        Recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainActivity.this,Recuperar.class);
                startActivity(a);
            }
        });

    }

    public void registrar(){
        Intent b = new Intent(this, Registro.class);
        startActivity(b);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        if(currentUser==null){
            Toast.makeText(getApplicationContext(),"Ingresa a tu cuenta",Toast.LENGTH_SHORT).show();
        }
        else {
            final Intent  a = new Intent(this, Principal.class);
            startActivity(a);
        }

    }

    private void updateUI(FirebaseUser currentUser) {
    }


    private void signin(){
        final Intent  a = new Intent(this, Principal.class);
        String email = TextEmail.getText().toString().trim();
        String password  = TextPassword.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacías
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Se debe ingresar un email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Falta ingresar la contraseña",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Ingresando a la cuenta");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this,"Ingreso exitoso "+ TextEmail.getText(),Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            startActivity(a);


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this,"La contraseña o el correo son incorrectos "+ TextEmail.getText(),Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                        progressDialog.dismiss();

                        // ...
                    }
                });


    }

    }


