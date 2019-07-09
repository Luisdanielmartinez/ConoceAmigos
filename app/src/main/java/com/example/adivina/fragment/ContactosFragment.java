package com.example.adivina.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adivina.R;
import com.example.adivina.adacter.AdaterContactos;
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

public class ContactosFragment extends Fragment {

    @BindView(R.id.recV_contactos)
    RecyclerView mRcv_contactos;
    private ArrayList<Usuario> listUsuario;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private AdaterContactos adaterContactos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_contactos, container, false);
        ButterKnife.bind(this,view);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        listUsuario=new ArrayList<>();
        mRcv_contactos.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcv_contactos.setHasFixedSize(true);

        leerUsuario();
        return view;
    }
    private void leerUsuario(){
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Usuario usuario=snapshot.getValue(Usuario.class);
                    if(!usuario.getId().equals(firebaseUser.getUid())){
                        listUsuario.add(usuario);
                    }
                }
                adaterContactos=new AdaterContactos(listUsuario,getContext());
                mRcv_contactos.setAdapter(adaterContactos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
