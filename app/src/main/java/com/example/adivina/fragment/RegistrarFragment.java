package com.example.adivina.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adivina.R;
import com.example.adivina.entity.UsuarioEntity;
import com.example.adivina.models.Usuario;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrarFragment extends Fragment {

    @BindView(R.id.edt_registro_userName)
    EditText mEdt_userName;
    @BindView(R.id.edt_registro_correo)EditText mEdt_correo;
    @BindView(R.id.edt_registro_password)EditText mEdt_password;
    @BindView(R.id.edt_registro_confirmar)EditText mEdt_confirmar;
    @BindView(R.id.toolbar_registro)
    Toolbar toolbar;
    private  UsuarioEntity usuarioEntity;
    private Usuario usuario;
    public RegistrarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_registrar, container, false);
        ButterKnife.bind(this,view);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
       activity.setSupportActionBar(toolbar);
       activity.getSupportActionBar().setTitle("Registro");
       activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,new LoginFragment()).commit();
           }
       });
        usuario=new Usuario();
        return view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @OnClick(R.id.bt_registrar)void createUser(){
        if(parametros()){
            Toast.makeText(getContext(),R.string.vacio,Toast.LENGTH_LONG).show();
        }else{
            if(validarPassConf()){
                Toast.makeText(getContext(),R.string.validar,Toast.LENGTH_LONG).show();
            }else{
                if(validarPassConf()){
                    Toast.makeText(getContext(),R.string.validarPass,Toast.LENGTH_LONG).show();
                }else{
                    usuario.setCorreo(mEdt_correo.getText().toString());
                    usuario.setPassword(mEdt_password.getText().toString());
                    usuario.setUserName(mEdt_userName.getText().toString());
                    usuarioEntity=new UsuarioEntity(getContext(),usuario);
                    usuarioEntity.createUser();
                }

            }
        }

    }
    public boolean parametros(){
        String userName=mEdt_userName.getText().toString();
        String correo=mEdt_correo.getText().toString();
        String password=mEdt_password.getText().toString();
        String confirmar=mEdt_confirmar.getText().toString();

        if(userName.isEmpty()||correo.isEmpty()||password.isEmpty()||confirmar.isEmpty()){
            return true;
        }
        return false;
    }
    public boolean validarPassConf(){
        String password=mEdt_password.getText().toString();
        String confirmar=mEdt_confirmar.getText().toString();
        if(!password.equals(confirmar)){
            return true;
        }else if (password.length()<=6){
            return true;
        }
        return false;
    }
    public boolean validarCorreo(){
        String password=mEdt_password.getText().toString();
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher=pattern.matcher(password);
        if(matcher.find()==true){
             return false;
        }else{
            return true;
        }
    }
}
