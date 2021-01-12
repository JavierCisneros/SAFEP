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

public class RespuestaAdapter extends RecyclerView.Adapter<RespuestaAdapter.ViewHolder> implements View.OnClickListener {

    private int resource;
    private ArrayList<Respuestas> respuestaslist;
    private ArrayList<Calificaciones> calificacioneslist;
    private View.OnClickListener listener;
    private ArrayList<Usuario> usuarioslist;

    public RespuestaAdapter(ArrayList<Respuestas> preguntalist,ArrayList<Calificaciones> calificacioneslist,ArrayList<Usuario> usuarioslist, int resource){
        this.respuestaslist = preguntalist;
        this.calificacioneslist=calificacioneslist;
        this.resource = resource;
        this.usuarioslist=usuarioslist;

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
        Respuestas respuestas = respuestaslist.get(index);
        Calificaciones calificaciones = calificacioneslist.get(index);
        Usuario usuario = usuarioslist.get(index);
        holder.tvRcRes.setText(respuestas.getRespuestas());
        holder.tvRcId.setText(Respuesta.ridList.get(index).toString());
        holder.tvUsuario.setText(usuario.getUsuario());
        holder.tvRcCal.setText(Respuesta.sCalRI.get(index).toString());
    }

    @Override
    public int getItemCount() {
        return respuestaslist.size();
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvRcRes,tvRcCal,tvRcId,tvUsuario;
        private Button btResponder;
        public View view;
        public ViewHolder (View view){
            super(view);
            this.view = view;
            this.tvRcRes= (TextView) view.findViewById(R.id.tvRcRes);
            this.tvRcId=(TextView) view.findViewById(R.id.tvRcIdR);
            this.tvUsuario=(TextView) view.findViewById(R.id.tvRcUsuarioR);
            this.tvRcCal=(TextView) view.findViewById(R.id.tvCalR);

        }
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
}
