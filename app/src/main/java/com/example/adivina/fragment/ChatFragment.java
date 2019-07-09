package com.example.adivina.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adivina.R;
import com.example.adivina.adacter.AdaterContactos;
import com.example.adivina.models.ListMensaje;
import com.example.adivina.models.Mensaje;
import com.example.adivina.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChatFragment extends Fragment {

    @BindView(R.id.rcv_show_chat)RecyclerView mRcv_show_chat;
    private AdaterContactos adaterContactos;
    private ArrayList <Usuario>listUsuario;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private ArrayList<ListMensaje>listMensajes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this,view);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        mRcv_show_chat.setLayoutManager(new LinearLayoutManager(getContext()));
        listMensajes=new ArrayList<>();
        getListMensajes();
        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
  //en este metodo obtenemos los id de los contactos que hay chat para luego comparar lo con el id que esta logiado
    public void getListMensajes(){
        databaseReference=FirebaseDatabase.getInstance().getReference("ListaMensajes").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMensajes.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ListMensaje list_mensaje=snapshot.getValue(ListMensaje.class);
                    listMensajes.add(list_mensaje);
                }
                getMensajesList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    //en este metodo obtenemos todos los contacto que tenemos chat con ellos
    public void getMensajesList(){
        listUsuario=new ArrayList<>();

        databaseReference=FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              listUsuario.clear();
              for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                  Usuario usuario=snapshot.getValue(Usuario.class);
                  for(ListMensaje listMensaje:listMensajes){
                      if(usuario.getId().equals(listMensaje.getId())){
                          listUsuario.add(usuario);
                      }
                  }
              }
              adaterContactos=new AdaterContactos(listUsuario,getContext());
              mRcv_show_chat.setAdapter(adaterContactos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
