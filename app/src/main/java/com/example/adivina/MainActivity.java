package com.example.adivina;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.adivina.fragment.LoginFragment;
import com.example.adivina.fragment.RegistrarFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
        RegistrarFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.content_main,new LoginFragment()).commit();
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
