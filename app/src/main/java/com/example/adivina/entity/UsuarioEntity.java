package com.example.adivina.entity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.adivina.HomeActivity;
import com.example.adivina.R;
import com.example.adivina.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UsuarioEntity {
    private FirebaseAuth firebaseAuth;
    private Context context;
    private Usuario usuario;
    private ProgressDialog progressDialog;
    private DatabaseReference reference;

    public UsuarioEntity(){}
    public UsuarioEntity(Context context,Usuario usuario){
        firebaseAuth=FirebaseAuth.getInstance();
        this.context=context;
        this.usuario=usuario;
        progressDialog=new ProgressDialog(context);

    }
    public void createUser(){
        getProgress();
        firebaseAuth.createUserWithEmailAndPassword(usuario.getCorreo(),usuario.getPassword()).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    //aqui obtenemos el id creado de la cuenta de firebase
                    RegistrarUsers();

                }else{
                    Toast.makeText(context,R.string.nocreate,Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    //metodo para registrar usuario en base de datos con firebase
    public void RegistrarUsers(){
        try{
            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
            //aqui creamos un nodo de usuario que va a tener otro nodo por el id
            reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            HashMap<String,Object>hashMap=new HashMap<>();
            hashMap.put("id",firebaseUser.getUid());
            hashMap.put("userName",usuario.getUserName());
            hashMap.put("imagenUrl","default");
            hashMap.put("correo",usuario.getCorreo());
            hashMap.put("password",usuario.getPassword());
            reference.setValue(hashMap).addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(context, R.string.create,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(context,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }

    }
    public void validarCuenta(){

        try{
            getProgress();
            firebaseAuth.signInWithEmailAndPassword(usuario.getCorreo(),usuario.getPassword()).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Intent intent=new Intent(context, HomeActivity.class);
                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context,R.string.noValido,Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(context,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void getProgress(){
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
    }
}
