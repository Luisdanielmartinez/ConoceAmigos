package com.example.adivina.adacter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adivina.MensajeActivity;
import com.example.adivina.R;
import com.example.adivina.models.Usuario;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdaterContactos extends RecyclerView.Adapter<AdaterContactos.ContactosViewHolder> {
    private ArrayList<Usuario> listUsuario;
    private Context context;

    public AdaterContactos(ArrayList<Usuario> listUsuario, Context context) {
        this.listUsuario = listUsuario;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_contactos,viewGroup,false);
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new ContactosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactosViewHolder contactosViewHolder, int i) {

        final Usuario usuario=listUsuario.get(i);
        contactosViewHolder.mTv_contacto_nombre.setText(usuario.getUserName());
        if(usuario.getImagenUrl().equals("default")){
            contactosViewHolder.mImv_contacto_perfil.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(usuario.getImagenUrl()).into(contactosViewHolder.mImv_contacto_perfil);
        }
        contactosViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MensajeActivity.class);
                intent.putExtra("usuario_id",usuario.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUsuario.size();
    }

    public class ContactosViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgV_contacto_perfil)ImageView mImv_contacto_perfil;
        @BindView(R.id.tv_contacto_nombre)
        TextView mTv_contacto_nombre;
        public ContactosViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
