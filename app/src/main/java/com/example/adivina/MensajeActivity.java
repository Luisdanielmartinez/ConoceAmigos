package com.example.adivina;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adivina.adacter.AdacterMensajes;
import com.example.adivina.entity.MensajeEntity;
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
import butterknife.OnClick;

public class MensajeActivity extends AppCompatActivity {
    @BindView(R.id.imv_mensaje_perfil)
    ImageView mImv_mensaje_perfil;
    @BindView(R.id.tv_mensaje_nombre_usuario)
    TextView mTv_mensaje_nombre_perfil;
    @BindView(R.id.tooball_mensaje_userInfo)
    Toolbar toolbar;
    @BindView(R.id.edt_send_mensaje)
    EditText mEdt_send_mensaje;
    @BindView(R.id.rcv_mensajes)
    RecyclerView mRcv_mensajes;

    private DatabaseReference databaseReference;
    private String usuario_id;
    private FirebaseUser firebaseUser;
    private Mensaje mensaje;
    private MensajeEntity mensajeEntity;
    private AdacterMensajes adaterMensaje;
    private ArrayList<Mensaje>listMensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        mRcv_mensajes.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRcv_mensajes.setHasFixedSize(true);
        usuario_id=getIntent().getStringExtra("usuario_id");
        obtenerUser();
    }
    private void obtenerUser(){

        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(usuario_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario=dataSnapshot.getValue(Usuario.class);
                mTv_mensaje_nombre_perfil.setText(usuario.getUserName());
                if(usuario.getImagenUrl().equals("default")){
                    mImv_mensaje_perfil.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(getBaseContext()).load(usuario.getImagenUrl()).into(mImv_mensaje_perfil);
                }
                leerMensajer(firebaseUser.getUid(),usuario_id,usuario.getImagenUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @OnClick(R.id.img_btn_send)void sendMensaje(){
        mensaje=new Mensaje();
        mensaje.setId_usuario_receptor(usuario_id);
        mensaje.setId_usuario_remitente(firebaseUser.getUid());
        mensaje.setMensaje(mEdt_send_mensaje.getText().toString());
        if(parametros()){
            Toast.makeText(getBaseContext(),"No puede enviar ningun mensaje vacio",Toast.LENGTH_LONG).show();
        }else{
            mensajeEntity=new MensajeEntity(getBaseContext(),mensaje);
            mensajeEntity.GuardarMensaje();
            mEdt_send_mensaje.setText("");
        }
    }
    public boolean parametros(){
        String mnj=mEdt_send_mensaje.getText().toString();
        if(mnj.isEmpty()){
            return true;
        }
        return false;
    }
    public void leerMensajer(final String myId, final String usuario_id, final String imagenUrl){
        listMensaje=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMensaje.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Mensaje mensaje=snapshot.getValue(Mensaje.class);

                    if (mensaje.getId_usuario_receptor().equals(myId)&&mensaje.getId_usuario_remitente().equals(usuario_id)
                    ||mensaje.getId_usuario_receptor().equals(usuario_id)&&mensaje.getId_usuario_remitente().equals(myId)){

                        listMensaje.add(mensaje);
                    }
                }
                adaterMensaje=new AdacterMensajes(getBaseContext(),listMensaje,imagenUrl);
                mRcv_mensajes.setAdapter(adaterMensaje);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
