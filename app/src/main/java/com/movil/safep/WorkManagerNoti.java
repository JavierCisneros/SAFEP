package com.movil.safep;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.content.res.Resources;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.movil.safep.ui.home.Gastos;
import com.movil.safep.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WorkManagerNoti extends Worker {
    float TotalDia,total;
    float TI,TG,TB,TImes,TGmes,TBmes;
    String[] LC = new String[12];
    String[] t = new String[2] ;
    String detalle,titulo;
    Boolean Bandera;
    private ArrayList<String> idListA = new ArrayList<>();

    public WorkManagerNoti(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static void guardanot(long duracion, Data data, String tag){
        OneTimeWorkRequest noti = new OneTimeWorkRequest.Builder(WorkManagerNoti.class)
                .setInitialDelay(duracion, TimeUnit.MILLISECONDS).addTag(tag)
                .setInputData(data).build();
        WorkManager instance = WorkManager.getInstance();
        instance.enqueue(noti);

    }



    @NonNull
    @Override
    public Result doWork() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        String fecha = format.format(calendar.getTime());
        System.out.println(fecha);
        titulo = getInputData().getString("titulo");
        detalle = getInputData().getString("detalle");
        String tag = getInputData().getString("id_noti");
        System.out.println("Este"+tag);
        int id = (int) getInputData().getLong("idnoti",0);
        if(titulo.equals("Balance")){
            cajas();
        }
        else{
            oreo(titulo,detalle);
        }
        get();
        eliminar(tag);
        return Result.success();
    }

    private void oreo(String t, String d){
        String id = "mensaje";
        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),id);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel nc = new NotificationChannel(id, "nuevo", NotificationManager.IMPORTANCE_HIGH);
            nc.setDescription("Notificacion SAFEP");
            nc.setShowBadge(true);
            assert nm != null;
            nm.createNotificationChannel(nc);
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent =  PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_ONE_SHOT);


        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis()).setContentTitle(t)
                .setTicker("Nueva Notificación")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText(d)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(""+d))
                .setContentInfo("nuevo");

        Random random = new Random();
        int idnotify = random.nextInt(8000);
        assert  nm != null;
        nm.notify(idnotify,builder.build());
    }
    public void cajas() {
        Bandera=true;
         DatabaseReference mDatabase;
         FirebaseAuth mAuth;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final String id = mAuth.getCurrentUser().getUid();
        String cat;
        Calendar fecha = Calendar.getInstance();
        int day = fecha.get(Calendar.DAY_OF_MONTH);
        int month = fecha.get(Calendar.MONTH)+1;
        int year = fecha.get(Calendar.YEAR);
        t[0] = "Gastos";
        t[1]= "Ingresos";
        for (int d = 0; d <= 1; d++) {
            String tipo= t[d];
            if(tipo.equals("Gastos")){
                LC = new String[]{"Categoria", "Comida", "Transporte","Entretenimineto","Cigarillos","Cafe","Mascoota","Reaglos","Personal","Golosinas","Otros"};
            }
            else {
                LC = new String[]{"Categoria","Salario","Premios","Ventas","Inversiones","Otros"};
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
                            String extra="";
                            if(finalDiames ==31 && Bandera && finalD==1){
                                TBmes=TImes-TGmes;
                                TotalDia=0;
                                Bandera=false;

                                if (TBmes < 0) {
                                    extra = "\nDeberia tratar de reducir tus gastos y recuerda registrar tus ingresos";

                                }
                                else if (TBmes < (TImes * .20)) {
                                    extra = "\nDeberias ahorrar una parte de tus ingresos para futuras ocasiones e invertirlo despues";
                                }
                                else if (TBmes > (TImes * .90)) {
                                    extra = "\nTrata de invertir una parte de tus ingresos no estas utilizando mas del 90%";
                                }
                                else if(TGmes > TImes*.20){
                                    extra = "\n Mas del 20% de tus ingresos han sido utilizados en microgastos";
                                }
                                detalle="Balance del mes:"+TBmes+extra;
                                oreo(titulo,detalle);
                                TGmes=0;
                                TImes=0;
                                TBmes=0;
                                TotalDia=0;
                                TG=0;
                                TI=0;
                                TB=0;
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            // ...
                        }

                    });

                }
            }
        }
        return ;
    }
    public void get(){
        DatabaseReference mDatabase;
        FirebaseAuth mAuth;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child("ID:" + id).child("Info").child("noti").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    idListA.clear();
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String tipo= ds.child("tipo").getValue().toString();
                        String hora = ds.child("hora").getValue().toString();
                        String fecha = ds.child("fecha").getValue().toString();
                        String id = ds.getKey().toString();
                        idListA.add(""+id);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void eliminar(String tag){
        DatabaseReference mDatabase;
        FirebaseAuth mAuth;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child("ID:" + id).child("Info").child("noti").child("" + tag).removeValue();

    }
}
