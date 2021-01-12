package com.movil.safep.ui.tools;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.movil.safep.R;
import com.movil.safep.ui.Sitios;

public class ToolsFragment extends Fragment {
    private Spinner spinner;
    private TextView tvtasa, tvmonto, tvtotal;
    private TextView tvp,tvtasab,tvmontoi,tvint,tvisr,tvto;
    private Button calcular;
    private ImageView sitios;
    String[] LC ;
    private ToolsViewModel toolsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        sitios=root.findViewById(R.id.btSitios);
        sitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Sitios.class);
                startActivity(intent);
            }
        });
        /////////spinner
        spinner = (Spinner) root.findViewById(R.id.spinnerPlazo);
        final ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(getContext(), R.array.Tipo_interes, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);
        //////
         calcular = root.findViewById(R.id.btCal);
         tvtasa= root.findViewById(R.id.tvTasa);
         tvmonto= root.findViewById(R.id.tvMonto);
         tvp=root.findViewById(R.id.plazo);
         tvtasab=root.findViewById(R.id.tasabruta);
         tvmontoi=root.findViewById(R.id.montoin);
         tvint=root.findViewById(R.id.interes);
         tvisr=root.findViewById(R.id.isr);
         tvto=root.findViewById(R.id.total);
         calcular.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String plazo = spinner.getSelectedItem().toString();
                 float tasareal;
                 String tasa = tvtasa.getText().toString();
                 String monto = tvmonto.getText().toString();
                 if (tasa.isEmpty()) {
                     Toast.makeText(getContext(), "Ingrese una tasa de interes", Toast.LENGTH_SHORT).show();
                 } else if (monto.isEmpty()) {
                     Toast.makeText(getContext(), "Ingresa un monto para invertir", Toast.LENGTH_SHORT).show();
                 }
                    else if(Float.parseFloat(monto)>100000){
                     AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext(),R.style.DatePickerDialogTheme);
                     dialogo1.setTitle("Importante");
                     dialogo1.setMessage("Para realizar el calculo de una inversion mayor a $100,000.00 se recomienda consultar con la institucion bancaria donde se hara la "+
                             "operacion, pues el ISR retenido cambia segun el instrumento de inversion utilizado al exceder este valor.");
                     dialogo1.setCancelable(false);
                     dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialogo1, int id) {

                         }
                     });
                     dialogo1.show();

                 }
                 else {
                     float tasaf=Float.parseFloat(tasa);
                     float montof=Float.parseFloat(monto);
                     if (tasaf >= 1) {
                         tasareal = tasaf / 100;
                     } else {
                         tasareal = tasaf;
                     }
                     Resources res = getResources();
                     LC = res.getStringArray(R.array.Categorias);
                     float total1,total3,total6,total12;
                     float tasabruta=tasareal*100;
                     float isr=.20f;
                     //1 mes
                     total1=(float) Math.pow((1+tasareal), .08333);
                     float intbruto1 = ((total1*montof)-montof);
                     float tot1=(total1*montof);
                     float isr1= (float) (intbruto1*isr);
                     //3 meses
                     total3=(float) Math.pow((1+tasareal), .25);
                     float intbruto3 = ((total3*montof)-montof);
                     float tot3=(total3*montof);
                     float isr3= (float) (intbruto3*isr);
                     //6 meses
                     total6=(float) Math.pow((1+tasareal), .50);
                     float intbruto6 = ((total6*montof)-montof);
                     float tot6=(total6*montof);
                     float isr6= (float) (intbruto6*isr);
                     //12 meses
                     total12=(float) Math.pow((1+tasareal), 1);
                     float intbruto12 = ((total12*montof)-montof);
                     float tot12=(total12*montof);
                     float isr12= (float) (intbruto12*isr);
                     switch (plazo) {
                         case "1 mes":
                            // Toast.makeText(getContext(), "1 mes", Toast.LENGTH_SHORT).show();
                             //tvtotal.setText("Inversion 1 mes"+"\n\n"+"Tasa bruta: \t"+tasabruta+"%\n\n"+"Monto invertido: \t$ "+montof+"\n\n"+
                                     //"Interés bruto: \t$ "+intbruto1+"\n\n"+"ISR: \t"+isr1+"\n\n"+"Total: \t$ "+(tot1-isr1));
                             tvp.setText("Inversion 1 mes");
                             tvtasab.setText(""+tasabruta);
                             tvmontoi.setText(""+montof);
                             tvint.setText("$"+intbruto1);
                             tvisr.setText(""+isr1);
                             tvto.setText("$"+(tot1-isr1));

                             break;
                         case "3 meses":
                            // Toast.makeText(getContext(), "3 meses", Toast.LENGTH_SHORT).show();
                             //tvtotal.setText("Inversion 3 meses"+"\n\n"+"Tasa bruta: \t"+tasabruta+"%\n\n"+"Monto invertido: \t$ "+montof+"\n\n"+
                               //      "Interés bruto: \t$ "+intbruto3+"\n\n"+"ISR: \t"+isr3+"\n\n"+"Total: \t$ "+(tot3-isr3));
                             tvp.setText("Inversion 3 meses");
                             tvtasab.setText(""+tasabruta);
                             tvmontoi.setText(""+montof);
                             tvint.setText("$"+intbruto3);
                             tvisr.setText(""+isr3);
                             tvto.setText("$"+(tot3-isr3));
                             break;

                         case "6 meses":
                             //Toast.makeText(getContext(), "6 meses", Toast.LENGTH_SHORT).show();
                             //tvtotal.setText("Inversion 6 meses"+"\n\n"+"Tasa bruta: \t"+tasabruta+"%\n\n"+"Monto invertido: \t$ "+montof+"\n\n"+
                               //      "Interés bruto: \t$ "+intbruto6+"\n\n"+"ISR: \t"+isr6+"\n\n"+"Total: \t$ "+(tot6-isr6));
                             tvp.setText("Inversion 6 meses");
                             tvtasab.setText(""+tasabruta);
                             tvmontoi.setText(""+montof);
                             tvint.setText("$"+intbruto6);
                             tvisr.setText(""+isr6);
                             tvto.setText("$"+(tot6-isr6));
                             break;

                         case "12 meses":
                             //Toast.makeText(getContext(),"12 meses", Toast.LENGTH_SHORT).show();
                             //tvtotal.setText("Inversion 12 meses"+"\n\n"+"Tasa bruta: \t"+tasabruta+"%\n\n"+"Monto invertido: \t$ "+montof+"\n\n"+
                               //      "Interés bruto: \t$ "+intbruto12+"\n\n"+"ISR: \t"+isr12+"\n\n"+"Total: \t$ "+(tot12-isr12));
                             tvp.setText("Inversion 12 meses");
                             tvtasab.setText(""+tasabruta);
                             tvmontoi.setText(""+montof);
                             tvint.setText("$"+intbruto12);
                             tvisr.setText(""+isr12);
                             tvto.setText("$"+(tot12-isr12));
                         break;

                         default:
                             Toast.makeText(getContext(), "Seleccione un plazo para la inversion", Toast.LENGTH_SHORT).show();
                     }

                 }
             }
         });
        return root;
    }
}