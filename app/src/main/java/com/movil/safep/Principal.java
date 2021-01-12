package com.movil.safep;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movil.safep.ui.gallery.GalleryFragment;
import com.movil.safep.ui.home.HomeFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Set;

public class Principal extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private AppBarConfiguration mAppBarConfiguration;
    private TextView txtNombre;
    private TextView txtCorreo;
    private ImageView perfil;
    private int dia,mes,year,day,month;
    private DatabaseReference mDatabase;
    public static final String Day ="Dia";
    public static final String Month = "Mes";
    public static final String Year = "Year";
    public static int Uno,Dos,Tres;
    public static int fin;
    public static String dateCompra;
    Calendar actual = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        txtNombre=(TextView) findViewById(R.id.TextViewNombre);
        txtCorreo=(TextView) findViewById(R.id.TextViewCorreo);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();


        if (user != null) {
            // Name, email address, and profile photo Url

            Toast.makeText(Principal.this,"Bienvenido: "+ email,Toast.LENGTH_LONG).show();
            View headView = navigationView.getHeaderView(0);
            TextView TextViewNombre = headView.findViewById(R.id.TextViewNombre);
            TextViewNombre.setText(""+email);


            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
        Toolbar ToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(ToolBar);
        final String id=mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Users").child("ID:"+id).child("Info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String nombre = dataSnapshot.child("name").getValue().toString();
                    View headView = navigationView.getHeaderView(0);
                    TextView TextViewMail = headView.findViewById(R.id.TextViewCorreo);
                    TextViewMail.setText(""+nombre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),"LogOut",Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                final Intent  a = new Intent(this, MainActivity.class);
                startActivity(a);
                return true;
            case R.id.action_notifications:
                final Intent  b = new Intent(this, Notificaciones.class);
                startActivity(b);
                return true;
            case R.id.action_tutorial:
                Uri uri = Uri.parse("https://drive.google.com/file/d/1BcjLbUnbuztuoWbRlCaUZ8TvC84lU0rd/view?usp=drivesdk");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}