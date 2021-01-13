package com.movil.safep.Foro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.movil.safep.R;

import java.util.ArrayList;

public class PreguntaAdapter extends RecyclerView.Adapter<PreguntaAdapter.ViewHolder> implements View.OnClickListener {

    private int resource;
    private ArrayList<Preguntas> preguntalist;
    private ArrayList<Calificaciones> calificacioneslist;
    private ArrayList<Usuario> usuarioslist;
    private View.OnClickListener listener;

    public PreguntaAdapter(ArrayList<Preguntas> preguntalist, ArrayList<Calificaciones> calificacioneslist, ArrayList<Usuario> usuarioslist, int resource){
        this.preguntalist = preguntalist;
        this.calificacioneslist=calificacioneslist;
        this.usuarioslist=usuarioslist;
        this.resource = resource;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int index) {
        Preguntas preguntas = preguntalist.get(index);
        Calificaciones calificaciones = calificacioneslist.get(index);
        Usuario usuario = usuarioslist.get(index);

        holder.tvPregunta.setText(preguntas.getPregunta());
        holder.tvId.setText(ForoI.idList.get(index));
        holder.tvUsuario.setText(usuario.getUsuario());
        holder.tvCal.setText(ForoI.sCalI.get(index));
    }

    @Override
    public int getItemCount() {
        return preguntalist.size();
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvPregunta,tvCal,tvId,tvUsuario;
        private Button btResponder;
        public View view;
        public ViewHolder (View view){
            super(view);
            this.view = view;
            this.tvPregunta= (TextView) view.findViewById(R.id.tvPregunta);
            this.tvId=(TextView) view.findViewById(R.id.tvId);
            this.tvUsuario=(TextView) view.findViewById(R.id.tvUsuario);
            this.tvCal=(TextView) view.findViewById(R.id.tvCal);

        }
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
}
