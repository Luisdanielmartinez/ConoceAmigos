package com.example.adivina.adacter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adivina.R;
import com.example.adivina.models.Mensaje;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdacterMensajes extends RecyclerView.Adapter<AdacterMensajes.MensajesViewHolder> {
    private static  final int MSG_TYPE_LEFT=0;
    private static  final int MSG_TYPE_RIGHT=1;

    private Context context;
    private ArrayList<Mensaje>listMensaje;
    private FirebaseUser firebaseUser;
    private String imagenUrl;
    public AdacterMensajes(Context context, ArrayList<Mensaje> listMensaje,String imagenUrl) {
        this.context = context;
        this.listMensaje = listMensaje;
        this.imagenUrl=imagenUrl;
    }

    @NonNull
    @Override
    public MensajesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_mensaje_derecho,viewGroup,false);
            ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            return new MensajesViewHolder(view);
        }else{
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_mensaje_izquierdo,viewGroup,false);
            ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            return new MensajesViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MensajesViewHolder mensajesViewHolder, int i) {
         Mensaje mensaje=listMensaje.get(i);
         mensajesViewHolder.mTv_list_mensaje_izquierdo.setText(mensaje.getMensaje());
         if(imagenUrl.equals("default")){
             mensajesViewHolder.mImv_list_mensaje_perfil.setImageResource(R.mipmap.ic_launcher);
         }else{
             Glide.with(context).load(imagenUrl).into(mensajesViewHolder.mImv_list_mensaje_perfil);
         }
    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }

    public class MensajesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_list_mensaje_izquierdo)TextView mTv_list_mensaje_izquierdo;
        @BindView(R.id.cImv_list_mensaje_perfil)
        ImageView mImv_list_mensaje_perfil;
        public MensajesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(listMensaje.get(position).getId_usuario_remitente().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;

        }
    }
}
