package com.example.adivina.entity;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.adivina.MensajeActivity;
import com.example.adivina.models.Mensaje;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MensajeEntity {

    private DatabaseReference databaseReference;
    private Context context;
    private Mensaje mensaje;
    public MensajeEntity(Context context,Mensaje mensaje){
     this.context=context;
     this.mensaje=mensaje;
    }
    public void GuardarMensaje(){
     try{
         databaseReference= FirebaseDatabase.getInstance().getReference();
         HashMap<String,Object>hashMap=new HashMap<>();
         hashMap.put("mensaje",mensaje.getMensaje());
         hashMap.put("id_usuario_receptor",mensaje.getId_usuario_receptor());
         hashMap.put("id_usuario_remitente",mensaje.getId_usuario_remitente());
         databaseReference.child("Chats").push().setValue(hashMap);
         listaChat();
     }catch (Exception e){

     }
    }
    public void listaChat(){
        databaseReference=FirebaseDatabase.getInstance().getReference("ListaMensajes")
                .child(mensaje.getId_usuario_remitente())
                .child(mensaje.getId_usuario_receptor());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(!dataSnapshot.exists()){
                  databaseReference.child("id").setValue(mensaje.getId_usuario_receptor());
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
