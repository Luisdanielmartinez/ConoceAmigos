package com.example.adivina.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adivina.R;
import com.example.adivina.entity.UsuarioEntity;
import com.example.adivina.models.Usuario;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginFragment extends Fragment {

     @BindView(R.id.edt_login_correo)EditText mEdt_login_correo;
     @BindView(R.id.edt_login_contrasena)EditText mEdt_login_contrasena;
     @BindView(R.id.toolbar_Login)
     Toolbar toolbar;
     private Usuario usuario;
     private UsuarioEntity usuarioEntity;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this,view);
        AppCompatActivity activity=(AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Login");
        usuario=new Usuario();
        return view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @OnClick(R.id.tv_login_cuenta_nueva)void registrarse(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,new RegistrarFragment()).addToBackStack(null).commit();
    }
    @OnClick(R.id.bt_login)void onclick(){
        if(parametros()){
            Toast.makeText(getContext(),R.string.vacio,Toast.LENGTH_LONG).show();
        }else{
            usuario.setCorreo(mEdt_login_correo.getText().toString());
            usuario.setPassword(mEdt_login_contrasena.getText().toString());
            usuarioEntity=new UsuarioEntity(getContext(),usuario);
            usuarioEntity.validarCuenta();
        }
    }
    public boolean parametros(){
        String correo=mEdt_login_correo.getText().toString();
        String password=mEdt_login_contrasena.getText().toString();
        if(correo.isEmpty()||password.isEmpty()){
            return true;
        }
        return false;
    }
}
